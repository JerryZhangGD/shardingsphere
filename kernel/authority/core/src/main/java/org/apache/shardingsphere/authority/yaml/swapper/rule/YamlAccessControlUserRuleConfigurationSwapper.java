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


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shardingsphere.authority.rule.AccessControlAssetRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlTableRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlAssetRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlCatalogRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;

/**
 * YAML mask table rule configuration swapper.
 */
public final class YamlAccessControlUserRuleConfigurationSwapper implements YamlConfigurationSwapper<YamlAccessControlUserRuleConfiguration, AccessControlUserRuleConfiguration> {

    private YamlAccessControlTableRuleConfigurationSwapper tableSwapper = new YamlAccessControlTableRuleConfigurationSwapper();
    private YamlAccessControlCatalogRuleConfigurationSwapper catalogSwapper = new YamlAccessControlCatalogRuleConfigurationSwapper();
    private YamlAccessControlAssetRuleConfigurationSwapper assetSwapper = new YamlAccessControlAssetRuleConfigurationSwapper();

    @Override
    public YamlAccessControlUserRuleConfiguration swapToYamlConfiguration(final AccessControlUserRuleConfiguration data) {
        YamlAccessControlUserRuleConfiguration result = new YamlAccessControlUserRuleConfiguration();
        if(data.getAllFlag()!=null){
            result.setAllFlag(data.getAllFlag());
        }
        Map<String, YamlAccessControlTableRuleConfiguration> tables = result.getTables();
        result.setName(data.getName());
        result.setAllFlag(data.getAllFlag());
        if(tables==null){
            tables = new HashMap<>();
        }
        for (AccessControlTableRuleConfiguration each : data.getTables()) {
            tables.put(each.getTableName(), tableSwapper.swapToYamlConfiguration(each));
        }
        result.setTables(tables);


        Map<Long, YamlAccessControlCatalogRuleConfiguration> catalogs = result.getCatalogs();
        if(catalogs==null){
            catalogs = new HashMap<>();
        }
        for(AccessControlCatalogRuleConfiguration each : data.getCatalogs()){
            catalogs.put(each.getThemeDomainId(),catalogSwapper.swapToYamlConfiguration(each));
        }
        result.setCatalogs(catalogs);


        Map<Long, YamlAccessControlAssetRuleConfiguration> assets = result.getAssets();
        if(assets==null){
            assets = new HashMap<>();
        }

        for(AccessControlAssetRuleConfiguration each : data.getAssets()){
            assets.put(each.getAssetId(),assetSwapper.swapToYamlConfiguration(each));
        }
        result.setAssets(assets);

        return result;
    }
    
    @Override
    public AccessControlUserRuleConfiguration swapToObject(final YamlAccessControlUserRuleConfiguration yamlConfig) {
        Collection<AccessControlCatalogRuleConfiguration> catalogs = new LinkedList<>();
        for (Entry<Long, YamlAccessControlCatalogRuleConfiguration> entry : yamlConfig.getCatalogs().entrySet()) {
            YamlAccessControlCatalogRuleConfiguration yamlAccessControlCatalogConfig = entry.getValue();
            yamlAccessControlCatalogConfig.setThemeDomainId(entry.getKey());
            catalogs.add(catalogSwapper.swapToObject(yamlAccessControlCatalogConfig));
        }


        Collection<AccessControlTableRuleConfiguration> tables = new LinkedList<>();
        for (Entry<String, YamlAccessControlTableRuleConfiguration> entry : yamlConfig.getTables().entrySet()) {
            YamlAccessControlTableRuleConfiguration yamlAccessControlTableConfig = entry.getValue();
            yamlAccessControlTableConfig.setTableName(entry.getKey());
            tables.add(tableSwapper.swapToObject(yamlAccessControlTableConfig));
        }



        Collection<AccessControlAssetRuleConfiguration> assets = new LinkedList<>();
        for (Entry<Long, YamlAccessControlAssetRuleConfiguration> entry : yamlConfig.getAssets().entrySet()) {
            YamlAccessControlAssetRuleConfiguration yamlAccessControlAssetRuleConfiguration = entry.getValue();
            yamlAccessControlAssetRuleConfiguration.setAssetId(entry.getKey());
            assets.add(assetSwapper.swapToObject(yamlAccessControlAssetRuleConfiguration));
        }


        return new AccessControlUserRuleConfiguration(yamlConfig.getAllFlag(),yamlConfig.getName(),catalogs,tables,assets);
    }
}
