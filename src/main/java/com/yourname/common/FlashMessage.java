package com.yourname.common;

import java.io.Serializable;

/**
 * 一次性提示消息（成功 / 失败）
 * <p>
 * 用于解决"redirect 后消息丢失"：写操作时把提示存进 session，
 * 跳转后的列表页读一次即清除。
 *
 * @author yourname
 */
public class FlashMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 成功提示 */
    public static final String OK = "ok";
    /** 失败提示 */
    public static final String ERR = "err";

    private final String type;
    private final String text;

    private FlashMessage(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public static FlashMessage ok(String text) {
        return new FlashMessage(OK, text);
    }

    public static FlashMessage err(String text) {
        return new FlashMessage(ERR, text);
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

}
