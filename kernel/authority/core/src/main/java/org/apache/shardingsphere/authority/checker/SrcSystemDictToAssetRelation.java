package org.apache.shardingsphere.authority.checker;


import lombok.Data;

@Data
public class SrcSystemDictToAssetRelation {
    private Long assetId;
    private Long srcSystemDictId;
    private AssetType assetType;
    private String fullTableName;
}
