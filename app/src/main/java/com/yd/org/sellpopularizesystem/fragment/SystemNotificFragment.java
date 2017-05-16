package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.InformationContentActivity;
import com.yd.org.sellpopularizesystem.adapter.NotificationAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.AnnouncementBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e-dot on 2017/5/8.
 */

public class SystemNotificFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener {
    public static SystemNotificFragment notificFragment;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private List<AnnouncementBean.ResultBean> informationContents = new ArrayList<>();
    private NotificationAdapter adapter;
    private int page = 1;
    private int cate_id;
    private boolean isShow = true;
    private int type = 0;


    public Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {


                //全选
                case 0:

                    //全选,全不选
                    if (msg.arg1 == 0) {
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < informationContents.size(); i++) {
                            adapter.getIsSelected().put(i, true);
                        }

                    } else {
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < informationContents.size(); i++) {
                            adapter.getIsSelected().put(i, false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;


                //编辑,取消
                case 1:

                    Log.e("编辑**", "JJ:" + msg.obj + "type:" + msg.arg2);
                    isShow = (boolean) msg.obj;
                    type = msg.arg2;
                    //还原数据,默认全不选
                    if (null != adapter) {
                        adapter.setsShowI(isShow);
                        for (int i = 0; i < informationContents.size(); i++) {
                            adapter.getIsSelected().put(i, false);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    break;

                //删除
                case 2:

                    // deleteNoticeLog();
                    break;
            }
        }
    };


    public static SystemNotificFragment getInstnce(int cate_id) {
        SystemNotificFragment notificFragmen = new SystemNotificFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cate_id", cate_id);
        notificFragmen.setArguments(bundle);
        return notificFragmen;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        notificFragment = this;
        setContentView(R.layout.fragment_notific);
        initViews();
    }

    private void initViews() {
        cate_id = getArguments().getInt("cate_id");
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        //获取数据
        getData(1, true, cate_id);
    }

    private void getData(int pages, final boolean isRefresh, final int cate_id) {
        showLoadingDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", pages + "");
        ajaxParams.put("cate_id", cate_id + "");
        ajaxParams.put("number", "10");
        fh.get(Contants.SYSTEM_ANNOUNCEMENT, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                dismissLoadingDialog();
                if (null != s) {
                    HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);
                    jsonParse(s, isRefresh, cate_id);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissLoadingDialog();
            }
        });
    }


    private void jsonParse(String s, boolean isRefresh, int cate_id) {
        Gson gson = new Gson();
        AnnouncementBean bean = gson.fromJson(s, AnnouncementBean.class);
        if (bean.getCode().equals("1")) {
            informationContents = bean.getResult();

        }

        if (bean.getTotal_number() > 0) {


            if (cate_id == 16) {
                Message message = new Message();
                message.what = 3;
                message.obj = String.valueOf(bean.getTotal_number());
                NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(0);
                NotificationFragment.notificationFragment.mhandler.sendMessage(message);
            }
        }

        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            adapter = new NotificationAdapter(getActivity());
            listView.setAdapter(adapter);

        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        adapter.addMore(informationContents);
    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnnouncementBean.ResultBean resultBean = (AnnouncementBean.ResultBean) adapter.getItem(position);


                if (type == 1) {
                    // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                    NotificationAdapter.ViewHoler holder = (NotificationAdapter.ViewHoler) view.getTag();
                    // 改变CheckBox的状态
                    holder.check_box.toggle();
                    // 将CheckBox的选中状况记录下来
                    adapter.getIsSelected().put(position, holder.check_box.isChecked());
                } else {
                    //预定推送消息
                    if (resultBean.getCate_id() == cate_id) {
                        deleteNoticeLog(resultBean.getId() + "", position);
                        //系统消息查看详情
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", resultBean.getTitle());
                        bundle.putString("notice_id", resultBean.getId() + "");
                        bundle.putString("data", resultBean.getContent());
                        ActivitySkip.forward(getActivity(), InformationContentActivity.class, bundle);
                    }
                }
            }


        });

    }

    /**
     * 删除预定通知
     *
     * @param notice_id
     * @param postion
     */
    private void deleteNoticeLog(String notice_id, final int postion) {
        showLoadingDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        // ajaxParams.put("notice_id", notice_id);
        finalHttp.get(Contants.DELETE_NOTICE, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("失败", "errorNo:" + errorNo);
                dismissLoadingDialog();

            }

            @Override
            public void onSuccess(String s) {
                dismissLoadingDialog();
                Gson g = new Gson();
                ErrorBean e = g.fromJson(s, ErrorBean.class);
                if (e.getCode().equals("1")) {
                    informationContents.remove(postion);
                    adapter.notifyDataSetChanged();
                    //发送消息数目
                    HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);

                }


            }
        });

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        getData(page, true, cate_id);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getData(page, false, cate_id);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
