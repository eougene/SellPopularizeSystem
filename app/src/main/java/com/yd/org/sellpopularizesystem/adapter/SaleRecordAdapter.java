package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.SaleRecordActivity;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

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

            Log.e("未取消", "id:" + viewHolder.resultBean.getProduct_orders_id());
            //意向金是否支付
            if (viewHolder.resultBean.getOrder_money_status() == 1) {


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
                    viewHolder.tvStatus.setText(mContext.getString(R.string.saler_04));
                    viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                    viewHolder.saleRecorTv2.setVisibility(View.VISIBLE);
                    viewHolder.saleRecorTv3.setVisibility(View.GONE);
                    viewHolder.saleRecorTv4.setVisibility(View.GONE);
                    viewHolder.tvStatus.setVisibility(View.GONE);


                }



//            }
//            //正在申请合同,等待管理员审核
//            else if (viewHolder.resultBean.getOrder_money_status() == 1
//                    && viewHolder.resultBean.getSales_advice_status() == 0
//                    && viewHolder.resultBean.getContract_apply_status() == 1
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_08));
//                viewHolder.saleRecorTv1.setVisibility(View.GONE);
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv3.setVisibility(View.GONE);
//                viewHolder.saleRecorTv4.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);


                //准备交换合同
            } else if (viewHolder.resultBean.getOrder_money_status() == 2
                    && viewHolder.resultBean.getContract_apply_status() == 2
                    && viewHolder.resultBean.getBuy_money_status() == 2
                    && viewHolder.resultBean.getCancel_apply_status() == 0
                    && viewHolder.resultBean.getUpload_contract_status() == 2) {


                viewHolder.saleRecorTv1.setVisibility(View.GONE);
                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.saleRecorTv3.setVisibility(View.GONE);
                viewHolder.saleRecorTv4.setVisibility(View.GONE);
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_13));


//            }
//
//            //合同首页已审核 , 首付款凭证审核中
//            else if (viewHolder.resultBean.getUpload_contract_status() == 2
//                    && viewHolder.resultBean.getBuy_money_status() == 1
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_05));
//
//                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
//                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
//
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv3.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);


                //请上传合同首页 ,请上传首付款凭证
            } else if (viewHolder.resultBean.getOrder_money_status() == 2
                    && (viewHolder.resultBean.getContract_apply_status() == 1 || viewHolder.resultBean.getContract_apply_status() == 2)
                    && (viewHolder.resultBean.getUpload_contract_status() == 0 || viewHolder.resultBean.getUpload_contract_status() == 1|| viewHolder.resultBean.getUpload_contract_status() == 3)
                    && (viewHolder.resultBean.getBuy_money_status() == 0 || viewHolder.resultBean.getBuy_money_status() == 1 || viewHolder.resultBean.getBuy_money_status() == 2)
                    && viewHolder.resultBean.getCancel_apply_status() == 0) {

                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_09));


                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv3.setVisibility(View.VISIBLE);
                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);

                viewHolder.saleRecorTv2.setVisibility(View.GONE);
                viewHolder.tvStatus.setVisibility(View.GONE);

//
//                //合同首页审核中,请上传首付款凭证
//            } else if (viewHolder.resultBean.getContract_apply_status() == 2
//                    && viewHolder.resultBean.getUpload_contract_status() == 1
//                    && viewHolder.resultBean.getBuy_money_status() == 0
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_10));
//
//
//                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
//
//                viewHolder.saleRecorTv1.setVisibility(View.GONE);
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv3.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);

//
//                // 合同首页已审核,请上传首付款凭证
//            } else if (viewHolder.resultBean.getOrder_money_status() == 2
//                    && viewHolder.resultBean.getContract_apply_status() == 2
//                    && viewHolder.resultBean.getUpload_contract_status() == 2
//                    && viewHolder.resultBean.getBuy_money_status() == 0
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_11));
//
//
//                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
//                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
//
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv3.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);


//                //合同首页已审核,首付款凭证审核中
//            } else if (viewHolder.resultBean.getOrder_money_status() == 2
//                    && viewHolder.resultBean.getContract_apply_status() == 2
//                    && viewHolder.resultBean.getUpload_contract_status() == 2
//                    && viewHolder.resultBean.getBuy_money_status() == 1
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_12));
//
//
//                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
//                viewHolder.saleRecorTv4.setVisibility(View.VISIBLE);
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv3.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);

//                //合同首页审核中,首付款凭证已审核
//            } else if (viewHolder.resultBean.getOrder_money_status() == 2
//                    && viewHolder.resultBean.getContract_apply_status() == 2
//                    && viewHolder.resultBean.getUpload_contract_status() == 1
//                    && viewHolder.resultBean.getBuy_money_status() == 2
//                    && viewHolder.resultBean.getCancel_apply_status() == 0) {
//
//                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_10_01));
//                viewHolder.saleRecorTv1.setVisibility(View.VISIBLE);
//                viewHolder.saleRecorTv3.setVisibility(View.VISIBLE);
//
//                viewHolder.saleRecorTv2.setVisibility(View.GONE);
//                viewHolder.saleRecorTv4.setVisibility(View.GONE);
//                viewHolder.tvStatus.setVisibility(View.GONE);
//
//
           }

            //订单已取消
        } else if (viewHolder.resultBean.getStatus() == 10) {


            viewHolder.saleRecorTv1.setVisibility(View.GONE);
            viewHolder.saleRecorTv2.setVisibility(View.GONE);
            viewHolder.saleRecorTv3.setVisibility(View.GONE);
            viewHolder.saleRecorTv4.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.VISIBLE);


            //订单放弃中
            if (viewHolder.resultBean.getCancel_apply_status() == 2) {
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_01));
                //订单已取消
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


        }


        viewHolder.saleRecorTv1.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv2.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv3.setOnClickListener(new OnClick(viewHolder.resultBean));
        viewHolder.saleRecorTv4.setOnClickListener(new OnClick(viewHolder.resultBean));

        return convertView;


    }

    public class OnClick implements View.OnClickListener {
        private SaleOrderBean.ResultBean resultBean;

        public OnClick(SaleOrderBean.ResultBean resultBean) {
            this.resultBean = resultBean;

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {


                //取消订单
                case R.id.saleRecorTv1:
                    SaleRecordActivity.saleRecordActivity.canceOrder(resultBean.getProduct_orders_id());
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


            }

        }
    }

    public class ViewHolder {
        public SaleOrderBean.ResultBean resultBean;
        private TextView tvSaleNum, tvSaleDes, tvSaleName, tvSalePrice, tvStatus, saleRecorTv1, saleRecorTv2, saleRecorTv3, saleRecorTv4;

    }
}
