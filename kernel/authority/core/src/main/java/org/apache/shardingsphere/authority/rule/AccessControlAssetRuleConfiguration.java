package org.apache.shardingsphere.authority.rule;

import java.util.Date;
import java.util.List;

public class AccessControlAssetRuleConfiguration {
    private Long assetId;
    private String assetType;
    private List<Long> themeDomainIdList;
    private Date expirationTime;

    public AccessControlAssetRuleConfiguration(Long assetId, String assetType, List<Long> themeDomainIdList, Date expirationTime) {
        this.assetId = assetId;
        this.assetType = assetType;
        this.themeDomainIdList = themeDomainIdList;
        this.expirationTime = expirationTime;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public List<Long> getThemeDomainIdList() {
        return themeDomainIdList;
    }

    public void setThemeDomainIdList(List<Long> themeDomainIdList) {
        this.themeDomainIdList = themeDomainIdList;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }
}
