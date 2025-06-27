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

package org.apache.shardingsphere.mask.rule.changed;

import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.util.yaml.YamlEngine;
import org.apache.shardingsphere.mask.config.AccessControlRuleConfiguration;
import org.apache.shardingsphere.mask.config.MaskRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.metadata.nodepath.AccessControlRuleNodePathProvider;
import org.apache.shardingsphere.mask.rule.AccessControlRule;
import org.apache.shardingsphere.mask.rule.MaskRule;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.swapper.rule.YamlAccessControlUserRuleConfigurationSwapper;
import org.apache.shardingsphere.mode.event.dispatch.rule.alter.AlterRuleItemEvent;
import org.apache.shardingsphere.mode.event.dispatch.rule.drop.DropNamedRuleItemEvent;
import org.apache.shardingsphere.mode.event.dispatch.rule.drop.DropRuleItemEvent;
import org.apache.shardingsphere.mode.spi.RuleItemConfigurationChangedProcessor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Mask table changed processor.
 */
public final class AccessControlUserChangedProcessor implements RuleItemConfigurationChangedProcessor<AccessControlRuleConfiguration, AccessControlUserRuleConfiguration> {
    
    @Override
    public AccessControlUserRuleConfiguration swapRuleItemConfiguration(final AlterRuleItemEvent event, final String yamlContent) {
        return new YamlAccessControlUserRuleConfigurationSwapper().swapToObject(YamlEngine.unmarshal(yamlContent, YamlAccessControlUserRuleConfiguration.class));
    }
    
    @Override
    public AccessControlRuleConfiguration findRuleConfiguration(final ShardingSphereDatabase database) {
        return database.getRuleMetaData().findSingleRule(AccessControlRule.class)
                .map(optional -> getConfiguration(optional.getConfiguration())).orElseGet(() -> new AccessControlRuleConfiguration(new LinkedList<>()));
    }
    
    private AccessControlRuleConfiguration getConfiguration(final AccessControlRuleConfiguration config) {
        return null == config.getUsers() ? new AccessControlRuleConfiguration(new LinkedList<>()) : config;
    }
    
    @Override
    public void changeRuleItemConfiguration(final AlterRuleItemEvent event, final AccessControlRuleConfiguration currentRuleConfig, final AccessControlUserRuleConfiguration toBeChangedItemConfig) {
        // TODO refactor DistSQL to only persist config
        currentRuleConfig.getUsers().removeIf(each ->each.getName().equals(toBeChangedItemConfig.getName()));
        currentRuleConfig.getUsers().add(toBeChangedItemConfig);
    }
    
    @Override
    public void dropRuleItemConfiguration(final DropRuleItemEvent event, final AccessControlRuleConfiguration currentRuleConfig) {
        currentRuleConfig.getUsers().removeIf(each -> each.getName().equals(((DropNamedRuleItemEvent) event).getItemName()));
    }
    
    @Override
    public String getType() {
        return AccessControlRuleNodePathProvider.RULE_TYPE + "." + AccessControlRuleNodePathProvider.USERS;
    }
}
