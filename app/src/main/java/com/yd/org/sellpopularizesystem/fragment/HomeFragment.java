package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.CustomeActivity;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.activity.LearningGardenActivity;
import com.yd.org.sellpopularizesystem.activity.NotificationActivity;
import com.yd.org.sellpopularizesystem.activity.SettingActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.HomeDataBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * Created by hejin on 2017/4/10.
 */

public class HomeFragment extends BaseFragmentView {

    private RelativeLayout rlBefore;
    private LinearLayout saleLinearLayyout, customLinerLayout, studyLinearLayout;
    private TextView tvScaleSource, tvNewAddSource, tvCustomNumber, tvNewCustomNumber,
            tvStydyDatumCount, tvNotCompleteCount, tvNoNewsCount, tvMessage;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //销售推广
                case R.id.saleLinearLayyout:
                    Bundle bundle = new Bundle();
                    bundle.putString(ExtraName.SCALETOCUSTOME, ExtraName.SCALETOCUSTOME);
                    ActivitySkip.forward(getActivity(), CustomeActivity.class, bundle);
                    break;
                //客户管理
                case R.id.customLinerLayout:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(ExtraName.SCALETOCUSTOME, ExtraName.TOCUSTOME);
                    ActivitySkip.forward(getActivity(), CustomeActivity.class, bundle2);
                    break;
                //学习园地
                case R.id.studyLinearLayout:
                    ActivitySkip.forward(getActivity(), LearningGardenActivity.class);
                    break;
                //设置中心
                case R.id.rlBefore:
                    ActivitySkip.forward(getActivity(), SettingActivity.class);
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.home_fragment);
        initWidget();
        getHomeData();
    }

    private void getHomeData() {
        int user_id = Integer.parseInt(SharedPreferencesHelps.getUserID());
        //String url = Contants.HOME_DAA + "user_id=" + user_id;
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        final FinalHttp fh = new FinalHttp();
        fh.get(Contants.HOME_DAA, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String json) {
                if (!TextUtils.isEmpty(json)) {

                    Gson gson = new Gson();
                    HomeDataBean homeDataBean = gson.fromJson(json, HomeDataBean.class);
                    if (homeDataBean.getCode() == 1) {

                        tvScaleSource.setText("共有" + homeDataBean.getResult().getTotal_product() + "个推广素材");
                        tvNewAddSource.setText("本周新增" + homeDataBean.getResult().getNew_product() + "个");
                        tvCustomNumber.setText("您有" + homeDataBean.getResult().getTotal_customer() + "位客户");
                        tvNewCustomNumber.setText("本月新增" + homeDataBean.getResult().getNew_customer() + "位");
                        tvStydyDatumCount.setText("共有" + homeDataBean.getResult().getTotal_study() + "个学习资料");
                        tvNotCompleteCount.setText("您有" + homeDataBean.getResult().getUncheck() + "个考核未完成");
                        tvNoNewsCount.setText("您还有" + homeDataBean.getResult().getUnread() + "条未读信息");
                        /*if (homeDataBean.getResult().getUnread() != 0) {
                            tvMessage.setVisibility(View.VISIBLE);
                            tvMessage.setText(String.valueOf(homeDataBean.getResult().getUnread()));
                        } else {
                            tvMessage.setVisibility(View.GONE);
                        }*/
                    }

                } else {

                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
            }
        });

    }

    private void initWidget() {
        //消息管理
        rlBefore = (RelativeLayout) getViewById(R.id.rlBefore);
        //资料
        saleLinearLayyout = (LinearLayout) getViewById(R.id.saleLinearLayyout);
        //客户管理
        customLinerLayout = (LinearLayout) getViewById(R.id.customLinerLayout);
        //学习园地
        studyLinearLayout = (LinearLayout) getViewById(R.id.studyLinearLayout);


        tvScaleSource = (TextView) getViewById(R.id.tvScaleSource);
        tvNewAddSource = (TextView) getViewById(R.id.tvNewAddSource);
        tvCustomNumber = (TextView) getViewById(R.id.tvCustomNumber);
        tvNewCustomNumber = (TextView) getViewById(R.id.tvNewCustomNumber);
        tvStydyDatumCount = (TextView) getViewById(R.id.tvStydyDatumCount);
        tvNotCompleteCount = (TextView) getViewById(R.id.tvNotCompleteCount);
        tvNoNewsCount = (TextView) getViewById(R.id.tvNoNewsCount);
    }

    @Override
    protected void setListener() {
        rlBefore.setOnClickListener(mOnClickListener);
        saleLinearLayyout.setOnClickListener(mOnClickListener);
        customLinerLayout.setOnClickListener(mOnClickListener);
        studyLinearLayout.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
