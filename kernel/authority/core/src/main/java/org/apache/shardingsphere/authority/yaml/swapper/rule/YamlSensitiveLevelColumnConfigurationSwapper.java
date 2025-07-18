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
import org.apache.shardingsphere.authority.rule.SensitiveLevelColumnConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlSensitiveLevelColumnConfiguration;
import org.apache.shardingsphere.infra.util.yaml.swapper.YamlConfigurationSwapper;

/**
 * YAML mask column rule configuration swapper.
 */
public final class YamlSensitiveLevelColumnConfigurationSwapper implements YamlConfigurationSwapper<YamlSensitiveLevelColumnConfiguration, SensitiveLevelColumnConfiguration> {
    
    @Override
    public YamlSensitiveLevelColumnConfiguration swapToYamlConfiguration(final SensitiveLevelColumnConfiguration data) {
        YamlSensitiveLevelColumnConfiguration result = new YamlSensitiveLevelColumnConfiguration();
        result.setName(data.getName());
        result.setSensitiveLevel(data.getSensitiveLevel());
        return result;
    }
    
    @Override
    public SensitiveLevelColumnConfiguration swapToObject(final YamlSensitiveLevelColumnConfiguration yamlConfig) {
        return new SensitiveLevelColumnConfiguration(yamlConfig.getName(),yamlConfig.getSensitiveLevel());
    }
}
