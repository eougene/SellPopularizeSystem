package com.yd.org.sellpopularizesystem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by bai on 2017/1/10.
 */

public class MyUtils {

    public static MyUtils utils;

    private MyUtils() {

    }

    public static MyUtils getInstance() {
        if (utils == null)
            utils = new MyUtils();
        return utils;
    }

    /**
     * 判断网络是否连接，返回false为没有任何连接
     *
     * @param context 上下文
     * @return boolean
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否WI-FI连接状态
     *
     * @param mContext
     * @return 0:无网络;1:Wi-Fi连接;2:移动网络连接
     */
    public int isWifiNetwork(Context mContext) {
        if (!isNetworkConnected(mContext)) {
            return 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return 1;
        }
        return 2;
    }


    /**
     * MD5加密，32位
     *
     * @param url 需加密的字符串
     * @return 加密后的字符串
     */
    public String MD5(String url) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // e.printStackTrace();
            return getFile(url);
        }
        char[] charArray = url.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 可逆的加密算法
     */
    public String encryptmd5(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'l');
        }

        return new String(a);
    }

    /**
     * 将URL转成能够识别的目录
     */
    public String getFile(String url) {
        String path = url;
        if (path.contains("?")) {
            path = path.replace("?", "_");
        }
        if (path.contains("/")) {
            path = path.replace("/", "_");
        }
        if (path.contains(".")) {
            path = path.replace(".", "_");
        }
        return path;
    }

    /**
     * 获取缓存目录中的自定义文件路径
     *
     * @param context  上下文
     * @param path     二级目录，三级目录中间用"/"分隔，前后不需要加"/"
     * @param fileName 文件名，自动转成md5的名字
     * @param isMD5    文件名是否加密
     * @return File
     */
    public File getCache(Context context, String path, String fileName,
                         boolean isMD5) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(context.getExternalCacheDir().getPath() + "/"
                    + path);
        } else {
            file = new File(context.getCacheDir() + "/" + path);
        }
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        if (isMD5)
            file = new File(file.getPath() + "/" + MD5(fileName));
        else
            file = new File(file.getPath() + "/" + fileName);
        return file;
    }

    /**
     * 获取缓存目录中的自定义目录
     *
     * @param context 上下文
     * @param path    自定义目录
     * @return File
     */
    public File getCache(Context context, String path) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(context.getExternalCacheDir().getPath() + "/"
                    + path);
        } else {
            file = new File(context.getCacheDir() + "/" + path);
        }
        if (file == null)
            return null;
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取内部版本号
     *
     * @param context 上下文
     * @return String
     */
    public String getVersionCode(Context context) {
        String versionCode = null;
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionCode = String.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取外部版本名称
     *
     * @param context 上下文
     * @return String 版本名称
     */
    public String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取设备号（IMEI）
     *
     * @param context 上下文
     * @return String 设备号
     */
    public String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String dwc = null;
        try {
            dwc = tm.getDeviceId();
        } catch (Exception e) {
            // TODO: handle exception
            dwc = getSN(context);
        }
        return dwc;
    }

    /**
     * 获取设备序列号
     *
     * @param context 上下文
     * @return String
     */
    public String getSN(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public String getSystemVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取GUID
     *
     * @return String
     */
    public String GetGUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 转换时间格式
     *
     * @param format 传入的time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param time   时间
     * @param isTime true为返回时:分:秒，false为返回年-月-日
     * @return String
     */
    @SuppressLint("DefaultLocale")
    public String parseTime(String format, String time, boolean isTime) {
        if (time != null && !"".equals(time)) {
            Date date = stringToDate(format, time);
            if (date == null)
                return time;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int years = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);

            if (isTime) {
                return String.format("%02d:%02d:%02d", hour, min, ss);
            }
            return String.format("%d-%02d-%02d", years, month, day);
        }
        return time;
    }

    /**
     * 转换时间格式
     *
     * @param format 传入的time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param time   时间
     * @return String
     * 返回年-月-日 时:分
     */
    @SuppressLint("DefaultLocale")
    public String parseTime(String format, String time) {
        if (!"".equals(time)) {
            Date date = stringToDate(format, time);
            if (date == null)
                return time;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int years = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);

            return String.format("%d-%02d-%02d %02d:%02d", years, month, day, hour, min);
        }
        return time;
    }

    /**
     * 根据2个日期求出之间的天数
     * @param start_date
     * @param end_date
     * @return
     */
    public int getGapDayCount(Date start_date, Date end_date) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(start_date);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(end_date);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 24小时制转12小时制
     *
     * @param time   24小时制的时间
     * @param format 时间时与分的间隔符号
     * @return 返回hh:mm Am/Pm.没有秒
     */
    public String parseTime24To12(String time, String format) {
        if (time == null || time.length() < 5
                || format == null || format.length() == 0 ||
                (!time.contains(format))) {
            return time;
        }
        time = time.substring(0, 5);
        String time12 = null;
        String[] times = time.split(format);
        if (times == null || times.length < 2) {
            return time;
        }
        int h = Parse.getInstance().parseInt(times[0]);
        int m = Parse.getInstance().parseInt(times[1]);
        String p = "Am";
        if (h == 0 || h == 24) {
            h = 12;
        } else if (h >= 12) {
            p = "Pm";
            if (h > 12)
                h -= 12;
        }
        time12 = String.format("%02d:%02d ", h, m) + p;
        return time12;
    }

    /**
     * String类型的时间转Date
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public Date stringToDate(String format, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }


    /**
     * 时间戳转换成时间字符窜
     *
     * @param format 时间格式
     * @param time   时间戳
     * @return
     */
    public String date2String(String format, long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);


    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param format 时间格式
     * @param time   时间
     * @return
     */
    public long string2Date(String format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            Log.e("error", "将字符串转换为时间戳异常");
            return 0;
        }
    }


    /**
     * 设置颜色并四舍五入
     *
     * @param context  上下文
     * @param text     需要比对的数值
     * @param dou      进行比对的数
     * @param num      小数保留位数
     * @param isJiaHao 正数前是否加＋号
     * @return
     */
    public SpannableString formatText(Context context, double text, double dou,
                                      int num, boolean isJiaHao) {
        String str = String.format("%." + num + "f", text);
        int color;
        String n = "#.";
        for (int i = 0; i < num; i++) {
            n += "#";
        }
        double d = Parse.getInstance().parseDouble(str, n);

        if (d == 0) {
            str = "--";
            color = Color.GRAY;
        } else {
            dou = Parse.getInstance().parseDouble(dou, n);
            if (d > dou) {
                if (isJiaHao)
                    str = "+" + str;
                color = Color.RED;
            } else if (d < dou)
                color = Color.GREEN;
            else
                color = Color.GRAY;
        }

        SpannableString spa = new SpannableString(str);
        spa.setSpan(new ForegroundColorSpan(color), 0, str.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spa;
    }

    /**
     * 设置颜色并四舍五入 针对白银，无小数点
     *
     * @param context  上下文
     * @param text     需要比对的数值
     * @param dou      进行比对的数
     * @param isJiaHao 正数前是否加＋号
     * @return
     */
    public SpannableString formatText1(Context context, double text,
                                       double dou, boolean isJiaHao) {
        String str = String.format("%.2f", text);
        int color;
        double d = Parse.getInstance().parseDouble(str, "#.##");
        if (d == 0) {
            str = "--";
            color = Color.GRAY;
        } else {
            dou = Parse.getInstance().parseDouble(dou, "#.##");
            if (d > dou) {
                if (isJiaHao)
                    str = "+" + str;
                color = Color.RED;
            } else if (d < dou)
                color = Color.GREEN;
            else
                color = Color.GRAY;
        }

        str = str.substring(0, str.length() - 3);
        SpannableString spa = new SpannableString(str);
        spa.setSpan(new ForegroundColorSpan(color), 0, str.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spa;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    /**
     * 获取24小时格式的当前系统时间
     *
     * @param format time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param locale 时区，例：Locale.CHINA（代表中国时区）
     */
    public String getTime24(String format, Locale locale) {
        if (format.contains("h")) {
            format = format.replace("h", "H");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(new Date());
    }

    /**
     * 获取12小时格式的当前系统时间
     *
     * @param format time的格式，例："yyyy/MM/dd hh:mm:ss"
     * @param locale 时区，例：Locale.CHINA（代表中国时区）
     */
    public String getTime12(String format, Locale locale) {
        if (format.contains("H")) {
            format = format.replace("H", "h");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(new Date());
    }


    /**
     * 键盘隐藏并把焦点置为false
     *
     * @param context   上下文
     * @param editTexts EditText数组
     */
    public void setKeyBoardFocusable(Context context, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(false);
            editTexts[i].setFocusableInTouchMode(false);
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTexts[i].getWindowToken(), 0);
        }
    }

    /**
     * 键盘隐藏
     *
     * @param context 上下文
     * @param views   EditText数组
     */
    public void setKeyBoardGone(Context context, View... views) {
        for (int i = 0; i < views.length; i++) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(views[i].getWindowToken(), 0);
        }
    }

    /**
     * 输入法在窗口上已经显示，则隐藏，反之则显示
     *
     * @param context
     */
    public void setKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     *
     * @param context
     */
    public void setKeyBoardVisibility(Context context, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTexts[i], InputMethodManager.SHOW_FORCED);
        }
    }


    /**
     * 查看密码
     *
     * @param mEditText
     * @param isView    是否查看,true查看,false隐藏
     */
    public void viewPassword(EditText mEditText, boolean isView) {
        if (mEditText == null) {
            throw new NullPointerException();
        } else {
            int cursorPosition = mEditText.getSelectionStart();
            if (isView) {
                mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mEditText.setSelection(cursorPosition);
            } else {
                mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mEditText.setSelection(cursorPosition);
            }
        }
    }

    /**
     * 根据传进来的日期时间计算年龄
     *
     * @param format
     * @param date
     * @return 年龄
     */
    public int getAge(String format, String date) {
        date = parseTime(format, date, false);
        if (date == null || date.length() < 10
                || !date.contains("-")) {
            return 0;
        }
        date = date.substring(0, 10);
        String[] dates = date.split("-");
        if (dates == null || dates.length < 3) {
            return 0;
        }
        String today = utils.date2String("yyyy-MM-dd", System.currentTimeMillis());
        int year = Parse.getInstance().parseInt(today.substring(0, 4));
        int month = Parse.getInstance().parseInt(today.substring(5, 7));
        int day = Parse.getInstance().parseInt(today.substring(8, 10));

        int yearOld = Parse.getInstance().parseInt(dates[0]);
        int monthOld = Parse.getInstance().parseInt(dates[1]);
        int dayOld = Parse.getInstance().parseInt(dates[2]);

        int age = year - yearOld;
        if (month - monthOld < 0) {
            age -= 1;
        } else if (month - monthOld == 0) {
            if (day - dayOld < 0) {
                age -= 1;
            }
        }
        if (age < 0)
            age = 0;
        return age;
    }


    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getScreenSize(Activity context) {
        WindowManager wm = context.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
    public static int getStatusHeight(Activity context){
        Rect rectangle= new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        //高度为rectangle.top-0仍为rectangle.top
       // Log.e("WangJ", "状态栏-方法3:" + rectangle.top);
        return rectangle.top;
    }
}