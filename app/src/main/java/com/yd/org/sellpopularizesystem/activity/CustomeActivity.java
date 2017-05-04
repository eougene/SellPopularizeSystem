package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.adapter.SortGroupMemberAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.custom.CharacterParser;
import com.yd.org.sellpopularizesystem.custom.PinyinComparator;
import com.yd.org.sellpopularizesystem.custom.SideBar;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CountrySortModel;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.LawyerBean;
import com.yd.org.sellpopularizesystem.myView.SearchEditText;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.GetCountryNameSort;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;


/**
 * 客户管理
 */
public class CustomeActivity extends BaseActivity implements SectionIndexer, PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    // private String flag = "default";
    private SideBar sideBar;
    private TextView dialog;
    private SortGroupMemberAdapter adapter;
    private CommonAdapter lawyerAdapter;
    private List<LawyerBean.ResultBean> lawyersData = new ArrayList<>();
    private LinearLayout titleLayout;
    private TextView title, tvNofriends;
    private SearchEditText searchEditText;
    //输入内容选择相关
    private GetCountryNameSort countryChangeUtil;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<CustomBean.ResultBean> SourceDateList = new ArrayList<CustomBean.ResultBean>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    //用以判断跳转不同界面
    String str1 = "default";
    private List filterDateList;

    @Override
    protected int setContentView() {
        setBaseLayoutBackground(Color.WHITE);
        return R.layout.activity_custome;
    }

    @Override
    public void initView() {
        setTitle(getResources().getString(R.string.home_custom));
        initViews();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //从homeActivity传过来的值用以判断跳转不同界面
        str1 = bundle.getString(ExtraName.SCALETOCUSTOME);
        //点击客户管理传来的
        // str1 = bundle.getString(ExtraName.TOCUSTOME);
        //从预订界面点击客户律师跳转
        // str1 = bundle.getString("lawyer");
        if (str1 != null && str1.equals(ExtraName.SCALETOCUSTOME)) {
            getCustomeListData(true, page);

        } else if (str1.equals(ExtraName.TORESVER)) {
            setTitle(getString(R.string.lawyer));
            // String id = bundle.getString("id");
            getLawyerListData("", true);
        } else {
            getCustomeListData(true, page);
        }
        setListener();
    }


    /**
     * 获取律师列表
     *
     * @param customeId
     */
    private void getLawyerListData(String customeId, final boolean b) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("customer_id", "");
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

    private void initViews() {

        searchEditText = getViewById(R.id.activity_main_input_edittext);
        titleLayout = getViewById(R.id.title_layout);
        title = getViewById(R.id.title_layout_catalog);
        tvNofriends = getViewById(R.id.noInfomation);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = getViewById(R.id.sidrbar);
        dialog = getViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);


    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List filledData(List date) {
        List list = new ArrayList();

        if (str1.equals(ExtraName.TORESVER)) {
            List<LawyerBean.ResultBean> mlawSortList = new ArrayList<LawyerBean.ResultBean>();
            for (int i = 0; i < date.size(); i++) {
                LawyerBean.ResultBean sortModel = (LawyerBean.ResultBean) date.get(i);
                // 汉字转换成拼音
                if (!TextUtils.isEmpty(sortModel.getLawyer_name())) {
                    String pinyin = characterParser.getSelling(sortModel.getLawyer_name());
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        sortModel.setSortLetters(sortString.toUpperCase());
                    } else {
                        sortModel.setSortLetters("#");
                    }
                    mlawSortList.add(sortModel);
                }
            }
            list.clear();
            list.addAll(mlawSortList);
            return list;
        } else {
            List<CustomBean.ResultBean> mSortList = new ArrayList<CustomBean.ResultBean>();
            for (int i = 0; i < date.size(); i++) {
                CustomBean.ResultBean sortModel = (CustomBean.ResultBean) date.get(i);
                // 汉字转换成拼音
                if (!TextUtils.isEmpty(sortModel.getTrue_name())) {
                    String pinyin = characterParser.getSelling(sortModel.getTrue_name());
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        sortModel.setSortLetters(sortString.toUpperCase());
                    } else {
                        sortModel.setSortLetters("#");
                    }

                    mSortList.add(sortModel);
                }
            }
            list.clear();
            list.addAll(mSortList);
            return list;
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        if (str1.equals(ExtraName.TORESVER)) {
            Log.e("tag", "filterData: "+filterDateList.size());
            filterDateList = new ArrayList<LawyerBean.ResultBean>();
            //filterDateList.addAll(filterDateList);
        } else {
            if (TextUtils.isEmpty(filterStr)) {
                /*filterDateList = new ArrayList<CustomBean.ResultBean>();
                filterDateList = SourceDateList;*/
                tvNofriends.setVisibility(View.GONE);
                adapter.updateListView(SourceDateList,null);
            } else {
                filterDateList = new ArrayList<CustomBean.ResultBean>();
                for (CustomBean.ResultBean sortModel : SourceDateList) {
                    String name = sortModel.getTrue_name();
                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).startsWith(
                            filterStr)) {
                        filterDateList.add(sortModel);
                    }
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList, lawyersData);
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {

        Log.e("onCreat***", "position:" + position + "data:" + SourceDateList.size());
        return SourceDateList.get(position).getSortLetters().charAt(0);

    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void getCustomeListData(final boolean b, int page) {
        showDialog();
        //String url = Contants.CUSTOMER_LIST + "user_id=" + SharedPreferencesHelps.getUserID() + "&page=" + page + "&number=20";
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", "20");
        final FinalHttp fh = new FinalHttp();
        fh.get(Contants.CUSTOMER_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Log.e("客户内容", "s:" + s);
                if (null != s) {
                    jsonParse(s, b);
                } else {
                    ToasShow.showToastBottom(CustomeActivity.this, "无数据");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();

            }
        });

    }

    private void jsonParse(String json, boolean isRefresh) {

        Gson gson = new Gson();
        if (str1.equals(ExtraName.TORESVER)) {

            LawyerBean lawyerBean = gson.fromJson(json, LawyerBean.class);
            if (lawyerBean.getCode() == 1) {
                lawyersData = filledData(lawyerBean.getResult());
                Log.e("TAG1", "jsonParse: " + lawyersData.size());
            }
            if (isRefresh) {
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortGroupMemberAdapter(this, "null");
                listView.setAdapter(adapter);
            }
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            adapter.addData(SourceDateList, lawyersData);

        } else {
            CustomBean product = gson.fromJson(json, CustomBean.class);
            if (product.getCode() == 1) {
                SourceDateList = filledData(product.getResult());
            }
            if (isRefresh) {
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortGroupMemberAdapter(this, "custome");
                listView.setAdapter(adapter);
            }
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            adapter.addData(SourceDateList, lawyersData);

            Log.e("TAG", "jsonParse: " + SourceDateList.size());

        }
    }


    @Override
    public void setListener() {
        Log.e("sourceData", "setListener: "+SourceDateList.size());
        if (str1.equals(ExtraName.SCALETOCUSTOME) || str1.equals(ExtraName.TORESVER)) {
            changeLeftImageView(R.mipmap.close, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, R.anim.out_anim);
                }
            });
        } else {
            clickRightImageView(R.mipmap.addim, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("add", "add");
                    ActivitySkip.forward(CustomeActivity.this, CustomDetailedActivity.class, bundle);

                }
            });
        }


        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                int section = getSectionForPosition(firstVisibleItem);
//                int nextSection = getSectionForPosition(firstVisibleItem + 1);
//                int nextSecPosition = getPositionForSection(+nextSection);
//                if (firstVisibleItem != lastFirstVisibleItem) {
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
//                            .getLayoutParams();
//                    params.topMargin = 0;
//                    titleLayout.setLayoutParams(params);
//                    title.setText(SourceDateList.get(
//                            getPositionForSection(section)).getSortLetters());
//
//                }
//                if (nextSecPosition == firstVisibleItem + 1) {
//                    View childView = view.getChildAt(0);
//                    if (childView != null) {
//                        int titleHeight = titleLayout.getHeight();
//                        int bottom = childView.getBottom();
//                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
//                                .getLayoutParams();
//                        if (bottom < titleHeight) {
//                            float pushedDistance = bottom - titleHeight;
//                            params.topMargin = (int) pushedDistance;
//                            titleLayout.setLayoutParams(params);
//                        } else {
//                            if (params.topMargin != 0) {
//                                params.topMargin = 0;
//                                titleLayout.setLayoutParams(params);
//                            }
//                        }
//                    }
//                }
//                lastFirstVisibleItem = firstVisibleItem;
//            }
//        });


        // 根据输入框输入值的改变来过滤搜索
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                titleLayout.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchEditText.getText().toString().length()==0){
                    adapter.updateListView(SourceDateList, null);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Bundle bundle = new Bundle();
                if (str1.equals(ExtraName.SCALETOCUSTOME)) {
                    bundle.putString("add", "list");
                    SortGroupMemberAdapter.ViewHolder viewHolder = (SortGroupMemberAdapter.ViewHolder) view.getTag();
                    CustomBean.ResultBean resultBean = viewHolder.resultBean;
                    BaseApplication app = BaseApplication.getInstance();
                    app.setResultBean(resultBean);
                    ActivitySkip.forward(CustomeActivity.this, ScaleActivity.class, bundle);
                    finish();
                } else if (str1.equals(ExtraName.TORESVER)) {
                    SortGroupMemberAdapter.ViewHolder viewHolder = (SortGroupMemberAdapter.ViewHolder) view.getTag();
                    LawyerBean.ResultBean lawBean = viewHolder.m;
                    Intent i = new Intent();
                    i.putExtra("lawyer", lawBean);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                } else if (str1.equals(ExtraName.TORESVER_TOCUSTOME)) {
                    SortGroupMemberAdapter.ViewHolder viewHolder = (SortGroupMemberAdapter.ViewHolder) view.getTag();
                    CustomBean.ResultBean lawBean = viewHolder.resultBean;
                    Intent i = new Intent();
                    i.putExtra("custome", lawBean);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                } else {
                    SortGroupMemberAdapter.ViewHolder viewHolder = (SortGroupMemberAdapter.ViewHolder) view.getTag();
                    CustomBean.ResultBean resultBean = viewHolder.resultBean;
                    BaseApplication app = BaseApplication.getInstance();
                    app.setResultBean(resultBean);
                    bundle.putSerializable("custome", resultBean);
                    bundle.putString("add", "list");
                    ActivitySkip.forward(CustomeActivity.this, CustomDetailedActivity.class, bundle);
                }
            }
        });

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        page = 1;
        getCustomeListData(true, page);
    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        page++;
        getCustomeListData(false, page);
    }

}
