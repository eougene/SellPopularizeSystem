package com.yd.org.sellpopularizesystem.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.SaleRecordActivity;
import com.yd.org.sellpopularizesystem.adapter.NotificationAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.AnnouncementBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

import static com.yd.org.sellpopularizesystem.adapter.NotificationAdapter.getIsSelected;


public class OrderNotificFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener {
    public static OrderNotificFragment notificFragment;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private List<AnnouncementBean.ResultBean> informationContents = new ArrayList<>();
    private NotificationAdapter adapter;
    private int page = 1;
    private int cate_id;
    private boolean isShow = true;
    private int type = 0;
    private AnnouncementBean.ResultBean resultBean;
    private int pos;
    private int flag;
    public static int size;

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
                            getIsSelected().put(i, true);
                        }

                    } else {
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < informationContents.size(); i++) {
                            getIsSelected().put(i, false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;


                //编辑,取消
                case 1:

                    Log.e("编辑**", "J:" + msg.obj + "type:" + msg.arg2);
                    isShow = (boolean) msg.obj;
                    type = msg.arg2;
                    //还原数据,默认全不选
                    if (null != adapter) {
                        adapter.setsShowI(isShow);
                        for (int i = 0; i < informationContents.size(); i++) {
                            getIsSelected().put(i, false);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    break;

                //删除
                case 2:
                    type=msg.arg2;
                    deleteNoticeLog(isSelected());
                    break;

                //动态改变消息是否已读
                case ExtraName.UPDATE:

                    adapter.getInformationtents().get(pos).setIs_read(1);
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    };
    private int firstVisibleItemPos;

    public NotificationAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(NotificationAdapter adapter) {
        this.adapter = adapter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 创建实体类
     *
     * @param cate_id
     * @return
     */
    public static OrderNotificFragment getInstnce(int cate_id) {
        OrderNotificFragment notificFragmen = new OrderNotificFragment();
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
        ajaxParams.put("number", "50");
        fh.get(Contants.SYSTEM_ANNOUNCEMENT, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                dismissLoadingDialog();
                if (null != s) {
                    jsonParse(s, isRefresh, cate_id);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dismissLoadingDialog();
            }
        });
    }


    private void jsonParse(String s, boolean isRefresh, int cate_id) {
        Gson gson = new Gson();
        AnnouncementBean bean = gson.fromJson(s, AnnouncementBean.class);
        if (bean.getCode().equals("1")) {
            informationContents = bean.getResult();
            size=informationContents.size();
            if (informationContents.size()==0){
                Bundle bundle=new Bundle();
                bundle.putString("size","0");
                NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(ExtraName.NO_DATA);
            }else {
                NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(ExtraName.NORMAL_DATA);
            }
        }
        int is_read = 0;
        if (bean.getTotal_number() > 0) {
            if (cate_id == 4) {
                for (int i = 0; i < informationContents.size(); i++) {
                    if (informationContents.get(i).getIs_read() != 1) {
                        is_read += 1;
                    }
                }

                Message message = new Message();
                message.what = 0;
                message.arg1 = is_read;
                //通知首页加载消息数量
                HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);
                NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(0);
                NotificationFragment.notificationFragment.mhandler.sendMessage(message);


            }
        }

        if (isRefresh) {
            if (informationContents.size() == 0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            adapter = new NotificationAdapter(getActivity());
            listView.setAdapter(adapter);
            if (flag==1){
                listView.setSelection(firstVisibleItemPos);

            }

        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        adapter.addMore(informationContents);
    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                resultBean = (AnnouncementBean.ResultBean) adapter.getItem(position);

                if (type == 1) {
                    // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                    NotificationAdapter.ViewHoler holder = (NotificationAdapter.ViewHoler) view.getTag();
                    // 改变CheckBox的状态
                    holder.check_box.toggle();
                    // 将CheckBox的选中状况记录下来
                    getIsSelected().put(position, holder.check_box.isChecked());
                } else {
                    //预定推送消息
                    Bundle bundle = new Bundle();
                    bundle.putString("saletoorder", "saletoorder");
                    bundle.putString("orderid",resultBean.getAbout_id()+"");
                    ActivitySkip.forward(getActivity(), SaleRecordActivity.class, ExtraName.ORDER_TO_SALE, bundle);
                }
            }


        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItemPos = firstVisibleItem;
            }
        });

    }

    /**
     * 删除预定通知
     *
     * @param notice_id
     */
    private void deleteNoticeLog(String notice_id) {
        showLoadingDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("notice_logs_id", notice_id);
        finalHttp.get(Contants.DELETE_NOTICE, ajaxParams, new AjaxCallBack<String>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                ToasShow.showToastCenter(getActivity(), strMsg);
                dismissLoadingDialog();

            }

            @Override
            public void onSuccess(String s) {
                dismissLoadingDialog();
                Gson g = new Gson();
                ErrorBean e = g.fromJson(s, ErrorBean.class);
                ToasShow.showToastCenter(getActivity(), e.getMsg());
                if (e.getCode().equals("1")) {
                    //发送消息数目
                    HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);
                    NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(4);
                    getData(1, true, cate_id);


                }


            }
        });

    }

    /**
     * 判断是否选中
     *
     * @param
     */

    private String isSelected() {
        StringBuffer stringBuffer = new StringBuffer();
        if (informationContents.size() > 0) {
            for (int i = 0; i < informationContents.size(); i++) {
                if (adapter.getIsSelected().get(i)) {
                    stringBuffer.append(informationContents.get(i).getId() + ",");
                }
            }
            return stringBuffer.toString();
        }

        return null;
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        flag=0;
        getData(page, true, cate_id);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        flag=0;
        getData(page, false, cate_id);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ExtraName.ORDER_TO_SALE:
                    adapter.getInformationtents().get(pos).setIs_read(1);
                    adapter.notifyDataSetChanged();
                    flag=1;
                    commitHasRead(resultBean.getId());
                    break;
            }
        }
    }

    private void commitHasRead(int id) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("notice_logs_id", id + "");
        http.get(Contants.SUBMIT_READED, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSucess: " + s);

                getData(page, true, cate_id);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e(TAG, "onFailure: " + strMsg);
            }
        });
    }

}
