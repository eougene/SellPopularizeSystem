package com.yd.org.sellpopularizesystem.myView.nestfulllistview;

import java.util.List;

/**
 * 完全伸展开的ListView的适配器
 */

public abstract class NestFullListViewAdapter<T> {
    private int mItemLayoutId;
    private List<T> mDatas;//数据源

    public NestFullListViewAdapter(int mItemLayoutId, List<T> mDatas) {
        this.mItemLayoutId = mItemLayoutId;
        this.mDatas = mDatas;
    }

    /**
     * 被FullListView调用
     *
     * @param i
     * @param holder
     */
    public void onBind(int i, NestFullViewHolder holder) {
        //回调bind方法，多传一个data过去
        onBind(i, mDatas.get(i), holder);
    }

    /**
     * 数据绑定方法
     *
     * @param pos    位置
     * @param t      数据
     * @param holder ItemView的ViewHolder
     */
    public abstract void onBind(int pos, T t, NestFullViewHolder holder);

    public int getItemLayoutId() {
        return mItemLayoutId;
    }

    public void setItemLayoutId(int mItemLayoutId) {
        this.mItemLayoutId = mItemLayoutId;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

}
