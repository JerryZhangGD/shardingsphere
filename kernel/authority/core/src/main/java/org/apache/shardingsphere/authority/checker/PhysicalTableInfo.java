package org.apache.shardingsphere.authority.checker;

import lombok.Data;

@Data
public class PhysicalTableInfo {
    private String dbName;
    private String tableName;
}
