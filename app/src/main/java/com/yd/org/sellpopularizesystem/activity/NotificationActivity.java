package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.AnnouncementBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private RelativeLayout rlMessage;
    private List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
    //private Map<String,Object> map=new HashMap<String,Object>();
    private List<AnnouncementBean.ResultBean> informationContents = new ArrayList<>();
    private CommonAdapter adapter;
    private int page = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_notification;
    }

    @Override
    public void initView() {
        setTitle(getResources().getString(R.string.setting_notification));
        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        rlMessage = (RelativeLayout) getViewById(R.id.rlMessage);
        hideRightImagview();
        getData(1, true);
        //SimpleAdapter simpleAdapter=new SimpleAdapter(this,messages,R.layout.notification_listview_item,R.id.tvMessageTitle,)
    }

    private void getData(int page, final boolean isRefresh) {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", "10");
        //String url = "https://www.wingaid.com/index.php/app/notice/index?user_id=100014&page=" + page + "&number=10";
        fh.get(Contants.SYSTEM_ANNOUNCEMENT,ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("s**","s:"+s);
                closeDialog();
                if (null != s) {
                    jsonParse(s, isRefresh);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
            }
        });
    }

    private void jsonParse(String s, boolean isRefresh) {
        Gson gson = new Gson();
        AnnouncementBean bean = gson.fromJson(s, AnnouncementBean.class);
        if (bean.getCode().equals("1")) {
            informationContents = bean.getResult();
            Log.e("inform", "jsonParse: " + informationContents.toString());
        }

        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            //ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.notification_listview_item,informationContents);
            adapter = new CommonAdapter<AnnouncementBean.ResultBean>(this, informationContents, R.layout.notification_listview_item) {
                @Override
                public void convert(ViewHolder holder, AnnouncementBean.ResultBean item) {
                    holder.setText(R.id.tvMessageTitle, item.getTitle());
                    if (item.getIs_read()==1){
                            holder.getView(R.id.tvPoint).setVisibility(View.INVISIBLE);
                    }else{
                        holder.getView(R.id.tvPoint).setVisibility(View.VISIBLE);
                    }
                }
            };
            listView.setAdapter(adapter);
        } else {
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            adapter.addMore(informationContents);
        }


    }

    @Override
    public void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle() ;
                bundle.putString("data",informationContents.get(position).getContent());
                bundle.putString("title",informationContents.get(position).getTitle());
                bundle.putString("notice_id",informationContents.get(position).getId()+"");
                ActivitySkip.forward(NotificationActivity.this, InformationContentActivity.class,bundle);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        page = 1;
        getData(page, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 千万别忘了告诉控件刷新完毕了哦！
        page++;
        getData(page, false);

    }
}
