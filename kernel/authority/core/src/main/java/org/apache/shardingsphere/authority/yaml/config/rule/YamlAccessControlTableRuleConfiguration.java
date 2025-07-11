package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class YamlAccessControlTableRuleConfiguration implements YamlConfiguration {
    private String tableName;

    private Boolean allFlag;

    private Boolean desensitizeWhiteListFlag;

    private Map<String,Integer> columns = new HashMap<>();
}
