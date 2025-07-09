package org.apache.shardingsphere.authority.rule;


import java.util.ArrayList;
import java.util.List;

public class AccessControlTableRuleConfiguration {
    private String tableName;
    private Boolean allFlag;
    private List<String> columns = new ArrayList();

    public AccessControlTableRuleConfiguration(String tableName, Boolean allFlag, List<String> columns) {
        this.tableName = tableName;
        this.allFlag = allFlag;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(Boolean allFlag) {
        this.allFlag = allFlag;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
