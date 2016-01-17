package com.gg.robot.bean;

/**
 * author cipherGG
 * Created by Administrator on 2016/1/10.
 * describe
 */
public class AnswerBean {
    public String reason;
    public Result result;
    public String error_code;

    public class Result {
        public String code;
        public String text;
    }
}
