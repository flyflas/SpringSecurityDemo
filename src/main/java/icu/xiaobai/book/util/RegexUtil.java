package icu.xiaobai.book.util;

import java.util.regex.Pattern;

public class RegexUtil {
    /* 邮箱 */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /* 密码 */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";


    /**
     * 校验邮箱
     * @param email 用户邮箱
     * @return 校验通过返回true，否则返回false
     */
    public static boolean checkEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }


    /**
     * 校验密码
     * @param password 用户密码
     * @return 校验通过返回true，否则返回false
     */
    public static boolean checkPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }




}
