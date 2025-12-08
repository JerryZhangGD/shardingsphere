package org.apache.shardingsphere.authority.decorator;

import lombok.Getter;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.pojo.SQLToken;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.pojo.Substitutable;

@Getter
public final class FilterConditionToken extends SQLToken implements Substitutable {
    private final int stopIndex;

    private final String subQueryWithCondition;

    public FilterConditionToken(final int startIndex, final int stopIndex,final String subQueryWithCondition) {
        super(startIndex);
        this.stopIndex = stopIndex;
        this.subQueryWithCondition = subQueryWithCondition;
    }

    @Override
    public String toString() {
        return subQueryWithCondition;
    }
}
