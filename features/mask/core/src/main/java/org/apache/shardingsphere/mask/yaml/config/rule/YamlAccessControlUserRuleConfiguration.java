package org.apache.shardingsphere.mask.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class YamlAccessControlUserRuleConfiguration implements YamlConfiguration {
    private Boolean allFlag;

    private String name;

    private Map<String, YamlAccessControlTableRuleConfiguration> tables = new LinkedHashMap<>();
}
