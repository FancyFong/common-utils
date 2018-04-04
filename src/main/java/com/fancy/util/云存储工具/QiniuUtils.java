package com.fancy.util.云存储工具;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br> 
 * 〈七牛云存储〉
 *  <dependency>
        <groupId>com.qiniu</groupId>
        <artifactId>qiniu-java-sdk</artifactId>
        <version>[7.2.0, 7.2.99]</version>
    </dependency>
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class QiniuUtils {

    // ...生成上传凭证，然后准备上传
    String accessKey;
    String secretKey;
    String bucket;
    String qiniuCDN;


    /**
     * base64字符串上传
     *
     * @param imageData
     * @return
     */
    public String base64Upload(String imageData) {

        if (!StringUtils.isEmpty(imageData)) {
            String imgStr = imageData.substring(imageData.indexOf(",") + 1, imageData.length());

            try {

                // 默认不指定key的情况下，以文件内容的hash值作为文件名
                String key = UUID.randomUUID().toString().replace("-", "") + ".png";
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);
                try {

                    String url = "https://upload-z2.qbox.me/putb64/" + -1 + "/key/" + UrlSafeBase64.encodeToString(key);

                    RequestBody rb = RequestBody.create(null, imgStr);
                    Request request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/octet-stream")
                            .addHeader("Authorization", "UpToken " + upToken).post(rb).build();

                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Response response = client.newCall(request).execute();

                    if (response.code() == 200) {
                        return qiniuCDN + key;
                    }

                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        // ignore
                    }
                }

                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * 以流形式上传
     *
     * @param inputStream
     * @return
     */
    public String uploadInputStream(InputStream inputStream, String fileName) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = null;
        try {
            cfg = new Configuration(Zone.zone2());
        } catch (Exception e) {
            try {
                cfg = new Configuration(Zone.zone0());
            } catch (Exception e0) {
                try {
                    cfg = new Configuration(Zone.zone1());
                } catch (Exception e1) {
                    try {
                        cfg = new Configuration(Zone.zoneNa0());
                    } catch (Exception e2) {
                        return null;
                    }
                }
            }
        }

        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        String key = fileName;

        try {

            // 默认不指定key的情况下，以文件内容的hash值作为文件名
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                return qiniuCDN + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * request上传
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String ioUpload(HttpServletRequest request) throws IOException {
        String upFlag = request.getParameter("isup");
        String delFlag = request.getParameter("isdel");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String upload = "";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            String originalFilename = file.getOriginalFilename();//文件名
            if (originalFilename.trim() != "") {
                String key =  UUID.randomUUID().toString().replace("-", "") + "." + originalFilename.length();
                InputStream inputStream = file.getInputStream();
                upload = uploadInputStream(inputStream, key);
            }
        }
        return upload;
    }

}