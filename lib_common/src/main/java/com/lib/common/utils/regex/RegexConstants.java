package com.lib.common.utils.regex;

/**
 * @author : yzf
 * time : 2019/08/05
 * description：
 */
public class RegexConstants {

    /**
     * 正则：手机号码
     */
    public static final String REGEX_MOBILE_EXACT = "^(1[0-9])\\d{9}$";

    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "([0-9]{3,4}-)?[0-9]{7,8}";

    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
}
