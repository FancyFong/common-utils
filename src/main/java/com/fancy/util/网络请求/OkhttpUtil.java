package com.fancy.util.网络请求;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 *  okhttp请求工具
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class OkhttpUtil {

    static OkHttpClient client = new OkHttpClient();

    public static String getMethod(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}