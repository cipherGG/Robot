package com.gg.robot.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/25.
 * describe
 */
public class HttpUtils {
    public static final String ROBOT_KEY = "141724ebf2336e4dee725909b41df44e";
    public static final String ROBOT_INDEX_PATH = "http://op.juhe.cn/robot/index";
    public static final String ROBOT_CODE_PATH = "http://op.juhe.cn/robot/code";

    public static ResponseBody getBody(String url, HashMap<String, Object> params) {
        OkHttpClient client = new OkHttpClient();

        try {
            StringBuilder buffer = new StringBuilder();
            //GET里记得加上？
            buffer.append("?");

            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    buffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), "utf-8"))
                            .append("&");
                }
                //删去最后一个符号&
                buffer.deleteCharAt(buffer.length() - 1);
            }

            Request request = new Request.Builder()
                    .url(url + buffer.toString())
                    .build();

            Response response = client.newCall(request).execute();

            if (response != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Response", " == null");
        return null;
    }

    public static String getString(String url, HashMap<String, Object> params) {
        ResponseBody responseBody = getBody(url, params);

        if (responseBody != null) {
            try {
                return responseBody.string();//看清楚是string，不是toString！！！
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("ResponseBody", "== null");
        return null;
    }

    public static InputStream getStream(String url, HashMap<String, Object> params) {
        ResponseBody responseBody = getBody(url, params);

        if (responseBody != null) {
            return responseBody.byteStream();
        }
        Log.e("ResponseBody", "== null");
        return null;
    }

    public static byte[] getBytes(String url, HashMap<String, Object> params) {
        ResponseBody responseBody = getBody(url, params);

        if (responseBody != null) {
            try {
                return responseBody.bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("ResponseBody", "== null");
        return null;
    }

    public static ResponseBody postBody(String url, String json) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
