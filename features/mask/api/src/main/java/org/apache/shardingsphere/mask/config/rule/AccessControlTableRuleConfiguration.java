package org.apache.shardingsphere.mask.config.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class AccessControlTableRuleConfiguration {
    private String tableName;
    private Boolean allFlag;
    private List<String> columns = new ArrayList();
}
