package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.*;

@Getter
@Setter
public class YamlAccessControlTableRuleConfiguration implements YamlConfiguration {
    private String tableName;

    private Boolean allFlag;

    private Boolean desensitizeWhiteListFlag;

    private Map<String,Integer> columns = new HashMap<>();

    private Date expirationTime;

    private String filterConditionSql;
}
