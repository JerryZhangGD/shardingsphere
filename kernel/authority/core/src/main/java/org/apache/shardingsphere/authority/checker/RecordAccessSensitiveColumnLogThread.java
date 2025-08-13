package org.apache.shardingsphere.authority.checker;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.config.props.ConfigurationPropertyKey;
import org.apache.shardingsphere.infra.util.datetime.DateTimeFormatterFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Slf4j(topic = "RecordAccessSensitiveColumnLogThread")
public class RecordAccessSensitiveColumnLogThread implements Runnable{
    private String user;
    private String sql;
    private String opeUser;
    private String riskModule;
    private List<Map<String, Long>> recognizeResultMapList;
    private String databaseName;
    private String tableName;
    private String columnName;
    private ConfigurationProperties props;

    @Override
    public void run() {
        String theTimeStr = LocalDateTime.now().format(DateTimeFormatterFactory.getStandardFormatter());
        String logUrl = props.getValue(ConfigurationPropertyKey.LOG_URL).toString();
        String logUserName = props.getValue(ConfigurationPropertyKey.LOG_USERNAME).toString();
        String logPassword = props.getValue(ConfigurationPropertyKey.LOG_PASSWORD).toString();
        String logApi = props.getValue(ConfigurationPropertyKey.LOG_API).toString();
        String logToken = props.getValue(ConfigurationPropertyKey.LOG_TOKEN).toString();
        log.info(String.format("获取的参数,user:%s,sql:%s,opeUser:%s,riskModule:%s,databaseName:%s,tableName:%s,columnName:%s,logUrl:%s,logUserName:%s,logPassword:%s,logApi:%s,logToken:%s",user,sql,opeUser,riskModule,databaseName,tableName,columnName,logUrl,logUserName,logPassword,logApi,logToken));

        String userId = "1000";
        if(user.contains("_")){
            userId = user.split("_")[1];
        }
        String userName = userId;
        if(StringUtils.isNotEmpty(opeUser)){
            userName = opeUser;
        }

        if(recognizeResultMapList!=null&&recognizeResultMapList.size()>0){
            for(Map<String,Long> map:recognizeResultMapList){
                Long ruleId = map.get("ruleId");
                Long categoryId = map.get("categoryId");
                Long levelId = map.get("levelId");

                try (
                        Connection connection = DriverManager.getConnection(logUrl,logUserName,logPassword);
                        Statement statement = connection.createStatement())
                {
                    statement.execute(String.format("INSERT INTO ods.ODS_DDW_SENSITIVE_ACCESS_LOG (`access_time`, `workcode`,`user`,`access_user`, `access_module`, `column_name`, `table_name`, `database_name`,`sql`, `rule_id`,`category_id`, `level_id`) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s','%s', '%s');", theTimeStr, userId,user,userName, riskModule, columnName, tableName,databaseName,sql,ruleId,categoryId,levelId));
                } catch (Exception e) {
                    log.error("发送日志到数仓敏感访问日志失败",e);
                }
            }
        }
    }
}
