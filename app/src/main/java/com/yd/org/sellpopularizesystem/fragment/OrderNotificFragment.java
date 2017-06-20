package com.yd.org.sellpopularizesystem.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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

import org.json.JSONObject;

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

                    Log.e("编辑**", "JJ:" + msg.obj + "type:" + msg.arg2);
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

                    deleteNoticeLog(isSelected());
                    break;
                case ExtraName.UPDATE:
                    adapter.getInformationtents().get(pos).setIs_read(1);
                    adapter.notifyDataSetChanged();
            }
        }
    };

    private AnnouncementBean.ResultBean resultBean;
    private int pos;

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
        int is_read = 0;
        if (bean.getTotal_number() > 0) {
            if (cate_id == 4) {
                for (int i = 0; i < informationContents.size(); i++) {
                    if (informationContents.get(i).getIs_read() != 1) {
                        is_read += 1;
                    }
                }
                if (is_read > 0) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = String.valueOf(is_read);
                    NotificationFragment.notificationFragment.mhandler.sendEmptyMessage(0);
                    NotificationFragment.notificationFragment.mhandler.sendMessage(message);
                }


            }
        }

        if (isRefresh) {
            if (informationContents.size()==0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
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
                   // bundle.putSerializable("announcementBean", resultBean);
//                    bundle.putString("title", resultBean.getTitle());
//                    bundle.putString("notice_id", resultBean.getId() + "");
//                    bundle.putString("data", resultBean.getContent());
                    bundle.putString("saletoorder", "saletoorder");
                    ActivitySkip.forward(getActivity(), SaleRecordActivity.class, ExtraName.ORDER_TO_SALE,bundle);
                }
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

        Log.e("参数**", "notice_id:" + notice_id);
        finalHttp.get(Contants.DELETE_NOTICE, ajaxParams, new AjaxCallBack<String>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("失败", "errorNo:" + errorNo);
                ToasShow.showToastCenter(getActivity(), strMsg);
                dismissLoadingDialog();

            }

            @Override
            public void onSuccess(String s) {
                Log.e("成功", "errorNo:" + s);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case ExtraName.ORDER_TO_SALE:
                    String flag=data.getStringExtra("saletoorder");
                    Log.e("TAG", "onActivityResult: "+flag);
                    adapter.getInformationtents().get(pos).setIs_read(1);
                    adapter.notifyDataSetChanged();
                    commitHasRead(resultBean.getId());
                    Log.e(TAG, "onActivityResult: "+resultBean.getId());
                    break;
            }
        }
    }

    private void commitHasRead(int id) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("notice_logs_id", id+"");
        http.get(Contants.SUBMIT_READED, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSuccess: "+s );
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
            }
        });
    }

}
