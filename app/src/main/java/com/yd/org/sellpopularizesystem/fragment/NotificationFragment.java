package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;

/**
 * Created by hejin on 2017/4/10.
 */

public class NotificationFragment extends BaseFragmentView {

    //基本控件
    public static NotificationFragment notificationFragment;

    private TextView allCheck, deleteNotification, tvCancel, tvOrderSum, tvBriefSum, tvCompanySum, tvSystemSum, tvOrder, tvBrief, tvCompany, tvSystem;
    private View viewOrderSum, viewBriefSum, viewCompanySum, viewSystemSum;
    private RelativeLayout orderRelat, brifeRelat, companyRelat, systemRelat;
    private int type = 0, array = 0;
    //
    private Boolean isShow = false;
    private int cate_id = 4;
    private OrderNotificFragment notificFragment;
    private BriefNotificFragment notificFragment1;
    private CompanyNotificFragment notificFragment2;
    private SystemNotificFragment notificFragment3;
    private int fragmentID = 0;


    /**
     * 接收消息,显示当前消息数量
     */
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                //订单消息数
                case 0:
                    tvOrderSum.setVisibility(View.VISIBLE);
                    tvOrderSum.setText((String) msg.obj);

                    break;
                //小组
                case 1:
                    tvBriefSum.setVisibility(View.VISIBLE);
                    tvBriefSum.setText((String) msg.obj);

                    break;

                //公司
                case 2:
                    tvCompanySum.setVisibility(View.VISIBLE);
                    tvCompanySum.setText((String) msg.obj);

                    break;
                //系统
                case 3:
                    tvSystemSum.setVisibility(View.VISIBLE);
                    tvSystemSum.setText((String) msg.obj);

                    break;


            }
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message message = null;
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
                        //标识符
                        type = 1;
                        //发送消息,通知
                        message = new Message();
                        message.what = 1;
                        message.obj = isShow;
                        message.arg1 = cate_id;
                        message.arg2 = type;
                        setNotific(fragmentID, message);


                    } else {
                        isShow = false;
                        tvCancel.setText(R.string.customdetaild_title);
                        allCheck.setVisibility(View.INVISIBLE);
                        deleteNotification.setVisibility(View.INVISIBLE);
                        type = 0;
                        message = new Message();
                        message.what = 1;
                        message.obj = isShow;
                        message.arg1 = cate_id;
                        message.arg2 = type;
                        setNotific(fragmentID, message);
                        array = 0;
                    }
                    break;

                //删除
                case R.id.deleteNotification:
                    message = new Message();
                    message.what = 2;
                    setNotific(fragmentID, message);
                    break;

                //全选
                case R.id.allCheck:
                    message = new Message();

                    //全选,全不选
                    if (array == 0) {
                        message.what = 0;
                        message.arg1 = array;
                        setNotific(fragmentID, message);
                        array = 1;

                    } else {
                        // 遍历list的长度，
                        message.what = 0;
                        message.arg1 = array;
                        setNotific(fragmentID, message);
                        array = 0;
                    }


                    break;

                //订单数
                case R.id.orderRelat:
                    fragmentID = 0;

                    tvOrder.setTextColor(getResources().getColor(R.color.yellowish));
                    viewOrderSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    tvBrief.setTextColor(getResources().getColor(R.color.black));
                    viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    tvCompany.setTextColor(getResources().getColor(R.color.black));
                    viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));


                    tvSystem.setTextColor(getResources().getColor(R.color.black));
                    viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));
                    cate_id = 4;

                    initFragment1(cate_id);

                    break;

                //小组
                case R.id.brifeRelat:
                    fragmentID = 1;
                    tvOrder.setTextColor(getResources().getColor(R.color.black));
                    viewOrderSum.setBackgroundColor(getResources().getColor(R.color.black));

                    tvBrief.setTextColor(getResources().getColor(R.color.yellowish));
                    viewBriefSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    tvCompany.setTextColor(getResources().getColor(R.color.black));
                    viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));

                    tvSystem.setTextColor(getResources().getColor(R.color.black));
                    viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));

                    cate_id = 3;
                    initFragment2(cate_id);

                    break;
                //公司
                case R.id.companyRelat:
                    fragmentID = 2;
                    tvOrder.setTextColor(getResources().getColor(R.color.black));
                    viewOrderSum.setBackgroundColor(getResources().getColor(R.color.black));

                    tvBrief.setTextColor(getResources().getColor(R.color.black));
                    viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    tvCompany.setTextColor(getResources().getColor(R.color.yellowish));
                    viewCompanySum.setBackgroundColor(getResources().getColor(R.color.yellowish));


                    tvSystem.setTextColor(getResources().getColor(R.color.black));
                    viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));

                    cate_id = 2;
                    initFragment3(cate_id);
                    break;
                //系统
                case R.id.systemRelat:
                    fragmentID = 3;
                    tvOrder.setTextColor(getResources().getColor(R.color.black));
                    viewOrderSum.setBackgroundColor(getResources().getColor(R.color.black));

                    tvBrief.setTextColor(getResources().getColor(R.color.black));
                    viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    tvCompany.setTextColor(getResources().getColor(R.color.black));
                    viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));


                    tvSystem.setTextColor(getResources().getColor(R.color.yellowish));
                    viewSystemSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    cate_id = 1;
                    initFragment4(cate_id);
                    break;
            }


        }
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        notificationFragment = this;
        setContentView(R.layout.activity_notification);
        initWedget();
        //第一次初始化首页默认显示第一个fragment
        initFragment1(cate_id);
    }

    //显示第一个fragment
    private void initFragment1(int cate_id) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        //初始化fragment并添加到事务中，如果为null就new一个
        if (notificFragment == null) {
            notificFragment = OrderNotificFragment.getInstnce(cate_id);
            transaction.add(R.id.main_frame_layout, notificFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(notificFragment);
        //提交事务
        transaction.commit();
    }

    //显示第二个fragment
    private void initFragment2(int cate_id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (notificFragment1 == null) {
            notificFragment1 = BriefNotificFragment.getInstnce(cate_id);
            transaction.add(R.id.main_frame_layout, notificFragment1);
        }
        hideFragment(transaction);
        transaction.show(notificFragment1);
        transaction.commit();
    }

    //显示第三个fragment
    private void initFragment3(int cate_id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (notificFragment2 == null) {
            notificFragment2 = CompanyNotificFragment.getInstnce(cate_id);
            transaction.add(R.id.main_frame_layout, notificFragment2);
        }
        hideFragment(transaction);
        transaction.show(notificFragment2);
        transaction.commit();
    }


    //显示第三个fragment
    private void initFragment4(int cate_id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (notificFragment3 == null) {
            notificFragment3 = SystemNotificFragment.getInstnce(cate_id);
            transaction.add(R.id.main_frame_layout, notificFragment3);
        }
        hideFragment(transaction);
        transaction.show(notificFragment3);
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (notificFragment != null) {
            transaction.hide(notificFragment);
        }
        if (notificFragment1 != null) {
            transaction.hide(notificFragment1);
        }
        if (notificFragment2 != null) {
            transaction.hide(notificFragment2);
        }
        if (notificFragment3 != null) {
            transaction.hide(notificFragment3);
        }
    }


    private void initWedget() {
        orderRelat = getViewById(R.id.orderRelat);
        orderRelat.setOnClickListener(mOnClickListener);

        brifeRelat = getViewById(R.id.brifeRelat);
        brifeRelat.setOnClickListener(mOnClickListener);

        companyRelat = getViewById(R.id.companyRelat);
        companyRelat.setOnClickListener(mOnClickListener);

        systemRelat = getViewById(R.id.systemRelat);
        systemRelat.setOnClickListener(mOnClickListener);


        //全选
        allCheck = getViewById(R.id.allCheck);
        allCheck.setOnClickListener(mOnClickListener);
        //删除
        deleteNotification = getViewById(R.id.deleteNotification);
        deleteNotification.setOnClickListener(mOnClickListener);
        //编辑
        tvCancel = getViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(mOnClickListener);

        //订单数据
        tvOrderSum = getViewById(R.id.tvOrderSum);
        //小结
        tvBriefSum = getViewById(R.id.tvBriefSum);
        //公司
        tvCompanySum = getViewById(R.id.tvCompanySum);
        //系统
        tvSystemSum = getViewById(R.id.tvSystemSum);


        //订单
        tvOrder = getViewById(R.id.tvOrder);
        //小结
        tvBrief = getViewById(R.id.tvBrief);
        //公司
        tvCompany = getViewById(R.id.tvCompany);
        //系统
        tvSystem = getViewById(R.id.tvSystem);


        viewOrderSum = getViewById(R.id.viewOrderSum);
        viewBriefSum = getViewById(R.id.viewBriefSum);
        viewCompanySum = getViewById(R.id.viewCompanySum);
        viewSystemSum = getViewById(R.id.viewSystemSum);


    }

    private void setNotific(int fnID, Message message) {
        if (fnID == 0) {
            OrderNotificFragment.notificFragment.mHandle.sendMessage(message);

        } else if (fnID == 1) {
            BriefNotificFragment.notificFragment.mHandle.sendMessage(message);

        } else if (fnID == 2) {
            CompanyNotificFragment.notificFragment.mHandle.sendMessage(message);

        } else if (fnID == 3) {
            SystemNotificFragment.notificFragment.mHandle.sendMessage(message);

        }
    }


    @Override
    protected void setListener() {


    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


}
