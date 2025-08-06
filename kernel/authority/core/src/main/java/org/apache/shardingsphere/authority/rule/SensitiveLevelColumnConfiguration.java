package org.apache.shardingsphere.authority.rule;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SensitiveLevelColumnConfiguration {
    private final String name;
    private final Integer sensitiveLevel;

    private List<Map<String,Long>> recognizeResultMapList = new ArrayList<>();

    public SensitiveLevelColumnConfiguration(String name, Integer sensitiveLevel, List<Map<String,Long>> recognizeResultMapList) {
        this.name = name;
        this.sensitiveLevel = sensitiveLevel;
        this.recognizeResultMapList = recognizeResultMapList;
    }

    public String getName() {
        return name;
    }

    public Integer getSensitiveLevel() {
        return sensitiveLevel;
    }

    public List<Map<String, Long>> getRecognizeResultMapList() {
        return recognizeResultMapList;
    }
}
