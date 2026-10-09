package org.apache.shardingsphere.authority.checker;

import lombok.Data;

import java.util.List;

@Data
public class ThemeAnalysisAssetRelation {
    private Long themeAnalysisId;
    private List<ThemeAnalysisToAssetRelation> themeAnalysisToAssetRelationList;
}
