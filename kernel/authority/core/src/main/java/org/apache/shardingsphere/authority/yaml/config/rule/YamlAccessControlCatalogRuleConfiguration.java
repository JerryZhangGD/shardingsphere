package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class YamlAccessControlCatalogRuleConfiguration implements YamlConfiguration {
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
}
