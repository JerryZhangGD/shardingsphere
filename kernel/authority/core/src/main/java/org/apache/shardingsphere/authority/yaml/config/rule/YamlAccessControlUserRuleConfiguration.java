package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.authority.yaml.config.rule.YamlAccessControlTableRuleConfiguration;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class YamlAccessControlUserRuleConfiguration implements YamlConfiguration {
    private Boolean allFlag;

    private String name;

    private Map<Long,YamlAccessControlCatalogRuleConfiguration> catalogs = new LinkedHashMap<>();

    private Map<String, YamlAccessControlTableRuleConfiguration> tables = new LinkedHashMap<>();

    private Map<Long,YamlAccessControlAssetRuleConfiguration> assets = new LinkedHashMap<>();
}
