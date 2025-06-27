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

package org.apache.shardingsphere.mask.yaml.swapper;

import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.algorithm.core.yaml.YamlAlgorithmConfiguration;
import org.apache.shardingsphere.infra.algorithm.core.yaml.YamlAlgorithmConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapper;
import org.apache.shardingsphere.mask.config.AccessControlRuleConfiguration;
import org.apache.shardingsphere.mask.config.MaskRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlTableRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.MaskTableRuleConfiguration;
import org.apache.shardingsphere.mask.constant.MaskOrder;
import org.apache.shardingsphere.mask.yaml.config.YamlAccessControlRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.YamlMaskRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlAccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.config.rule.YamlMaskTableRuleConfiguration;
import org.apache.shardingsphere.mask.yaml.swapper.rule.YamlAccessControlTableRuleConfigurationSwapper;
import org.apache.shardingsphere.mask.yaml.swapper.rule.YamlAccessControlUserRuleConfigurationSwapper;
import org.apache.shardingsphere.mask.yaml.swapper.rule.YamlMaskTableRuleConfigurationSwapper;

import java.util.*;
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
