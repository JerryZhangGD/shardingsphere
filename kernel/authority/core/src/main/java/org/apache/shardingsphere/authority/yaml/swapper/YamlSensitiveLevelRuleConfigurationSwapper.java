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

package org.apache.shardingsphere.authority.yaml.swapper;


import org.apache.shardingsphere.authority.rule.AccessControlRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.rule.SensitiveLevelRuleConfiguration;
import org.apache.shardingsphere.authority.rule.SensitiveLevelTableConfiguration;
import org.apache.shardingsphere.authority.yaml.config.YamlAccessControlRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.YamlSensitiveLevelRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlSensitiveLevelTableConfiguration;
import org.apache.shardingsphere.authority.yaml.swapper.rule.YamlAccessControlUserRuleConfigurationSwapper;
import org.apache.shardingsphere.authority.yaml.swapper.rule.YamlSensitiveLevelTableConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * YAML mask rule configuration swapper.
 */
public final class YamlSensitiveLevelRuleConfigurationSwapper implements YamlRuleConfigurationSwapper<YamlSensitiveLevelRuleConfiguration, SensitiveLevelRuleConfiguration> {
    
    private YamlSensitiveLevelTableConfigurationSwapper tableSwapper = new YamlSensitiveLevelTableConfigurationSwapper();
    
    @Override
    public YamlSensitiveLevelRuleConfiguration swapToYamlConfiguration(final SensitiveLevelRuleConfiguration data) {
        YamlSensitiveLevelRuleConfiguration result = new YamlSensitiveLevelRuleConfiguration();
        data.getTables().forEach(each -> result.getTables().put(each.getName(),tableSwapper.swapToYamlConfiguration(each)));
        return result;
    }
    
    @Override
    public SensitiveLevelRuleConfiguration swapToObject(final YamlSensitiveLevelRuleConfiguration yamlConfig) {

        Collection<SensitiveLevelTableConfiguration> result = new LinkedList<>();
        for (Entry<String, YamlSensitiveLevelTableConfiguration> entry : yamlConfig.getTables().entrySet()) {
            YamlSensitiveLevelTableConfiguration yamlSensitiveLevelTableConfiguration = entry.getValue();
            yamlSensitiveLevelTableConfiguration.setName(entry.getKey());
            result.add(tableSwapper.swapToObject(yamlSensitiveLevelTableConfiguration));
        }

        return new SensitiveLevelRuleConfiguration(result);
    }
    
    @Override
    public Class<SensitiveLevelRuleConfiguration> getTypeClass() {
        return SensitiveLevelRuleConfiguration.class;
    }
    
    @Override
    public String getRuleTagName() {
        return "SENSITIVE_LEVEL";
    }
    
    @Override
    public int getOrder() {
        return 36;
    }
}
