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

package org.apache.shardingsphere.proxy.frontend.mysql.command.query.binary.prepare;

import org.apache.shardingsphere.infra.database.core.DefaultDatabase;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereColumn;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereSchema;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereTable;
import org.apache.shardingsphere.infra.parser.ShardingSphereSQLParserEngine;
import org.apache.shardingsphere.infra.spi.type.typed.TypedSPILoader;
import org.apache.shardingsphere.sql.parser.api.CacheOption;
import org.apache.shardingsphere.sql.parser.statement.core.statement.SQLStatement;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MySQLComStmtPrepareParameterMarkerExtractorTest {
    
    @Test
    void assertFindColumnsOfParameterMarkersForInsertStatement() {
        String sql = "select \n" +
                "a.DQ, -- 大区\n" +
                "a.XQ, -- 销区\n" +
                "a.PQ, -- 片区\n" +
                "a.KHBM,-- 勤哲客户编码\n" +
                "a.KH, -- 客户描述\n" +
                "a.CUSTOMER,-- sap 客户编码\n" +
                "a.BNJH,-- 本年计划\n" +
                "a.BNXL,-- 本年销量\n" +
                "a.NLJTQXL, -- 同期年累计销量\n" +
                "a.BYJH, -- 本月计划\n" +
                "a.BYXL, -- 本月销量\n" +
                "a.YTQXL, -- 月同期累计销量\n" +
                "b.ZRXL, -- 昨日销量\n" +
                "a.QKJE, -- 欠款金额\n" +
                "a.CZQQK, -- 超周期欠款\n" +
                "a.YJXL, -- 预计销量\n" +
                "a.ZZTS, -- 周转天数\n" +
                "a.LJSK, -- 累计收款\n" +
                "a.YJSK -- 预计收款\n" +
                "from  (\n" +
                "\t\tselect  DQ,XQ,PQ,KHBM,KH,CUSTOMER,sum(BNJH) as BNJH,sum(BNXL) BNXL,sum(BYXL) BYXL,sum(NLJTQXL) as NLJTQXL,sum(BYJH) as BYJH,\n" +
                "\t\tsum(YJXL) YJXL,sum(LJSK) LJSK,sum(YJSK) YJSK,sum(QKJE) QKJE,sum(CZQQK) CZQQK,sum(ZZTS) ZZTS ,sum(YTQXL) as YTQXL\n" +
                "\t\tfrom  dwd.DWD_SD_XCLKHMX_DF\n" +
                "\t\tgroup  by  DQ,XQ,PQ,KHBM,KH,CUSTOMER\n" +
                ") a \n" +
                "left  join \n" +
                "\t\t(select  DQ,PQ,XQ2,KHBM,KH,RQ,sum(ZDS) as ZRXL  from  ods.ODS_QZ_XCLXSMX_DF \n" +
                "\t\twhere  DATE_FORMAT(RQ ,'%Y-%m-%d') =  DATE_FORMAT(DATE_SUB(now(),INTERVAL '1' DAY),'%Y-%m-%d')\n" +
                "\t\tand KHBM is  NOT  null  and ifnull(XSSM,'其他') <>  '试料'\n" +
                "\t\tgroup  by  DQ,PQ,XQ2,KHBM,KH,RQ,RQ\n" +
                ") b on a.KHBM = b.KHBM;";
        SQLStatement sqlStatement = new ShardingSphereSQLParserEngine(TypedSPILoader.getService(DatabaseType.class, "MySQL"), new CacheOption(0, 0L), new CacheOption(0, 0L)).parse(sql, false);
        ShardingSphereSchema schema = prepareSchema();
        List<ShardingSphereColumn> actual = MySQLComStmtPrepareParameterMarkerExtractor.findColumnsOfParameterMarkers(sqlStatement, schema);
        assertThat(actual.get(0), is(schema.getTable("user").getColumn("name")));
        assertThat(actual.get(1), is(schema.getTable("user").getColumn("age")));
        assertThat(actual.get(2), is(schema.getTable("user").getColumn("id")));
        assertThat(actual.get(3), is(schema.getTable("user").getColumn("age")));
    }
    
    private ShardingSphereSchema prepareSchema() {
        ShardingSphereTable table = new ShardingSphereTable();
        table.putColumn(new ShardingSphereColumn("id", Types.BIGINT, true, false, false, false, true, false));
        table.putColumn(new ShardingSphereColumn("name", Types.VARCHAR, false, false, false, false, false, false));
        table.putColumn(new ShardingSphereColumn("age", Types.SMALLINT, false, false, false, false, true, false));
        ShardingSphereSchema result = new ShardingSphereSchema(DefaultDatabase.LOGIC_NAME);
        result.getTables().put("user", table);
        return result;
    }
}
