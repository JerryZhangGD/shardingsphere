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

package org.apache.shardingsphere.mask.rule;

import com.cedarsoftware.util.CaseInsensitiveMap;
import org.apache.shardingsphere.infra.annotation.HighFrequencyInvocation;
import org.apache.shardingsphere.mask.config.AccessControlRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlTableRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.MaskColumnRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.MaskTableRuleConfiguration;
import org.apache.shardingsphere.mask.spi.MaskAlgorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mask table.
 */
public final class AccessControlUser {

    private final Boolean allFlag;
    private final Map<String, AccessControlTable> tables;

    public AccessControlUser(final AccessControlUserRuleConfiguration config) {
        this.allFlag = config.getAllFlag();
        this.tables = config.getTables().stream().collect(Collectors.toMap(AccessControlTableRuleConfiguration::getTableName,
                each -> new AccessControlTable(each.getAllFlag(),each.getTableName(),each.getColumns()), (oldValue, currentValue) -> oldValue, CaseInsensitiveMap::new));
    }
}
