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

package org.apache.shardingsphere.authority.rule;

import com.cedarsoftware.util.CaseInsensitiveMap;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mask table.
 */
@Data
public final class AccessControlUser {

    private final Boolean allFlag;
    private final Map<Long,AccessControlCatalogRule> catalogs;
    private final Map<String, AccessControlTable> tables;

    public AccessControlUser(final AccessControlUserRuleConfiguration config) {
        this.allFlag = config.getAllFlag();
        this.catalogs = config.getCatalogs().stream().collect(Collectors.toMap(AccessControlCatalogRuleConfiguration::getThemeDomainId,
                each->new AccessControlCatalogRule(each.getThemeDomainId(),each.getAssetTableAccessFlag(),each.getAssetApiAccessFlag(),each.getAssetIndicatorAccessFlag(),each.getAssetReportTableAccessFlag(),each.getDesensitizeWhiteListFlag(),each.getExpirationTime(),each.getAssetTableAccessTime(),each.getAssetApiAccessTime(),each.getAssetIndicatorAccessTime(),each.getAssetReportTableAccessTime()),(v1,v2)->v1,CaseInsensitiveMap::new));
        this.tables = config.getTables().stream().collect(Collectors.toMap(AccessControlTableRuleConfiguration::getTableName,
                each -> new AccessControlTable(each.getAllFlag(),each.getTableName(),each.getDesensitizeWhiteListFlag(),each.getColumns(),each.getExpirationTime(),each.getFilterConditionSql()), (oldValue, currentValue) -> oldValue, CaseInsensitiveMap::new));
    }
}
