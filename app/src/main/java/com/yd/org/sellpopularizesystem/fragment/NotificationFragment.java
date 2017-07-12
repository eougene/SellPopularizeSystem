package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.FragAdapter;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/4/10.
 */

public class NotificationFragment extends BaseFragmentView {


    private ViewPager studyViewPager;
    private FragAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    //基本控件
    public static NotificationFragment notificationFragment;

    private TextView allCheck, deleteNotification, tvCancel, tvOrderSum, tvBriefSum, tvCompanySum, tvSystemSum;
    private RadioButton rbOrder, rbBrief, rbCompany, rbSystem;
    private int type = 0, array = 0;
    private Boolean isShow = false;
    //对应数据的Id,4表示订单消息
    private int cate_id = 4;
    private OrderNotificFragment notificFragment, notificFragment1, notificFragment2, notificFragment3;
    /**
     * 接收消息,显示当前消息数量
     */
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                //订单消息数
                case 0:
                    if (msg.arg1 > 0) {
                        tvOrderSum.setVisibility(View.VISIBLE);
                    } else {
                        tvOrderSum.setVisibility(View.GONE);
                    }

                    tvOrderSum.setText(String.valueOf(msg.arg1));

                    break;
                //小组
                case 1:
                    if (msg.arg1 > 0) {
                        tvBriefSum.setVisibility(View.VISIBLE);
                    } else {
                        tvBriefSum.setVisibility(View.GONE);
                    }
                    tvBriefSum.setText(String.valueOf(msg.arg1));

                    break;

                //公司
                case 2:

                    if (msg.arg1 > 0) {
                        tvCompanySum.setVisibility(View.VISIBLE);
                    } else {
                        tvCompanySum.setVisibility(View.GONE);
                    }
                    tvCompanySum.setText(String.valueOf(msg.arg1));

                    break;
                //系统
                case 3:
                    if (msg.arg1 > 0) {
                        tvSystemSum.setVisibility(View.VISIBLE);
                    } else {
                        tvSystemSum.setVisibility(View.GONE);
                    }
                    tvSystemSum.setText(String.valueOf(msg.arg1));

                    break;


                //删除成功,回复编辑状态
                case 4:
                    isShow = false;
                    tvCancel.setText(R.string.customdetaild_title);
                    allCheck.setVisibility(View.INVISIBLE);
                    deleteNotification.setVisibility(View.INVISIBLE);
                    type = 0;

                    Message message = new Message();
                    message.arg2 = type;
                    message.obj = isShow;
                    message.what = 1;

                    notificFragment.mHandle.sendMessage(message);


                    Message message1 = new Message();
                    message1.arg2 = type;
                    message1.obj = isShow;
                    message1.what = 1;

                    notificFragment1.mHandle.sendMessage(message1);


                    Message message2 = new Message();
                    message2.arg2 = type;
                    message2.obj = isShow;
                    message2.what = 1;

                    notificFragment2.mHandle.sendMessage(message2);


                    Message message3 = new Message();
                    message3.arg2 = type;
                    message3.obj = isShow;
                    message3.what = 1;

                    notificFragment3.mHandle.sendMessage(message3);


                    break;

            }
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //编辑
                case R.id.tvCancel:

                    //是否显示全选,删除等
                    if (type == 0) {
                        //是否全选
                        isShow = true;
                        tvCancel.setText(R.string.cancel);
                        allCheck.setVisibility(View.VISIBLE);
                        deleteNotification.setVisibility(View.VISIBLE);
                        //标识符,用于判断是否选中或直接进入详情页
                        type = 1;


                        Message message = new Message();
                        message.arg2 = type;
                        message.obj = isShow;
                        message.what = 1;

                        notificFragment.mHandle.sendMessage(message);


                        Message message1 = new Message();
                        message1.arg2 = type;
                        message1.obj = isShow;
                        message1.what = 1;

                        notificFragment1.mHandle.sendMessage(message1);


                        Message message2 = new Message();
                        message2.arg2 = type;
                        message2.obj = isShow;
                        message2.what = 1;

                        notificFragment2.mHandle.sendMessage(message2);


                        Message message3 = new Message();
                        message3.arg2 = type;
                        message3.obj = isShow;
                        message3.what = 1;

                        notificFragment3.mHandle.sendMessage(message3);


                    } else {
                        isShow = false;
                        tvCancel.setText(R.string.customdetaild_title);
                        allCheck.setVisibility(View.INVISIBLE);
                        deleteNotification.setVisibility(View.INVISIBLE);
                        type = 0;

                        Message message = new Message();
                        message.arg2 = type;
                        message.obj = isShow;
                        message.what = 1;

                        notificFragment.mHandle.sendMessage(message);


                        Message message1 = new Message();
                        message1.arg2 = type;
                        message1.obj = isShow;
                        message1.what = 1;

                        notificFragment1.mHandle.sendMessage(message1);


                        Message message2 = new Message();
                        message2.arg2 = type;
                        message2.obj = isShow;
                        message2.what = 1;

                        notificFragment2.mHandle.sendMessage(message2);


                        Message message3 = new Message();
                        message3.arg2 = type;
                        message3.obj = isShow;
                        message3.what = 1;

                        notificFragment3.mHandle.sendMessage(message3);


                    }

                    break;

                //删除
                case R.id.deleteNotification:


                    if (cate_id == 4) {
                        Message messages = new Message();
                        messages.what = 2;
                        messages.arg2 = type;
                        notificFragment.mHandle.sendMessage(messages);

                    } else if (cate_id == 3) {
                        Message messages2 = new Message();
                        messages2.what = 2;
                        messages2.arg2 = type;
                        notificFragment1.mHandle.sendMessage(messages2);


                    } else if (cate_id == 2) {
                        Message messages3 = new Message();
                        messages3.what = 2;
                        messages3.arg2 = type;
                        notificFragment2.mHandle.sendMessage(messages3);

                    } else if (cate_id == 1) {
                        Message messages4 = new Message();
                        messages4.what = 2;
                        messages4.arg2 = type;
                        notificFragment3.mHandle.sendMessage(messages4);

                    }


                    break;

                //全选
                case R.id.allCheck:

                    //全选,全不选
                    if (array == 0) {


                        if (cate_id == 4) {

                            Message message = new Message();
                            message.what = 0;
                            message.arg1 = array;
                            notificFragment.mHandle.sendMessage(message);

                        } else if (cate_id == 3) {
                            Message message1 = new Message();
                            message1.what = 0;
                            message1.arg1 = array;
                            notificFragment1.mHandle.sendMessage(message1);

                        } else if (cate_id == 2) {
                            //
                            Message message2 = new Message();
                            message2.what = 0;
                            message2.arg1 = array;
                            notificFragment2.mHandle.sendMessage(message2);

                        } else if (cate_id == 1) {

                            //
                            Message message3 = new Message();
                            message3.what = 0;
                            message3.arg1 = array;
                            notificFragment3.mHandle.sendMessage(message3);


                        }

                        array = 1;


                    } else {
                        // 遍历list的长度，

                        if (cate_id == 4) {

                            Message message = new Message();
                            message.what = 0;
                            message.arg1 = array;
                            notificFragment.mHandle.sendMessage(message);
                        } else if (cate_id == 3) {
                            //
                            Message message1 = new Message();
                            message1.what = 0;
                            message1.arg1 = array;
                            notificFragment1.mHandle.sendMessage(message1);
                        } else if (cate_id == 2) {
                            //
                            Message message2 = new Message();
                            message2.what = 0;
                            message2.arg1 = array;
                            notificFragment2.mHandle.sendMessage(message2);
                        } else if (cate_id == 1) {
                            //
                            Message message3 = new Message();
                            message3.what = 0;
                            message3.arg1 = array;
                            notificFragment3.mHandle.sendMessage(message3);
                        }


                        array = 0;

                    }


                    break;

                //订单数
                case R.id.tvOrder:
                    cate_id = 4;
                    rbOrder.setChecked(true);
                    rbBrief.setChecked(false);
                    rbCompany.setChecked(false);
                    rbSystem.setChecked(false);
                    studyViewPager.setCurrentItem(0);


                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = 1;
                    notificFragment.mHandle.sendMessage(message);
                    array = 0;


                    break;

                //小组
                case R.id.tvBrief:
                    cate_id = 3;
                    rbOrder.setChecked(false);
                    rbBrief.setChecked(true);
                    rbCompany.setChecked(false);
                    rbSystem.setChecked(false);
                    studyViewPager.setCurrentItem(1);

                    //
                    Message message1 = new Message();
                    message1.what = 0;
                    message1.arg1 = 1;
                    notificFragment1.mHandle.sendMessage(message1);
                    array = 0;

                    break;
                //公司
                case R.id.tvCompany:
                    cate_id = 2;
                    rbOrder.setChecked(false);
                    rbBrief.setChecked(false);
                    rbCompany.setChecked(true);
                    rbSystem.setChecked(false);
                    studyViewPager.setCurrentItem(2);

                    Message message2 = new Message();
                    message2.what = 0;
                    message2.arg1 = 1;
                    notificFragment2.mHandle.sendMessage(message2);
                    array = 0;

                    break;
                //系统
                case R.id.tvSystem:
                    cate_id = 1;
                    rbOrder.setChecked(false);
                    rbBrief.setChecked(false);
                    rbCompany.setChecked(false);
                    rbSystem.setChecked(true);
                    studyViewPager.setCurrentItem(3);


                    Message message3 = new Message();
                    message3.what = 0;
                    message3.arg1 = 1;
                    notificFragment3.mHandle.sendMessage(message3);
                    array = 0;
                    break;
            }


        }
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        notificationFragment = this;
        setContentView(R.layout.activity_notification);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.white), 20);
        initWedget();
        initViewPage();
    }



    private void initWedget() {

        //全选
        allCheck = getViewById(R.id.allCheck);

        //删除
        deleteNotification = getViewById(R.id.deleteNotification);

        //编辑
        tvCancel = getViewById(R.id.tvCancel);


        //订单数据
        tvOrderSum = getViewById(R.id.tvOrderSum);
        //小结
        tvBriefSum = getViewById(R.id.tvBriefSum);
        //公司
        tvCompanySum = getViewById(R.id.tvCompanySum);
        //系统
        tvSystemSum = getViewById(R.id.tvSystemSum);

        //订单
        rbOrder = getViewById(R.id.tvOrder);
        //小结
        rbBrief = getViewById(R.id.tvBrief);
        //公司
        rbCompany = getViewById(R.id.tvCompany);
        //系统
        rbSystem = getViewById(R.id.tvSystem);
        //viewpager
        studyViewPager = getViewById(R.id.studyViewPager);


    }

    private void initViewPage() {
        notificFragment = OrderNotificFragment.getInstnce(4);
        notificFragment1 = OrderNotificFragment.getInstnce(3);
        notificFragment2 = OrderNotificFragment.getInstnce(2);
        notificFragment3 = OrderNotificFragment.getInstnce(1);

        fragments.add(notificFragment);
        fragments.add(notificFragment1);
        fragments.add(notificFragment2);
        fragments.add(notificFragment3);

        adapter = new FragAdapter(getActivity().getSupportFragmentManager(), fragments);
        studyViewPager.setAdapter(adapter);
        studyViewPager.setCurrentItem(0);
        studyViewPager.setOffscreenPageLimit(4);
        studyViewPager.setOnPageChangeListener(new MyVPageChangeListener());

    }

    private class MyVPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int location) {
            changeTextColor(location);
        }

    }

    private void changeTextColor(int location) {
        switch (location) {
            case 0:
                cate_id = 4;
                rbOrder.setChecked(true);
                rbBrief.setChecked(false);
                rbCompany.setChecked(false);
                rbSystem.setChecked(false);

                Message message = new Message();
                message.what = 0;
                message.arg1 = 1;
                notificFragment.mHandle.sendMessage(message);
                array = 0;
                break;
            case 1:
                cate_id = 3;
                rbOrder.setChecked(false);
                rbBrief.setChecked(true);
                rbCompany.setChecked(false);
                rbSystem.setChecked(false);

                //
                Message message1 = new Message();
                message1.what = 0;
                message1.arg1 = 1;
                notificFragment1.mHandle.sendMessage(message1);
                array = 0;
                break;
            case 2:
                cate_id = 2;
                rbOrder.setChecked(false);
                rbBrief.setChecked(false);
                rbCompany.setChecked(true);
                rbSystem.setChecked(false);

                Message message2 = new Message();
                message2.what = 0;
                message2.arg1 = 1;
                notificFragment2.mHandle.sendMessage(message2);
                array = 0;
                break;
            case 3:
                cate_id = 1;
                rbOrder.setChecked(false);
                rbBrief.setChecked(false);
                rbCompany.setChecked(false);
                rbSystem.setChecked(true);


                Message message3 = new Message();
                message3.what = 0;
                message3.arg1 = 1;
                notificFragment3.mHandle.sendMessage(message3);
                array = 0;
                break;

        }
    }


    @Override
    protected void setListener() {
        rbOrder.setOnClickListener(mOnClickListener);
        rbBrief.setOnClickListener(mOnClickListener);
        rbCompany.setOnClickListener(mOnClickListener);
        rbSystem.setOnClickListener(mOnClickListener);
        tvCancel.setOnClickListener(mOnClickListener);
        deleteNotification.setOnClickListener(mOnClickListener);
        allCheck.setOnClickListener(mOnClickListener);
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


}
