/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.authority.checker;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.authority.checker.AuthorityChecker;
import org.apache.shardingsphere.authority.rule.AccessControlRule;
import org.apache.shardingsphere.authority.rule.AccessControlTable;
import org.apache.shardingsphere.authority.rule.AccessControlUser;
import org.apache.shardingsphere.authority.rule.AuthorityRule;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.Projection;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.ProjectionsContext;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.impl.ColumnProjection;
import org.apache.shardingsphere.infra.binder.context.segment.table.TablesContext;
import org.apache.shardingsphere.infra.binder.context.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.infra.exception.dialect.exception.syntax.database.UnknownDatabaseException;
import org.apache.shardingsphere.infra.exception.kernel.metadata.HasNotAccessRightException;
import org.apache.shardingsphere.infra.executor.checker.SQLExecutionChecker;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.infra.metadata.user.ShardingSphereUser;
import org.apache.shardingsphere.infra.session.query.QueryContext;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.combine.CombineSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.datetime.DatetimeExpression;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.*;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.complex.CommonTableExpressionSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.subquery.SubquerySegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.item.*;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.join.OuterJoinExpression;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.CommentSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.match.MatchAgainstExpression;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.table.*;
import org.apache.shardingsphere.sql.parser.statement.core.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.statement.mysql.dml.MySQLSelectStatement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authority SQL execution checker.
 */
public final class AccessControlExecutionChecker implements SQLExecutionChecker {
    
    @Override
    public void check(final ShardingSphereMetaData metaData, final Grantee grantee, final QueryContext queryContext, final ShardingSphereDatabase database) {
        Optional<ShardingSphereUser> user = metaData.getGlobalRuleMetaData().findSingleRule(AuthorityRule.class).get().findUser(grantee);
        Map<String, Map<String, List<String>>> accessMap = getAccessMapFromQueryContext(queryContext);
        if(accessMap!=null&&!accessMap.isEmpty()){
            List<ShardingSphereDatabase> shardingSphereDatabaseList = new ArrayList<>();
            accessMap.entrySet().forEach(entry ->{
                String databaseName = entry.getKey();
                ShardingSphereDatabase shardingSphereDatabase = metaData.getDatabase(databaseName);
                if(shardingSphereDatabase==null){
                    throw new HasNotAccessRightException(databaseName);
                }else{
                    shardingSphereDatabaseList.add(shardingSphereDatabase);
                    Optional<AccessControlRule> singleRule = shardingSphereDatabase.getRuleMetaData().findSingleRule(AccessControlRule.class);
                    if(!singleRule.isPresent()){
                        throw new HasNotAccessRightException(databaseName);
                    }else {
                        Optional<AccessControlUser> accessControlUser = singleRule.get().findAccessControlUser(user.get().getGrantee().getUsername());
                        if(!accessControlUser.isPresent()){
                            throw new HasNotAccessRightException(databaseName);
                        }else {
                            AccessControlUser accessControlUser1 = accessControlUser.get();
                            if(!accessControlUser1.getAllFlag()){
                                Map<String, List<String>> tableMapColumnList = entry.getValue();
                                Map<String, AccessControlTable> tables = accessControlUser1.getTables();
                                List<String> wantToAccessTableNameList = tableMapColumnList.keySet().stream().collect(Collectors.toList());
                                List<String> hasRightTableNameList = tables.keySet().stream().map(tableName->tableName.toLowerCase()).collect(Collectors.toList());
                                if(wantToAccessTableNameList!=null&&!wantToAccessTableNameList.isEmpty()){
                                    if(hasRightTableNameList==null&&hasRightTableNameList.isEmpty()){
                                        throw new HasNotAccessRightException(databaseName,wantToAccessTableNameList);
                                    }
                                    List<String> hasNotRightTableNameList = new ArrayList<>();
                                    wantToAccessTableNameList.forEach(tableName->{
                                        if(!hasRightTableNameList.contains(tableName.toLowerCase())){
                                            hasNotRightTableNameList.add(tableName);
                                        }
                                    });
                                    if(!hasNotRightTableNameList.isEmpty()){
                                        throw new HasNotAccessRightException(databaseName,hasNotRightTableNameList);
                                    }
                                    tableMapColumnList.entrySet().forEach(entry1->{
                                        String tableName = entry1.getKey();
                                        AccessControlTable accessControlTable = tables.get(tableName);
                                        if(accessControlTable==null){
                                            throw new HasNotAccessRightException(databaseName, Collections.singletonList(tableName));
                                        }else if(!accessControlTable.getAllFlag()){
                                            List<String> hasRightColumnList = accessControlTable.getColumns();
                                            List<String> wantAccessColumnList = tableMapColumnList.get(tableName);
                                            if(wantAccessColumnList!=null||!wantAccessColumnList.isEmpty()){
                                                if(hasRightColumnList==null||hasRightColumnList.isEmpty()){
                                                    throw new HasNotAccessRightException(databaseName,tableName,wantAccessColumnList);
                                                }
                                                List<String> collect = hasRightColumnList.stream().map(columnName -> columnName.toLowerCase()).collect(Collectors.toList());
                                                List<String> hasNotAccessConlumnList = new ArrayList<>();
                                                wantAccessColumnList.forEach(columnName->{
                                                    if(!collect.contains(columnName.toLowerCase())){
                                                        hasNotAccessConlumnList.add(columnName);
                                                    }
                                                });
                                                if(!hasNotAccessConlumnList.isEmpty()){
                                                    throw new HasNotAccessRightException(databaseName,tableName,hasNotAccessConlumnList);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

            });
        }
    }

    private static Map<String, Map<String, List<String>>> getAccessMapFromQueryContext(QueryContext queryContext) {
        Map<String,Map<String,List<String>>> accessMap=new HashMap<>();

        SQLStatementContext sqlStatementContext = queryContext.getSqlStatementContext();
        if(sqlStatementContext instanceof SelectStatementContext){//目前只支持查询语句
            SelectStatementContext selectStatementContext = (SelectStatementContext) sqlStatementContext;
            fillAccessMap(selectStatementContext,accessMap);
            /*SelectStatement sqlStatement = selectStatementContext.getSqlStatement();
            TablesContext tablesContext = selectStatementContext.getTablesContext();
            tablesContext.getSimpleTables().forEach(simpleTableSegment -> {
                String databaseName = simpleTableSegment.getOwner().isPresent()?simpleTableSegment.getOwner().get().getIdentifier().getValue(): queryContext.getUsedDatabaseName();
                if(!accessMap.containsKey(databaseName)){
                    Map<String,List<String>> databaseAccessMap = new HashMap<>();
                    accessMap.put(databaseName,databaseAccessMap);
                }
                Map<String, List<String>> tableAccessMap = accessMap.get(databaseName);
                String tableName = simpleTableSegment.getTableName().getIdentifier().getValue();
                if(!tableAccessMap.containsKey(tableName)){
                    List<String> columnList = new ArrayList<>();
                    tableAccessMap.put(tableName,columnList);
                }
            });*/
           /* fillColumnAccessWithSqlStatement(sqlStatement,accessMap);*/
            accessMap.get("a");
        } else {
            //todo 访问权限丢失
            System.out.println("访问权限丢失");
        }
        return accessMap;
    }

    private static void fillAccessMap(SelectStatementContext selectStatementContext, Map<String, Map<String, List<String>>> accessMap) {
        SelectStatement sqlStatement = selectStatementContext.getSqlStatement();
        Optional<TableSegment> from = sqlStatement.getFrom();
        List<ProjectionSegment> projectionSegments = findAllProjectionSegments(sqlStatement);
        projectionSegments.forEach(projectionSegment -> {
            List<ColumnSegment> columnSegmentList=getColumnSegmentListByProjectionSegment(projectionSegment);
            columnSegmentList.forEach(columnSegment -> {
                fillAccessMapBySimpleTableSegmentMap(columnSegment,from,accessMap);
            });
        });
        Map<Integer, SelectStatementContext> subqueryContexts = selectStatementContext.getSubqueryContexts();
        if(subqueryContexts!=null&&!subqueryContexts.isEmpty()){
            Collection<SelectStatementContext> subQueryContexts = subqueryContexts.values();
            projectionSegments.forEach(projectionSegment -> {
                List<ColumnSegment> columnSegmentList=getColumnSegmentListByProjectionSegment(projectionSegment);
                columnSegmentList.forEach(columnSegment -> {
                    String value = columnSegment.getIdentifier().getValue();
                    fillAccessMapBySubqueryContexts(columnSegment,subQueryContexts,accessMap);
                });

            });
        }

    }

    private static List<ProjectionSegment> findAllProjectionSegments(SelectStatement sqlStatement) {
        List<ProjectionSegment> projectionSegmentList = new ArrayList<>();
        //todo 这里兼容查询所有的情况，看还有没有别的兼容
        sqlStatement.getProjections().getProjections().forEach(projectionSegment -> {
            if(projectionSegment instanceof ShorthandProjectionSegment){
                ShorthandProjectionSegment shorthandProjectionSegment = (ShorthandProjectionSegment) projectionSegment;
                projectionSegmentList.addAll(shorthandProjectionSegment.getActualProjectionSegments());
            }else {
                projectionSegmentList.add(projectionSegment);
            }
        });
        if(sqlStatement.getCombine().isPresent()){
            projectionSegmentList.addAll(findAllProjectionSegments(sqlStatement.getCombine().get().getLeft().getSelect()));
            projectionSegmentList.addAll(findAllProjectionSegments(sqlStatement.getCombine().get().getRight().getSelect()));
        }
        return projectionSegmentList;
    }

    private static List<ColumnSegment> getColumnSegmentListByProjectionSegment(ProjectionSegment projectionSegment) {
        List<ColumnSegment> columnSegmentList = new ArrayList<>();
        if(projectionSegment instanceof ColumnProjectionSegment){
            ColumnProjectionSegment columnProjectionSegment = (ColumnProjectionSegment) projectionSegment;
            ColumnSegment column = columnProjectionSegment.getColumn();
            columnSegmentList.add(column);

        } else if (projectionSegment instanceof AggregationProjectionSegment) {
            AggregationProjectionSegment aggregationProjectionSegment = (AggregationProjectionSegment) projectionSegment;
            aggregationProjectionSegment.getParameters().forEach(expressionSegment -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment));
            });

        } else if (projectionSegment instanceof DatetimeProjectionSegment) {
            DatetimeProjectionSegment datetimeProjectionSegment = (DatetimeProjectionSegment) projectionSegment;
            columnSegmentList.addAll(findColumnSegment(datetimeProjectionSegment.getLeft()));
            columnSegmentList.addAll(findColumnSegment(datetimeProjectionSegment.getRight()));
        } else if (projectionSegment instanceof ExpressionProjectionSegment) {
            ExpressionProjectionSegment expressionProjectionSegment = (ExpressionProjectionSegment) projectionSegment;
            columnSegmentList.addAll(findColumnSegment(expressionProjectionSegment.getExpr()));
        } else if (projectionSegment instanceof IntervalExpressionProjection) {
            IntervalExpressionProjection intervalExpressionProjection = (IntervalExpressionProjection) projectionSegment;
            columnSegmentList.addAll(findColumnSegment(intervalExpressionProjection.getLeft()));
            columnSegmentList.addAll(findColumnSegment(intervalExpressionProjection.getRight()));
        } else if (projectionSegment instanceof ShorthandProjectionSegment) {
            ShorthandProjectionSegment shorthandProjectionSegment = (ShorthandProjectionSegment) projectionSegment;
            shorthandProjectionSegment.getActualProjectionSegments().forEach(projectionSegment1 -> {
                columnSegmentList.addAll(getColumnSegmentListByProjectionSegment(projectionSegment1));
            });
        }else {
            //todo 访问权限丢失
            System.out.println("访问权限丢失"+projectionSegment.getColumnLabel());
        }


        return columnSegmentList;
    }

    private static void fillAccessMapBySubqueryContexts(ColumnSegment columnSegment, Collection<SelectStatementContext> subQueryContexts, Map<String, Map<String, List<String>>> accessMap) {
        subQueryContexts.forEach(selectStatementContext -> {
            SelectStatement sqlStatement = selectStatementContext.getSqlStatement();
            Optional<TableSegment> from = sqlStatement.getFrom();
            List<ProjectionSegment> projectionSegments = findAllProjectionSegments(sqlStatement);
            projectionSegments.addAll(sqlStatement.getProjections().getProjections());
            if(sqlStatement.getCombine().isPresent()){
                projectionSegments.addAll(sqlStatement.getCombine().get().getLeft().getSelect().getProjections().getProjections());
                projectionSegments.addAll(sqlStatement.getCombine().get().getRight().getSelect().getProjections().getProjections());
            }
            List<ColumnSegment> subqueryColumnSegmentList = findSubqueryColumnSegmentList(columnSegment,projectionSegments);
            subqueryColumnSegmentList.forEach(subQueryColumnSegment -> {
                fillAccessMapBySimpleTableSegmentMap(subQueryColumnSegment,from,accessMap);
            });

            Map<Integer, SelectStatementContext> subqueryContexts = selectStatementContext.getSubqueryContexts();
            if(subqueryContexts!=null&&!subqueryContexts.isEmpty()){
                Collection<SelectStatementContext> subQueryContexts2 = subqueryContexts.values();
                subqueryColumnSegmentList.forEach(subqueryColumnSegment->{
                    fillAccessMapBySubqueryContexts(subqueryColumnSegment,subQueryContexts2,accessMap);
                });
            }
        });
    }

    private static List<ColumnSegment> findSubqueryColumnSegmentList(ColumnSegment columnSegment, Collection<ProjectionSegment> projections) {
        List<ColumnSegment> columnSegmentList = new ArrayList<>();
        projections.forEach(projectionSegment -> {
            String columnLabel = projectionSegment.getColumnLabel();
            String value = columnSegment.getIdentifier().getValue();
            if(columnLabel!=null&&value.toLowerCase().equals(columnLabel.toLowerCase())){
                columnSegmentList.addAll(getColumnSegmentListByProjectionSegment(projectionSegment));
            }
        });
        return columnSegmentList;
    }

    private static void fillAccessMapBySimpleTableSegmentMap(ColumnSegment columnSegment,Optional<TableSegment> from, Map<String, Map<String, List<String>>> accessMap) {
        if (columnSegment.getColumnBoundInfo()!=null
                && StringUtils.isNotEmpty(columnSegment.getColumnBoundInfo().getOriginalColumn().getValue())
                && StringUtils.isNotEmpty(columnSegment.getColumnBoundInfo().getOriginalTable().getValue())
                && StringUtils.isNotEmpty(columnSegment.getColumnBoundInfo().getOriginalDatabase().getValue())) {
            String databaseName = columnSegment.getColumnBoundInfo().getOriginalDatabase().getValue();
            String tableName = columnSegment.getColumnBoundInfo().getOriginalTable().getValue();
            String columnName = columnSegment.getColumnBoundInfo().getOriginalColumn().getValue();
            Map<String, List<String>> stringListMap = accessMap.get(databaseName);
            if(stringListMap==null){
                stringListMap = new HashMap<>();
            }
            List<String> strings = stringListMap.get(tableName);
            if(strings==null){
                strings = new ArrayList<>();
            }
            if(!strings.contains(columnName)){
                strings.add(columnName);
            }
            stringListMap.put(tableName,strings);
            accessMap.put(databaseName,stringListMap);
        }else if (from.isPresent()&&(from.get() instanceof SimpleTableSegment)){
            SimpleTableSegment simpleTableSegment = (SimpleTableSegment)from.get();
            if(simpleTableSegment.getOwner().isPresent()&&simpleTableSegment.getTableName()!=null&&columnSegment.getColumnBoundInfo()!=null&& StringUtils.isNotEmpty(columnSegment.getColumnBoundInfo().getOriginalColumn().getValue())){
                String databaseName = simpleTableSegment.getOwner().get().getIdentifier().getValue();
                String tableName = simpleTableSegment.getTableName().getIdentifier().getValue();
                String columnName = columnSegment.getColumnBoundInfo().getOriginalColumn().getValue();
                Map<String, List<String>> stringListMap = accessMap.get(databaseName);
                if(stringListMap==null){
                    stringListMap = new HashMap<>();
                }
                List<String> strings = stringListMap.get(tableName);
                if(strings==null){
                    strings = new ArrayList<>();
                }
                if(!strings.contains(columnName)){
                    strings.add(columnName);
                }
                stringListMap.put(tableName,strings);
                accessMap.put(databaseName,stringListMap);

            }
        } else if (from.isPresent()&&(from.get() instanceof JoinTableSegment)&&columnSegment.getOwner().isPresent()&&columnSegment.getColumnBoundInfo()!=null&& StringUtils.isNotEmpty(columnSegment.getColumnBoundInfo().getOriginalColumn().getValue())) {
            JoinTableSegment joinTableSegment = (JoinTableSegment) from.get();
            TableSegment right = joinTableSegment.getRight();
            if(right instanceof SimpleTableSegment){
                SimpleTableSegment rightSimTableSegment = (SimpleTableSegment) right;
                if((columnSegment.getOwner().get().getIdentifier().getValue().equals(rightSimTableSegment.getAliasName().get()))||
                        columnSegment.getOwner().get().getIdentifier().getValue().equals(rightSimTableSegment.getTableName().getIdentifier().getValue())
                ){
                    if(rightSimTableSegment.getOwner().isPresent()){
                        String databaseName = rightSimTableSegment.getOwner().get().getIdentifier().getValue();
                        String tableName = rightSimTableSegment.getTableName().getIdentifier().getValue();
                        String columnName = columnSegment.getColumnBoundInfo().getOriginalColumn().getValue();
                        Map<String, List<String>> stringListMap = accessMap.get(databaseName);
                        if(stringListMap==null){
                            stringListMap = new HashMap<>();
                        }
                        List<String> strings = stringListMap.get(tableName);
                        if(strings==null){
                            strings = new ArrayList<>();
                        }
                        if(!strings.contains(columnName)){
                            strings.add(columnName);
                        }
                        stringListMap.put(tableName,strings);
                        accessMap.put(databaseName,stringListMap);

                    }
                }
            }

            TableSegment left = joinTableSegment.getLeft();
            if(left instanceof SimpleTableSegment){
                SimpleTableSegment leftSimTableSegment = (SimpleTableSegment) left;
                if((columnSegment.getOwner().get().getIdentifier().getValue().equals(leftSimTableSegment.getAliasName().get()))||
                        columnSegment.getOwner().get().getIdentifier().getValue().equals(leftSimTableSegment.getTableName().getIdentifier().getValue())
                ){
                    if(leftSimTableSegment.getOwner().isPresent()){
                        String databaseName = leftSimTableSegment.getOwner().get().getIdentifier().getValue();
                        String tableName = leftSimTableSegment.getTableName().getIdentifier().getValue();
                        String columnName = columnSegment.getColumnBoundInfo().getOriginalColumn().getValue();
                        Map<String, List<String>> stringListMap = accessMap.get(databaseName);
                        if(stringListMap==null){
                            stringListMap = new HashMap<>();
                        }
                        List<String> strings = stringListMap.get(tableName);
                        if(strings==null){
                            strings = new ArrayList<>();
                        }
                        if(!strings.contains(columnName)){
                            strings.add(columnName);
                        }
                        stringListMap.put(tableName,strings);
                        accessMap.put(databaseName,stringListMap);

                    }
                }
            }

        }
    }





    private static void fillColumnAccessWithSqlStatement(SelectStatement sqlStatement, Map<String, Map<String, List<String>>> accessMap) {
        if(sqlStatement instanceof MySQLSelectStatement){
            MySQLSelectStatement mySQLSelectStatement =(MySQLSelectStatement) sqlStatement;
            TableSegment tableSegment = mySQLSelectStatement.getFrom().get();
            mySQLSelectStatement.getProjections().getProjections().forEach(projectionSegment -> {
                fillColumnAccess(tableSegment,projectionSegment,accessMap);
            });
            if(mySQLSelectStatement.getCombine().isPresent()){
                CombineSegment combineSegment = mySQLSelectStatement.getCombine().get();
                SelectStatement leftSelectStatement = combineSegment.getLeft().getSelect();
                SelectStatement rightSelectStatement = combineSegment.getRight().getSelect();
                fillColumnAccessWithSqlStatement(leftSelectStatement,accessMap);
                fillColumnAccessWithSqlStatement(rightSelectStatement,accessMap);
            }
        } else {
            //todo 访问权限丢失
            System.out.println("非mysql和非select无访问权限控制");
        }
    }

    private static void fillColumnAccess(TableSegment fromTableSegment, ProjectionSegment projectionSegment, Map<String, Map<String, List<String>>> accessMap) {
        if(projectionSegment instanceof ColumnProjectionSegment){
            ColumnProjectionSegment columnProjectionSegment = (ColumnProjectionSegment) projectionSegment;
            ColumnSegment column = columnProjectionSegment.getColumn();
            fillColumnAccessWithColumnSegment(fromTableSegment,column,accessMap);

        } else if (projectionSegment instanceof AggregationProjectionSegment) {
            AggregationProjectionSegment aggregationProjectionSegment = (AggregationProjectionSegment) projectionSegment;
            aggregationProjectionSegment.getParameters().forEach(expressionSegment -> {
                findColumnSegment(expressionSegment).forEach(columnSegment -> {
                    fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
                });
            });

        } else if (projectionSegment instanceof DatetimeProjectionSegment) {
            DatetimeProjectionSegment datetimeProjectionSegment = (DatetimeProjectionSegment) projectionSegment;
            findColumnSegment(datetimeProjectionSegment.getLeft()).forEach(columnSegment -> {
                fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
            });
            findColumnSegment(datetimeProjectionSegment.getRight()).forEach(columnSegment -> {
                fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
            });
        } else if (projectionSegment instanceof ExpressionProjectionSegment) {
            ExpressionProjectionSegment expressionProjectionSegment = (ExpressionProjectionSegment) projectionSegment;
            findColumnSegment(expressionProjectionSegment.getExpr()).forEach(columnSegment -> {
                fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
            });
        } else if (projectionSegment instanceof IntervalExpressionProjection) {
            IntervalExpressionProjection intervalExpressionProjection = (IntervalExpressionProjection) projectionSegment;
            findColumnSegment(intervalExpressionProjection.getLeft()).forEach(columnSegment -> {
                fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
            });
            findColumnSegment(intervalExpressionProjection.getRight()).forEach(columnSegment -> {
                fillColumnAccessWithColumnSegment(fromTableSegment,columnSegment,accessMap);
            });
        } else if (projectionSegment instanceof ShorthandProjectionSegment) {
            ShorthandProjectionSegment shorthandProjectionSegment = (ShorthandProjectionSegment) projectionSegment;
            shorthandProjectionSegment.getActualProjectionSegments().forEach(projectionSegment1 -> {
                fillColumnAccess(fromTableSegment,projectionSegment1,accessMap);
            });
        }
    }

    private static List<ColumnSegment> findColumnSegment(ExpressionSegment expressionSegment) {
        List<ColumnSegment> columnSegmentList = new ArrayList<>();
        if(expressionSegment==null){
            return columnSegmentList;
        } else if(expressionSegment instanceof ColumnSegment){
            columnSegmentList.add((ColumnSegment)expressionSegment);
        } else if(expressionSegment instanceof AggregationProjectionSegment){
            AggregationProjectionSegment aggregationProjectionSegment = (AggregationProjectionSegment)expressionSegment;
            aggregationProjectionSegment.getParameters().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        } else if(expressionSegment instanceof BetweenExpression){
            BetweenExpression betweenExpression = (BetweenExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(betweenExpression.getLeft()));
            columnSegmentList.addAll(findColumnSegment(betweenExpression.getBetweenExpr()));
            columnSegmentList.addAll(findColumnSegment(betweenExpression.getAndExpr()));
        } else if(expressionSegment instanceof BinaryOperationExpression){
            BinaryOperationExpression binaryOperationExpression = (BinaryOperationExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(binaryOperationExpression.getLeft()));
            columnSegmentList.addAll(findColumnSegment(binaryOperationExpression.getRight()));
        }else if(expressionSegment instanceof CaseWhenExpression){
            CaseWhenExpression caseWhenExpression = (CaseWhenExpression)expressionSegment;
            columnSegmentList.addAll(findColumnSegment(caseWhenExpression.getCaseExpr()));
            columnSegmentList.addAll(findColumnSegment(caseWhenExpression.getElseExpr()));
            caseWhenExpression.getWhenExprs().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
            caseWhenExpression.getThenExprs().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        }else if(expressionSegment instanceof CollateExpression){
            CollateExpression collateExpression = (CollateExpression)expressionSegment;
            columnSegmentList.addAll(findColumnSegment(collateExpression.getExpr().get()));
        } else if (expressionSegment instanceof DatetimeExpression) {
            DatetimeExpression datetimeExpression =(DatetimeExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(datetimeExpression.getLeft()));
            columnSegmentList.addAll(findColumnSegment(datetimeExpression.getRight()));
        }else if (expressionSegment instanceof ExpressionProjectionSegment){
            ExpressionProjectionSegment expressionProjectionSegment = (ExpressionProjectionSegment)expressionSegment;
            columnSegmentList.addAll(findColumnSegment(expressionProjectionSegment.getExpr()));
        }else if (expressionSegment instanceof  FunctionSegment){
            FunctionSegment functionSegment = (FunctionSegment) expressionSegment;
            functionSegment.getParameters().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        }else if (expressionSegment instanceof InExpression){
            InExpression inExpression = (InExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(inExpression.getLeft()));
            columnSegmentList.addAll(findColumnSegment(inExpression.getRight()));
            inExpression.getExpressionList().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        }else if(expressionSegment instanceof IntervalExpressionProjection){
            IntervalExpressionProjection intervalExpressionProjection = (IntervalExpressionProjection)expressionSegment;
            columnSegmentList.addAll(findColumnSegment(intervalExpressionProjection.getLeft()));
            columnSegmentList.addAll(findColumnSegment(intervalExpressionProjection.getMinus()));
            columnSegmentList.addAll(findColumnSegment(intervalExpressionProjection.getRight()));
        } else if (expressionSegment instanceof KeyValueSegment) {
            KeyValueSegment keyValueSegment = (KeyValueSegment)expressionSegment;
            columnSegmentList.addAll(findColumnSegment(keyValueSegment.getKey()));
            columnSegmentList.addAll(findColumnSegment(keyValueSegment.getValue()));
        } else if (expressionSegment instanceof ListExpression) {
            ListExpression listExpression = (ListExpression) expressionSegment;
            listExpression.getItems().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        } else if (expressionSegment instanceof MatchAgainstExpression) {
            MatchAgainstExpression matchAgainstExpression = (MatchAgainstExpression) expressionSegment;
            columnSegmentList.addAll(matchAgainstExpression.getColumns());
            columnSegmentList.addAll(findColumnSegment(matchAgainstExpression.getExpr()));
        } else if (expressionSegment instanceof NotExpression) {
            NotExpression notExpression = (NotExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(notExpression.getExpression()));
        } else if (expressionSegment instanceof OuterJoinExpression){
            OuterJoinExpression outerJoinExpression = (OuterJoinExpression) expressionSegment;
            columnSegmentList.add(outerJoinExpression.getColumnName());
        } else if (expressionSegment instanceof RowExpression) {
            RowExpression rowExpression = (RowExpression) expressionSegment;
            rowExpression.getItems().forEach(expressionSegment1 -> {
                columnSegmentList.addAll(findColumnSegment(expressionSegment1));
            });
        } else if (expressionSegment instanceof TypeCastExpression) {
            TypeCastExpression typeCastExpression = (TypeCastExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(typeCastExpression.getExpression()));
        } else if (expressionSegment instanceof UnaryOperationExpression) {
            UnaryOperationExpression unaryOperationExpression = (UnaryOperationExpression) expressionSegment;
            columnSegmentList.addAll(findColumnSegment(unaryOperationExpression.getExpression()));
        } else if (expressionSegment instanceof ValuesExpression) {
            ValuesExpression valuesExpression = (ValuesExpression) expressionSegment;
            valuesExpression.getRowConstructorList().forEach(insertValuesSegment -> {
               insertValuesSegment.getValues().forEach(expressionSegment1 -> {
                   columnSegmentList.addAll(findColumnSegment(expressionSegment1));
               });
            });
        } else if (expressionSegment instanceof LiteralExpressionSegment){
            //普通固定字符型字段，无需脱敏和访问控制
        }else {
            //todo 访问权限丢失
            System.out.println("访问权限丢失"+expressionSegment.getText());
        }
        return columnSegmentList;
    }

    private static void fillColumnAccessWithColumnSegment(TableSegment fromTableSegment, ColumnSegment column, Map<String, Map<String, List<String>>> accessMap) {
        Boolean columnFlag= false;
        if (column.getColumnBoundInfo()!=null
                && StringUtils.isNotEmpty(column.getColumnBoundInfo().getOriginalColumn().getValue())
                && StringUtils.isNotEmpty(column.getColumnBoundInfo().getOriginalTable().getValue())
                && StringUtils.isNotEmpty(column.getColumnBoundInfo().getOriginalDatabase().getValue())) {
            String databaseName = column.getColumnBoundInfo().getOriginalDatabase().getValue();
            String tableName = column.getColumnBoundInfo().getOriginalTable().getValue();
            String columnName = column.getColumnBoundInfo().getOriginalColumn().getValue();
            Map<String, List<String>> stringListMap = accessMap.get(databaseName);
            List<String> strings = stringListMap.get(tableName);
            if(strings==null){
                strings = new ArrayList<>();
            }
            if(!strings.contains(columnName)){
                strings.add(columnName);
            }
            stringListMap.put(tableName,strings);
            columnFlag =true;
        }
        List<TableSegment> ownerTableSegmentList=findOwnerTableSegment(fromTableSegment,column);
        if(!ownerTableSegmentList.isEmpty()){
            for(TableSegment ownerTableSegment:ownerTableSegmentList){
                if(ownerTableSegment instanceof  SimpleTableSegment){
                    SimpleTableSegment simpleTableSegment = (SimpleTableSegment) ownerTableSegment;
                    String databaseName = simpleTableSegment.getOwner().get().getIdentifier().getValue();
                    String tableName = simpleTableSegment.getTableName().getIdentifier().getValue();
                    String columnName = column.getIdentifier().getValue();
                    if(column.getColumnBoundInfo()!=null&&StringUtils.isNotEmpty(column.getColumnBoundInfo().getOriginalColumn().getValue())){
                        columnName = column.getColumnBoundInfo().getOriginalColumn().getValue();
                    }
                    Map<String, List<String>> stringListMap = accessMap.get(databaseName);
                    List<String> strings = stringListMap.get(tableName);
                    if(strings==null){
                        strings = new ArrayList<>();
                    }
                    if(!strings.contains(columnName)){
                        strings.add(columnName);
                    }
                    stringListMap.put(tableName,strings);
                } else {
                    Map<TableSegment,ProjectionSegment> tableSegmentListMap=findTableSegmentListMap(ownerTableSegment,column);
                    for(Map.Entry<TableSegment,ProjectionSegment> entry:tableSegmentListMap.entrySet()){
                        fillColumnAccess(entry.getKey(),entry.getValue(),accessMap);
                    }
                }
            }
        }else if(!columnFlag){
                //todo 访问权限丢失
                System.out.println("访问权限丢失"+column);
        }
    }

    private static List<TableSegment> findOwnerTableSegment(TableSegment fromTableSegment, ColumnSegment column) {
        List<TableSegment> ownerTableSegmentList = new ArrayList<>();
        if(fromTableSegment==null){
            return ownerTableSegmentList;
        }
        if(fromTableSegment instanceof SubqueryTableSegment){
            SubqueryTableSegment subqueryTableSegment = (SubqueryTableSegment) fromTableSegment;
            ownerTableSegmentList.addAll(findOwnerTableSegmentListBySubQueryTableSegment(subqueryTableSegment,column));
        }else if(!column.getOwner().isPresent()){
            ownerTableSegmentList.add(fromTableSegment);
            /*if(fromTableSegment instanceof SimpleTableSegment){
                ownerTableSegmentList.add(fromTableSegment);
            } else if ((fromTableSegment instanceof SubqueryTableSegment)) {
                SubqueryTableSegment subqueryTableSegment = (SubqueryTableSegment) fromTableSegment;
                ownerTableSegmentList.addAll(findOwnerTableSegmentListBySubQueryTableSegment(subqueryTableSegment,column));
            } else {
                //todo 访问权限丢失
                System.out.println("访问权限丢失:"+column.getIdentifier().getValue());
            }*/
        }else if(fromTableSegment.getAliasName().isPresent()&&column.getOwner().get().getIdentifier().getValue().equals(fromTableSegment.getAliasName().get())){
            ownerTableSegmentList.add(fromTableSegment);
        } else if(fromTableSegment instanceof JoinTableSegment){
                JoinTableSegment joinTableSegment = (JoinTableSegment)fromTableSegment;
                ownerTableSegmentList.addAll(findOwnerTableSegment(joinTableSegment.getLeft(),column));
                ownerTableSegmentList.addAll(findOwnerTableSegment(joinTableSegment.getRight(),column));
        }else {
            //todo 访问权限丢失
            System.out.println("访问权限丢失"+column);
        }
        return ownerTableSegmentList;
    }

    private static List<TableSegment> findOwnerTableSegmentListBySubQueryTableSegment(SubqueryTableSegment subqueryTableSegment, ColumnSegment column) {
        List<TableSegment> ownerTableSegmentList = new ArrayList<>();
        SelectStatement select = subqueryTableSegment.getSubquery().getSelect();
        if(select.getFrom().isPresent()){
            ownerTableSegmentList.addAll(findOwnerTableSegment(select.getFrom().get(), column));
        }
        if(select.getCombine().isPresent()){
            CombineSegment combineSegment = select.getCombine().get();
            SubquerySegment left = combineSegment.getLeft();
            ownerTableSegmentList.addAll(findOwnerTableSegmentBySubquerySegment(left,column));
            SubquerySegment right = combineSegment.getRight();
            ownerTableSegmentList.addAll(findOwnerTableSegmentBySubquerySegment(right,column));
        }

        return  ownerTableSegmentList;
    }

    private static List<TableSegment> findOwnerTableSegmentBySubquerySegment(SubquerySegment left, ColumnSegment column) {
        List<TableSegment> ownerTableSegmentList = new ArrayList<>();
        SelectStatement select = left.getSelect();
        TableSegment tableSegment = select.getFrom().get();
        ownerTableSegmentList.add(tableSegment);
        if(select.getCombine().isPresent()){
            CombineSegment combineSegment = select.getCombine().get();
            SubquerySegment left1 = combineSegment.getLeft();
            SubquerySegment right1 = combineSegment.getRight();
            ownerTableSegmentList.addAll(findOwnerTableSegmentBySubquerySegment(left1,column));
            ownerTableSegmentList.addAll(findOwnerTableSegmentBySubquerySegment(right1,column));
        }
        return ownerTableSegmentList;
    }

    private static Map<TableSegment, ProjectionSegment> findTableSegmentListMap(TableSegment fromTableSegment, ColumnSegment column) {
        Map<TableSegment, ProjectionSegment> tableSegmentListMap = new HashMap<>();
        if(fromTableSegment==null){
            return tableSegmentListMap;
        }
            Collection<ProjectionSegment> projections = new ArrayList<>();
            TableSegment tableSegment;
            if(fromTableSegment instanceof CommonTableExpressionSegment){
                CommonTableExpressionSegment commonTableExpressionSegment = (CommonTableExpressionSegment) fromTableSegment;
                SubquerySegment subquery = commonTableExpressionSegment.getSubquery();
                projections = subquery.getSelect().getProjections().getProjections();
                tableSegment = subquery.getSelect().getFrom().get();
                tableSegmentListMap = findMapProjectSegment(tableSegment,column,projections);
            }else if(fromTableSegment instanceof SubqueryTableSegment){
                SubqueryTableSegment subqueryTableSegment = (SubqueryTableSegment) fromTableSegment;
                SelectStatement select = subqueryTableSegment.getSubquery().getSelect();
                tableSegmentListMap = findMapProjectSegmentBySelectStatement(select,column);
            }else {
                //todo 访问权限丢失
                System.out.println("访问权限丢失:"+column.getIdentifier().getValue());
            }
            return tableSegmentListMap;
    }

    private static Map<TableSegment, ProjectionSegment> findMapProjectSegmentBySelectStatement(SelectStatement select, ColumnSegment column) {
        Map<TableSegment, ProjectionSegment> tableSegmentListMap = new HashMap<>();
        Collection<ProjectionSegment> projections = select.getProjections().getProjections();
        TableSegment tableSegment = select.getFrom().get();
        findMapProjectSegment(tableSegment,column,projections).entrySet().forEach(entry->{
            tableSegmentListMap.put(entry.getKey(),entry.getValue());
        });
        if(select.getCombine().isPresent()){
            CombineSegment combineSegment = select.getCombine().get();
            findMapProjectSegmentBySelectStatement(combineSegment.getLeft().getSelect(), column).entrySet().forEach(entry->{
                tableSegmentListMap.put(entry.getKey(),entry.getValue());
            });
            findMapProjectSegmentBySelectStatement(combineSegment.getRight().getSelect(), column).entrySet().forEach(entry->{
                tableSegmentListMap.put(entry.getKey(),entry.getValue());
            });
        }
        return tableSegmentListMap;
    }

    private static Map<TableSegment, ProjectionSegment> findMapProjectSegment(TableSegment tableSegment, ColumnSegment column, Collection<ProjectionSegment> projections) {
        Map<TableSegment, ProjectionSegment> tableSegmentListMap = new HashMap<>();
        projections.forEach(projectionSegment -> {
            if(projectionSegment instanceof ColumnProjectionSegment){
                ColumnProjectionSegment columnProjectionSegment = (ColumnProjectionSegment)projectionSegment;
                if((columnProjectionSegment.getAliasName().isPresent()&&column.getIdentifier().getValue().equals(columnProjectionSegment.getAliasName().get()))||(columnProjectionSegment.getColumn().getIdentifier().getValue().equals(column.getIdentifier().getValue()))){
                    tableSegmentListMap.put(tableSegment,projectionSegment);
                }
            } else if (projectionSegment instanceof AggregationProjectionSegment) {
                AggregationProjectionSegment aggregationProjectionSegment = (AggregationProjectionSegment) projectionSegment;
                if(aggregationProjectionSegment.getAliasName().isPresent()&&column.getIdentifier().getValue().equals(aggregationProjectionSegment.getAliasName().get())){
                    tableSegmentListMap.put(tableSegment,projectionSegment);
                }
            } else if (projectionSegment instanceof ExpressionProjectionSegment) {
                ExpressionProjectionSegment expressionProjectionSegment = (ExpressionProjectionSegment) projectionSegment;
                if(expressionProjectionSegment.getAliasName().isPresent()&&column.getIdentifier().getValue().equals(expressionProjectionSegment.getAliasName().get())){
                    tableSegmentListMap.put(tableSegment,projectionSegment);
                }
            } else if (projectionSegment instanceof ShorthandProjectionSegment) {
                ShorthandProjectionSegment shorthandProjectionSegment = (ShorthandProjectionSegment) projectionSegment;
                Collection<ProjectionSegment> actualProjectionSegments = shorthandProjectionSegment.getActualProjectionSegments();
                Map<TableSegment, ProjectionSegment> tableSegmentListMap2 =findMapProjectSegment(tableSegment,column,actualProjectionSegments);
                for(Map.Entry<TableSegment,ProjectionSegment> entry:tableSegmentListMap2.entrySet()){
                    tableSegmentListMap.put(entry.getKey(),entry.getValue());
                }
            }else {
                //todo 访问权限丢失
                System.out.println("访问权限丢失:"+column.getIdentifier().getValue());
            }
        });
        if(tableSegmentListMap.isEmpty()){
            //todo 访问权限丢失
            System.out.println("访问权限丢失:"+column.getIdentifier().getValue());
        }
        return tableSegmentListMap;
    }
}
