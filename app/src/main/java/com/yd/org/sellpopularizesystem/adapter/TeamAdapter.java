package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.Lawyer;
import com.yd.org.sellpopularizesystem.javaBean.PagerDetailsBean;
import com.yd.org.sellpopularizesystem.javaBean.TeamBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/5/17.
 */

public class TeamAdapter extends BaseAdapter implements SectionIndexer {
    private LayoutInflater layoutInflater;
    private List<TeamBean.ResultBean.SubBeanX.SubBean> tasks;
    private int[] sectionIndices;
    private String[] sectionHeaders;
    private Context mContext;

    public TeamAdapter(Context context, List<TeamBean.ResultBean.SubBeanX.SubBean> tasks) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.tasks = tasks;
        sectionIndices = getSectionIndices();
        sectionHeaders = getSectionHeaders();
    }

    public void refresh(List<TeamBean.ResultBean.SubBeanX.SubBean> tasks) {
        this.tasks = tasks;
        sectionIndices = getSectionIndices();
        sectionHeaders = getSectionHeaders();
        notifyDataSetChanged();
    }

    public int[] getSectionIndices() {
        List<Integer> sectionIndices = new ArrayList<Integer>();
        sectionIndices.add(1);
        int count = 0;
        for (int i = 1; i < tasks.size(); i++) {
            //count += tasks.get(i - 1).getSub();
            sectionIndices.add(count);
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    public String[] getSectionHeaders() {
        String[] sectionHeaders = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            sectionHeaders[i] = tasks.get(i).getSurname() + mContext.getString(R.string.single_blank_space) + tasks.get(i).getFirstname();
        }
        return sectionHeaders;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<TeamBean.ResultBean.SubBeanX.SubBean> list) {


        if (list == null) {
            this.tasks = new ArrayList<>();
        } else {
            this.tasks = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.lawyer_listview_item, null);
            viewHolder.tvTeamName = (TextView) convertView.findViewById(R.id.law_firm);
            viewHolder.tvTeamMemberName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int sec = getSectionForPosition(position);
        if (position == getPositionForSection(sec)) {
            viewHolder.tvTeamName.setVisibility(View.VISIBLE);
            //viewHolder.tvTeamMemberName.setText(tasks.get(position).get);
        } else {
            viewHolder.tvTeamName.setVisibility(View.GONE);
        }
        viewHolder.tvTeamMemberName.setText(tasks.get(position).getSurname() + tasks.get(position).getFirstname());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    class ViewHolder {
        TextView tvTeamName;
        TextView tvTeamMemberName;
    }
}
