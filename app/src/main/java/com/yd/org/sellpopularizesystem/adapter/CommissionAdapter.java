package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.CommissionBean;
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
            viewHolder.dataCommission = (TextView) convertView.findViewById(R.id.dataCommission);
            viewHolder.sumCommission = (TextView) convertView.findViewById(R.id.sumCommission);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHoler) convertView.getTag();
        }


        viewHolder.resultBean = datas.get(position);
        viewHolder.commissionID.setText(datas.get(position).getOrder_id() + "");
        viewHolder.titleCommission.setText(datas.get(position).getProduct_name() + " - " + datas.get(position).getProduct_childs_unit_number());
        viewHolder.dataCommission.setText(MyUtils.getInstance().date2String("yyyy/MM/dd HH:mm", Long.parseLong(datas.get(position).getAdd_time() + "000")));
        viewHolder.sumCommission.setText(datas.get(position).getTotal() + "");


        return convertView;
    }

    public class ViewHoler {
        private TextView commissionID,
                titleCommission,
                dataCommission, sumCommission;

        public CommissionBean.ResultBean resultBean;

    }
}
