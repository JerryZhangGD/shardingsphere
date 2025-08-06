package org.apache.shardingsphere.proxy.frontend.mysql.command;

import com.google.gson.Gson;
import lombok.Data;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.binder.context.segment.select.projection.SensitiveSource;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.config.props.ConfigurationPropertyKey;
import org.apache.shardingsphere.infra.util.datetime.DateTimeFormatterFactory;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordSqlLogThread implements Runnable{
    private String sourceIp;
    private String user;
    private String method;
    private String sql;
    private String detail;
    private Integer total;
    private String riskType;
    private String opeUser;
    private List<SensitiveSource> sensitiveSourceList;

    @Override
    public void run() {
        Gson gson = new Gson();
        String theTimeStr = LocalDateTime.now().format(DateTimeFormatterFactory.getStandardFormatter());

        ConfigurationProperties props = ProxyContext.getInstance().getContextManager().getMetaDataContexts().getMetaData().getProps();

        String logUrl = props.getValue(ConfigurationPropertyKey.LOG_URL).toString();
        String logUserName = props.getValue(ConfigurationPropertyKey.LOG_USERNAME).toString();
        String logPassword = props.getValue(ConfigurationPropertyKey.LOG_PASSWORD).toString();
        String logApi = props.getValue(ConfigurationPropertyKey.LOG_API).toString();
        String logToken = props.getValue(ConfigurationPropertyKey.LOG_TOKEN).toString();

        String userId = user;
        if(user.contains("_")){
            userId = user.split("_")[1];
        }
        String userName = userId;
        if(StringUtils.isNotEmpty(opeUser)){
            userName = opeUser;
        }



        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("methodName",method);
        formBuilder.add("ip",sourceIp);
        formBuilder.add("args",sql);
        formBuilder.add("messages",detail);
        formBuilder.add("name","数据安全网关");
        formBuilder.add("results","SUCCESS");
        formBuilder.add("theTime",theTimeStr);
        formBuilder.add("type","OPERATION");
        formBuilder.add("userId",userId);
        formBuilder.add("userName",userName);

        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(logApi)
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization",logToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body1 = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }











        try (
                Connection connection = DriverManager.getConnection(logUrl,logUserName,logPassword);
                Statement statement = connection.createStatement())
        {
            String sensitiveSourceListStr = "";
            if(this.sensitiveSourceList!=null&&sensitiveSourceList.size()>0){
                sensitiveSourceListStr=gson.toJson(this.sensitiveSourceList);
            }
            statement.execute(String.format("INSERT INTO ods.ODS_DDW_DSG_AUDIT_LOG (`opeTime`, `workcode`,`user`,`user_name`, `ip`, `method`, `sql`, `result`, `detail`,`total`, `sensitive_source_list_str`) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s');", theTimeStr, userId,user,userName, sourceIp, method, sql,riskType,detail,total,sensitiveSourceListStr));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
