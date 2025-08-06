package org.apache.shardingsphere.infra.binder.context.segment.select.projection;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SensitiveSource {
    private String databaseName;
    private String tableName;
    private String columnName;
    private List<Map<String,Long>> recognizeResultMapList = new ArrayList<>();
}
