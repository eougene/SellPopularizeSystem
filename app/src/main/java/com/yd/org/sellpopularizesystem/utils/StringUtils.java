package com.yd.org.sellpopularizesystem.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hejin on 2017/6/1.
 */

public class StringUtils {
    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }
    //从字符串提取数字
    public static String getDigtalFromString(String string){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
       return m.replaceAll("").trim();
    }
    // 判断一个字符串是否含有数字
    public static boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches())

            flag = true;

        return flag;

    }

}
