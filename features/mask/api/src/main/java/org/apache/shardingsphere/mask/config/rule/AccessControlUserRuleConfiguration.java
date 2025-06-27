package org.apache.shardingsphere.mask.config.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccessControlUserRuleConfiguration {
    private Boolean allFlag;
    private final String name;
    private final Collection<AccessControlTableRuleConfiguration> tables;
}
