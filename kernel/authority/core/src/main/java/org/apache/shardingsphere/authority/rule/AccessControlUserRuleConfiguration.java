package org.apache.shardingsphere.authority.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlSrcSystemRuleConfiguration;

import java.util.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccessControlUserRuleConfiguration {
    private Boolean allFlag;
    private final String name;
    private final Collection<AccessControlCatalogRuleConfiguration> catalogs;
    private final Collection<AccessControlTableRuleConfiguration> tables;
    private final Collection<AccessControlAssetRuleConfiguration> assets;
    private final Collection<AccessControlSrcSystemRuleConfiguration> srcSystems;
    private final List<String> tableFullNameList;
}
