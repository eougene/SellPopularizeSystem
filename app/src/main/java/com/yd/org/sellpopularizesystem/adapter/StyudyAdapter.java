package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.StudyBean;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bai on 2017/1/19.
 */

public class StyudyAdapter extends BaseAdapter {

    protected List<StudyBean.ResultBean> list = new ArrayList<>();// 数据
    protected Context context;// 上下文
    protected LayoutInflater getInflater;// 加载布局


    public StyudyAdapter(Context context) {
        this.context = context;
        this.getInflater = LayoutInflater.from(context);
    }


    /**
     * 返回数据长度
     *
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }


    /**
     * 新增数据并刷新数据
     *
     * @param list
     */
    public void addData(List<StudyBean.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 返回项
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * 返回ID
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder = null;

        if (convertView == null) {
            holder = new HolderView();
            convertView = getInflater.inflate(R.layout.study_item, null);
            holder.studyDisImageView = (ImageView) convertView.findViewById(R.id.studyDisImageView);
            holder.lockImageView = (ImageView) convertView.findViewById(R.id.lockImageView);
            holder.studyTextView = (TextView) convertView.findViewById(R.id.studyTextView);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }


        holder.productListBean = list.get(position);


        BitmapUtil.loadImageView(context,Contants.DOMAIN + "/" + list.get(position).getThumb(),holder.studyDisImageView);

        holder.studyTextView.setText(list.get(position).getStudy_title());
        holder.descriptionTextView.setText(list.get(position).getDescription());
        if (list.get(position).getCan_study() == 1) {
           holder.lockImageView.setVisibility(View.GONE);
       } else {
           holder.lockImageView.setVisibility(View.VISIBLE);
       }


        return convertView;
    }

    public class HolderView {
        private ImageView studyDisImageView, lockImageView;
        private TextView studyTextView, descriptionTextView;
        public StudyBean.ResultBean productListBean;

    }
}
