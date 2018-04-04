package com.fancy.util.云存储工具;

import com.aliyun.oss.OSSClient;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br> 
 * 〈阿里云oss上传工具〉
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class AliyunOssUtils {

    private static String ENDPOINT = "<endpoint, http://oss-cn-hangzhou.aliyuncs.com>";
    private static String ACCESSKEY_ID = "<accessKeyId>";
    private static String ACCESS_KEY_SECRET = "<accessKeySecret>";
    private static String BUCKET_NAME = "<bucketName>";
    private static String PROJECT_ENDPOINT = "<bucketName>";

    public static String base64Upload(String imageData, String directory) throws Exception {

        if (!StringUtils.isEmpty(imageData)) {
            String imgStr = imageData.substring(imageData.indexOf(",") + 1, imageData.length());
            try {
                // Base64解码
                byte[] b = Base64.decode(imgStr);
                String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
                InputStream io = new ByteArrayInputStream(b);
                try {
                    // 上传阿里云
                    OSSClient ossClient = new OSSClient(ENDPOINT,ACCESSKEY_ID,ACCESS_KEY_SECRET);
                    ossClient.putObject(BUCKET_NAME,directory + fileName, io);
                    // 关闭client
                    ossClient.shutdown();
                    return PROJECT_ENDPOINT + "/" + directory + fileName;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("上传失败");
                }

            } catch (Exception e) {
                throw new Exception("编码失败");
            }
        } else {
            throw new Exception("编码内容为空");
        }
    }

    /**
     * 表单上传
     * @param request
     * @param directory
     * @return
     * @throws Exception
     */
    public static List<String>  requestUpload(HttpServletRequest request,String directory) throws Exception {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartRequest.getFiles("file");
            if(files.size() == 0) {
                MultipartFile file = multipartRequest.getFile("file");
                files.add(file);
            }

            List<String> imgUrls = new ArrayList<>();
            for (MultipartFile mFile : files) {
                String originalFilename = mFile.getOriginalFilename();
                InputStream io = mFile.getInputStream();
                String fileName = UUID.randomUUID().toString().replaceAll("-", "")
                        + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                try {
                    // 上传阿里云
                    OSSClient ossClient = new OSSClient(ENDPOINT,ACCESSKEY_ID,ACCESS_KEY_SECRET);
                    ossClient.putObject(BUCKET_NAME,directory + fileName, io);
                    // 关闭client
                    ossClient.shutdown();

                    imgUrls.add(PROJECT_ENDPOINT + "/" + directory + fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("上传失败");
                }
            }

            return imgUrls;

        } catch (Exception e) {
            throw new Exception("文件读取失败");
        }
    }

}