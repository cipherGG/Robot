package com.gg.robot.bean;

/**
 * 聊天信息对象
 *
 * @author Kevin
 */
public class ChatBean {

    public String text;// 内容
    public boolean isAsk;// true表示提问者,否则是回答者

    public ChatBean(String text, boolean isAsk) {
        this.text = text;
        this.isAsk = isAsk;
    }

}
