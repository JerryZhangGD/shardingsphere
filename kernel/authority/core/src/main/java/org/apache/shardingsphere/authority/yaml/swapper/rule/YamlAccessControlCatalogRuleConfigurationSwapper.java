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


import org.apache.shardingsphere.authority.rule.AccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;

/**
 * YAML mask column rule configuration swapper.
 */
public final class YamlAccessControlCatalogRuleConfigurationSwapper implements YamlConfigurationSwapper<YamlAccessControlCatalogRuleConfiguration, AccessControlCatalogRuleConfiguration> {
    
    @Override
    public YamlAccessControlCatalogRuleConfiguration swapToYamlConfiguration(final AccessControlCatalogRuleConfiguration data) {
        YamlAccessControlCatalogRuleConfiguration result = new YamlAccessControlCatalogRuleConfiguration();
        result.setThemeDomainId(data.getThemeDomainId());
        result.setAssetTableAccessFlag(data.getAssetTableAccessFlag());
        result.setAssetApiAccessFlag(data.getAssetApiAccessFlag());
        result.setAssetIndicatorAccessFlag(data.getAssetIndicatorAccessFlag());
        result.setAssetReportTableAccessFlag(data.getAssetReportTableAccessFlag());
        result.setDesensitizeWhiteListFlag(data.getDesensitizeWhiteListFlag());
        result.setExpirationTime(data.getExpirationTime());

        result.setAssetTableAccessTime(data.getAssetTableAccessTime());
        result.setAssetApiAccessTime(data.getAssetApiAccessTime());
        result.setAssetIndicatorAccessTime(data.getAssetIndicatorAccessTime());
        result.setAssetReportTableAccessTime(data.getAssetReportTableAccessTime());
        return result;
    }
    
    @Override
    public AccessControlCatalogRuleConfiguration swapToObject(final YamlAccessControlCatalogRuleConfiguration yamlConfig) {
        return new AccessControlCatalogRuleConfiguration(yamlConfig.getThemeDomainId(),yamlConfig.getAssetTableAccessFlag(),yamlConfig.getAssetApiAccessFlag(),yamlConfig.getAssetIndicatorAccessFlag(),yamlConfig.getAssetReportTableAccessFlag(),yamlConfig.getDesensitizeWhiteListFlag(),yamlConfig.getExpirationTime(),yamlConfig.getAssetTableAccessTime(),yamlConfig.getAssetApiAccessTime(),yamlConfig.getAssetIndicatorAccessTime(),yamlConfig.getAssetReportTableAccessTime());
    }
}
