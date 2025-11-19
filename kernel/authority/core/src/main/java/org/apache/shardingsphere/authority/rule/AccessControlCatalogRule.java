package org.apache.shardingsphere.authority.rule;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@Getter
public class AccessControlCatalogRule {
    private final Long themeDomainId;
    private final Boolean assetTableAccessFlag;
    private final Boolean assetApiAccessFlag;
    private final Boolean assetIndicatorAccessFlag;
    private final Boolean assetReportTableAccessFlag;
    private final Boolean desensitizeWhiteListFlag;
    private final Date expirationTime;

    private final  Date assetTableAccessTime;
    private final Date assetApiAccessTime;
    private final Date assetIndicatorAccessTime;
    private final Date assetReportTableAccessTime;

}
