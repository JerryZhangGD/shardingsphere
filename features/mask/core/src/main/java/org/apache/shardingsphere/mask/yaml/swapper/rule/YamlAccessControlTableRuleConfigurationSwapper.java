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

package org.apache.shardingsphere.mask.yaml.swapper.rule;

import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;
import org.apache.shardingsphere.mask.config.rule.AccessControlTableRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.MaskColumnRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlMaskColumnRuleConfiguration;

/**
 * YAML mask column rule configuration swapper.
 */
public final class YamlAccessControlTableRuleConfigurationSwapper implements YamlConfigurationSwapper<YamlAccessControlTableRuleConfiguration, AccessControlTableRuleConfiguration> {
    
    @Override
    public YamlAccessControlTableRuleConfiguration swapToYamlConfiguration(final AccessControlTableRuleConfiguration data) {
        YamlAccessControlTableRuleConfiguration result = new YamlAccessControlTableRuleConfiguration();
        result.setTableName(data.getTableName());
        result.setAllFlag(data.getAllFlag());
        result.setColumns(data.getColumns());
        return result;
    }
    
    @Override
    public AccessControlTableRuleConfiguration swapToObject(final YamlAccessControlTableRuleConfiguration yamlConfig) {
        return new AccessControlTableRuleConfiguration(yamlConfig.getTableName(), yamlConfig.getAllFlag(),yamlConfig.getColumns());
    }
}
