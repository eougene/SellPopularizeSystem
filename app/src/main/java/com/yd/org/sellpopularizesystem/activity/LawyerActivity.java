package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.adapter.CountrySortAdapter;
import com.yd.org.sellpopularizesystem.adapter.LawyerAdapter;
import com.yd.org.sellpopularizesystem.adapter.SortGroupMemberAdapter;
import com.yd.org.sellpopularizesystem.adapter.TeamAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CountrySortModel;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.Lawyer;
import com.yd.org.sellpopularizesystem.javaBean.LawyerBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.javaBean.TeamBean;
import com.yd.org.sellpopularizesystem.myView.SearchEditText;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.CharacterParserUtil;
import com.yd.org.sellpopularizesystem.utils.CountryComparator;
import com.yd.org.sellpopularizesystem.utils.GetCountryNameSort;
import com.yd.org.sellpopularizesystem.utils.GetLawyerNameSort;
import com.yd.org.sellpopularizesystem.utils.LawyerComparator;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.TeamComparator;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LawyerActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private SearchEditText searchEditText;
    //private CommonAdapter lawyerAdapter;
    private LawyerAdapter lawyerAdapter;
    private TeamAdapter teamAdapter;
    //律师组集合
    private List<Lawyer.ResultBean> lawyerGroupListData = new ArrayList<Lawyer.ResultBean>();
    //律师集合
    private List<Lawyer.ResultBean.LawyerListBean> lawyerListData = new ArrayList<Lawyer.ResultBean.LawyerListBean>();
    //团队集合
    private List<TeamBean.ResultBean.SubBeanX>  teamGroupListData=new ArrayList<TeamBean.ResultBean.SubBeanX>();
    //团队成员集合
    private List<TeamBean.ResultBean.SubBeanX.SubBean>  teamListData=new ArrayList<TeamBean.ResultBean.SubBeanX.SubBean>();
    /**
     * 分组的布局
     */
    private LinearLayout titleLayout;

    /**
     * 分组上显示的字母
     */
    private TextView title;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    private int cout;
    //搜索相关
    private GetLawyerNameSort countryChangeUtil;
    private CharacterParserUtil characterParserUtil;
    private LawyerComparator pinyinComparator;
    private TeamComparator teamComparator;
    private List<CountrySortModel> mAllCountryList;
    public static LawyerActivity lawyerActivity;
    private String strTeam;

    @Override
    protected int setContentView() {
        lawyerActivity = this;
        setTitle(getString(R.string.selectlawyer));
        changeLeftImageView(R.mipmap.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }
        });

        return R.layout.activity_lawyer;
    }

    @Override
    public void initView() {
        searchEditText = getViewById(R.id.activity_main_input_edittext);
        searchEditText.addTextChangedListener(textWatcher);
        titleLayout = getViewById(R.id.lltitle);
        title = getViewById(R.id.title);
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(LawyerActivity.this);
        listView = getViewById(R.id.content_view);
        mAllCountryList = new ArrayList<CountrySortModel>();
        pinyinComparator = new LawyerComparator();
        teamComparator=new TeamComparator();
        countryChangeUtil = new GetLawyerNameSort();
        characterParserUtil = new CharacterParserUtil();
        strTeam = getIntent().getExtras().getString("team");
        if (strTeam != null && strTeam.equals("team")) {
            getTeamListData("", true);
        } else {
            getLawyerListData("", true);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ExtraName.UPDATE) {
                getLawyerListData("", true);
            }
        }
    };

    /**
     * 获取我的团队列表
     */
    private void getTeamListData(String s, final boolean b) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        http.get(Contants.TEAM_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                if (null != s) {
                    //jsonParse(s);
                    jsonParse(s, b);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 获取律师列表
     */
    private void getLawyerListData(String customeId, final boolean b) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("customer_id", "");
        ajaxParams.put("law_firm", "");
        http.get(Contants.LAWYER_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("律师信息**", "s:" + s);
                closeDialog();
                if (null != s) {
                    //jsonParse(s);
                    jsonParse(s, b);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }

        });
    }

    private void jsonParse(String json, boolean isRefresh) {
        Gson gson = new Gson();
        if (strTeam == null) {
            Lawyer lawyerBean = gson.fromJson(json, Lawyer.class);
            if (lawyerBean.getCode() == 1) {
                lawyerGroupListData = lawyerBean.getResult();
                Log.e("TAG1", "jsonParse: " + lawyerGroupListData.size());
                for (int i = 0; i < lawyerGroupListData.size(); i++) {
                    //cout+= lawyerGroupListData.get(i).getLawyer_list().size();
                    for (int j = 0; j < lawyerGroupListData.get(i).getLawyer_list().size(); j++) {
                        Lawyer.ResultBean.LawyerListBean lawyer = lawyerGroupListData.get(i).getLawyer_list().get(j);
                /*if (lawyerListData.size()==0||lawyerListData.size()==(cout-(lawyerGroupListData.get(i).getLawyer_list().size()))){
                    lawyer.setFirst(true);
                }*/
                        lawyerListData.add(lawyer);
                    }
                }
            }
        }else {
            TeamBean tb=gson.fromJson(json, TeamBean.class);
            if (tb.getCode().equals("1")){
                teamGroupListData.addAll(tb.getResult().getSub());
                for (int i = 0; i <teamGroupListData.size(); i++) {
                    for (int j = 0; j <teamGroupListData.get(i).getSub().size() ; j++) {
                        TeamBean.ResultBean.SubBeanX.SubBean sb=teamGroupListData.get(i).getSub().get(j);
                        teamListData.add(sb);
                    }
                }
            }
        }
        Log.e("tag", "jsonParse: " + lawyerListData.size());
        if (isRefresh) {
            //ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            /*lawyerAdapter = new CommonAdapter<Lawyer.ResultBean.LawyerListBean>(this, lawyerListData, R.layout.lawyer_listview_item) {
                @Override
                public void convert(ViewHolder holder, Lawyer.ResultBean.LawyerListBean item) {
                    int sec = getSectionForPosition(holder.getPosition());
                    if (holder.getPosition() == getPositionForSection(sec)) {
                        holder.getView(R.id.law_firm).setVisibility(View.VISIBLE);
                        holder.setText(R.id.law_firm, item.getLaw_firm());
                    } else {
                        holder.getView(R.id.law_firm).setVisibility(View.GONE);
                    }
                    holder.setText(R.id.tvName, item.getLawyer_id() + item.getFirst_name() + item.getSurname());
                }
            };*/
            if (strTeam == null){
                Collections.sort(lawyerListData, pinyinComparator);
                lawyerAdapter = new LawyerAdapter(LawyerActivity.this, lawyerListData);
                listView.setAdapter(lawyerAdapter);
            }else {
                Collections.sort(teamListData, teamComparator);

            }
            setListener();
        } else {
            lawyerAdapter.notifyDataSetChanged();
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    /* private void getLawyerNameData() {
         String[] names=new String[lawyerListData.size()];
         for (int i = 0; i <lawyerListData.size(); i++) {
             names[i]=lawyerListData.get(i).isFirst()+lawyerListData.get(i).getSurname();
             String countrySortKey = characterParserUtil.getSelling(names[i]);
             CountrySortModel countrySortModel = new CountrySortModel(names[i], null,countrySortKey);
             String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
             if (sortLetter == null) {
                 sortLetter = countryChangeUtil.getSortLetterBySortKey(names[i]);
             }
             countrySortModel.sortLetters = sortLetter;
             mAllCountryList.add(countrySortModel);
         }
         adapter = new LawyerAdapter(LawyerActivity.this, mAllCountryList);
         adapter.updateListView(mAllCountryList);
     }*/
    @Override
    public void setListener() {
        if (lawyerListData.size() > 0) {
            listView.setAdapter(lawyerAdapter);
            Log.e("tag", "onScroll: " + lawyerListData.size());
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int section = lawyerAdapter.getSectionForPosition(firstVisibleItem);
                    int nextSecPosition = lawyerAdapter.getPositionForSection(section + 1);
                    if (firstVisibleItem != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                        params.topMargin = 0;
                        titleLayout.setLayoutParams(params);
                        if (firstVisibleItem == 0) {
                            title.setVisibility(View.GONE);
                        } else {
                            title.setVisibility(View.VISIBLE);
                            title.setText(lawyerAdapter.getSectionHeaders()[section]);
                        }
                    }
                    if (nextSecPosition == firstVisibleItem + 1) {
                        View childView = view.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = titleLayout.getHeight();
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                    .getLayoutParams();
                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                //params.topMargin = (int) pushedDistance;
                                titleLayout.setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    titleLayout.setLayoutParams(params);
                                }
                            }
                        }
                    }
                    lastFirstVisibleItem = firstVisibleItem;
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                intent.putExtra("lawyer", (Serializable) lawyerListData.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        Log.e("lawyerListData", "setListener: " + lawyerListData.size());
        clickRightImageView(R.mipmap.addim, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySkip.forward(LawyerActivity.this, LawyerAddActivity.class);
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s != null && s.length() > 0) {

            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String strSearch = searchEditText.getText().toString();
            if (strSearch.length() > 0) {
                ArrayList<Lawyer.ResultBean.LawyerListBean> fileterList = (ArrayList<Lawyer.ResultBean.LawyerListBean>) countryChangeUtil
                        .search(strSearch, lawyerListData);
                lawyerAdapter.updateListView(fileterList);
            } else {
                lawyerAdapter.updateListView(lawyerListData);
            }
            listView.setSelection(0);
        }
    };

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

  /*  @Override
    public Object[] getSections() {
        return sectionHeaders;
    }*/

    /*@Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= sectionIndices.length) {
            sectionIndex = sectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return sectionIndices[sectionIndex];
    }*/

    /*@Override
    public int getSectionForPosition(int position) {
        if (sectionIndices.length > 0) {
            for (int i = 0; i < sectionIndices.length; i++) {
                if (position < sectionIndices[i]) {
                    return i - 1;
                }
            }
        }
        return sectionIndices.length - 1;
    }*/
}
