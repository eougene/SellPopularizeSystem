package com.yd.org.sellpopularizesystem.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.DepositActivity;
import com.yd.org.sellpopularizesystem.activity.SaleRecordActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ReceiptBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import static com.yd.org.sellpopularizesystem.R.id.depositImageView;
import static com.yd.org.sellpopularizesystem.R.id.tvStatus;

/**
 * Created by e-dot on 2017/6/8.
 * 订单页面
 */

public class SaleRecordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();

    public SaleRecordAdapter(Context mContext) {

        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }


    public void addMore(List<SaleOrderBean.ResultBean> sobRbData) {
        this.sobRbData.addAll(sobRbData);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return sobRbData.size();
    }

    @Override
    public Object getItem(int position) {
        return sobRbData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.saleorder_listview_item, null);
            viewHolder.tvSaleNum = (TextView) convertView.findViewById(R.id.tvSaleNum);
            viewHolder.tvSaleDes = (TextView) convertView.findViewById(R.id.tvSaleDes);
            viewHolder.tvSalePrice = (TextView) convertView.findViewById(R.id.tvSalePrice);
            viewHolder.tvStatus = (TextView) convertView.findViewById(tvStatus);
            viewHolder.tvSaleName = (TextView) convertView.findViewById(R.id.tvSaleName);


            viewHolder.saleRecorTv1 = (TextView) convertView.findViewById(R.id.saleRecorTv1);
            viewHolder.saleRecorTv2 = (TextView) convertView.findViewById(R.id.saleRecorTv2);
            viewHolder.saleRecorTv3 = (TextView) convertView.findViewById(R.id.saleRecorTv3);
            viewHolder.saleRecorTv4 = (TextView) convertView.findViewById(R.id.saleRecorTv4);

            viewHolder.depositImageView = (TextView) convertView.findViewById(depositImageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //实体类
        viewHolder.resultBean = sobRbData.get(position);
        //ID
        viewHolder.tvSaleNum.setText(viewHolder.resultBean.getProduct_orders_id() + "");
        //价格
        viewHolder.tvSalePrice.setText("$" + " " + MyUtils.getInstance().addComma(viewHolder.resultBean.getPrice().split("\\.")[0]));
        //姓名
        viewHolder.tvSaleName.setText(viewHolder.resultBean.getCustomer_surname() + mContext.getString(R.string.single_blank_space) + viewHolder.resultBean.getCustomer_first_name());

        //说明
        if (null != viewHolder.resultBean.getProduct_name() && !TextUtils.isEmpty(viewHolder.resultBean.getProduct_name() + "")) {
            viewHolder.tvSaleDes.setText(viewHolder.resultBean.getProduct_name().getProduct_name() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_lot_number() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_unit_number());
        } else {
            viewHolder.tvSaleDes.setText("Null" + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_lot_number() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_unit_number());
        }


        if (viewHolder.resultBean.getStatus() == 0) {

            //正常情况不会出现此情况
            if (viewHolder.resultBean.getOrder_money_status() == 0) {
                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.saleRecorTv3.setVisibility(View.GONE);
                viewHolder.saleRecorTv4.setVisibility(View.GONE);
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText(mContext.getResources().getString(R.string.nopayintent));

                //查看定金按钮
                viewHolder.depositImageView.setVisibility(View.GONE);

            }
            //意向金是否支付
            else if (viewHolder.resultBean.getOrder_money_status() == 1) {

                //查看定金按钮
                viewHolder.depositImageView.setVisibility(View.GONE);


                if ((viewHolder.resultBean.getPayment_method() == 1 || viewHolder.resultBean.getPayment_method() == 4)) {
                    //意向金凭证已上传,请等待管理员审核,可以取消订单

                    viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                    viewHolder.saleRecorTv2.setVisibility(View.GONE);
                    viewHolder.saleRecorTv3.setVisibility(View.GONE);
                    viewHolder.saleRecorTv4.setVisibility(View.GONE);
                    viewHolder.tvStatus.setVisibility(View.VISIBLE);
                    viewHolder.tvStatus.setText(mContext.getString(R.string.saler_03));

                } else {
                    //意向金未支付,支付意向金,可以取消订单
                    viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                    viewHolder.saleRecorTv2.setVisibility(View.VISIBLE);
                    viewHolder.saleRecorTv3.setVisibility(View.GONE);
                    viewHolder.saleRecorTv4.setVisibility(View.GONE);
                    viewHolder.tvStatus.setVisibility(View.GONE);
                    viewHolder.tvStatus.setText(mContext.getString(R.string.saler_04));



                }


                //准备交换合同
            } else if (viewHolder.resultBean.getOrder_money_status() == 2
                    && viewHolder.resultBean.getContract_apply_status() == 2
                    && viewHolder.resultBean.getBuy_money_status() == 2
                    && viewHolder.resultBean.getCancel_apply_status() == 0
                    && viewHolder.resultBean.getUpload_contract_status() == 2) {


                //查看定金按钮
                viewHolder.depositImageView.setVisibility(View.VISIBLE);


                viewHolder.saleRecorTv1.setVisibility(View.GONE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.saleRecorTv3.setVisibility(View.GONE);
                viewHolder.saleRecorTv4.setVisibility(View.GONE);
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_13));



                //请上传合同首页 ,请上传首付款凭证
            } else if (viewHolder.resultBean.getOrder_money_status() == 2
                    && viewHolder.resultBean.getCancel_apply_status() == 0) {


                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv3.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.tvStatus.setVisibility(View.GONE);
                //查看定金
                viewHolder.depositImageView.setVisibility(View.VISIBLE);

                if (viewHolder.resultBean.getBuy_money_upload_number() == 1) {
                    viewHolder.saleRecorTv4.setVisibility(View.GONE);

                }


            }


        }


        //订单已取消
        else if (viewHolder.resultBean.getStatus() == 10) {


            viewHolder.saleRecorTv1.setVisibility(View.GONE);
            viewHolder.saleRecorTv2.setVisibility(View.GONE);
            viewHolder.saleRecorTv3.setVisibility(View.GONE);
            viewHolder.saleRecorTv4.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.VISIBLE);
            //查看定金
            viewHolder.depositImageView.setVisibility(View.VISIBLE);


            if (viewHolder.resultBean.getOrder_money_status() == 3 && viewHolder.resultBean.getCancel_apply_status() == 0) {
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_21));
            } else {
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_02));
            }


        } //订单已完成
        else if (viewHolder.resultBean.getStatus() == 11) {


            viewHolder.saleRecorTv1.setVisibility(View.GONE);
            viewHolder.saleRecorTv2.setVisibility(View.GONE);
            viewHolder.saleRecorTv3.setVisibility(View.GONE);
            viewHolder.saleRecorTv4.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_14));
            //查看定金
            viewHolder.depositImageView.setVisibility(View.VISIBLE);


            //合同已交换
        } else if (viewHolder.resultBean.getStatus() == 13) {

            viewHolder.saleRecorTv1.setVisibility(View.GONE);
            viewHolder.saleRecorTv2.setVisibility(View.GONE);
            viewHolder.saleRecorTv3.setVisibility(View.GONE);
            viewHolder.saleRecorTv4.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(mContext.getString(R.string.exchanged));
            //查看定金按钮
            viewHolder.depositImageView.setVisibility(View.VISIBLE);

            //未满10%房款
        } else if (viewHolder.resultBean.getStatus() == 14) {
            //查看定金按钮
            viewHolder.depositImageView.setVisibility(View.VISIBLE);

            if (viewHolder.resultBean.getBuy_money_upload_number() == 1) {
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.saleRecorTv3.setVisibility(View.GONE);


                viewHolder.tvStatus.setText(mContext.getString(R.string.without_full_amount));

                if (viewHolder.resultBean.getBuy_money_status() == 0) {
                    viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.saleRecorTv4.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.saleRecorTv3.setVisibility(View.GONE);
                viewHolder.saleRecorTv4.setVisibility(View.GONE);
                viewHolder.tvStatus.setText(mContext.getString(R.string.without_full_amount));
            }


        }


        viewHolder.saleRecorTv1.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv2.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv3.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv4.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.depositImageView.setOnClickListener(new OnClick(viewHolder.resultBean));

        return convertView;


    }

    public class OnClick implements View.OnClickListener {
        private SaleOrderBean.ResultBean resultBean;
        private TextView imageView;

        public OnClick(SaleOrderBean.ResultBean resultBean) {
            this.resultBean = resultBean;

        }

        public OnClick(SaleOrderBean.ResultBean resultBean, TextView imageView) {
            this.resultBean = resultBean;
            this.imageView = imageView;

        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {


                //取消订单
                case R.id.saleRecorTv1:
                    new AlertDialog.Builder(mContext).setMessage(mContext.getResources().getString(R.string.error_)).setPositiveButton(mContext.getResources().getString(R.string.home_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaleRecordActivity.saleRecordActivity.canceOrder(resultBean.getProduct_orders_id());
                        }
                    }).setNegativeButton(mContext.getResources().getString(R.string.cancel), null).create().show();


                    break;
                //支付意向金
                case R.id.saleRecorTv2:
                    SaleRecordActivity.saleRecordActivity.payMoney(resultBean);
                    break;
                //上传合同首页
                case R.id.saleRecorTv3:

                    SaleRecordActivity.saleRecordActivity.askntractO(resultBean, "1");
                    break;
                //上传首付款凭证
                case R.id.saleRecorTv4:

                    SaleRecordActivity.saleRecordActivity.startPhotos(resultBean, "2");
                    break;

                //查看定金
                case depositImageView:
                    getDepositInfo(resultBean);
                    break;


            }

        }
    }

    /**
     * 获取定金消息
     * @param resultBean
     */
    private void getDepositInfo(SaleOrderBean.ResultBean resultBean) {
        EasyHttp.get(Contants.RECEIPT_INFO)
                .cacheMode(CacheMode.NO_CACHE)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("order_id", resultBean.getProduct_orders_id() + "")
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        SaleRecordActivity.saleRecordActivity.showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        SaleRecordActivity.saleRecordActivity.closeDialog();
                        ToasShow.showToast(mContext, mContext.getResources().getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        SaleRecordActivity.saleRecordActivity.closeDialog();

                        Gson gs = new Gson();

                        ReceiptBean receiptBean = gs.fromJson(json, ReceiptBean.class);
                        if (receiptBean.getCode().equals("1")) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("keys", receiptBean);
                            ActivitySkip.forward(SaleRecordActivity.saleRecordActivity, DepositActivity.class, bundle);
                        } else {
                            ToasShow.showToastCenter(mContext, receiptBean.getMsg());
                        }

                    }
                });
    }

    private HideViewListener mHideViewListener;

    public HideViewListener getmHideViewListener() {
        return mHideViewListener;
    }

    public void setmHideViewListener(HideViewListener mHideViewListener) {
        this.mHideViewListener = mHideViewListener;
    }

    public interface HideViewListener {
        void hideView(View view);
    }

    public class ViewHolder {
        public SaleOrderBean.ResultBean resultBean;
        private TextView tvSaleNum, tvSaleDes, tvSaleName, tvSalePrice, tvStatus, saleRecorTv1, saleRecorTv2, saleRecorTv3, saleRecorTv4,depositImageView;

    }
}
