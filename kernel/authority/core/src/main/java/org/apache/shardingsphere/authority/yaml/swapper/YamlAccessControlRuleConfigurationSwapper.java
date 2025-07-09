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
import org.apache.shardingsphere.authority.yaml.config.YamlAccessControlRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.authority.yaml.swapper.rule.YamlAccessControlUserRuleConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * YAML mask rule configuration swapper.
 */
public final class YamlAccessControlRuleConfigurationSwapper implements YamlRuleConfigurationSwapper<YamlAccessControlRuleConfiguration, AccessControlRuleConfiguration> {
    
    private YamlAccessControlUserRuleConfigurationSwapper userSwapper = new YamlAccessControlUserRuleConfigurationSwapper();
    
    @Override
    public YamlAccessControlRuleConfiguration swapToYamlConfiguration(final AccessControlRuleConfiguration data) {
        YamlAccessControlRuleConfiguration result = new YamlAccessControlRuleConfiguration();
        data.getUsers().forEach(each ->result.getUsers().put(each.getName(),userSwapper.swapToYamlConfiguration(each)));
        return result;
    }
    
    @Override
    public AccessControlRuleConfiguration swapToObject(final YamlAccessControlRuleConfiguration yamlConfig) {

        Collection<AccessControlUserRuleConfiguration> result = new LinkedList<>();
        for (Entry<String, YamlAccessControlUserRuleConfiguration> entry : yamlConfig.getUsers().entrySet()) {
            YamlAccessControlUserRuleConfiguration yamlAccessControlUserRuleConfiguration = entry.getValue();
            yamlAccessControlUserRuleConfiguration.setName(entry.getKey());
            result.add(userSwapper.swapToObject(yamlAccessControlUserRuleConfiguration));
        }

        return new AccessControlRuleConfiguration(result);
    }
    
    @Override
    public Class<AccessControlRuleConfiguration> getTypeClass() {
        return AccessControlRuleConfiguration.class;
    }
    
    @Override
    public String getRuleTagName() {
        return "ACCESS_CONTROL";
    }
    
    @Override
    public int getOrder() {
        return 35;
    }
}
