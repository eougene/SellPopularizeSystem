package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.CommissionActivity;
import com.yd.org.sellpopularizesystem.activity.OrderDetailActivity;
import com.yd.org.sellpopularizesystem.javaBean.CommissionBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e-dot on 2017/5/2.
 */

public class CommissionAdapter extends BaseAdapter {
    private List<CommissionBean.ResultBean> datas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayou;
    private int temp = 0;

    public CommissionAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLayou = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public void addMore(List<CommissionBean.ResultBean> data) {
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHoler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHoler();
            convertView = mLayou.inflate(R.layout.commission_item, null);
            viewHolder.commissionID = (TextView) convertView.findViewById(R.id.commissionID);
            viewHolder.titleCommission = (TextView) convertView.findViewById(R.id.titleCommission);
            viewHolder.sumCommission = (TextView) convertView.findViewById(R.id.sumCommission);

            viewHolder.commissionRel = (RelativeLayout) convertView.findViewById(R.id.commissionRel);
            viewHolder.commissionLinear = (LinearLayout) convertView.findViewById(R.id.commissionLinear);


            viewHolder.firstCommissionSum = (TextView) convertView.findViewById(R.id.firstCommissionSum);
            viewHolder.firstCommissionDate = (TextView) convertView.findViewById(R.id.firstCommissionDate);
            viewHolder.secondCommissionSum = (TextView) convertView.findViewById(R.id.secondCommissionSum);
            viewHolder.secondCommissionDate = (TextView) convertView.findViewById(R.id.secondCommissionDate);
            viewHolder.thirdCommissionSum = (TextView) convertView.findViewById(R.id.thirdCommissionSum);
            viewHolder.thirdCommissionDate = (TextView) convertView.findViewById(R.id.thirdCommissionDate);


            viewHolder.commissionRightImageView = (ImageView) convertView.findViewById(R.id.commissionRightImageView);
            viewHolder.moreImageView = (ImageView) convertView.findViewById(R.id.moreImageView);


            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHoler) convertView.getTag();
        }


        viewHolder.resultBean = datas.get(position);
        viewHolder.commissionID.setText(mContext.getString(R.string.order_id) + ":" + datas.get(position).getOrder_id() + "");
        viewHolder.titleCommission.setText(datas.get(position).getProduct_name() + " - " + datas.get(position).getProduct_childs_unit_number());
        viewHolder.sumCommission.setText(datas.get(position).getTotal() + "");


        //佣金1
        viewHolder.firstCommissionSum.setText(viewHolder.resultBean.getFirst_commossion());
        viewHolder.firstCommissionDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(viewHolder.resultBean.getFirst_time() + "000")));

        //佣金2
        viewHolder.secondCommissionSum.setText(viewHolder.resultBean.getSecond_commossion());
        viewHolder.secondCommissionDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(viewHolder.resultBean.getSecond_time() + "000")));

        //佣金3
        viewHolder.thirdCommissionSum.setText(viewHolder.resultBean.getThird_commossion());
        viewHolder.thirdCommissionDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(viewHolder.resultBean.getThird_time() + "000")));


        viewHolder.commissionRel.setOnClickListener(new OnClick(viewHolder.resultBean, viewHolder.moreImageView, viewHolder.commissionLinear));
        viewHolder.commissionRightImageView.setOnClickListener(new OnClick(viewHolder.resultBean, viewHolder.moreImageView, viewHolder.commissionLinear));


        return convertView;
    }

    class OnClick implements View.OnClickListener {
        private ImageView imageView;
        private LinearLayout linearLayout;
        private CommissionBean.ResultBean resultBean;


        public OnClick(CommissionBean.ResultBean resultBean, ImageView imageView, LinearLayout linearLayout) {
            this.imageView = imageView;
            this.linearLayout = linearLayout;
            this.resultBean = resultBean;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {


                //右边按钮事件
                case R.id.commissionRightImageView:

                    SaleOrderBean.ResultBean bean = new SaleOrderBean.ResultBean();
                    bean.setProduct_orders_id(resultBean.getOrder_id());


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", bean);
                    ActivitySkip.forward(CommissionActivity.commissionActivity, OrderDetailActivity.class, bundle);
                    break;


                //查看更多
                case R.id.commissionRel:

                    if (temp == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.mipmap.more_top);
                        temp = 1;
                    } else {
                        linearLayout.setVisibility(View.GONE);
                        imageView.setImageResource(R.mipmap.more);
                        temp = 0;
                    }


                    break;
            }
        }
    }

    public class ViewHoler {
        private TextView commissionID,
                titleCommission, sumCommission, firstCommissionSum, firstCommissionDate, secondCommissionSum, secondCommissionDate, thirdCommissionSum, thirdCommissionDate;
        private RelativeLayout commissionRel;
        private LinearLayout commissionLinear;
        private ImageView commissionRightImageView, moreImageView;
        public CommissionBean.ResultBean resultBean;

    }
}
