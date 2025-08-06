package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class YamlSensitiveLevelColumnConfiguration implements YamlConfiguration {
    private String name;
    private Integer sensitiveLevel;
    private List<Map<String,Long>> recognizeResultMapList = new ArrayList<>();
}
