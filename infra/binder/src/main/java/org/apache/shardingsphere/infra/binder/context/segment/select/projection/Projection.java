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

package org.apache.shardingsphere.infra.binder.context.segment.select.projection;

import org.apache.shardingsphere.sql.parser.statement.core.value.identifier.IdentifierValue;

import java.util.Optional;

/**
 * Projection interface.
 */
public interface Projection {
    
    /**
     * Get column name.
     *
     * @return column name
     */
    String getColumnName();
    
    /**
     * Get column label.
     *
     * @return column label
     */
    String getColumnLabel();
    
    /**
     * Get expression.
     *
     * @return expression
     */
    String getExpression();
    
    /**
     * Get alias.
     *
     * @return alias
     */
    Optional<IdentifierValue> getAlias();

    Integer getSensitiveLevel();

    void setSensitiveLevel(Integer sensitiveLevel);

    Boolean getDesensitizeFlag();

    void setDesensitizeFlag(Boolean desensitizeFlag);

    int getProjectionType();

    void setProjectionType(int projectionType);

    int getProjectionLength();

    void setProjectionLength(int projectionLength);
}
