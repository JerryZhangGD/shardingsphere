package org.apache.shardingsphere.authority.rule;

import java.util.Date;

public class AccessControlSrcSystemRuleConfiguration {
    private Long srcSystemDictId;
    private Boolean assetTableAccessFlag;
    private Boolean assetApiAccessFlag;
    private Boolean assetIndicatorAccessFlag;
    private Boolean assetReportTableAccessFlag;
    private Boolean analysisThemeAccessFlag;
    private Date expirationTime;

    private Date assetTableAccessTime;
    private Date assetApiAccessTime;
    private Date assetIndicatorAccessTime;
    private Date assetReportTableAccessTime;
    private Date analysisThemeAccessTime;

    public AccessControlSrcSystemRuleConfiguration(Long srcSystemDictId, Boolean assetTableAccessFlag, Boolean assetApiAccessFlag, Boolean assetIndicatorAccessFlag, Boolean assetReportTableAccessFlag, Boolean analysisThemeAccessFlag, Date expirationTime, Date assetTableAccessTime, Date assetApiAccessTime, Date assetIndicatorAccessTime, Date assetReportTableAccessTime, Date analysisThemeAccessTime) {
        this.srcSystemDictId = srcSystemDictId;
        this.assetTableAccessFlag = assetTableAccessFlag;
        this.assetApiAccessFlag = assetApiAccessFlag;
        this.assetIndicatorAccessFlag = assetIndicatorAccessFlag;
        this.assetReportTableAccessFlag = assetReportTableAccessFlag;
        this.analysisThemeAccessFlag = analysisThemeAccessFlag;
        this.expirationTime = expirationTime;
        this.assetTableAccessTime = assetTableAccessTime;
        this.assetApiAccessTime = assetApiAccessTime;
        this.assetIndicatorAccessTime = assetIndicatorAccessTime;
        this.assetReportTableAccessTime = assetReportTableAccessTime;
        this.analysisThemeAccessTime = analysisThemeAccessTime;
    }

    public Boolean getAnalysisThemeAccessFlag() {
        return analysisThemeAccessFlag;
    }

    public void setAnalysisThemeAccessFlag(Boolean analysisThemeAccessFlag) {
        this.analysisThemeAccessFlag = analysisThemeAccessFlag;
    }

    public Date getAnalysisThemeAccessTime() {
        return analysisThemeAccessTime;
    }

    public void setAnalysisThemeAccessTime(Date analysisThemeAccessTime) {
        this.analysisThemeAccessTime = analysisThemeAccessTime;
    }

    public Long getSrcSystemDictId() {
        return srcSystemDictId;
    }

    public void setSrcSystemDictId(Long srcSystemDictId) {
        this.srcSystemDictId = srcSystemDictId;
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
