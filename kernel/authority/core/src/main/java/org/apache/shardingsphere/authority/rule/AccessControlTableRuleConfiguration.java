package org.apache.shardingsphere.authority.rule;


import java.util.*;

public class AccessControlTableRuleConfiguration {
    private String tableName;
    private Boolean allFlag;

    private Boolean desensitizeWhiteListFlag;
    private Map<String,Integer> columns;

    private Date expirationTime;

    public AccessControlTableRuleConfiguration(String tableName, Boolean allFlag, Boolean desensitizeWhiteListFlag, Map<String, Integer> columns, Date expirationTime) {
        this.tableName = tableName;
        this.allFlag = allFlag;
        this.desensitizeWhiteListFlag = desensitizeWhiteListFlag;
        this.columns = columns;
        this.expirationTime = expirationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
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

    public Boolean getDesensitizeWhiteListFlag() {
        return desensitizeWhiteListFlag;
    }

    public void setDesensitizeWhiteListFlag(Boolean desensitizeWhiteListFlag) {
        this.desensitizeWhiteListFlag = desensitizeWhiteListFlag;
    }

    public Map<String, Integer> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Integer> columns) {
        this.columns = columns;
    }
}
