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

package org.apache.shardingsphere.mask.rule;

import com.cedarsoftware.util.CaseInsensitiveMap;
import com.cedarsoftware.util.CaseInsensitiveSet;
import com.google.common.base.Preconditions;
import org.apache.shardingsphere.infra.rule.PartialRuleUpdateSupported;
import org.apache.shardingsphere.infra.rule.attribute.RuleAttributes;
import org.apache.shardingsphere.infra.rule.scope.DatabaseRule;
import org.apache.shardingsphere.mask.config.AccessControlRuleConfiguration;
import org.apache.shardingsphere.mask.config.rule.AccessControlUserRuleConfiguration;
import org.apache.shardingsphere.mask.rule.attribute.AccessControlUserMapperRuleAttribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Mask rule.
 */
public final class AccessControlRule implements DatabaseRule, PartialRuleUpdateSupported<AccessControlRuleConfiguration> {

    private final AtomicReference<AccessControlRuleConfiguration> configuration = new AtomicReference<>();

    private final Map<String, AccessControlUser> users = new CaseInsensitiveMap<>(Collections.emptyMap(), new ConcurrentHashMap<>());

    private final AtomicReference<RuleAttributes> attributes = new AtomicReference<>();

    public AccessControlRule(final AccessControlRuleConfiguration ruleConfig) {
        configuration.set(ruleConfig);
        ruleConfig.getUsers().forEach(each -> users.put(each.getName(), new AccessControlUser(each)));
        //属性暂时用不到，看后面用不用得到
        attributes.set(new RuleAttributes(new AccessControlUserMapperRuleAttribute(users.keySet())));
    }
    
    /**
     * Find mask table.
     *
     * @param userName table name
     * @return found mask table
     */
    public Optional<AccessControlUser> findAccessControlUser(final String userName) {
        return Optional.ofNullable(users.get(userName));
    }
    
    @Override
    public RuleAttributes getAttributes() {
        return attributes.get();
    }
    
    @Override
    public AccessControlRuleConfiguration getConfiguration() {
        return configuration.get();
    }
    
    @Override
    public void updateConfiguration(final AccessControlRuleConfiguration toBeUpdatedRuleConfig) {
        configuration.set(toBeUpdatedRuleConfig);
    }
    
    @Override
    public boolean partialUpdate(final AccessControlRuleConfiguration toBeUpdatedRuleConfig) {
        Collection<String> toBeUpdatedUserNames = toBeUpdatedRuleConfig.getUsers().stream().map(AccessControlUserRuleConfiguration::getName).collect(Collectors.toCollection(CaseInsensitiveSet::new));
        Collection<String> toBeAddedUserNames = toBeUpdatedUserNames.stream().filter(each ->!users.containsKey(each)).collect(Collectors.toList());
        if(!toBeAddedUserNames.isEmpty()){
            toBeAddedUserNames.forEach(each ->addUserRule(each,toBeUpdatedRuleConfig));
            return true;
        }

        Collection<String> toBeRemovedUserNames = users.keySet().stream().filter(each -> !toBeUpdatedUserNames.contains(each)).collect(Collectors.toList());
        if(!toBeRemovedUserNames.isEmpty()) {
            toBeRemovedUserNames.forEach(users::remove);
            return true;
        }
        return false;
    }
    
    private void addUserRule(final String userName, final AccessControlRuleConfiguration toBeUpdatedRuleConfig) {
        AccessControlUserRuleConfiguration userRuleConfig = getUserRuleConfiguration(userName,toBeUpdatedRuleConfig);
        users.put(userName,new AccessControlUser(userRuleConfig));
    }
    
    private AccessControlUserRuleConfiguration getUserRuleConfiguration(final String userName, final AccessControlRuleConfiguration toBeUpdatedRuleConfig) {
        Optional<AccessControlUserRuleConfiguration> result = toBeUpdatedRuleConfig.getUsers().stream().filter(user -> user.getName().equals(userName)).findFirst();
        Preconditions.checkState(result.isPresent());
        return result.get();
    }
}
