package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.ReferUserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e-dot on 2017/8/29.
 */

public class MySalesAdapter extends BaseAdapter implements SectionIndexer {
    private List<ReferUserBean.ResultBean> list = new ArrayList<>();
    private Context mContext;


    public MySalesAdapter(Context mContext) {
        this.mContext = mContext;

    }


    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ReferUserBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<ReferUserBean.ResultBean> list) {
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addMore(List<ReferUserBean.ResultBean> datas) {
        this.list.addAll(datas);
        notifyDataSetChanged();
    }

    public int getCount() {

        return this.list.size();

    }

    public Object getItem(int position) {

        return list.get(position);

    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.customName);
            viewHolder.tvNickName = (TextView) view.findViewById(R.id.tvNickName);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.customImageiView);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();

        }

        viewHolder.resultBean = list.get(position);
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);


        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(viewHolder.resultBean.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(list.get(position).getSurname()+" "+list.get(position).getFirst_name());

        return view;

    }

    public class ViewHolder {
        TextView tvLetter;
        TextView tvTitle, tvNickName;
        ImageView imageView;
        public ReferUserBean.ResultBean resultBean;


    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);

    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        String sortStr;
        for (int i = 0; i < getCount(); i++) {
            sortStr = list.get(i).getSortLetters();

            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
