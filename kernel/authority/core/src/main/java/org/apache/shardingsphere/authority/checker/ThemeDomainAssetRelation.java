package org.apache.shardingsphere.authority.checker;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ThemeDomainAssetRelation {
    private Long themeDomainId;
    private List<AssetToTableRelation> assetToTableRelationList;
}
