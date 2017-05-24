package com.yd.org.sellpopularizesystem.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.order;

public class SaleRecordActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView lvSaleRecord;
    private PullToRefreshLayout ptrlSaleRecord;
    private int page = 1;
    private View mSalePopView, mUpdatePopView;
    private Button btApplyContract, btOrderUpdate, btPayIntention, btOrderCancel, btSaleCancel;
    private TextView tvOrderSequence, tvOrderContent, tvOrderUpdateSubmit;
    private EditText etOrderRemark;
    private LinearLayout llSalePop;
    private SaleOrderBean sob;
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();
    private CommonAdapter saleAdapter;
    private CommonPopuWindow mSalePopuwindow, mUpdatePop;
    private Dialog mUpdateDialog;
    private int orderId, pos;
    public static SaleRecordActivity sra;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getSaleData(page, 50, true);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_sale_record;
    }

    @Override
    public void initView() {
        sra = this;
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
        mSalePopView = mSalePopuwindow.getContentView();
        llSalePop = (LinearLayout) mSalePopView.findViewById(R.id.llSalePop);
        btApplyContract = (Button) mSalePopView.findViewById(R.id.btApplyContract);
        btPayIntention = (Button) mSalePopView.findViewById(R.id.btPayIntention);
        btOrderUpdate = (Button) mSalePopView.findViewById(R.id.btOrderUpdate);
        btOrderCancel = (Button) mSalePopView.findViewById(R.id.btOrderCancel);
        btSaleCancel = (Button) mSalePopView.findViewById(R.id.btSaleCancel);

        mUpdateDialog = new Dialog(SaleRecordActivity.this);
        mUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mUpdateDialog.setContentView(R.layout.update_order_popview);
        Window dialogWindow = mUpdateDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = MyUtils.getStatusBarHeight(SaleRecordActivity.this);
        dialogWindow.setAttributes(lp);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        tvOrderSequence = (TextView) mUpdateDialog.findViewById(R.id.tvOrderSequence);
        tvOrderContent = (TextView) mUpdateDialog.findViewById(R.id.tvOrderContent);
        etOrderRemark = (EditText) mUpdateDialog.findViewById(R.id.etOrderRemark);
        tvOrderUpdateSubmit = (TextView) mUpdateDialog.findViewById(R.id.tvOrderUpdateSubmit);
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
                Log.e("TAG", "onSuccess: " + s);
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
                    if (item.getProduct_name() == null) {
                        holder.setText(R.id.tvSaleDes, "null" + "-" + item.getProduct_info().getProduct_childs_lot_number() + "-" + item.getProduct_info().getProduct_childs_unit_number());
                    } else {
                        holder.setText(R.id.tvSaleDes, item.getProduct_name().getProduct_name() + "-" + item.getProduct_info().getProduct_childs_lot_number() + "-" + item.getProduct_info().getProduct_childs_unit_number());
                    }
                    /*holder.setText(R.id.tvBedRoom, item.getProduct_info().getBedroom());
                    holder.setText(R.id.tvBathroom, item.getProduct_info().getBathroom());
                    holder.setText(R.id.tvCarSquare, item.getProduct_info().getCar_space());*/
                    holder.setText(R.id.tvSalePrice, item.getProduct_info().getCurrency() + item.getProduct_info().getPrice());
                    holder.setText(R.id.tvSaleName, item.getCustomer_surname() + getString(R.string.single_blank_space) + item.getCustomer_first_name());
                    if (saleAdapter.getmCurrentItem() == item.getProduct_orders_id()) {
                        holder.setView(R.id.tvStatus, ContextCompat.getColor(SaleRecordActivity.this, R.color.transparent));
                        holder.getConvertView().setBackgroundColor(ContextCompat.getColor(SaleRecordActivity.this, R.color.home_scale));
                    } else {
                        holder.setView(R.id.tvStatus, ContextCompat.getColor(SaleRecordActivity.this, R.color.home_scale));
                        holder.getConvertView().setBackground(null);
                    }
                    if (item.getStatus() != 11) {
                        //订单状态判断
                        if (item.getCancel_apply_status() == 1) {
                            holder.setText(R.id.tvStatus, "订单放弃中");
                        } else if (item.getCancel_apply_status() == 2) {
                            Log.e("Cancel_apply_status", "convert: " + item.getProduct_orders_id());
                            holder.setText(R.id.tvStatus, "订单已取消");
                        } else {
                            if (item.getOrder_money_status() == 1) {
                                if (item.getPayment_method()==1 || item.getPayment_method()==4) {
                                    if (item.getContract_apply_status() == 0) {
                                        holder.setText(R.id.tvStatus, "意向金凭证已上传\n请申请合同");
                                    }
                                }else {
                                    holder.setText(R.id.tvStatus, "尚未支付意向金\n请付款");
                                }
                            }
                            if (item.getUpload_contract_status() == 2 && item.getBuy_money_status() == 1) {
                                holder.setText(R.id.tvStatus, "合同首页已审核\n首付款凭证审核中");
                            }
                            if (item.getOrder_money_status() == 2) {
                                if (item.getContract_apply_status() == 0) {
                                    holder.setText(R.id.tvStatus, "意向金已支付\n请申请合同");
                                }
                                if (item.getContract_apply_status() == 1) {
                                    holder.setText(R.id.tvStatus, "正在申请合同\n请等待管理员审核");
                                }
                                if (item.getContract_apply_status() == 2) {
                                    if (item.getUpload_contract_status() == 0 && item.getBuy_money_status() == 0) {
                                        holder.setText(R.id.tvStatus, "请上传合同首页\n请上传首付款凭证");
                                    }
                                    if (item.getUpload_contract_status() == 1 && item.getBuy_money_status() == 0) {
                                        holder.setText(R.id.tvStatus, "合同首页审核中\n请上传首付款凭证");
                                    } else if (item.getUpload_contract_status() == 2 && item.getBuy_money_status() == 0) {
                                        holder.setText(R.id.tvStatus, "合同首页已审核\n请上传首付款凭证");
                                    } else if (item.getUpload_contract_status() == 2 && item.getBuy_money_status() == 1) {
                                        holder.setText(R.id.tvStatus, "合同首页已审核\n首付款凭证审核中");
                                    } else if (item.getUpload_contract_status() == 2 && item.getBuy_money_status() == 2) {
                                        holder.setText(R.id.tvStatus, "准备交换合同");
                                    }
                                }
                            } else if (item.getOrder_money_status() == 2) {
                                if (item.getUpload_contract_status() == 2 && item.getBuy_money_status() == 2) {
                                    holder.setText(R.id.tvStatus, "准备交换合同");
                                }
                            }
                        }
                    } else if (item.getStatus() == 11) {
                        holder.setText(R.id.tvStatus, "订单已完成");
                    }
                }
            };
            saleAdapter.setCurrentItem(0);
            lvSaleRecord.setAdapter(saleAdapter);
        } else {
            saleAdapter.addMore(sobRbData);
            ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId + "");
            switch (v.getId()) {
                //点击pop内容区之外pop消失
                case R.id.llSalePop:
                    mSalePopuwindow.dismiss();
                    break;
                //点击申请合同
                case R.id.btApplyContract:
                    if (btApplyContract.getText().equals("上传合同首页")) {
                        mSalePopuwindow.dismiss();
                        BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
                    } else {
                        mSalePopuwindow.dismiss();
                        ActivitySkip.forward(SaleRecordActivity.this, AskContractActivity.class, bundle);
                    }
                    break;
                //点击意向金
                case R.id.btPayIntention:
                    if (btPayIntention.getText().equals("上传首款凭证")) {
                        mSalePopuwindow.dismiss();
                        BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
                    } else {
                        Bundle bun=new Bundle();
                        bun.putString("payurlId",sobRbData.get(pos).getBuy_money_url());
                        bun.putString("payment_method",sobRbData.get(pos).getPayment_amount());
                            ActivitySkip.forward(SaleRecordActivity.this,PaymentQrActivity.class,bun);
                    }

                    break;
                //点击修改订单
                case R.id.btOrderUpdate:
                    mSalePopuwindow.dismiss();
                    mUpdateDialog.show();
                    SaleOrderBean.ResultBean sb = (SaleOrderBean.ResultBean) saleAdapter.getItem(pos);
                    tvOrderSequence.setText(sb.getProduct_orders_id() + ":");
                    if (sb.getProduct_name() == null) {
                        tvOrderContent.setText("null" + "-" +
                                sb.getProduct_info().getProduct_childs_lot_number() + "-"
                                + sb.getProduct_info().getProduct_childs_unit_number());
                    } else {
                        tvOrderContent.setText(sb.getProduct_name().getProduct_name() + "-" +
                                sb.getProduct_info().getProduct_childs_lot_number() + "-"
                                + sb.getProduct_info().getProduct_childs_unit_number());
                    }
                    etOrderRemark.setText(sb.getRemark());
                    break;
                //点击修改订单界面提交按钮
                case R.id.tvOrderUpdateSubmit:
                    submitOrderUpdate(orderId);
                    break;
                //点击放弃订单
                case R.id.btOrderCancel:
                    sobRbData.get(pos).setCancel_apply_status(1);
                    saleAdapter.notifyDataSetChanged();
                    canceOrder(orderId);
                    mSalePopuwindow.dismiss();
                    break;
                //点击取消
                case R.id.btSaleCancel:
                    mSalePopuwindow.dismiss();
                    break;
            }
        }
    };
    //修改订单
    private void submitOrderUpdate(int orderId) {
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", orderId + "");
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("remark", etOrderRemark.getText().toString());
        fh.post(Contants.UPDATE_ORDER, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        mUpdateDialog.dismiss();
                        ToasShow.showToastCenter(SaleRecordActivity.this, json.getString("msg"));
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

    private void canceOrder(int orderId) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", orderId + "");
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        http.post(Contants.ORDER_CANCEL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        mUpdateDialog.dismiss();
                        ToasShow.showToastCenter(SaleRecordActivity.this, json.getString("msg"));
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

    @Override
    public void setListener() {
        lvSaleRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetSalePopView();
                saleAdapter.setCurrentItem(sobRbData.get(position).getProduct_orders_id());
                saleAdapter.notifyDataSetChanged();

                if (sobRbData.get(position).getCancel_apply_status() != 1 && sobRbData.get(position).getCancel_apply_status() != 2) {
                    orderId = sobRbData.get(position).getProduct_orders_id();
                    //saleAdapter.getView(position,)
                    pos = position;
                    Log.e("TAG", "onItemClick: " + pos);
                    TextView tvText = (TextView) view.findViewById(R.id.tvStatus);
                    //tvText.setBackgroundColor(ContextCompat.getColor(SaleRecordActivity.this,R.color.transparent));
                    Log.e("view", "onItemClick: " + tvText.getText().toString());
                    tvText.setBackground(null);
                    //尚未支付意向金情况:Order_money_status() == 0
                    if (tvText.getText().toString().equals("尚未支付意向金\n请付款")) {
                        btApplyContract.setVisibility(View.GONE);
                        btPayIntention.setText("支付意向金");
                    }
                    //意向金已支付情况:Order_money_status() == 1,Contract_apply_status() == 0
                    if (tvText.getText().toString().equals("意向金已支付\n请申请合同")) {
                        btPayIntention.setVisibility(View.GONE);
                    }
                    if (tvText.getText().toString().equals("意向金凭证已上传\n请申请合同")) {
                        btPayIntention.setVisibility(View.GONE);
                    }
                    //请上传合同首页请上传首付款凭证情况:Order_money_status() == 2,Contract_apply_status() == 2
                    if (tvText.getText().toString().equals("请上传合同首页\n请上传首付款凭证")) {
                        btApplyContract.setText("上传合同首页");
                        btPayIntention.setText("上传首款凭证");
                        btOrderCancel.setVisibility(View.GONE);
                    }
                    if (tvText.getText().toString().equals("合同首页已审核\n首付款凭证审核中")) {
                        btApplyContract.setText("上传首款凭证");
                        btPayIntention.setVisibility(View.GONE);
                    }
                    if (tvText.getText().toString().equals("准备交换合同")) {
                        btApplyContract.setVisibility(View.GONE);
                        btPayIntention.setVisibility(View.GONE);
                        btOrderCancel.setVisibility(View.GONE);
                    }
                    mSalePopuwindow.showAtLocation(SaleRecordActivity.this.findViewById(R.id.flSale), Gravity.CENTER, 0, 0);
                }

            }
        });

        llSalePop.setOnClickListener(mOnClickListener);
        btApplyContract.setOnClickListener(mOnClickListener);
        btOrderCancel.setOnClickListener(mOnClickListener);
        btSaleCancel.setOnClickListener(mOnClickListener);
        btPayIntention.setOnClickListener(mOnClickListener);
        btOrderUpdate.setOnClickListener(mOnClickListener);
        tvOrderUpdateSubmit.setOnClickListener(mOnClickListener);
    }

    private void resetSalePopView() {
        btApplyContract.setVisibility(View.VISIBLE);
        btPayIntention.setVisibility(View.VISIBLE);
        btPayIntention.setVisibility(View.VISIBLE);
        btOrderCancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getSaleData(page, 10, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getSaleData(page, 10, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
