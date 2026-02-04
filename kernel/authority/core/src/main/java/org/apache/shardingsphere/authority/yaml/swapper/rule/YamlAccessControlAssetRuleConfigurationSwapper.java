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

package org.apache.shardingsphere.authority.yaml.swapper.rule;


import org.apache.shardingsphere.authority.rule.AccessControlAssetRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlAssetRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;

/**
 * YAML mask column rule configuration swapper.
 */
public final class YamlAccessControlAssetRuleConfigurationSwapper implements YamlConfigurationSwapper<YamlAccessControlAssetRuleConfiguration, AccessControlAssetRuleConfiguration> {
    
    @Override
    public YamlAccessControlAssetRuleConfiguration swapToYamlConfiguration(final AccessControlAssetRuleConfiguration data) {
        YamlAccessControlAssetRuleConfiguration result = new YamlAccessControlAssetRuleConfiguration();
        result.setAssetId(data.getAssetId());
        result.setAssetType(data.getAssetType());
        result.setThemeDomainIdList(data.getThemeDomainIdList());
        result.setExpirationTime(data.getExpirationTime());
        return result;
    }
    
    @Override
    public AccessControlAssetRuleConfiguration swapToObject(final YamlAccessControlAssetRuleConfiguration yamlConfig) {
        return new AccessControlAssetRuleConfiguration(yamlConfig.getAssetId(),yamlConfig.getAssetType(),yamlConfig.getThemeDomainIdList(),yamlConfig.getExpirationTime());
    }
}
