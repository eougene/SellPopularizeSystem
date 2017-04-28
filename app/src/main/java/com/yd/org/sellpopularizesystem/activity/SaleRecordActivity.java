package com.yd.org.sellpopularizesystem.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;

public class SaleRecordActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView lvSaleRecord;
    private PullToRefreshLayout ptrlSaleRecord;
    private int page = 1;
    private View mSalePopView;
    private Button btApplyContract,btOrderCancel,btSaleCancel;
    private LinearLayout llSalePop;
    private SaleOrderBean sob;
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();
    private CommonAdapter saleAdapter;
    private CommonPopuWindow mSalePopuwindow;
    private int orderId;
    public  static SaleRecordActivity sra;

    public  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getSaleData(page, 10, true);
        }
    };
    @Override
    protected int setContentView() {
        return R.layout.activity_sale_record;
    }

    @Override
    public void initView() {
        sra=this;
        hideRightImagview();
        setTitle(getString(R.string.sale_list));
        lvSaleRecord = getViewById(R.id.content_view);
        lvSaleRecord.setDrawSelectorOnTop(true);
        lvSaleRecord.setSelector(R.color.home_scale_d);
        ptrlSaleRecord = getViewById(R.id.refresh_view);
        ptrlSaleRecord.setOnRefreshListener(this);
        mSalePopuwindow = new CommonPopuWindow(SaleRecordActivity.this, mOnClickListener) {
            @Override
            protected int getLayoutId() {
                return R.layout.saleoperate_popuwindow;
            }
        };
        mSalePopView=mSalePopuwindow.getContentView();
        llSalePop= (LinearLayout) mSalePopView.findViewById(R.id.llSalePop);
        btApplyContract = (Button) mSalePopView.findViewById(R.id.btApplyContract);
        btOrderCancel= (Button) mSalePopView.findViewById(R.id.btOrderCancel);
        btSaleCancel= (Button) mSalePopView.findViewById(R.id.btSaleCancel);
        getSaleData(page, 10, true);
    }

    private void getSaleData(int page, int number, final boolean isRefresh) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("company_id", SharedPreferencesHelps.getCompanyId());
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", page + "");
        ajaxParams.put("number", number + "");
        http.get(Contants.INQUIRE_ORDER_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Log.e("TAG", "onSuccess: "+s );
                try {
                    JSONObject josn = new JSONObject(s);
                    if (josn.getInt("code") == 1) {
                        parseJson(s, isRefresh);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void parseJson(String json, boolean isRefresh) {
        Gson gson = new Gson();
        sob = gson.fromJson(json, SaleOrderBean.class);
        sobRbData = sob.getResult();
        if (isRefresh) {
            //ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
            saleAdapter = new CommonAdapter<SaleOrderBean.ResultBean>(this, sobRbData, R.layout.saleorder_listview_item) {
                @Override
                public void convert(ViewHolder holder, SaleOrderBean.ResultBean item) {
                    holder.setText(R.id.tvSaleNum, item.getProduct_orders_id() + "");
                    holder.setText(R.id.tvSaleDdes, item.getProduct_info().getProduct_childs_lot_number() + "-" + item.getProduct_info().getProduct_childs_unit_number());
                    holder.setText(R.id.tvBedRoom, item.getProduct_info().getBedroom());
                    holder.setText(R.id.tvBathroom, item.getProduct_info().getBathroom());
                    holder.setText(R.id.tvCarSquare, item.getProduct_info().getCar_space());
                    holder.setText(R.id.tvSalePrice, item.getProduct_info().getCurrency() + item.getProduct_info().getPrice());
                    holder.setText(R.id.tvSaleName, item.getCustomer_first_name() + item.getCustomer_surname());
                    if (saleAdapter.getmCurrentItem() == holder.getPosition() && isClick()) {
                        holder.setView(R.id.tvStatus, ContextCompat.getColor(SaleRecordActivity.this, R.color.transparent));
                        holder.getConvertView().setBackgroundColor(ContextCompat.getColor(SaleRecordActivity.this, R.color.home_scale));
                    } else {
                        holder.setView(R.id.tvStatus, ContextCompat.getColor(SaleRecordActivity.this, R.color.home_scale));
                        holder.getConvertView().setBackground(null);
                    }
                    if (item.getStatus()!=11){
                        //合同状态判断
                        if(item.getContract_apply_status()==0){
                            holder.setText(R.id.tvStatus,"意向金凭证已经上传\n请申请合同");
                        }else if(item.getContract_apply_status()==1){
                            holder.setText(R.id.tvStatus,"正在申请合同\n请等待管理员审核");
                        }else if(item.getContract_apply_status()==2){
                            holder.setText(R.id.tvStatus,"合同首页已审核请上传首付款凭证");
                            if (item.getContract_check_time()!=null && item.getBuy_money_status()==0){
                                holder.setText(R.id.tvStatus,"合同首页已审核请上传首付款凭证");
                            }else if (item.getContract_check_time()!=null && item.getBuy_money_status()==1){
                                holder.setText(R.id.tvStatus,"首付款凭证审核中");
                            }else {
                                holder.setText(R.id.tvStatus,"合同首页已审核首付款凭证已审核");
                            }
                        }
                    }else if (item.getStatus()==11){
                        holder.setText(R.id.tvStatus,"订单已完成");
                    }
                }
            };
            lvSaleRecord.setAdapter(saleAdapter);
        }else{
            saleAdapter.addMore(sobRbData);
            ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putString("orderId",orderId+"");
            switch (v.getId()) {
                case R.id.llSalePop:
                    mSalePopuwindow.dismiss();
                    break;
                case R.id.btApplyContract:
                    mSalePopuwindow.dismiss();
                    ActivitySkip.forward(SaleRecordActivity.this,AskContractActivity.class,bundle);
                    break;
                case R.id.btOrderCancel:
                    canceOrder(orderId);
                    mSalePopuwindow.dismiss();
                    break;
                case R.id.btSaleCancel:
                    mSalePopuwindow.dismiss();
                    break;
            }
        }
    };



    private void canceOrder(int orderId) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", orderId + "");
        http.post(Contants.ORDER_CANCEL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    @Override
    public void setListener() {
        lvSaleRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderId = ((SaleOrderBean.ResultBean) saleAdapter.getItem(position)).getProduct_orders_id();
                //saleAdapter.getView(position,)
                Log.e("TAG", "onItemClick: " + orderId);
                TextView tvText = (TextView) view.findViewById(R.id.tvStatus);
                //tvText.setBackgroundColor(ContextCompat.getColor(SaleRecordActivity.this,R.color.transparent));
                tvText.setBackground(null);
                saleAdapter.setCurrentItem(position);
                if (((SaleOrderBean.ResultBean) saleAdapter.getItem(position)).getContract_apply_status()==1){
                    saleAdapter.setClick(true);
                    saleAdapter.notifyDataSetChanged();
                }else{
                    saleAdapter.setClick(true);
                    saleAdapter.notifyDataSetChanged();
                    mSalePopuwindow.showAtLocation(SaleRecordActivity.this.findViewById(R.id.flSale), Gravity.CENTER,0,0);
                }

            }
        });

        llSalePop.setOnClickListener(mOnClickListener);
        btApplyContract.setOnClickListener(mOnClickListener);
        btOrderCancel.setOnClickListener(mOnClickListener);
        btSaleCancel.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getSaleData(page, 10, false);
    }
}
