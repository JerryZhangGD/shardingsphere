package org.apache.shardingsphere.authority.checker;

import lombok.Data;

@Data
public class ThemeAnalysisToAssetRelation {
    private Long assetId;
    private Long themeAnalysisId;
    private AssetType assetType;
    private String fullTableName;
}
