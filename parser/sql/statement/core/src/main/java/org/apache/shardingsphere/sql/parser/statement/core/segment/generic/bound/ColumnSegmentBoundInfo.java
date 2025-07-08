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

package org.apache.shardingsphere.sql.parser.statement.core.segment.generic.bound;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.sql.parser.statement.core.value.identifier.IdentifierValue;

/**
 * Column segment bound info.
 */
@Getter
public final class ColumnSegmentBoundInfo {
    
    private IdentifierValue originalDatabase;
    
    private IdentifierValue originalSchema;
    
    private IdentifierValue originalTable;
    
    private IdentifierValue originalColumn;

    private Integer sensitiveLevel;

    public ColumnSegmentBoundInfo(IdentifierValue originalDatabase, IdentifierValue originalSchema, IdentifierValue originalTable, IdentifierValue originalColumn) {
        this.originalDatabase = originalDatabase;
        this.originalSchema = originalSchema;
        this.originalTable = originalTable;
        this.originalColumn = originalColumn;
        if(StringUtils.isNotEmpty(originalDatabase.getValue())&&StringUtils.isNotEmpty(originalTable.getValue())&&StringUtils.isNotEmpty(originalColumn.getValue())){
            //todo 这里匹配设置敏感等级

            //默认敏感2级
            this.sensitiveLevel = 1;
        }else {
            this.sensitiveLevel = 2;
        }
    }

    public ColumnSegmentBoundInfo(IdentifierValue originalDatabase, IdentifierValue originalSchema, IdentifierValue originalTable, IdentifierValue originalColumn, Integer sensitiveLevel) {
        this.originalDatabase = originalDatabase;
        this.originalSchema = originalSchema;
        this.originalTable = originalTable;
        this.originalColumn = originalColumn;
        this.sensitiveLevel = sensitiveLevel;
    }

    public ColumnSegmentBoundInfo(final IdentifierValue originalColumn) {
        originalDatabase = new IdentifierValue("");
        originalSchema = new IdentifierValue("");
        originalTable = new IdentifierValue("");
        this.originalColumn = originalColumn;
    }
}
