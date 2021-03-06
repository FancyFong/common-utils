package com.fancy.util.短信工具;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * Created on 17/6/7. 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可) 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar 2:aliyun-java-sdk-dysmsapi.jar
 * <p>
 * 备注:Demo工程编码采用UTF-8 国际短信发送请勿参照此DEMO
 */
public class DayuSmsUtil {

    // 产品名称:云通信短信API产品,开发者无需替换
    static final String PRODUCT = "Dysmsapi";
    // 产品域名,开发者无需替换
    static final String DOMAIN = "dysmsapi.aliyuncs.com";

    static final String ACCESSKEYID = "";

    static final String ACCESSKEYSECRET = "";

    /**
     * 发送短信
     * @param phone 待发送手机号
     * @param templateParam 模板内容
     * @param signName 短信签名
     * @param templateCode 短信模板
     * @return
     */
    public static boolean send(String phone, String templateParam,String signName,String templateCode) throws Exception {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            // 组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            // 必填:待发送手机号
            request.setPhoneNumbers(phone);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            // 必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"您的验证码是：${code}，请在${min}分钟内完成验证,此处的值为
            request.setTemplateParam(templateParam);

            // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            // request.setSmsUpExtendCode("90997");

            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            // hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            String res = sendSmsResponse.getCode();
            String message = sendSmsResponse.getMessage();
            if (res.equals("OK")) {
                return true;
            }else{
                throw new Exception("发送短信异常:"+message);
            }
        } catch (ClientException e) {
            e.printStackTrace();
            throw new Exception("发送短信-未知错误");
        }
    }



}