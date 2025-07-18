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

package org.apache.shardingsphere.authority.rule.changed;

import org.apache.shardingsphere.authority.metadata.nodepath.AccessControlRuleNodePathProvider;
import org.apache.shardingsphere.authority.metadata.nodepath.SensitiveLevelRuleNodePathProvider;
import org.apache.shardingsphere.authority.rule.*;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlSensitiveLevelTableConfiguration;
import org.apache.shardingsphere.authority.yaml.swapper.rule.YamlAccessControlUserRuleConfigurationSwapper;
import org.apache.shardingsphere.authority.yaml.swapper.rule.YamlSensitiveLevelTableConfigurationSwapper;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.util.yaml.YamlEngine;
import org.apache.shardingsphere.mode.event.dispatch.rule.alter.AlterRuleItemEvent;
import org.apache.shardingsphere.mode.event.dispatch.rule.drop.DropNamedRuleItemEvent;
import org.apache.shardingsphere.mode.event.dispatch.rule.drop.DropRuleItemEvent;
import org.apache.shardingsphere.mode.spi.RuleItemConfigurationChangedProcessor;

import java.util.LinkedList;

/**
 * Mask table changed processor.
 */
public final class SensitiveLevelTableChangedProcessor implements RuleItemConfigurationChangedProcessor<SensitiveLevelRuleConfiguration, SensitiveLevelTableConfiguration> {
    
    @Override
    public SensitiveLevelTableConfiguration swapRuleItemConfiguration(final AlterRuleItemEvent event, final String yamlContent) {
        return new YamlSensitiveLevelTableConfigurationSwapper().swapToObject(YamlEngine.unmarshal(yamlContent, YamlSensitiveLevelTableConfiguration.class));
    }
    
    @Override
    public SensitiveLevelRuleConfiguration findRuleConfiguration(final ShardingSphereDatabase database) {
        return database.getRuleMetaData().findSingleRule(SensitiveLevelRule.class)
                .map(optional -> getConfiguration(optional.getConfiguration())).orElseGet(() -> new SensitiveLevelRuleConfiguration(new LinkedList<>()));
    }
    
    private SensitiveLevelRuleConfiguration getConfiguration(final SensitiveLevelRuleConfiguration config) {
        return null == config.getTables() ? new SensitiveLevelRuleConfiguration(new LinkedList<>()) : config;
    }
    
    @Override
    public void changeRuleItemConfiguration(final AlterRuleItemEvent event, final SensitiveLevelRuleConfiguration currentRuleConfig, final SensitiveLevelTableConfiguration toBeChangedItemConfig) {
        // TODO refactor DistSQL to only persist config
        currentRuleConfig.getTables().removeIf(each ->each.getName().equals(toBeChangedItemConfig.getName()));
        currentRuleConfig.getTables().add(toBeChangedItemConfig);
    }
    
    @Override
    public void dropRuleItemConfiguration(final DropRuleItemEvent event, final SensitiveLevelRuleConfiguration currentRuleConfig) {
        currentRuleConfig.getTables().removeIf(each -> each.getName().equals(((DropNamedRuleItemEvent) event).getItemName()));
    }
    
    @Override
    public String getType() {
        return SensitiveLevelRuleNodePathProvider.RULE_TYPE + "." + SensitiveLevelRuleNodePathProvider.TABLES;
    }
}
