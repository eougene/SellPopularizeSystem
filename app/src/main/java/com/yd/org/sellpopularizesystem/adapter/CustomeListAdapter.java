package com.yd.org.sellpopularizesystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.LearningGardenActivity;
import com.yd.org.sellpopularizesystem.activity.ProductItemDetailActivity;
import com.yd.org.sellpopularizesystem.activity.ProductSubunitListActivity;
import com.yd.org.sellpopularizesystem.activity.ScaleActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProSubUnitClassifyBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/3/28.
 */

public class CustomeListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ProductListBean.ResultBean> list = new ArrayList<>();// 数据
    private LayoutInflater inflater;
    private boolean isBoolean;

    public CustomeListAdapter(Activity mContext, boolean isBoolean) {
        this.mContext = mContext;
        this.isBoolean = isBoolean;
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_layout, null);
            viewHolder.prductImageView = (ImageView) convertView.findViewById(R.id.prductImageView);
            viewHolder.ivLockImageView = (ImageView) convertView.findViewById(R.id.ivIslock);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.lvSubItem = (ListView) convertView.findViewById(R.id.lvSubItem);
            viewHolder.rlViewAll = (RelativeLayout) convertView.findViewById(R.id.rlViewAll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.productListBean = list.get(position);
        Picasso.with(mContext).load(Contants.DOMAIN + "/" + list.get(position).getThumb()).into(viewHolder.prductImageView);

        // if (!MyUtils.getInstance().isNetworkConnected(mContext)) {
//            if (BaseApplication.getInstance().getaCache().getAsBitmap(list.get(position).getThumb()) != null) {
//                viewHolder.prductImageView.setImageBitmap(BaseApplication.getInstance().getaCache().getAsBitmap(list.get(position).getThumb()));
//            } else {
//                ToasShow.showToastCenter(mContext, mContext.getString(R.string.network_error));
//            }
        // } else {
        //
        // }

        final String title = list.get(position).getProduct_name().trim();
        final String product_id = list.get(position).getProduct_id() + "";
        viewHolder.productName.setText(list.get(position).getProduct_name().trim());
        viewHolder.childs = list.get(position).getChilds();
        /*tempChilds.clear();
        tempChilds.addAll(viewHolder.childs);*/
        viewHolder.lvSubItem.setAdapter(new ItemAdapter(mContext, viewHolder.childs));
        final ViewHolder finalViewHolder = viewHolder;
        if (list.get(position).getIs_study() == 0) {
            //点击查看单个item
            viewHolder.prductImageView.setOnClickListener(new MyOnClick(viewHolder.productListBean, ProductItemDetailActivity.class, title, product_id));
            //点击查看所有
            viewHolder.rlViewAll.setOnClickListener(new MyOnClick(viewHolder.childs, ProductSubunitListActivity.class, title, product_id));
            //产品子单元listview点击
            viewHolder.lvSubItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                    ScaleActivity.scaleActivity.goTo(finalViewHolder.childs.get(positions), ProductSubunitListActivity.class, title, product_id);
                }
            });
        } else {
            viewHolder.ivLockImageView.setVisibility(View.VISIBLE);
            viewHolder.lvSubItem.setVisibility(View.GONE);
            viewHolder.rlViewAll.setVisibility(View.GONE);
            viewHolder.prductImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("studynum", "0");
                    ActivitySkip.forward((Activity) mContext, LearningGardenActivity.class, bundle);
                }
            });
        }
        return convertView;
    }

    /**
     * 自定义接口
     */
//    public class CropSquareTransformation implements Transformation {
//        private int position;
//
//        public CropSquareTransformation(int position) {
//            this.position = position;
//
//        }
//
//        @Override
//        public Bitmap transform(Bitmap source) {
//            if (isBoolean && BaseApplication.getInstance().getaCache().getAsBitmap(list.get(position).getThumb()) == null) {
//                BaseApplication.getInstance().getaCache().put(list.get(position).getThumb(), source, ACache.TIME_HOUR);
//            }
//            return source;
//        }
//
//        @Override
//        public String key() {
//            return "square()";
//        }
//    }

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
                    break;
                case R.id.rlViewAll:
                    ScaleActivity.scaleActivity.goTo(mObject, mClass, title, productId);
                    break;

            }
        }
    }


    public class ViewHolder {
        private ImageView prductImageView, ivLockImageView;
        private TextView productName;
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
            holder1.tvPriceRange.setText("$" + mContext.getString(R.string.single_blank_space) +
                    MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(holder1.childBean.getMin_price())) / 1000).split("\\.")[0]) + "k"
                    + mContext.getString(R.string.single_blank_space) + "to" + mContext.getString(R.string.single_blank_space) + "$"
                    + mContext.getString(R.string.single_blank_space) + MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(holder1.childBean.getMax_price())) / 1000).split("\\.")[0]) + "k");
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
