package org.apache.shardingsphere.authority.checker;

import lombok.Data;

import java.util.List;

@Data
public class SrcSystemDictAssetRelation {
    private Long srcSystemDictId;
    private List<SrcSystemDictToAssetRelation> srcSystemDictToAssetRelationList;
}
