package org.apache.shardingsphere.authority.decorator;

import org.apache.shardingsphere.authority.rule.AccessControlRule;
import org.apache.shardingsphere.infra.annotation.HighFrequencyInvocation;
import org.apache.shardingsphere.infra.binder.context.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.rewrite.context.SQLRewriteContext;
import org.apache.shardingsphere.infra.rewrite.context.SQLRewriteContextDecorator;
import org.apache.shardingsphere.infra.rewrite.parameter.rewriter.ParameterRewriter;
import org.apache.shardingsphere.infra.rewrite.sql.token.common.generator.builder.SQLTokenGeneratorBuilder;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.session.connection.ConnectionContext;
import org.apache.shardingsphere.infra.session.query.QueryContext;

import java.util.Collection;
import java.util.Collections;

@HighFrequencyInvocation
public class FilterConditionDecorator implements SQLRewriteContextDecorator<AccessControlRule> {
    @Override
    public void decorate(AccessControlRule rule, ConfigurationProperties props, SQLRewriteContext sqlRewriteContext, RouteContext routeContext,final QueryContext queryContext) {
        SQLStatementContext sqlStatementContext = sqlRewriteContext.getSqlStatementContext();
        ConnectionContext connectionContext = sqlRewriteContext.getConnectionContext();
        sqlRewriteContext.addSQLTokenGenerators(Collections.singleton(new FilterConditionTokenGenerator(rule, sqlStatementContext, connectionContext,queryContext)));
    }

    @Override
    public int getOrder() {
        return 9999;
    }

    @Override
    public Class<AccessControlRule> getTypeClass() {
        return AccessControlRule.class;
    }
}
