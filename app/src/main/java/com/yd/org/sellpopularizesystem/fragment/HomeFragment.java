package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.CustomeActivity;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.activity.LearningGardenActivity;
import com.yd.org.sellpopularizesystem.activity.OldProjectActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.HomeDataBean;
import com.yd.org.sellpopularizesystem.javaBean.MessageCountBean;
import com.yd.org.sellpopularizesystem.myView.Gradient;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BadgeUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/4/10.
 */

public class HomeFragment extends BaseFragmentView {
    public static HomeFragment homeFragment;
    private RelativeLayout rlBefore;
    private LinearLayout saleLinearLayyout, customLinerLayout, studyLinearLayout;
    private TextView tvScaleSource, tvNewAddSource, tvCustomNumber, tvNewCustomNumber,
            tvStydyDatumCount, tvNotCompleteCount, tvNoNewsCount, tvMessage;
    private Gradient homeGradient;
    private List<ImageView> imageViews = new ArrayList<>();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //销售推广
                case R.id.saleLinearLayyout:
                    Bundle bundle = new Bundle();
                    bundle.putString(ExtraName.SCALETOCUSTOME, ExtraName.SCALETOCUSTOME);
                    ActivitySkip.forward(getActivity(), CustomeActivity.class, bundle);
                    getActivity().overridePendingTransition(R.anim.downtoup_in_anim, 0);
                    break;
                //客户管理
                case R.id.customLinerLayout:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(ExtraName.SCALETOCUSTOME, ExtraName.TOCUSTOME);
                    ActivitySkip.forward(getActivity(), CustomeActivity.class, bundle2);
                    break;
                //学习园地
                case R.id.studyLinearLayout:
                    Bundle bundle3 = new Bundle();
                    if (homeDataBean!=null){
                        bundle3.putString("studynum",homeDataBean.getResult().getTotal_study()+"");
                    }else {
                        bundle3.putString("studynum",0+"");
                    }
                    ActivitySkip.forward(getActivity(), LearningGardenActivity.class,bundle3);
                    break;
                //往期项目
                case R.id.rlBefore:
                    ActivitySkip.forward(getActivity(), OldProjectActivity.class);
                    break;
            }
        }
    };

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    getHomeData();
                    break;

            }
        }
    };
    private HomeDataBean homeDataBean;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.home_fragment);
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        homeFragment = this;
        initWidget();
        getHomeData();
    }


    @Override
    public void onResume() {
        super.onResume();
        //唤醒界面的时候加载数据,用于准确的更新消息
        getHomeData();
    }


    private void initWidget() {
        //消息管理
        rlBefore = getViewById(R.id.rlBefore);
        //资料
        saleLinearLayyout = getViewById(R.id.saleLinearLayyout);
        //客户管理
        customLinerLayout = getViewById(R.id.customLinerLayout);
        //学习园地
        studyLinearLayout = getViewById(R.id.studyLinearLayout);

        tvScaleSource = getViewById(R.id.tvScaleSource);
        tvNewAddSource = getViewById(R.id.tvNewAddSource);
        tvCustomNumber = getViewById(R.id.tvCustomNumber);
        tvNewCustomNumber = getViewById(R.id.tvNewCustomNumber);
        tvStydyDatumCount = getViewById(R.id.tvStydyDatumCount);
        tvNotCompleteCount = getViewById(R.id.tvNotCompleteCount);
        //tvNoNewsCount = getViewById(R.id.tvNoNewsCount);

        //渐变动画
        homeGradient = getViewById(R.id.homeGradient);

        //初始化imageview
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.mipmap.homebg);

        ImageView imageViewOne = new ImageView(getActivity());
        imageViewOne.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewOne.setImageResource(R.mipmap.home);

        //添加要渐变的ImageView
        imageViews.add(imageView);
        imageViews.add(imageViewOne);
        //设置ImageView集合
        homeGradient.setImageViews(imageViews);


    }

    @Override
    protected void setListener() {
        rlBefore.setOnClickListener(mOnClickListener);
        saleLinearLayyout.setOnClickListener(mOnClickListener);
        customLinerLayout.setOnClickListener(mOnClickListener);
        studyLinearLayout.setOnClickListener(mOnClickListener);
    }


    private void getHomeData() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        final FinalHttp fh = new FinalHttp();
        fh.get(Contants.HOME_DAA, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String json) {
                if (!TextUtils.isEmpty(json)) {

                    Gson gson = new Gson();
                    homeDataBean = gson.fromJson(json, HomeDataBean.class);
                    if (homeDataBean.getCode() == 1) {

                        tvScaleSource.setText(getString(R.string.owned_by_all) + " " + homeDataBean.getResult().getTotal_product() + " " + getString(R.string.home_01));
                        tvNewAddSource.setText(getString(R.string.newly_increased) + " " + homeDataBean.getResult().getNew_product() + " " + getString(R.string.home_02));
                        tvCustomNumber.setText(getString(R.string.your_there) + " " + homeDataBean.getResult().getTotal_customer() + " " + getString(R.string.home_03));
                        tvNewCustomNumber.setText(getString(R.string.this_month_newly) + " " + homeDataBean.getResult().getNew_customer() + " " + getString(R.string.home_04));
                        //tvStydyDatumCount.setText(getString(R.string.owned_by_all) + " " + homeDataBean.getResult().getTotal_study() + " " + getString(R.string.home_05));
                        tvNotCompleteCount.setVisibility(View.INVISIBLE);
                        //如果首次进去有消息条数.则通知显示
                        if (homeDataBean.getResult().getUnread() > 0) {

                            MessageCountBean messageCount = new MessageCountBean();
                            messageCount.state = "1";
                            messageCount.count = homeDataBean.getResult().getUnread() + "";

                            Message message = new Message();
                            message.what = 1;
                            message.obj = messageCount;


                            //通知主页面显示消息条目
                            HomeActiviyt.homeActiviyt.handler.sendMessage(message);
                            //通知App icon显示未读消息
                            BadgeUtil.setBadgeCount(getActivity(), Integer.parseInt(messageCount.count));

                        } else {
                            //清楚消息,
                            BadgeUtil.resetBadgeCount(getActivity());
                        }

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
            }
        });

    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
