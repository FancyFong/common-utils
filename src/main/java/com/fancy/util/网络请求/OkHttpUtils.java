package com.fancy.util.网络请求;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * okhttp请求工具
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class OkHttpUtils {

    static OkHttpClient client = new OkHttpClient();

    private final static String APPLICATION_JSON = "application/json; charset=utf-8";

    private final static String OCTET_STREAM = "application/octet-stream";

    /**
     * get请求
     * 提交键值对形式 @RequestParam方式接收
     *
     * @param url 请求地址 + 路径参数
     */
    public static String get(String url) throws IOException {
        return get(url, setHeaders(null));
    }

    /**
     * post请求(application/x-www-form-urlencoded)
     * 提交键值对形式 @RequestParam方式接收
     *
     * @param url  请求地址
     * @param form 表单参数（Key-Value）
     * @return
     */
    public static String postForm(String url, Map<String, Object> form) throws IOException {
        return postForm(url, setHeaders(null), form);
    }

    /**
     * post 请求(application/json)
     * 提交json字符串 @RequestBody方式接收
     *
     * @param url  请求地址 + 路径参数
     * @param body body参数
     * @return
     */
    public static String postJson(String url, String body) throws IOException {
        return postJson(url, setHeaders(null), body);
    }

    /**
     * PUT 请求(application/x-www-form-urlencoded)
     * 提交键值对形式 @RequestParam方式接收
     *
     * @param url  请求地址
     * @param form 表单参数（Key-Value）
     * @return
     */
    public static String putForm(String url, Map<String, Object> form) throws IOException {
        return putForm(url, setHeaders(null), form);
    }

    /**
     * put 请求(application/json)
     * 提交json字符串 @RequestBody方式接收
     *
     * @param url  请求地址 + 路径参数
     * @param body body参数
     * @return
     */
    public static String putJson(String url, String body) throws IOException {
        return putJson(url, setHeaders(null), body);
    }

    /**
     * delete 请求
     * 提交键值对形式 @RequestParam方式接收
     *
     * @param url 请求地址 + 路径参数
     * @return
     */
    public static String delete(String url) throws IOException {
        return delete(url, setHeaders(null));
    }

    /**
     * delete 请求(application/json)
     * 提交json字符串 @RequestBody方式接收
     *
     * @param url  请求地址 + 路径参数
     * @param body body参数
     * @return
     */
    public static String deleteJson(String url, String body) throws IOException {
        return deleteJson(url, setHeaders(null), body);
    }

    /**
     * 单文件上传 POST请求
     *
     * @param url  请求地址 + 路径参数
     * @param name 参数名称
     * @param file 文件
     * @return
     */
    public static String uploadMultiFile(String url, String name, File file) throws IOException {
        return uploadMultiFile(url, setHeaders(null), name, file);
    }


    /********************************************************************************/

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    public static Headers setHeaders(Map<String, String> headersParams) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headersParams != null) {
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
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @return
     */
    public static String get(String url, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    /**
     * post请求(application/x-www-form-urlencoded)
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param form    表单参数（Key-Value）
     * @return
     */
    public static String postForm(String url, Headers headers, Map<String, Object> form) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
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
     * PUT 请求(application/x-www-form-urlencoded)
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param form    表单参数（Key-Value）
     * @return
     */
    public static String putForm(String url, Headers headers, Map<String, Object> form) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .put(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    /**
     * delete 请求
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @return
     */
    public static String delete(String url, Headers headers) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }


    /**
     * post 请求(application/json)
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @param body    body参数
     * @return
     */
    public static String postJson(String url, Headers headers, String body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(RequestBody.create(
                        MediaType.parse(APPLICATION_JSON),
                        body))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * put 请求(application/json)
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @param body    body参数
     * @return
     */
    public static String putJson(String url, Headers headers, String body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .put(RequestBody.create(
                        MediaType.parse(APPLICATION_JSON),
                        body))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * delete 请求(application/json)
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @param body    body参数
     * @return
     */
    public static String deleteJson(String url, Headers headers, String body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .delete(RequestBody.create(
                        MediaType.parse(APPLICATION_JSON),
                        body))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 单文件上传 POST请求
     *
     * @param url     请求地址 + 路径参数
     * @param headers 请求头
     * @param name    参数名称
     * @param file    文件
     * @return
     */
    public static String uploadMultiFile(String url, Headers headers, String name, File file) throws IOException {
        RequestBody fileBody = RequestBody.create(MediaType.parse(OCTET_STREAM), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)//传参数、文件或者混合，改一下就行请求体就行
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}