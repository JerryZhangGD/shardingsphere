/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.binder.context.segment.select.projection.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.Projection;
import org.apache.shardingsphere.sql.parser.statement.core.segment.SQLSegment;
import org.apache.shardingsphere.sql.parser.statement.core.value.identifier.IdentifierValue;

import java.util.Optional;

/**
 * Derived projection.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class DerivedProjection implements Projection {
    
    private final String expression;
    
    private final IdentifierValue alias;
    
    private final SQLSegment derivedProjectionSegment;

    private Integer sensitiveLevel;

    private Boolean desensitizeFlag;

    private int projectionType;

    private int projectionLength;

    @Override
    public int getProjectionLength() {
        return this.projectionLength;
    }

    @Override
    public void setProjectionLength(int projectionLength) {
        this.projectionLength = projectionLength;
    }
    
    @Override
    public String getColumnName() {
        return expression;
    }
    
    @Override
    public String getColumnLabel() {
        return getAlias().map(IdentifierValue::getValue).orElse(expression);
    }
    
    @Override
    public Optional<IdentifierValue> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Integer getSensitiveLevel() {
        return this.sensitiveLevel;
    }

    @Override
    public void setSensitiveLevel(Integer sensitiveLevel) {
        this.sensitiveLevel = sensitiveLevel;
    }

    @Override
    public Boolean getDesensitizeFlag() {
        return this.desensitizeFlag;
    }

    @Override
    public void setDesensitizeFlag(Boolean desensitizeFlag) {
        this.desensitizeFlag=desensitizeFlag;
    }

    @Override
    public int getProjectionType() {
        return this.projectionType;
    }

    @Override
    public void setProjectionType(int projectionType) {
        this.projectionType = projectionType;
    }
}
