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
import com.cedarsoftware.util.CaseInsensitiveSet;
import com.google.common.base.Preconditions;
import org.apache.shardingsphere.authority.rule.attribute.AccessControlUserMapperRuleAttribute;
import org.apache.shardingsphere.authority.rule.attribute.SensitiveLevelTableMapperRuleAttribute;
import org.apache.shardingsphere.infra.rule.PartialRuleUpdateSupported;
import org.apache.shardingsphere.infra.rule.attribute.RuleAttributes;
import org.apache.shardingsphere.infra.rule.scope.DatabaseRule;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Mask rule.
 */
public final class SensitiveLevelRule implements DatabaseRule, PartialRuleUpdateSupported<SensitiveLevelRuleConfiguration> {

    private final AtomicReference<SensitiveLevelRuleConfiguration> configuration = new AtomicReference<>();

    private final Map<String, SensitiveLevelTable> tables = new CaseInsensitiveMap<>(Collections.emptyMap(), new ConcurrentHashMap<>());

    private final AtomicReference<RuleAttributes> attributes = new AtomicReference<>();

    public SensitiveLevelRule(final SensitiveLevelRuleConfiguration ruleConfig) {
        configuration.set(ruleConfig);
        ruleConfig.getTables().forEach(each -> tables.put(each.getName(), new SensitiveLevelTable(each)));
        //属性暂时用不到，看后面用不用得到
        attributes.set(new RuleAttributes(new SensitiveLevelTableMapperRuleAttribute(tables.keySet())));
    }
    
    /**
     * Find mask table.
     *
     * @param tableName table name
     * @return found mask table
     */
    public Optional<SensitiveLevelTable> findSensitiveLevelTable(final String tableName) {
        return Optional.ofNullable(tables.get(tableName));
    }
    
    @Override
    public RuleAttributes getAttributes() {
        return attributes.get();
    }
    
    @Override
    public SensitiveLevelRuleConfiguration getConfiguration() {
        return configuration.get();
    }
    
    @Override
    public void updateConfiguration(final SensitiveLevelRuleConfiguration toBeUpdatedRuleConfig) {
        configuration.set(toBeUpdatedRuleConfig);
    }
    
    @Override
    public boolean partialUpdate(final SensitiveLevelRuleConfiguration toBeUpdatedRuleConfig) {
        Collection<String> toBeUpdatedTableNames = toBeUpdatedRuleConfig.getTables().stream().map(SensitiveLevelTableConfiguration::getName).collect(Collectors.toCollection(CaseInsensitiveSet::new));
        Collection<String> toBeAddedTableNames = toBeUpdatedTableNames.stream().filter(each ->!tables.containsKey(each)).collect(Collectors.toList());
        if(!toBeAddedTableNames.isEmpty()){
            toBeAddedTableNames.forEach(each ->addTable(each,toBeUpdatedRuleConfig));
            return true;
        }

        Collection<String> toBeRemovedUserNames = tables.keySet().stream().filter(each -> !toBeUpdatedTableNames.contains(each)).collect(Collectors.toList());
        if(!toBeRemovedUserNames.isEmpty()) {
            toBeRemovedUserNames.forEach(tables::remove);
            return true;
        }
        return false;
    }
    
    private void addTable(final String tableName, final SensitiveLevelRuleConfiguration toBeUpdatedRuleConfig) {
        SensitiveLevelTableConfiguration tableConfig = getTableConfiguration(tableName,toBeUpdatedRuleConfig);
        tables.put(tableName,new SensitiveLevelTable(tableConfig));
    }
    
    private SensitiveLevelTableConfiguration getTableConfiguration(final String tableName, final SensitiveLevelRuleConfiguration toBeUpdatedRuleConfig) {
        Optional<SensitiveLevelTableConfiguration> result = toBeUpdatedRuleConfig.getTables().stream().filter(table -> table.getName().equals(table)).findFirst();
        Preconditions.checkState(result.isPresent());
        return result.get();
    }
}
