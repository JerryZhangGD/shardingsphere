package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class YamlSensitiveLevelTableConfiguration implements YamlConfiguration {

    private String name;

    private Map<String, YamlSensitiveLevelColumnConfiguration> columns = new LinkedHashMap<>();
}
