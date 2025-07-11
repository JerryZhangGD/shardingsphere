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


import org.apache.shardingsphere.authority.rule.AccessControlTableRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.rule.SensitiveLevelColumnConfiguration;
import org.apache.shardingsphere.authority.rule.SensitiveLevelTableConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlSensitiveLevelColumnConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlSensitiveLevelTableConfiguration;
import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * YAML mask table rule configuration swapper.
 */
public final class YamlSensitiveLevelTableConfigurationSwapper implements YamlConfigurationSwapper<YamlSensitiveLevelTableConfiguration, SensitiveLevelTableConfiguration> {

    private YamlSensitiveLevelColumnConfigurationSwapper columnSwapper = new YamlSensitiveLevelColumnConfigurationSwapper();

    @Override
    public YamlSensitiveLevelTableConfiguration swapToYamlConfiguration(final SensitiveLevelTableConfiguration data) {
        YamlSensitiveLevelTableConfiguration result = new YamlSensitiveLevelTableConfiguration();
        result.setName(data.getName());
        Map<String, YamlSensitiveLevelColumnConfiguration> columns = result.getColumns();
        if(columns==null){
            columns = new HashMap<>();
        }
        for (SensitiveLevelColumnConfiguration each : data.getColumns()) {
            columns.put(each.getName(), columnSwapper.swapToYamlConfiguration(each));
        }
        result.setColumns(columns);
        return result;
    }
    
    @Override
    public SensitiveLevelTableConfiguration swapToObject(final YamlSensitiveLevelTableConfiguration yamlConfig) {
        Collection<SensitiveLevelColumnConfiguration> columns = new LinkedList<>();
        for (Entry<String, YamlSensitiveLevelColumnConfiguration> entry : yamlConfig.getColumns().entrySet()) {
            YamlSensitiveLevelColumnConfiguration yamlSensitiveLevelColumnConfig = entry.getValue();
            yamlSensitiveLevelColumnConfig.setName(entry.getKey());
            columns.add(columnSwapper.swapToObject(yamlSensitiveLevelColumnConfig));
        }
        return new SensitiveLevelTableConfiguration(yamlConfig.getName(),columns);
    }
}
