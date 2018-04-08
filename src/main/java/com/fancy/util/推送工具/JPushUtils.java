package com.fancy.util.推送工具;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br> 
 * 〈极光推送工具〉
 *  <dependency>
     <groupId>cn.jpush.api</groupId>
     <artifactId>jpush-client</artifactId>
     <version>3.3.4</version>
    </dependency>
 *
 * @author fangdaji
 * @create 2018/4/8
 * @since 1.0.0
 */
public class JPushUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);

    protected static final String APP_KEY ="d4ee2375846bc30fa51334f5";
    protected static final String MASTER_SECRET = "f3b222f7e0dde430b6d8fa5a";
    public static ClientConfig clientConfig = ClientConfig.getInstance();
    public static JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);

    protected static final Boolean APNS_PRODUCTION = true; // true 生产环境


    /**
     * 给指定设备发送推送消息
     * @param jpushClient
     * @param message
     * @param extra
     * @param alias
     * @return
     * @throws Exception
     */
    public static PushResult pushMessage(JPushClient jpushClient,Platform platform,String message,JsonObject extra,String... alias)
            throws Exception {
        PushPayload payload ;
        Audience audience ;
        if(StringUtils.isEmpty(alias)){
            audience = Audience.all(); // Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }else{
            audience = Audience.alias(alias); // Audience设置为alias，说明采用指定别名推送
        }

        if(StringUtils.isEmpty(extra)){
            payload = PushPayload.newBuilder().setPlatform(platform)// 设置接受的平台
                    .setAudience(audience)
                    .setNotification(Notification.alert(message)).build();
        }else{
            payload = PushPayload.newBuilder().setPlatform(platform)// 设置接受的平台
                    .setAudience(audience)
                    .setNotification(
                            Notification.newBuilder()
                            .setAlert(message)
                            .addPlatformNotification(AndroidNotification.newBuilder().addExtra("attribute", extra).build())
                            .addPlatformNotification(IosNotification.newBuilder().setSound("happy").addExtra("attribute", extra).build())
                            .build()
                    )
                    .setMessage(Message.content(message))
                    .setOptions(Options.newBuilder().setApnsProduction(APNS_PRODUCTION).build())
                    .build();
        }

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            //System.out.println(result);
            return result;
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
        }

        throw new Exception("推送异常");
    }

    /**
     * 给所有设备发送推送消息
     * @param jpushClient
     * @param message
     * @param extra
     * @param alias
     * @return
     * @throws Exception
     */
    public static PushResult pushAllPlatMessage(JPushClient jpushClient,String message,JsonObject extra,String... alias) throws Exception {
        return pushMessage(jpushClient,Platform.all(),message,extra,alias);
    }

    /**
     * 给所有设备发送推送消息
     * @param jpushClient
     * @param message
     * @param extra
     * @param alias
     * @return
     * @throws Exception
     */
    public static PushResult pushIOSPlatMessage(JPushClient jpushClient,String message,JsonObject extra,String... alias) throws Exception {
        return pushMessage(jpushClient,Platform.ios(),message,extra,alias);
    }

    /**
     * 给所有设备发送推送消息
     * @param jpushClient
     * @param message
     * @param extra
     * @param alias
     * @return
     * @throws Exception
     */
    public static PushResult pushAndroidPlatMessage(JPushClient jpushClient,String message,JsonObject extra,String... alias) throws Exception {
        return pushMessage(jpushClient,Platform.android(),message,extra,alias);
    }


    public static void main(String[] args) {
        try {
            pushAllPlatMessage(JPushUtils.jpushClient,"Test",null,null);
        } catch (Exception e) {
            LOG.error("推送异常", e);
        }
    }


}