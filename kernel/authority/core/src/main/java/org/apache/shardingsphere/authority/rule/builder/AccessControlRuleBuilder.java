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

package org.apache.shardingsphere.authority.rule.builder;

import org.apache.shardingsphere.authority.rule.AccessControlRule;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.instance.ComputeNodeInstanceContext;
import org.apache.shardingsphere.infra.metadata.database.resource.ResourceMetaData;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.authority.rule.AccessControlRuleConfiguration;
import org.apache.shardingsphere.infra.rule.builder.database.DatabaseRuleBuilder;

import java.util.Collection;

/**
 * Mask rule builder.
 */
public final class AccessControlRuleBuilder implements DatabaseRuleBuilder<AccessControlRuleConfiguration> {
    
    @Override
    public AccessControlRule build(final AccessControlRuleConfiguration ruleConfig, final String databaseName, final DatabaseType protocolType,
                                   final ResourceMetaData resourceMetaData, final Collection<ShardingSphereRule> builtRules, final ComputeNodeInstanceContext computeNodeInstanceContext) {
        return new AccessControlRule(ruleConfig);
    }
    
    @Override
    public int getOrder() {
        return 35;
    }
    
    @Override
    public Class<AccessControlRuleConfiguration> getTypeClass() {
        return AccessControlRuleConfiguration.class;
    }
}
