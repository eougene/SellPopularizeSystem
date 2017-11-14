package com.yd.org.sellpopularizesystem.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.yd.org.sellpopularizesystem.javaBean.EoiBean;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/11/13.
 */

public class EoiFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener{
    private int cate_id, type = 0,page = 1;
    private PullToRefreshLayout ptrl;
    private PullableListView listView;
    private List<EoiBean.ResultBean> eoiList = new ArrayList<>();
    private CommonAdapter eoiAdapter;
    private String eoi_ID = "";
    private PopupWindow firbSelectPopWindow;
    private Button btUnknown, btSure, btFalse;
    private View firbPwView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_notific);
        initViews();
    }

    private void initViews() {
        cate_id = getArguments().getInt("cate_id")+1;
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        //showZh();
        getEOIData(1, true);
    }

    private void getEOIData(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.EOI_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .headers("Cache-Control", "max-age=0")
                .timeStamp(true).params("user_id", SharedPreferencesHelps.getUserID()).params("state", cate_id+"")
                .params("company_id", "").params("project_id", "").params("page", page + "")
                .params("number", "100").params("sort", "0").execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onError(ApiException e) {
                dismissLoadingDialog();
                Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
            }

            @Override
            public void onSuccess(String json) {
                Log.e("onSuccess", "onSuccess:" + json);

                dismissLoadingDialog();
                Gson gson = new Gson();
                EoiBean eoiBean = gson.fromJson(json, EoiBean.class);
                eoiList = eoiBean.getResult();


                if (isRefresh) {

                    if (eoiList.size() == 0) {
                        getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        getViewById(R.id.noInfomation).setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }


                    eoiAdapter = new CommonAdapter<EoiBean.ResultBean>(getActivity(), eoiList, R.layout.eoi_item_layout) {

                        @Override
                        public void convert(ViewHolder holder, EoiBean.ResultBean item) {
                            //编号
                           // holder.setText(R.id.tvEoiNum, item.getEoi_id() + "");
                            //销售名
                           // holder.setText(R.id.salesName, item.getCustomer_info().getSurname() + " " + item.getCustomer_info().getFirst_name());
                            if (item.getProject() != null) {
                                //标题
                                holder.setText(R.id.tvProjectName, item.getProject().getName());
                                holder.setText(R.id.tvUnitNum, item.getProject().getProperty().getUnit_number());
                               // holder.setText(R.id.tvProm03, item.getProduct_childs_info().getCar_space());
                            }
                            holder.setText(R.id.tvTime, MyUtils.date2String("yyyy/MM/dd",item.getApply_date()));

                        }
                    };

                    listView.setAdapter(eoiAdapter);
                } else {
                    eoiAdapter.addMore(eoiList);
                }
            }
        });


    }

    /**
     * 申请退款
     */
    private void showZh() {
        firbPwView = LayoutInflater.from(getContext()).inflate(R.layout.firb_popuwindow, null);
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
               // cancel_eoi(eoi_ID);
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });


    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EoilistBean.ResultBean eoilistBean = (EoilistBean.ResultBean) eoiAdapter.getItem(position);
                eoi_ID = eoilistBean.getEoi_id() + "";


                //如果是未使用可以退款
                if (eoilistBean.getStatus() != 1  && eoilistBean.getPay_info().getIs_use().equals("0")) {
                    firbSelectPopWindow.showAtLocation(getActivity().findViewById(R.id.flContent), Gravity.BOTTOM, 0, 0);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 创建实体类
     *
     * @param cate_id
     * @return
     */
    public static EoiFragment getInstnce(int cate_id) {
        EoiFragment eoiFragmen = new EoiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cate_id", cate_id);
        eoiFragmen.setArguments(bundle);
        return eoiFragmen;
    }
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getEOIData(page, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        page++;
        getEOIData(page, false);
    }
}
