package org.apache.shardingsphere.authority.rule;


public class SensitiveLevelColumnConfiguration {
    private final String name;
    private final Integer sensitiveLevel;

    public SensitiveLevelColumnConfiguration(String name, Integer sensitiveLevel) {
        this.name = name;
        this.sensitiveLevel = sensitiveLevel;
    }

    public String getName() {
        return name;
    }

    public Integer getSensitiveLevel() {
        return sensitiveLevel;
    }
}
