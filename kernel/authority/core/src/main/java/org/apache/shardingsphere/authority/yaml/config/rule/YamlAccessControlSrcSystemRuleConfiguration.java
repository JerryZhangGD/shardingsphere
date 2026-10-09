package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.Date;

@Getter
@Setter
public class YamlAccessControlSrcSystemRuleConfiguration implements YamlConfiguration {
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
}
