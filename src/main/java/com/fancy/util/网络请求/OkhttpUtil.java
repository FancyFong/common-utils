package com.fancy.util.网络请求;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  okhttp请求工具
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class OkhttpUtil {

    static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        String getStr = getMethod("http://www.baidu.com?position=0&page=1&rows=10",OkhttpUtil.setHeaders(null));
        System.out.println(getStr);

        Map<String,Object> map = new HashMap<>();
        map.put("openId","oIBWWjkNo4i7NUJ2-S5TpG7ikYaE");
        map.put("communityId",1);
        map.put("type",4);
        String getPostStr = postFormMethod("http://www.baidu.com", OkhttpUtil.setHeaders(null),map);
        System.out.println(getPostStr);

        String json = "{\n" +
                "  \"address\": \"string\",\n" +
                "  \"age\": 0,\n" +
                "  \"city\": \"string\",\n" +
                "  \"departId\": 0,\n" +
                "  \"district\": \"string\",\n" +
                "  \"doctorImgurl\": \"string\",\n" +
                "  \"doctorMajor\": \"string\",\n" +
                "  \"doctorName\": \"string\",\n" +
                "  \"doctorPosition\": \"string\",\n" +
                "  \"hospitalName\": \"string\",\n" +
                "  \"id\": 0,\n" +
                "  \"identity\": \"string\",\n" +
                "  \"openId\": \"string\",\n" +
                "  \"province\": \"string\",\n" +
                "  \"sex\": 0\n" +
                "}";
        String getPostJsonStr = postJsonMethod("http://www.baidu.com", OkhttpUtil.setHeaders(null),json);
        System.out.println(getPostJsonStr);
    }

    /**
     * 设置请求头
     * @param headersParams
     * @return
     */
    public static Headers setHeaders(Map<String, String> headersParams){
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headersParams != null){
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersBuilder.add(key, headersParams.get(key));
            }
        }
        Headers headers = headersBuilder.build();

        return headers;
    }

    /**
     * get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String getMethod(String url,Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * post Form表单请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String postFormMethod(String url,Headers headers, Map<String, Object> map) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.add(entry.getKey(),String.valueOf(entry.getValue()));
        }


        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * post Json请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String postJsonMethod(String url,Headers headers, String postBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        postBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


}