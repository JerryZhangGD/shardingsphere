package org.apache.shardingsphere.authority.rule;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class AccessControlAssetRule {
    private final Long assetId;
    private final String assetType;
    private final List<Long> themeDomainIdList;
    private final Date expirationTime;
}
