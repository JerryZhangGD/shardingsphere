package org.apache.shardingsphere.authority.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccessControlUserRuleConfiguration {
    private Boolean allFlag;
    private final String name;
    private final Collection<AccessControlCatalogRuleConfiguration> catalogs;
    private final Collection<AccessControlTableRuleConfiguration> tables;
}
