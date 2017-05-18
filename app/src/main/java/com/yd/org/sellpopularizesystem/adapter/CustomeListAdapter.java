package com.yd.org.sellpopularizesystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.opengl.GLException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.ProductSubunitListActivity;
import com.yd.org.sellpopularizesystem.activity.ProductItemDetailActivity;
import com.yd.org.sellpopularizesystem.activity.ScaleActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProSubUnitClassifyBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.utils.ACache;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/3/28.
 */

public class CustomeListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ProductListBean.ResultBean> list = new ArrayList<>();// 数据
    private LayoutInflater inflater;
    private ViewHolder holder;
    private List tempChilds = new ArrayList<>();
    private String str, mTempStr;
    private int pos;

    public CustomeListAdapter(Activity mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 新增数据并刷新数据
     *
     * @param list
     */
    public void addData(List<ProductListBean.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        pos = position;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            holder = viewHolder;
            convertView = inflater.inflate(R.layout.list_item_layout, null);
            viewHolder.prductImageView = (ImageView) convertView.findViewById(R.id.prductImageView);
            viewHolder.ivIslock = (ImageView) convertView.findViewById(R.id.ivIslock);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.lvSubItem = (ListView) convertView.findViewById(R.id.lvSubItem);
            viewHolder.rlViewAll = (RelativeLayout) convertView.findViewById(R.id.rlViewAll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.productListBean = list.get(position);
        //Picasso.with(mContext).load(Contants.DOMAIN + "/" + list.get(position).getThumb()).into(viewHolder.prductImageView);
        final ViewHolder finalViewHolder1 = viewHolder;
        if (!MyUtils.getInstance().isNetworkConnected(mContext)){
            if (ScaleActivity.scaleActivity.aCache.getAsBitmap(list.get(pos).getThumb())!=null){
                viewHolder.prductImageView.setImageBitmap(ScaleActivity.scaleActivity.aCache.getAsBitmap(list.get(pos).getThumb()));
            }else {
                ToasShow.showToastCenter(mContext,"当前处于无网络状态");
            }
        }

        Picasso.with(mContext).load(Contants.DOMAIN + "/" + list.get(position).getThumb()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                finalViewHolder1.prductImageView.setImageBitmap(bitmap);
                if (ScaleActivity.scaleActivity.aCache.getAsBitmap(list.get(pos).getThumb())==null){
                    ScaleActivity.scaleActivity.aCache.put(list.get(pos).getThumb(),bitmap,ACache.TIME_HOUR);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        if (list.get(position).getIs_study()==1){
            viewHolder.ivIslock.setVisibility(View.VISIBLE);

        }
        final String title = list.get(position).getProduct_name().trim();
        final String product_id = list.get(position).getProduct_id() + "";
        viewHolder.productName.setText(list.get(position).getProduct_name().trim());
        viewHolder.childs = list.get(position).getChilds();
        /*tempChilds.clear();
        tempChilds.addAll(viewHolder.childs);*/
        if (list.get(position).getIs_study()==1){
            viewHolder.lvSubItem.setVisibility(View.GONE);
            viewHolder.rlViewAll.setVisibility(View.GONE);
        }else {
            viewHolder.lvSubItem.setAdapter(new ItemAdapter(mContext, viewHolder.childs));
            final ViewHolder finalViewHolder = viewHolder;
            //点击查看单个item
            viewHolder.prductImageView.setOnClickListener(new MyOnClick(viewHolder.productListBean, ProductItemDetailActivity.class, title, product_id));
            //点击查看所有
            viewHolder.rlViewAll.setOnClickListener(new MyOnClick(viewHolder.childs, ProductSubunitListActivity.class, title, product_id));
            //产品子单元listview点击
            viewHolder.lvSubItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                    //Log.e("position***", "position:" + positions);
                    ScaleActivity.scaleActivity.goTo(finalViewHolder.childs.get(positions), ProductSubunitListActivity.class, title, product_id);
                }
            });
        }
        return convertView;
    }

    private class MyOnClick implements View.OnClickListener {
        private Object mObject;
        private Class<?> mClass;
        private String title;
        private String productId;

        public MyOnClick(Object mObject, Class<?> mClass, String title, String productId) {
            this.mObject = mObject;
            this.mClass = mClass;
            this.title = title;
            this.productId = productId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prductImageView:
                    ScaleActivity.scaleActivity.goTo(mObject, mClass, title, productId);
                    //Log.e("str", "onClick: " + title);
                    break;
                case R.id.rlViewAll:
                    ScaleActivity.scaleActivity.goTo(mObject, mClass, title, productId);
                    //Log.e("str", "onClick** " + title);
                    break;

            }
        }
    }

    class ViewHolder {
        private ImageView prductImageView;
        private ImageView ivIslock;
        private TextView productName, productDescription;
        private ListView lvSubItem;
        private RelativeLayout rlViewAll;
        public ProductListBean.ResultBean productListBean;
        public List<ProSubUnitClassifyBean> childs = new ArrayList();
    }

    class ItemAdapter extends BaseAdapter {
        private Context mItemContext;
        private List<ProSubUnitClassifyBean> childs = new ArrayList<>();
        private LayoutInflater inflater;

        public ItemAdapter(Context mContext, List<ProSubUnitClassifyBean> childs) {
            this.mItemContext = mContext;
            this.childs = childs;
            this.inflater = LayoutInflater.from(mItemContext);
        }

        @Override
        public int getCount() {
            //Log.e("TAG", "getCount: " + childs.size());
            return childs.size();
        }

        @Override
        public ProSubUnitClassifyBean getItem(int position) {
            return childs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder1 holder1 = null;
            if (convertView == null) {
                holder1 = new Holder1();
                convertView = inflater.inflate(R.layout.product_listview_item_sublay, null);
                holder1.tvPriceRange = (TextView) convertView.findViewById(R.id.tvPriceRange);
                holder1.tvHouse = (TextView) convertView.findViewById(R.id.tvHouse);
                holder1.tvBathroom = (TextView) convertView.findViewById(R.id.tvBathroom);
                holder1.tvCar = (TextView) convertView.findViewById(R.id.tvCar);
                convertView.setTag(holder1);
            } else {
                holder1 = (Holder1) convertView.getTag();
            }
            holder1.childBean = childs.get(position);
            //Log.e("TAG", "getView: " + holder1.childBean.getBathroom());
            holder1.tvPriceRange.setText("$" + mContext.getString(R.string.single_blank_space) +
                    MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(holder1.childBean.getMin_price()))/1000).split("\\.")[0]) + "k"
                    + mContext.getString(R.string.single_blank_space) + "to" + mContext.getString(R.string.single_blank_space) + "$"
                    + mContext.getString(R.string.single_blank_space) + MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(holder1.childBean.getMax_price()))/1000).split("\\.")[0]) + "k");
            holder1.tvHouse.setText(holder1.childBean.getBedroom());
            holder1.tvBathroom.setText(holder1.childBean.getBathroom());
            holder1.tvCar.setText(holder1.childBean.getCar_space());
            return convertView;
        }

        class Holder1 {
            private TextView tvPriceRange, tvHouse, tvBathroom, tvCar;
            public ProSubUnitClassifyBean childBean;

        }
    }

}
