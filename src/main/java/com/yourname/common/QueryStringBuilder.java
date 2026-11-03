package com.yourname.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 把搜索条件拼成 URL 查询串，供分页链接复用（翻页时不丢条件）
 *
 * @author yourname
 */
public final class QueryStringBuilder {

    private QueryStringBuilder() {
    }

    /** 非空才追加，值统一 UTF-8 编码 */
    public static void append(StringBuilder sb, String name, String value) {
        if (value == null) {
            return;
        }
        String v = value.trim();
        if (v.isEmpty()) {
            return;
        }
        try {
            sb.append('&').append(name).append('=').append(URLEncoder.encode(v, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // UTF-8 必然存在，忽略
        }
    }

}
