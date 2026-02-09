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

package org.apache.shardingsphere.infra.exception.kernel.metadata;

import org.apache.shardingsphere.infra.exception.core.external.sql.sqlstate.XOpenSQLState;
import org.apache.shardingsphere.infra.exception.core.external.sql.type.kernel.category.MetaDataSQLException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Table not found exception.
 */
public final class HasNotAccessRightException extends MetaDataSQLException {

    private static final long serialVersionUID = -2507596759730534895L;

    public HasNotAccessRightException(final String databaseName ) {
        super(XOpenSQLState.PRIVILEGE_NOT_GRANTED, 2, "你暂时没有【'%s'】数据库的权限，请先申请权限", databaseName);
    }

    public HasNotAccessRightException(final String databaseName,final List<String> tableNameList) {
        super(XOpenSQLState.PRIVILEGE_NOT_GRANTED, 2, "你暂时没有【'%s'.'%s'】数据表的权限，请先申请权限", databaseName, tableNameList.stream().collect(Collectors.joining(",")));
    }

    public HasNotAccessRightException(final String databaseName,final String tableName,final List<String> columnList) {
        super(XOpenSQLState.PRIVILEGE_NOT_GRANTED, 2, "由于管理员为你设置了列权限，你暂时没有【'%s'.'%s'】数据表中【%s】列的权限，请勿使用select * 进行查询，改为明确的查询列，或者再次申请权限并联系管理员进行设置。", databaseName, tableName, columnList.stream().collect(Collectors.joining(",")));
    }

    public HasNotAccessRightException(final String assetTypeCnName,final String assetName) {
        super(XOpenSQLState.PRIVILEGE_NOT_GRANTED, 2, "你暂时没有【%s】%s的权限，请先申请权限",assetName, assetTypeCnName);
    }
}
