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

package org.apache.shardingsphere.infra.rewrite.sql.impl;

import org.apache.shardingsphere.infra.rewrite.sql.token.common.pojo.RouteUnitAware;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.pojo.SQLToken;
import org.apache.shardingsphere.infra.route.context.RouteUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL builder with route.
 */
public final class RouteSQLBuilder extends AbstractSQLBuilder {
    
    private final RouteUnit routeUnit;
    
    public RouteSQLBuilder(final String sql, final List<SQLToken> sqlTokens, final RouteUnit routeUnit) {
        //todo 删除sqlTokens替换，直接使用原始sql，为了代理，如果是分库分表其它功能会报错
        super(sql,new ArrayList<>());
        //super(sql, sqlTokens);
        this.routeUnit = routeUnit;
    }
    
    @Override
    protected String getSQLTokenText(final SQLToken sqlToken) {
        if (sqlToken instanceof RouteUnitAware) {
            return ((RouteUnitAware) sqlToken).toString(routeUnit);
        }
        return sqlToken.toString();
    }
}
