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

package org.apache.shardingsphere.proxy.frontend.mysql.command;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.db.protocol.mysql.packet.MySQLPacket;
import org.apache.shardingsphere.db.protocol.mysql.packet.command.MySQLCommandPacket;
import org.apache.shardingsphere.db.protocol.mysql.packet.command.MySQLCommandPacketType;
import org.apache.shardingsphere.db.protocol.mysql.packet.generic.MySQLEofPacket;
import org.apache.shardingsphere.db.protocol.mysql.payload.MySQLPacketPayload;
import org.apache.shardingsphere.db.protocol.packet.DatabasePacket;
import org.apache.shardingsphere.db.protocol.packet.command.CommandPacket;
import org.apache.shardingsphere.db.protocol.packet.command.CommandPacketType;
import org.apache.shardingsphere.db.protocol.payload.PacketPayload;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.Projection;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.SensitiveSource;
import org.apache.shardingsphere.infra.binder.context.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.config.props.ConfigurationPropertyKey;
import org.apache.shardingsphere.infra.hint.HintValueContext;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.infra.session.connection.ConnectionContext;
import org.apache.shardingsphere.infra.session.query.QueryContext;
import org.apache.shardingsphere.proxy.backend.connector.ProxyDatabaseConnectionManager;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.session.ConnectionSession;
import org.apache.shardingsphere.proxy.frontend.command.CommandExecuteEngine;
import org.apache.shardingsphere.proxy.frontend.command.executor.CommandExecutor;
import org.apache.shardingsphere.proxy.frontend.command.executor.QueryCommandExecutor;
import org.apache.shardingsphere.proxy.frontend.command.executor.ResponseType;
import org.apache.shardingsphere.proxy.frontend.mysql.command.query.text.query.MySQLComQueryPacketExecutor;
import org.apache.shardingsphere.proxy.frontend.mysql.err.MySQLErrorPacketFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Command execute engine for MySQL.
 */
public final class MySQLCommandExecuteEngine implements CommandExecuteEngine {
    
    @Override
    public MySQLCommandPacketType getCommandPacketType(final PacketPayload payload) {
        return MySQLCommandPacketType.valueOf(((MySQLPacketPayload) payload).readInt1());
    }
    
    @Override
    public MySQLCommandPacket getCommandPacket(final PacketPayload payload, final CommandPacketType type, final ConnectionSession connectionSession) {
        return MySQLCommandPacketFactory.newInstance((MySQLCommandPacketType) type, (MySQLPacketPayload) payload, connectionSession);
    }
    
    @Override
    public CommandExecutor getCommandExecutor(final CommandPacketType type, final CommandPacket packet, final ConnectionSession connectionSession) throws SQLException {
        return MySQLCommandExecutorFactory.newInstance((MySQLCommandPacketType) type, packet, connectionSession);
    }
    
    @Override
    public MySQLPacket getErrorPacket(final Exception cause) {
        return MySQLErrorPacketFactory.newInstance(cause);
    }
    
    @Override
    public void writeQueryData(final long duration,final ChannelHandlerContext context,
                               final ProxyDatabaseConnectionManager databaseConnectionManager, final QueryCommandExecutor queryCommandExecutor, final int headerPackagesCount) throws SQLException {
        if (ResponseType.QUERY != queryCommandExecutor.getResponseType() || !context.channel().isActive()) {
            return;
        }
        int total = 0;
        int count = 0;
        int flushThreshold = ProxyContext.getInstance().getContextManager().getMetaDataContexts().getMetaData().getProps().<Integer>getValue(ConfigurationPropertyKey.PROXY_FRONTEND_FLUSH_THRESHOLD);
        while (queryCommandExecutor.next()) {
            total++;
            count++;
            databaseConnectionManager.getResourceLock().doAwait(context);
            DatabasePacket dataValue = queryCommandExecutor.getQueryRowPacket();
            context.write(dataValue);
            if (flushThreshold == count) {
                context.flush();
                count = 0;
            }
        }
        context.write(new MySQLEofPacket(ServerStatusFlagCalculator.calculateFor(databaseConnectionManager.getConnectionSession())));




        List<SensitiveSource> sensitiveSourceList = new ArrayList<>();
        Set<String> keySet= new HashSet<>();
        SQLStatementContext sqlStatementContext = databaseConnectionManager.getConnectionSession().getQueryContext().getSqlStatementContext();
        if(sqlStatementContext instanceof SelectStatementContext) {
            SelectStatementContext selectStatementContext = (SelectStatementContext) sqlStatementContext;
            List<Projection> projectionList = selectStatementContext.getProjectionsContext().getExpandProjections();
            if(projectionList!=null){
                for(Projection projection:projectionList){
                    List<SensitiveSource> sensitiveSourceList1 = projection.getSensitiveSourceList();
                    if(sensitiveSourceList1!=null){
                        for(SensitiveSource sensitiveSource:sensitiveSourceList1){
                            String key = sensitiveSource.getDatabaseName()+"."+sensitiveSource.getTableName()+"."+sensitiveSource.getColumnName();
                            if(!keySet.contains(key)){
                                keySet.add(key);
                                sensitiveSourceList.add(sensitiveSource);
                            }
                        }
                    }
                }
            }
        }


        QueryContext queryContext = databaseConnectionManager.getConnectionSession().getQueryContext();
        String sql = queryContext.getSql();
        String riskType = "other";
        String opeUser ="";
        HintValueContext hintValueContext = queryContext.getHintValueContext();
        if(hintValueContext!=null){
            String riskType1 = hintValueContext.getRiskType();
            if(StringUtils.isNotEmpty(riskType1)){
                riskType = riskType1;
            }
            String opeUser1 = hintValueContext.getOpeUser();
            if(StringUtils.isNotEmpty(opeUser1)){
                opeUser = opeUser1;
            }
        }
        String durationStr = duration+"ms";
        String sourceIp = "";
        String user = "";
        ConnectionContext connectionContext = queryContext.getConnectionContext();
        if(connectionContext!=null){
            Grantee grantee = connectionContext.getGrantee();
            if(grantee!=null){
                String hostname = grantee.getHostname();
                if(StringUtils.isNotEmpty(hostname)){
                    sourceIp = hostname;
                }
                String username = grantee.getUsername();
                if(StringUtils.isNotEmpty(username)){
                    user = username;
                }
            }
        }
        RecordSqlLogThread recordSqlLogThread = new RecordSqlLogThread();
        recordSqlLogThread.setSql(sql);
        recordSqlLogThread.setUser(user);
        recordSqlLogThread.setOpeUser(opeUser);
        recordSqlLogThread.setMethod("select");
        recordSqlLogThread.setSourceIp(sourceIp);
        recordSqlLogThread.setTotal(total);
        recordSqlLogThread.setRiskType(riskType);
        recordSqlLogThread.setSensitiveSourceList(sensitiveSourceList);
        recordSqlLogThread.setDetail("耗时: "+durationStr+", 行数: "+total);





        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(recordSqlLogThread);
        executor.shutdown();
        System.out.println("线程调用结束");

        System.out.println(total);
    }


}
