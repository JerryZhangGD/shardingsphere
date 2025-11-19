package org.apache.shardingsphere.authority.checker;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class AssetToTableRelation {
    private Long assetId;
    private Long themeDomainId;
    private AssetType assetType;
    private Set<String> fullTableNameSet;
}
