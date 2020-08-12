package com.taxiao.cn.commonlibrary.uitl.data;

import android.text.TextUtils;

import java.util.Locale;

/**
 * 字符串相关工具类
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 */
public class StringUtils {

    private StringUtils() {

    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 判断一个字符串是否为整型
     *
     * @param str
     * @return
     */
    public static boolean isNumberic(CharSequence str) {
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 判断字符串是一个0或者全部是0
     *
     * @param number
     * @return true是0 false不是0
     */
    public static boolean isZeroOrAllZero(String number) {
        boolean flag = true;
        int length = number.length();
        char[] chars = number.toCharArray();
        if (length == 1) {
            if (!number.equals("0")) {
                if (!number.equals(".")) {
                    flag = false;
                }
            }
        } else if (length > 1) {
            for (int i = 0; i < length; i++) {
                if (!String.valueOf(chars[i]).equals("0")) {
                    if (!String.valueOf(chars[i]).equals(".")) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }


    /**
     * 转换成带* 处理
     *
     * @param str
     * @return
     */
    public static String stringFormat(String str) {
        StringBuilder builder = new StringBuilder();
        if (TextUtils.isEmpty(str)) {
            builder.append("");
        } else {
            int length = str.length();
            if (length == 1) {
                builder.append(str);
            } else if (length == 2) {
                builder.append("*");
                builder.append(str.charAt(1));
            } else {
                for (int i = 0; i < length; i++) {
                    if (i == 0 || i == length - 1) {
                        builder.append(str.charAt(i));
                    } else {
                        builder.append("*");
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * 返回小数点后两位的数值
     */
    public static String getCost(double cost) {
        return String.format(Locale.CHINESE, "%.2f元", cost);
    }

    /**
     * 返回小数点后两位的数值
     */
    public static String getCostPlus(double cost) {
        if (cost >= 0)
            return String.format(Locale.CHINESE, "+ %.2f", cost);
        else
            return String.format(Locale.CHINESE, "%.2f", cost);
    }

    /**
     * 返回小数点后两位的数值
     */
    public static String getCostPlusYuan(double cost) {
        return String.format(Locale.CHINESE, "+ %.2f元", cost);

    }

    /**
     * 返回小数点后两位的数值
     */
    public static String getCostMinusYuan(double cost) {
        return String.format(Locale.CHINESE, "- %.2f元", cost);
    }

    /**
     * 返回小数点后两位的数值
     */
    public static String getCost¥(double cost) {
        return String.format(Locale.CHINESE, "¥%.2f", cost);
    }

}