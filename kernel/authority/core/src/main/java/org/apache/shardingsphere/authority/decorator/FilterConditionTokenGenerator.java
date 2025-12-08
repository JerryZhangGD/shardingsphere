package org.apache.shardingsphere.authority.decorator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.authority.rule.AccessControlRule;
import org.apache.shardingsphere.authority.rule.AccessControlTable;
import org.apache.shardingsphere.authority.rule.AccessControlUser;
import org.apache.shardingsphere.infra.binder.context.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.generator.CollectionSQLTokenGenerator;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.pojo.SQLToken;
import org.apache.shardingsphere.infra.session.connection.ConnectionContext;
import org.apache.shardingsphere.infra.session.query.QueryContext;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.OwnerSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.generic.table.TableNameSegment;

import java.util.*;

public class FilterConditionTokenGenerator implements CollectionSQLTokenGenerator<SQLStatementContext> {
    private AccessControlRule rule;

    private SQLStatementContext sqlStatementContext;
    private ConnectionContext connectionContext;

    private QueryContext queryContext;
    public FilterConditionTokenGenerator(AccessControlRule rule, SQLStatementContext sqlStatementContext, ConnectionContext connectionContext,QueryContext queryContext) {
        this.rule = rule;
        this.sqlStatementContext = sqlStatementContext;
        this.connectionContext = connectionContext;
        this.queryContext = queryContext;
    }

    @Override
    public Collection<SQLToken> generateSQLTokens(SQLStatementContext sqlStatementContext) {
        Collection<SQLToken> result = new LinkedList<>();
        if(sqlStatementContext instanceof SelectStatementContext){
            result.addAll(generateFilterConditionTokens(((SelectStatementContext) sqlStatementContext).getTablesContext().getSimpleTables(),this.connectionContext,this.rule));
        }
        return result;
    }

    private Collection<FilterConditionToken> generateFilterConditionTokens(Collection<SimpleTableSegment> simpleTables, ConnectionContext connectionContext,AccessControlRule rule) {
        Collection<FilterConditionToken> result = new LinkedList<>();

        if(connectionContext!=null){
            Grantee grantee = connectionContext.getGrantee();
            if(grantee!=null){
                String username = grantee.getUsername();
                if(StringUtils.isNotEmpty(username)){
                    if(simpleTables!=null&&!simpleTables.isEmpty()){
                        for(SimpleTableSegment simpleTableSegment:simpleTables){

                            if(simpleTableSegment.getOwner().isPresent()&&simpleTableSegment.getTableName()!=null){
                                OwnerSegment ownerSegment = simpleTableSegment.getOwner().get();
                                TableNameSegment tableNameSegment = simpleTableSegment.getTableName();
                                int startIndex = ownerSegment.getStartIndex();
                                int stopIndex = tableNameSegment.getStopIndex();
                                String dbName = ownerSegment.getIdentifier().getValue();
                                String tableName = tableNameSegment.getIdentifier().getValue();
                                if(queryContext!=null){
                                    ShardingSphereMetaData metaData = queryContext.getMetaData();
                                    if(metaData!=null){
                                        ShardingSphereDatabase shardingSphereDatabase = metaData.getDatabase(dbName);
                                        if(shardingSphereDatabase!=null){
                                            Optional<AccessControlRule> singleRule = shardingSphereDatabase.getRuleMetaData().findSingleRule(AccessControlRule.class);
                                            if(singleRule.isPresent()){
                                                AccessControlRule accessControlRule = singleRule.get();
                                                Optional<AccessControlUser> accessControlUserOptional = accessControlRule.findAccessControlUser(username);
                                                if(accessControlUserOptional.isPresent()){
                                                    AccessControlUser accessControlUser = accessControlUserOptional.get();
                                                    Map<String, AccessControlTable> tables = accessControlUser.getTables();
                                                    AccessControlTable accessControlTable = tables.get(tableName);
                                                    if(accessControlTable!=null){
                                                        String filterConditionSql = accessControlTable.getFilterConditionSql();
                                                        if(StringUtils.isNotEmpty(filterConditionSql)){
                                                            StringBuffer subSelectSql = new StringBuffer();
                                                            subSelectSql.append(" (select * from ");
                                                            subSelectSql.append(dbName).append(".").append(tableName).append(" where ").append(filterConditionSql).append(") ");
                                                            if(!simpleTableSegment.getAlias().isPresent()){
                                                                Date date = new Date();
                                                                subSelectSql.append(" as result_"+date.getTime());
                                                            }
                                                            FilterConditionToken filterConditionToken = new FilterConditionToken(startIndex,stopIndex,subSelectSql.toString());
                                                            result.add(filterConditionToken);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean isGenerateSQLToken(SQLStatementContext sqlStatementContext) {
        return sqlStatementContext instanceof SelectStatementContext;
    }

    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    public void setConnectionContext(ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }
}
