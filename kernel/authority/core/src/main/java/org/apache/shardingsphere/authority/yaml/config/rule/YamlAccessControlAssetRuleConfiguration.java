package org.apache.shardingsphere.authority.yaml.config.rule;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.util.yaml.YamlConfiguration;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class YamlAccessControlAssetRuleConfiguration implements YamlConfiguration {
    private Long assetId;
    private String assetType;
    private List<Long> themeDomainIdList;
    private Date expirationTime;
}
