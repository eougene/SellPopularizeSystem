package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.WheelOptions;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DialogOptionActivity extends AppCompatActivity {
    private EditText etReserTime, etRemindTime, etReserContent, etVistTitle, etVistContent;
    private TextView tvSubmit, tvVistTime;
    private int intType;
    private FrameLayout flOperate;
    private OptionsPickerView pvCustomTime;
    private WheelOptions wheelOptions;
    private CommonPopuWindow pvCustomTimePop;
    private List weeks = new ArrayList<String>();
    private List hours = new ArrayList<String>();
    private List minutes = new ArrayList<String>();
    private String[] reservDate;
    private long reserverTime = 0;
    private long reminderTime = 0;
    private String strFlag;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_oprate_popwin_view);
        flOperate = (FrameLayout) findViewById(R.id.flOprate);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);
        strFlag = getIntent().getExtras().getString("cora");
        if (strFlag.equals("reserver")) {
            setContentId(R.layout.reserver_operate_view);
            initReserverViews();
        } else if (strFlag.equals("visit")) {
            setContentId(R.layout.visit_operate_view);
            initVisitViews();
        }
        //初始化自定义选择器的数据
        initOptionData();
        //初始化自定义选择器
        initOptionPicker();
        setListener();
    }

    private void setContentId(int layoutId) {
        flOperate.addView(LayoutInflater.from(this).inflate(layoutId, null));
    }

    private void initVisitViews() {
        View view = LayoutInflater.from(this).inflate(R.layout.visit_operate_view, null);
        etVistTitle = (EditText) view.findViewById(R.id.etVistTitle);
        tvVistTime = (TextView) view.findViewById(R.id.tvVisitTime);
        Calendar tvCa=Calendar.getInstance();
        tvVistTime.setText(tvCa.get(Calendar.MONTH)+"/"+tvCa.get(Calendar.DAY_OF_MONTH)
                +getString(R.string.blank_space)+tvCa.get(Calendar.HOUR)+":"+tvCa.get(Calendar.MINUTE));
        etVistContent = (EditText) view.findViewById(R.id.etVistContent);
        tvSubmit = (TextView) view.findViewById(R.id.tvVisitSubmit);
    }

    private void initReserverViews() {
        View view = LayoutInflater.from(this).inflate(R.layout.reserver_operate_view, null);
        etReserTime = (EditText) view.findViewById(R.id.etReserTime);
        etReserTime.requestFocus();
        intType = etReserTime.getInputType();
        etRemindTime = (EditText) view.findViewById(R.id.etRemindTime);
        etReserContent = (EditText) view.findViewById(R.id.etRemarkContent);
        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
    }

    private void initOptionData() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMon = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int date = cal.get(Calendar.DATE);
        //int daysCountOfYear=cal.getActualMaximum(Calendar.YEAR);
        //cal.setTime(new Date());
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date_start = null;
        Date date_end = null;
        try {
            date_start = sdf.parse(mYear+"-01-01");
            date_end = sdf.parse((mYear+1)+"-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int daysCountOfYear=getGapDayCount(date_start,date_end);*/
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, mYear);
        for (int i = 1; i < 13; i++) {
            ca.set(Calendar.MONTH, i);
            int days = 0;
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
                days = 32;
            } else if (i == 2) {
                if (mYear % 4 != 0 && mYear % 400 != 0) {
                    days = 29;
                } else {
                    days = 30;
                }
            } else {
                days = 31;
            }
            for (int j = 1; j < days; j++) {
                if (i == mMon && j == mDay) {
                    weeks.add("今天");
                } else {
                    String dayOfWeek = getdayOfWeek(ca, i, j);
                    String str = i + "月" + j + "日" + "周" + dayOfWeek;
                    weeks.add(str);
                }

            }
        }
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format("%02d", i));
        }
    }

    private Calendar cal;

    private void initOptionPicker() {
        cal = Calendar.getInstance();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                setText(cal, options1, options2, options3);
            }
        }).setTitleText("请在指定时间完成").setTitleColor(R.color.black)
                .setCyclic(true, true, true)/*.setOutSideCancelable(false)*/
                .setSelectOptions(weeks.indexOf("今天"), hours.indexOf(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))), minutes.indexOf(String.format("%02d", cal.get(Calendar.MINUTE))))
                .isDialog(true);

        builder.setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvFinish = (TextView) v.findViewById(R.id.tv_finish);
                ImageView ivUp = (ImageView) v.findViewById(R.id.ivUp);
                ImageView ivDown = (ImageView) v.findViewById(R.id.ivDown);
                tvFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strFlag.equals("reserver")){
                            if (!TextUtils.isEmpty(etReserTime.getText()) && !TextUtils.isEmpty(etReserTime.getText())) {
                                Log.e("TAG", "onClick: " + "已经执行");
                                pvCustomTime.returnData();
                            } else {
                                pvCustomTime.returnData();
                                pvCustomTime.show();
                            }
                        }else {
                            pvCustomTime.returnData();
                        }

                    }
                });

                ivUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strFlag.equals("reserver")) {
                            if (etReserTime.isFocused()) {
                                etReserContent.requestFocus();
                            } else if (etRemindTime.isFocused()) {
                                etReserTime.requestFocus();
                            } else if (etReserContent.isFocused()) {
                                etRemindTime.requestFocus();
                            }
                        }
                    }
                });

                ivDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strFlag.equals("reserver")) {
                            if (etReserTime.isFocused()) {
                                etRemindTime.requestFocus();
                            } else if (etRemindTime.isFocused()) {
                                etReserContent.requestFocus();
                            } else if (etReserContent.isFocused()) {
                                etReserTime.requestFocus();
                            }
                        }
                    }
                });
            }
        });
        pvCustomTime = new OptionsPickerView(builder);
        View view = LayoutInflater.from(this).inflate(R.layout.pickerview_custom_time, null);
        LinearLayout llOptionPicker = (LinearLayout) view.findViewById(R.id.optionspicker);
        wheelOptions = new WheelOptions(llOptionPicker, false);
        pvCustomTime.setNPicker(weeks, hours, minutes);
        //让控件延时显示
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pvCustomTime.show();
            }
        }, 200);

    }

    private void setText(Calendar cal, int options1, int options2, int options3) {
        Calendar ca = Calendar.getInstance();
        String str1 = (String) weeks.get(options1);
        String str2 = (String) hours.get(options2);
        String str3 = (String) minutes.get(options3);
                /*Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(str);
                m.find();
                String newStr = m.group();*/
        String[] nums = new String[2];
        if (str1.equals("今天")) {
            nums[0] = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            nums[1] = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        } else {
            nums = str1.split("\\D+");
        }
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), 0);
        long currentTime = ca.getTimeInMillis();
        Log.e("tag", "currentTime: " + currentTime);
        if (strFlag.equals("reserver")) {
            if (etReserTime.isFocused()) {
                if (!TextUtils.isEmpty(etRemindTime.getText())) {
                    reminderTime = getTimeMillis(etRemindTime);
                }
                ca.set(ca.get(Calendar.YEAR), Integer.parseInt(nums[0]) - 1, Integer.parseInt(nums[1]), Integer.parseInt(str2), Integer.parseInt(str3), 0);
                Log.e("TAG", "setText: " + str1 + nums[0] + nums[1]);
                Log.e("TAG", "setText: " + ca.getTime());
                reserverTime = ca.getTimeInMillis();
                String str = sdf.format(ca.getTime());
                Log.e("tag", "reserverTime: " + reserverTime + "\n" + str);
                if (reserverTime < currentTime) {
                    ToasShow.showToastBottom(DialogOptionActivity.this, "预约时间必须大于当前时间");
                } else {
                    etReserTime.setText(String.format("%02d", Integer.parseInt(nums[0])) + "/"
                            + String.format("%02d", Integer.parseInt(nums[1])) + getString(R.string.blank_space) + str2 + ":" + str3);
                    etReserTime.setInputType(intType);
                    etReserTime.setSelection(etReserTime.getText().length());
                    etRemindTime.requestFocus();
                }

            } else if (etRemindTime.isFocused()) {
                ca.set(ca.get(Calendar.YEAR), Integer.parseInt(nums[0]) - 1, Integer.parseInt(nums[1]), Integer.parseInt(str2), Integer.parseInt(str3), 0);
                reminderTime = ca.getTimeInMillis();
                Log.e("TAG", "setText: " + str1 + nums[0] + nums[1]);
                Log.e("TAG", "setText: " + ca.getTime() + ca.getTimeInMillis());
                if (!TextUtils.isEmpty(etReserTime.getText())) {
                    reserverTime = getTimeMillis(etReserTime);
                    if (reminderTime > reserverTime) {
                        Log.e("tag", "currentTime: " + reminderTime);
                        Log.e("tag", "reminderTime: " + reserverTime);
                        ToasShow.showToastBottom(DialogOptionActivity.this, "提醒时间必须小于预约时间且大于当前时间");
                    } else if (reminderTime < currentTime) {
                        Log.e("tag", "currentTime: " + reminderTime);
                        Log.e("tag", "reminderTime: " + currentTime);
                        ToasShow.showToastBottom(DialogOptionActivity.this, "提醒时间必须小于预约时间且大于当前时间");
                    } else {
                        etRemindTime.setText(String.format("%02d", Integer.parseInt(nums[0])) + "/"
                                + String.format("%02d", Integer.parseInt(nums[1])) + getString(R.string.blank_space) + str2 + ":" + str3);
                        etRemindTime.setInputType(intType);
                        etRemindTime.setSelection(etRemindTime.getText().length());
                    }
                }
            }
        } else {
            tvVistTime.setText(String.format("%02d", Integer.parseInt(nums[0])) + "/"
                    + String.format("%02d", Integer.parseInt(nums[1])) + getString(R.string.blank_space) + str2 + ":" + str3);
        }
    }

    private long getTimeMillis(View view) {
        long millis = 0;
        String[] reservDate = new String[2];
        ;
        String[] reservTime = new String[2];
        ;
        if (view instanceof EditText) {
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String[] reserv = ((EditText) view).getText().toString().trim().split("\\s+");//根据03/14 08:29形式拆分
            Log.e("reserv", "getTimeMillis: " + reserv[1] + ((EditText) view).getText().toString().trim());
            reservDate = reserv[0].split("\\/");//得到03 14
            reservTime = reserv[1].split("\\:");//得到08 29
            Log.e("reservDate", "getTimeMillis: " + reservDate[0] + reservDate[1]);
        }
        Calendar dateCal = Calendar.getInstance();
        Log.e("tag", "TimeMillis: ");
        Log.e("time", dateCal.get(Calendar.YEAR) + "-" + reservDate[0] + "-" + reservDate[1] + getString(R.string.single_blank_space) + reservTime[0] + ":" + reservTime[1] + ":00");
        //Date date1 = sdf.parse(String.valueOf(dateCal.get(Calendar.YEAR)) + "-" + reservDate[0] + "-" + reservDate[1] +getString(R.string.single_blank_space)+ reserv[1]+":00");
        dateCal.set(dateCal.get(Calendar.YEAR), Integer.parseInt(reservDate[0]) - 1, Integer.parseInt(reservDate[1]), Integer.parseInt(reservTime[0]), Integer.parseInt(reservTime[1]), 00);
        // millis = date1.getTime();
        millis = dateCal.getTimeInMillis();
        Log.e("millis", "getTimeMillis: " + millis);
        return millis;
    }


    //根据日期判断星期几
    private String getdayOfWeek(Calendar ca, int i, int j) {
        ca.set(Calendar.MONTH, i - 1);
        ca.set(Calendar.DAY_OF_MONTH, j);
        int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
        String str = "null";
        switch (dayOfWeek) {
            case 1:
                str = "日";
                break;
            case 2:
                str = "一";
                break;
            case 3:
                str = "二";
                break;
            case 4:
                str = "三";
                break;
            case 5:
                str = "四";
                break;
            case 6:
                str = "五";
                break;
            case 7:
                str = "六";
                break;
        }
        return str;
    }

    View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.etReserTime:
                    etReserTime.requestFocus();
                    if (TextUtils.isEmpty(etReserTime.getText())) {
                        etReserTime.setInputType(InputType.TYPE_NULL);
                    }
                    pvCustomTime.setSelectOptions(weeks.indexOf("今天"), hours.indexOf(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))), minutes.indexOf(String.format("%02d", cal.get(Calendar.MINUTE))));
                    pvCustomTime.show();
                    break;
                case R.id.etRemindTime:
                    etRemindTime.requestFocus();
                    if (TextUtils.isEmpty(etRemindTime.getText())) {
                        etRemindTime.setInputType(InputType.TYPE_NULL);
                    }
                    pvCustomTime.setSelectOptions(weeks.indexOf("今天"), hours.indexOf(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))), minutes.indexOf(String.format("%02d", cal.get(Calendar.MINUTE))));
                    pvCustomTime.show();
                    break;
                case R.id.tvVisitTime:
                    Log.e("tvVisitTime", "onClick: ");
                    pvCustomTime.show();
                    break;
                case R.id.tvSubmit:
                    if (strFlag.equals("reserver")) {
                        submit();
                        ToasShow.showToastCenter(DialogOptionActivity.this, "提交成功");
                    }
                    finish();
                    break;
            }
        }
    };

    private void submit() {
        if (TextUtils.isEmpty(etReserTime.getText().toString())) {
            ToasShow.showToastBottom(DialogOptionActivity.this, "请设置预约时间");
        } else if (TextUtils.isEmpty(etRemindTime.getText().toString())) {
            ToasShow.showToastBottom(DialogOptionActivity.this, "请设置提醒时间,必须早于预约时间");
        } else {
            //获取日历账户id
            Log.e("TAG", "submit: " + reserverTime + "\n" + reminderTime);
            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(ExtraName.CALANDERURL), null, null, null, null);
            if (userCursor.getCount() > 0) {
                //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                userCursor.moveToLast();
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            } else {
                Toast.makeText(this, "没有账户，请先添加账户", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues event = new ContentValues();
            event.put("title", "预约申请");
            if (!TextUtils.isEmpty(etReserContent.getText())) {
                event.put("description", etReserContent.getText().toString());
            }
            event.put(CalendarContract.Events.CALENDAR_ID, calId);
            //添加事件时间
            Calendar mCalendar = Calendar.getInstance();
            /*String[] resevers = etReserTime.getText().toString().split("\\/");
            mCalendar.set(mCalendar.get(Calendar.YEAR), Integer.parseInt(resevers[0]), Integer.parseInt(resevers[1]));//设置开始时间*/
            long start = reserverTime;
            mCalendar.setTimeInMillis(start + 3600000);//设置终止时间
            long end = mCalendar.getTime().getTime();
            event.put(CalendarContract.Events.DTSTART, start);
            event.put(CalendarContract.Events.DTEND, end);
            event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");//这个是时区，必须有，
            //添加事件
            Uri newEvent = getContentResolver().insert(Uri.parse(ExtraName.CALANDEREVENTURL), event);
            if (newEvent == null) {
                // 添加日历事件失败直接返回
                return;
            }
            //事件提醒的设定
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
            long id = Long.parseLong(newEvent.getLastPathSegment());
            values.put(CalendarContract.Reminders.EVENT_ID, id);
            // 计算预约日期和提醒日期差值
            long mins = getGapTime("mins");
            Log.e("tag", "submit: " + mins);
            //设置提前多少分钟
            values.put(CalendarContract.Reminders.MINUTES, mins);
            //设置提醒方式
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            //向日历中插入提醒事件
            getContentResolver().insert(Uri.parse(ExtraName.CALANDERREMIDERURL), values);
        }
    }

    private long getGapTime(String str) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] reserv = etReserTime.getText().toString().split("\\ ");//根据03/14 08:29形式拆分
        String[] reservDate = reserv[0].split("\\/");//得到03 14
        String[] reminder = etRemindTime.getText().toString().split("\\ ");//根据03/14 08:25形式拆分
        String[] reminDate1 = reminder[0].split("\\/");//得到03 14
        Log.e("TAG", "submit: " + etRemindTime.getText().toString());
        Calendar dateCal = Calendar.getInstance();*/
        long days = 0;
        long hours = 0;
        long mins = 0;
        long diff = 0;
        long time = 0;
        //try {
            /*Date date1 = sdf.parse(dateCal.get(Calendar.YEAR) + "-" + reservDate[0] + "-" + reservDate[1] + getString(R.string.single_blank_space) + reserv[1]);
            Date date2 = sdf.parse(dateCal.get(Calendar.YEAR) + "-" + reminDate1[0] + "-" + reminDate1[1] + getString(R.string.single_blank_space) + reminder[1]);
            // 计算预约日期和提醒日期差值
            diff = date1.getTime() - date2.getTime();*/
        diff = reserverTime - reminderTime;
        days = diff / (1000 * 60 * 60 * 24);
        //hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        hours = diff / (1000 * 60 * 60);
        //mins = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        mins = diff / (1000 * 60);
        Log.e("diff", "getGapTime: " + diff);
        Log.e("diff", "getGapTime: " + days);
        Log.e("diff", "getGapTime: " + hours);
        /*} catch (ParseException e) {
            e.printStackTrace();
        }*/
        if (str.equals("days")) {
            time = days;
        } else if (str.equals("hours")) {
            time = hours;
        } else if (str.equals("mins")) {
            time = mins;
            Log.e("Time", "getGapTime: " + time);
        } else {
            time = diff;
        }
        Log.e("Time", "getGapTime: " + time);
        return time;
    }

    private void setListener() {
        if (strFlag.equals("reserver")){
            etReserTime.setOnClickListener(mOnclickListener);
            etRemindTime.setOnClickListener(mOnclickListener);
        }else {
            tvVistTime.setOnClickListener(mOnclickListener);
        }
        tvSubmit.setOnClickListener(mOnclickListener);
    }

}
