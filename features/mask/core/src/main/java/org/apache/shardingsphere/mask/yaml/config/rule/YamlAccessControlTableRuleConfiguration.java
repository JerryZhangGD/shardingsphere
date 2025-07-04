package org.apache.shardingsphere.mask.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class YamlAccessControlTableRuleConfiguration implements YamlConfiguration {
    private String tableName;

    private Boolean allFlag;

    private List<String> columns = new ArrayList();
}
