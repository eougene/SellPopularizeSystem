package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

/**
 * Created by hejin on 2017/4/10.
 */

public class NotificationFragment extends BaseFragmentView {

    //基本控件
    public static NotificationFragment notificationFragment;

    private TextView allCheck, deleteNotification, tvCancel, tvOrderSum, tvBriefSum, tvCompanySum, tvSystemSum;
    private RadioButton rbOrder, rbBrief, rbCompany, rbSystem;
    private RelativeLayout orderRelat, brifeRelat, companyRelat, systemRelat;
    private int type = 0, array = 0;
    private Message msg =new Message();;
    private Boolean isShow = false;
    private int cate_id = 4;
    private OrderNotificFragment notificFragment;
    private BriefNotificFragment notificFragment1;
    private CompanyNotificFragment notificFragment2;
    private SystemNotificFragment notificFragment3;
    private int fragmentID = 0;
    private int size=0x11;
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


                //删除成功
                case 4:
                    isShow = false;
                    tvCancel.setText(R.string.customdetaild_title);
                    allCheck.setVisibility(View.INVISIBLE);
                    deleteNotification.setVisibility(View.INVISIBLE);
                    type = 0;
                    break;
                //对应fragment里没有数据
                case ExtraName.NO_DATA:
                    size=0;
                    break;
                    //对应fragment里有数据
                case ExtraName.NORMAL_DATA:
                    size=1;
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
                    Log.e("tvCancel", "onClick: "+size );
                    if (size!=0) {
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
                    }
                    break;

                //删除
                case R.id.deleteNotification:
                    message = new Message();
                    message.what = 2;
                    message.arg2=0;
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
                case R.id.tvOrder:
                    fragmentID = 0;
                    cate_id = 4;
                    resetStatus();
                    initFragment1(cate_id);
                    if (notificFragment.size==0){
                        mhandler.sendEmptyMessage(ExtraName.NO_DATA);
                    }else {
                        mhandler.sendEmptyMessage(ExtraName.NORMAL_DATA);
                        notificFragment.getAdapter().setsShowI(isShow);
                        notificFragment.setType(0);
                        //setNotific(fragmentID, msg);
                    }
                    rbOrder.setChecked(true);
                    //viewOrderSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    rbBrief.setChecked(false);
                    //viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    rbCompany.setChecked(false);
                    //viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));


                    rbSystem.setChecked(false);
                    //viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));

                    break;

                //小组
                case R.id.tvBrief:
                    fragmentID = 1;
                    cate_id = 3;
                    resetStatus();
                    initFragment2(cate_id);
                    if (notificFragment1.size==0){
                        mhandler.sendEmptyMessage(ExtraName.NO_DATA);
                    }else {
                        mhandler.sendEmptyMessage(ExtraName.NORMAL_DATA);
                        notificFragment1.getAdapter().setsShowI(isShow);
                        notificFragment1.setType(0);
                        //setNotific(fragmentID, msg);
                    }
                    Log.e("tag", "onClick: "+ notificFragment1.size);
                    rbOrder.setChecked(false);
                    rbBrief.setChecked(true);
                    //viewBriefSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    rbCompany.setChecked(false);
                    //viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));

                    rbSystem.setChecked(false);
                    //viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));

                    break;
                //公司
                case R.id.tvCompany:
                    fragmentID = 2;
                    cate_id = 2;
                    resetStatus();
                    initFragment3(cate_id);
                    if (notificFragment2.size==0){
                        mhandler.sendEmptyMessage(ExtraName.NO_DATA);
                    }else {
                        mhandler.sendEmptyMessage(ExtraName.NORMAL_DATA);
                        notificFragment2.getAdapter().setsShowI(isShow);
                        notificFragment2.setType(0);
                        //setNotific(fragmentID, msg);
                    }
                    rbOrder.setChecked(false);
                    //viewOrderSum.setBackgroundColor(getResources().getColor(R.color.black));

                    rbBrief.setChecked(false);
                    //viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    rbCompany.setChecked(true);
                    //viewCompanySum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    rbSystem.setChecked(false);
                    //viewSystemSum.setBackgroundColor(getResources().getColor(R.color.black));

                    break;
                //系统
                case R.id.tvSystem:
                    fragmentID = 3;
                    cate_id = 1;
                    resetStatus();
                    initFragment4(cate_id);
                    if (notificFragment3.size==0){
                        mhandler.sendEmptyMessage(ExtraName.NO_DATA);
                    }else {
                        mhandler.sendEmptyMessage(ExtraName.NORMAL_DATA);
                        notificFragment3.getAdapter().setsShowI(isShow);
                        notificFragment3.setType(0);
                        //setNotific(fragmentID, msg);
                    }
                    rbOrder.setChecked(false);
                    //viewOrderSum.setBackgroundColor(getResources().getColor(R.color.black));

                    rbBrief.setChecked(false);
                    //viewBriefSum.setBackgroundColor(getResources().getColor(R.color.black));


                    rbCompany.setChecked(false);
                    //viewCompanySum.setBackgroundColor(getResources().getColor(R.color.black));

                    rbSystem.setChecked(true);
                    //viewSystemSum.setBackgroundColor(getResources().getColor(R.color.yellowish));

                    break;
            }


        }
    };

    private void resetStatus() {
        if (type==1){
            isShow = false;
            tvCancel.setText(R.string.customdetaild_title);
            allCheck.setVisibility(View.INVISIBLE);
            deleteNotification.setVisibility(View.INVISIBLE);
            type = 0;
            msg.what = 1;
            msg.obj = isShow;
            msg.arg1 = cate_id;
            msg.arg2 = type;
            array = 0;
        }
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        notificationFragment = this;
        setContentView(R.layout.activity_notification);
        int color = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(getActivity(), color, 20);
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
        rbOrder = getViewById(R.id.tvOrder);
        //小结
        rbBrief = getViewById(R.id.tvBrief);
        //公司
        rbCompany = getViewById(R.id.tvCompany);
        //系统
        rbSystem = getViewById(R.id.tvSystem);
        rbOrder.setOnClickListener(mOnClickListener);
        rbBrief.setOnClickListener(mOnClickListener);
        rbCompany.setOnClickListener(mOnClickListener);
        rbSystem.setOnClickListener(mOnClickListener);


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
