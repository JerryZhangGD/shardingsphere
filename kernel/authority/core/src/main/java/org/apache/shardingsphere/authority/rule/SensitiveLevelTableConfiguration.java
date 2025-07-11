package org.apache.shardingsphere.authority.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
@Setter
public class SensitiveLevelTableConfiguration {
    private final String name;
    private final Collection<SensitiveLevelColumnConfiguration> columns;
}
