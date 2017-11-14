package com.yd.org.sellpopularizesystem.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.adapter.FragAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.fragment.CommissionFragment;
import com.yd.org.sellpopularizesystem.fragment.EoiFragment;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class EOIActivity extends FragmentActivity  {

    private int page = 1;
    private List<EoilistBean.ResultBean> eoiList = new ArrayList<>();
    private CommonAdapter eoiAdapter;
    private PopupWindow firbSelectPopWindow;
    private View firbPwView;
    private FragAdapter adapter;
    private ImageView ivBack;
    private RadioButton rbNotUse,rbAlreadyUsed,rbRefunded;
    private TextView tvTitle;
    private Button btUnknown, btSure, btFalse;
    private String eoi_ID = "";
    private ViewPager vpEoi;
    private EoiFragment notUseFragment,alreadyUsedFragment,refundedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eoi);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        initView();
        initViewPage();
    }

    public void initView() {
        ivBack= (ImageView) findViewById(R.id.ivBack);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.my_eoi);
        rbNotUse= (RadioButton) findViewById(R.id.rbNotUse);
        rbAlreadyUsed= (RadioButton) findViewById(R.id.rbAlreadyUsed);
        rbRefunded= (RadioButton) findViewById(R.id.rbRefunded);
        vpEoi= (ViewPager) findViewById(R.id.vpEoi);
        ivBack.setOnClickListener(mOnClickListener);
        rbNotUse.setOnClickListener(mOnClickListener);
        rbAlreadyUsed.setOnClickListener(mOnClickListener);
        rbRefunded.setOnClickListener(mOnClickListener);
        //showZh();
        //getEOIData(1, true);

    }

    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        notUseFragment = EoiFragment.getInstnce(0);
        alreadyUsedFragment = EoiFragment.getInstnce(1);
        refundedFragment = EoiFragment.getInstnce(2);
        fragments.add(notUseFragment);
        fragments.add(alreadyUsedFragment);
        fragments.add(refundedFragment);
        adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vpEoi.setAdapter(adapter);
        vpEoi.setCurrentItem(0);
        vpEoi.addOnPageChangeListener(new MyVPageChangeListener());
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //返回
                case R.id.ivBack:
                    finish();
                    break;

                //未使用
                case R.id.rbNotUse:
                    selectRadio(0);
                    vpEoi.setCurrentItem(0);
                    break;

                //已使用
                case R.id.rbAlreadyUsed:
                    selectRadio(1);
                    vpEoi.setCurrentItem(1);
                    break;
                    //已退款
                case R.id.rbRefunded:
                    selectRadio(2);
                    vpEoi.setCurrentItem(2);
                    break;
                default:
                    break;

            }
        }
    };

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
                selectRadio(0);
                break;

            case 1:
                selectRadio(1);
                break;

            case 2:
                selectRadio(2);
                break;
            default:
                break;
        }
    }

    private void selectRadio(int type) {

        if (type == 0) {
            rbNotUse.setChecked(true);
            rbAlreadyUsed.setChecked(false);
            rbRefunded.setChecked(false);
        } else if (type == 1){
            rbNotUse.setChecked(false);
            rbAlreadyUsed.setChecked(true);
            rbRefunded.setChecked(false);
        }else {
            rbNotUse.setChecked(false);
            rbAlreadyUsed.setChecked(false);
            rbRefunded.setChecked(true);
        }

    }

    /*private void getEOIData(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.EOI_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .headers("Cache-Control", "max-age=0")
                .timeStamp(true).params("user_id", SharedPreferencesHelps.getUserID())
                .params("company_id", "").params("product_id", "").params("page", page + "")
                .params("number", "100").execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onError(ApiException e) {
               // closeDialog();
                Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
            }

            @Override
            public void onSuccess(String json) {
                Log.e("onSuccess", "onSuccess:" + json);

              //  closeDialog();
                Gson gson = new Gson();
                EoilistBean eoilistBean = gson.fromJson(json, EoilistBean.class);
                eoiList = eoilistBean.getResult();


                if (isRefresh) {

                    if (eoiList.size() == 0) {
                        getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                        lvSaleRecord.setVisibility(View.GONE);
                    } else {
                        getViewById(R.id.noInfomation).setVisibility(View.GONE);
                        lvSaleRecord.setVisibility(View.VISIBLE);
                    }


                    eoiAdapter = new CommonAdapter<EoilistBean.ResultBean>(EOIActivity.this, eoiList, R.layout.eoi_listview_item_layout) {

                        @Override
                        public void convert(ViewHolder holder, EoilistBean.ResultBean item) {
                            //编号
                            holder.setText(R.id.tvEoiNum, item.getEoi_id() + "");
                            //销售名
                            holder.setText(R.id.salesName, item.getCustomer_info().getSurname() + " " + item.getCustomer_info().getFirst_name());
                            if (item.getProduct_childs_info() != null) {
                                //标题
                                holder.setText(R.id.eoiTitle, item.getProduct_info().getProduct_name() + "/" + item.getProduct_childs_info().getProduct_childs_unit_number());
                                holder.setText(R.id.tvProm01, item.getProduct_childs_info().getBedroom());
                                holder.setText(R.id.tvProm02, item.getProduct_childs_info().getBathroom());
                                holder.setText(R.id.tvProm03, item.getProduct_childs_info().getCar_space());

                            }


                            if (item.getStatus() == 1) {
                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.eoi_eoi));
                            } else {
                                //未使用
                                if (item.getPay_info().getIs_use().equals("0")) {


                                    //退款申请正在审核
                                    if (item.getPay_info().getCancel_apply_status().equals("1")) {
                                        holder.setText(R.id.tvEoiStatusDes, getString(R.string.refund));
                                        //已退款
                                    } else if (item.getPay_info().getCancel_apply_status().equals("2")) {
                                        holder.setText(R.id.tvEoiStatusDes, getString(R.string.done_re));
                                        //退款已拒绝
                                    } else if (item.getPay_info().getCancel_apply_status().equals("3")) {
                                      //eoi_cancel
                                        holder.setText(R.id.tvEoiStatusDes, getString(R.string.nouse));

                                        //未使用,未退款
                                    } else if (item.getPay_info().getCancel_apply_status().equals("0")) {
                                        holder.setText(R.id.tvEoiStatusDes, getString(R.string.nouse));
                                    }


                                } else {
                                    holder.setText(R.id.tvEoiStatusDes, getString(R.string.isuse));
                                }
                            }


                        }
                    };

                    lvSaleRecord.setAdapter(eoiAdapter);
                } else {
                    eoiAdapter.addMore(eoiList);
                }
            }
        });


    }*/


   /* @Override
    public void setListener() {

        lvSaleRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EoilistBean.ResultBean eoilistBean = (EoilistBean.ResultBean) eoiAdapter.getItem(position);
                eoi_ID = eoilistBean.getEoi_id() + "";


                //如果是未使用可以退款
                if (eoilistBean.getStatus() != 1  && eoilistBean.getPay_info().getIs_use().equals("0")) {
                    firbSelectPopWindow.showAtLocation(EOIActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM, 0, 0);
                }
            }
        });

    }*/

    /*@Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getEOIData(page, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        page++;
        getEOIData(page, false);
    }*/

    /**
     * 申请退款
     */
    private void showZh() {
        firbPwView = LayoutInflater.from(this).inflate(R.layout.firb_popuwindow, null);
        RelativeLayout rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown = (Button) firbPwView.findViewById(R.id.btUnknown);
        btUnknown.setVisibility(View.GONE);
        btSure = (Button) firbPwView.findViewById(R.id.btSure);
        btSure.setText(getString(R.string.eoi_refund));
        btFalse = (Button) firbPwView.findViewById(R.id.btFalse);
        btFalse.setText(getString(R.string.cancel));
        firbSelectPopWindow = new PopupWindow(firbPwView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        firbSelectPopWindow.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable firb = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        firbSelectPopWindow.setBackgroundDrawable(firb);
        rlFirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
                cancel_eoi(eoi_ID);
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });


    }


    /**
     * 退款
     *
     * @param eoi_id
     */
    private void cancel_eoi(String eoi_id) {

        EasyHttp.get(Contants.EOI_REFUND)
                .cacheMode(CacheMode.NO_CACHE)
                .params("eoi_id", eoi_id).timeStamp(true)
                .execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                //showDialog();
            }

            @Override
            public void onError(ApiException e) {
                //closeDialog();
                ToasShow.showToastCenter(EOIActivity.this, e.getMessage());
                Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
            }

            @Override
            public void onSuccess(String json) {
                Log.e("onSuccess***", "UserBean:" + json);

               // closeDialog();
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    ErrorBean userBean = gson.fromJson(json, ErrorBean.class);
                    ToasShow.showToastCenter(EOIActivity.this, userBean.getMsg());
                }


            }
        });


    }

}
