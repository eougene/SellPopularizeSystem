package com.yd.org.sellpopularizesystem.activity;

import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class EOIActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullableListView lvSaleRecord;
    private PullToRefreshLayout ptrlSaleRecord;
    private int page = 1;
    private List<EoilistBean.ResultBean> eoiList = new ArrayList<>();
    private CommonAdapter eoiAdapter;
    private PopupWindow firbSelectPopWindow;
    private View firbPwView;
    private Button btUnknown, btSure, btFalse;
    private String eoi_ID = "";


    @Override
    protected int setContentView() {
        return R.layout.activity_eoi;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.eoi_));
        hideRightImagview();
        lvSaleRecord = getViewById(R.id.content_view);
        ptrlSaleRecord = getViewById(R.id.refresh_view);
        ptrlSaleRecord.setOnRefreshListener(this);

        showZh();

        getEOIData(1, true);

    }

    private void getEOIData(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.EOI_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .headers("Cache-Control", "max-age=0")
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("company_id", "")
                .params("product_id", "")
                .params("page", page + "")
                .params("number", "100")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess", "onSuccess:" + json);

                        closeDialog();
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
                                    if (item.getProduct_info()!=null){
                                        //标题
                                        holder.setText(R.id.eoiTitle, item.getProduct_info().getProduct_name() + "/" + item.getProduct_childs_info().getProduct_childs_unit_number());

                                    }

                                    holder.setText(R.id.tvProm01, item.getProduct_childs_info().getBedroom());
                                    holder.setText(R.id.tvProm02, item.getProduct_childs_info().getBathroom());
                                    holder.setText(R.id.tvProm03, item.getProduct_childs_info().getCar_space());


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
                                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.eoi_cancel));

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


    }


    @Override
    public void setListener() {

        lvSaleRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EoilistBean.ResultBean eoilistBean = (EoilistBean.ResultBean) eoiAdapter.getItem(position);
                eoi_ID = eoilistBean.getEoi_id() + "";


                //如果是未使用可以退款
                if (eoilistBean.getStatus()!=1&&eoilistBean.getPay_info().getCancel_apply_status().equals("0") && eoilistBean.getPay_info().getIs_use().equals("0")) {
                    firbSelectPopWindow.showAtLocation(EOIActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM, 0, 0);
                }
            }
        });

    }

    @Override
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
    }

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
        firbSelectPopWindow = new PopupWindow(firbPwView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
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
                .params("eoi_id", eoi_id)
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(EOIActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);

                        closeDialog();
                        if (!TextUtils.isEmpty(json)) {
                            Gson gson = new Gson();
                            ErrorBean userBean = gson.fromJson(json, ErrorBean.class);
                            ToasShow.showToastCenter(EOIActivity.this, userBean.getMsg());
                        }


                    }
                });


    }

}
