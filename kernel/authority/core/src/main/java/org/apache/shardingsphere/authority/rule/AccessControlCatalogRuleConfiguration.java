package org.apache.shardingsphere.authority.rule;

import java.util.Date;

public class AccessControlCatalogRuleConfiguration {
    private Long themeDomainId;
    private Boolean assetTableAccessFlag;
    private Boolean assetApiAccessFlag;
    private Boolean assetIndicatorAccessFlag;
    private Boolean assetReportTableAccessFlag;
    private Boolean desensitizeWhiteListFlag;

    private Date expirationTime;

    private Date assetTableAccessTime;
    private Date assetApiAccessTime;
    private Date assetIndicatorAccessTime;
    private Date assetReportTableAccessTime;

    public AccessControlCatalogRuleConfiguration(Long themeDomainId, Boolean assetTableAccessFlag, Boolean assetApiAccessFlag, Boolean assetIndicatorAccessFlag, Boolean assetReportTableAccessFlag, Boolean desensitizeWhiteListFlag, Date expirationTime, Date assetTableAccessTime, Date assetApiAccessTime, Date assetIndicatorAccessTime, Date assetReportTableAccessTime) {
        this.themeDomainId = themeDomainId;
        this.assetTableAccessFlag = assetTableAccessFlag;
        this.assetApiAccessFlag = assetApiAccessFlag;
        this.assetIndicatorAccessFlag = assetIndicatorAccessFlag;
        this.assetReportTableAccessFlag = assetReportTableAccessFlag;
        this.desensitizeWhiteListFlag = desensitizeWhiteListFlag;
        this.expirationTime = expirationTime;
        this.assetTableAccessTime = assetTableAccessTime;
        this.assetApiAccessTime = assetApiAccessTime;
        this.assetIndicatorAccessTime = assetIndicatorAccessTime;
        this.assetReportTableAccessTime = assetReportTableAccessTime;
    }

    public Long getThemeDomainId() {
        return themeDomainId;
    }

    public void setThemeDomainId(Long themeDomainId) {
        this.themeDomainId = themeDomainId;
    }

    public Boolean getAssetTableAccessFlag() {
        return assetTableAccessFlag;
    }

    public void setAssetTableAccessFlag(Boolean assetTableAccessFlag) {
        this.assetTableAccessFlag = assetTableAccessFlag;
    }

    public Boolean getAssetApiAccessFlag() {
        return assetApiAccessFlag;
    }

    public void setAssetApiAccessFlag(Boolean assetApiAccessFlag) {
        this.assetApiAccessFlag = assetApiAccessFlag;
    }

    public Boolean getAssetIndicatorAccessFlag() {
        return assetIndicatorAccessFlag;
    }

    public void setAssetIndicatorAccessFlag(Boolean assetIndicatorAccessFlag) {
        this.assetIndicatorAccessFlag = assetIndicatorAccessFlag;
    }

    public Boolean getAssetReportTableAccessFlag() {
        return assetReportTableAccessFlag;
    }

    public void setAssetReportTableAccessFlag(Boolean assetReportTableAccessFlag) {
        this.assetReportTableAccessFlag = assetReportTableAccessFlag;
    }

    public Boolean getDesensitizeWhiteListFlag() {
        return desensitizeWhiteListFlag;
    }

    public void setDesensitizeWhiteListFlag(Boolean desensitizeWhiteListFlag) {
        this.desensitizeWhiteListFlag = desensitizeWhiteListFlag;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getAssetTableAccessTime() {
        return assetTableAccessTime;
    }

    public void setAssetTableAccessTime(Date assetTableAccessTime) {
        this.assetTableAccessTime = assetTableAccessTime;
    }

    public Date getAssetApiAccessTime() {
        return assetApiAccessTime;
    }

    public void setAssetApiAccessTime(Date assetApiAccessTime) {
        this.assetApiAccessTime = assetApiAccessTime;
    }

    public Date getAssetIndicatorAccessTime() {
        return assetIndicatorAccessTime;
    }

    public void setAssetIndicatorAccessTime(Date assetIndicatorAccessTime) {
        this.assetIndicatorAccessTime = assetIndicatorAccessTime;
    }

    public Date getAssetReportTableAccessTime() {
        return assetReportTableAccessTime;
    }

    public void setAssetReportTableAccessTime(Date assetReportTableAccessTime) {
        this.assetReportTableAccessTime = assetReportTableAccessTime;
    }
}
