package com.gg.robot.engine;

import android.content.Context;
import android.text.TextUtils;

import com.gg.robot.bean.AnswerBean;
import com.gg.robot.bean.VoiceBean;
import com.gg.robot.utils.HttpUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author cipherGG
 * Created by Administrator on 2016/1/10.
 * describe
 */
public class Chats {

    /**
     * 获取回答文本
     */
    public static String getTextAnswer(String question) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", HttpUtils.ROBOT_KEY);
        map.put("info", question);

        String data = HttpUtils.getString(HttpUtils.ROBOT_INDEX_PATH, map);

        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            AnswerBean answer = gson.fromJson(data, AnswerBean.class);

            if (answer != null && answer.result != null && !TextUtils.isEmpty(answer.result.text)) {
                return answer.result.text;
            }
        }
        return "宝宝没听清~~~";
    }


    /**
     * 解析语音
     */
    public static String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean bean = gson.fromJson(resultString, VoiceBean.class);
        ArrayList<VoiceBean.WSBean> ws = bean.ws;

        StringBuilder sb = new StringBuilder();
        for (VoiceBean.WSBean wsBean : ws) {
            String text = wsBean.cw.get(0).w;
            sb.append(text);
        }
        return sb.toString();
    }

    /**
     * 语音朗诵
     */
    public static void read(Context context, String text) {
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);

        mTts.setParameter(SpeechConstant.VOICE_NAME, "vinn");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        mTts.startSpeaking(text, null);
    }

}
