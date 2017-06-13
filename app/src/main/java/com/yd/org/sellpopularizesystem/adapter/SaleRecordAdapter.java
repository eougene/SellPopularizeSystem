package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by e-dot on 2017/6/8.
 * 订单页面
 */

public class SaleRecordAdapter extends BaseAdapter {
    private int mCurrentItem = 0;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();

    public SaleRecordAdapter(Context mContext) {

        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public void setCurrentItem(int mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
        notifyDataSetChanged();
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
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvSaleName = (TextView) convertView.findViewById(R.id.tvSaleName);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        //实体类
        viewHolder.resultBean = sobRbData.get(position);
        //ID
        viewHolder.tvSaleNum.setText(viewHolder.resultBean.getProduct_orders_id() + "");
        //价格
        viewHolder.tvSalePrice.setText("$" + " " + MyUtils.getInstance().addComma(viewHolder.resultBean.getPrice()));
        //姓名
        viewHolder.tvSaleName.setText(viewHolder.resultBean.getCustomer_surname() + mContext.getString(R.string.single_blank_space) + viewHolder.resultBean.getCustomer_first_name());

        //说明
        if (null != viewHolder.resultBean.getProduct_name() && !TextUtils.isEmpty(viewHolder.resultBean.getProduct_name() + "")) {
            viewHolder.tvSaleDes.setText(viewHolder.resultBean.getProduct_name().getProduct_name() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_lot_number() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_unit_number());
        } else {
            viewHolder.tvSaleDes.setText("Null" + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_lot_number() + "-" + viewHolder.resultBean.getProduct_info().getProduct_childs_unit_number());
        }


        if (mCurrentItem == viewHolder.resultBean.getProduct_orders_id()) {
            viewHolder.tvStatus.setBackgroundColor(getColor(mContext, R.color.transparent));
            convertView.setBackgroundColor(getColor(mContext, R.color.home_scale));
        } else {
            viewHolder.tvStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.home_scale));
            convertView.setBackground(null);
        }


        //正在申请合同,等待管理员审核
        if (viewHolder.resultBean.getOrder_money_status() == 1
                && viewHolder.resultBean.getSales_advice_status() == 0
                && viewHolder.resultBean.getContract_apply_status() == 1
                && viewHolder.resultBean.getStatus() == 0
                && viewHolder.resultBean.getCancel_apply_status() == 0) {

            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_08));


        }
        //意向金凭证已上传,,,请申请合同
        else if (viewHolder.resultBean.getOrder_money_status() == 1
                && viewHolder.resultBean.getCancel_apply_status() == 0
                && viewHolder.resultBean.getSale_advice_status() == 0
                && viewHolder.resultBean.getStatus() == 0) {

            if (viewHolder.resultBean.getPayment_method() == 1 || viewHolder.resultBean.getPayment_method() == 4) {
                //意向金已支付
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_03));
            } else {
                viewHolder.tvStatus.setText(mContext.getString(R.string.saler_04));
            }


            //订单放弃中
        } else if ((viewHolder.resultBean.getOrder_money_status() == 1 || viewHolder.resultBean.getOrder_money_status() == 2)
                && viewHolder.resultBean.getCancel_apply_status() == 1
                && viewHolder.resultBean.getStatus() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_01));

            //订单已取消
        } else if (viewHolder.resultBean.getOrder_money_status() == 1
                && viewHolder.resultBean.getCancel_apply_status() == 2
                && viewHolder.resultBean.getStatus() == 10) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_02));


            //准备交换合同
        } else if (viewHolder.resultBean.getOrder_money_status() == 2
                && viewHolder.resultBean.getContract_apply_status() == 2
                && viewHolder.resultBean.getBuy_money_status() == 2
                && viewHolder.resultBean.getCancel_apply_status() == 0
                && viewHolder.resultBean.getUpload_contract_status() == 2
                && viewHolder.resultBean.getStatus() == 0) {

            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_13));


            //合同首页已审核 , 首付款凭证审核中
        } else if (viewHolder.resultBean.getUpload_contract_status() == 2
                && viewHolder.resultBean.getBuy_money_status() == 1
                && viewHolder.resultBean.getCancel_apply_status() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_05));


            //请上传合同首页 ,请上传首付款凭证
        } else if (viewHolder.resultBean.getContract_apply_status() == 2
                && viewHolder.resultBean.getUpload_contract_status() == 0
                && viewHolder.resultBean.getBuy_money_status() == 0
                && viewHolder.resultBean.getCancel_apply_status() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_09));

            //合同首页审核中,请上传首付款凭证
        } else if (viewHolder.resultBean.getContract_apply_status() == 2
                && viewHolder.resultBean.getUpload_contract_status() == 1
                && viewHolder.resultBean.getBuy_money_status() == 0
                && viewHolder.resultBean.getCancel_apply_status() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_10));

            // 合同首页已审核,请上传首付款凭证
        } else if (viewHolder.resultBean.getContract_apply_status() == 2
                && viewHolder.resultBean.getUpload_contract_status() == 2
                && viewHolder.resultBean.getBuy_money_status() == 0
                && viewHolder.resultBean.getCancel_apply_status() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_11));

            //合同首页已审核,首付款凭证审核中
        } else if (viewHolder.resultBean.getContract_apply_status() == 2
                && viewHolder.resultBean.getUpload_contract_status() == 2
                && viewHolder.resultBean.getBuy_money_status() == 1
                && viewHolder.resultBean.getCancel_apply_status() == 0) {
            viewHolder.tvStatus.setText(mContext.getString(R.string.saler_12));
        }

        return convertView;


    }

    public class ViewHolder {
        public SaleOrderBean.ResultBean resultBean;
        private TextView tvSaleNum, tvSaleDes, tvSaleName, tvSalePrice, tvStatus;

    }
}
