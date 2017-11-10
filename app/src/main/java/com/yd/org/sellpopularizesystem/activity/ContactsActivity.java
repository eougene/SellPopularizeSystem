package com.yd.org.sellpopularizesystem.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.ContactsAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.custom.CharacterParser;
import com.yd.org.sellpopularizesystem.custom.PinyinComparator;
import com.yd.org.sellpopularizesystem.custom.SideBar;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.myView.SearchEditText;
import com.yd.org.sellpopularizesystem.utils.GetCustomerNameSort;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsActivity extends BaseActivity implements SectionIndexer, PullToRefreshLayout.OnRefreshListener {

    public static ContactsActivity mContactsActivity;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private SideBar sideBar;
    private TextView dialog;
    private TextView tvNofriends;
    private LinearLayout titleLayout;
    private SearchEditText searchEditText;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<CustomBean.ResultBean> SourceDateList = new ArrayList<>();
    private GetCustomerNameSort nameChangeUtil;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private ContactsAdapter adapter;

    //通讯录
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /** 头像ID **/
    // private static final int PHONES_PHOTO_ID_INDEX = 2;
    /** 联系人的ID **/
    // private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 库 phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    private String push_to = "1";
    private CustomBean mCustomBean = new CustomBean();

    @Override
    protected int setContentView() {
        mContactsActivity = this;
        return R.layout.activity_contacts;
    }

    @Override
    public void initView() {
        mCustomBean = (CustomBean) getIntent().getSerializableExtra("key");
        hideRightImagview();
        setTitle(getString(R.string.contacts_title));
        initViews();
        showContacts(true);

    }

    private void initViews() {

        searchEditText = getViewById(R.id.activity_main_input_edittext);
        titleLayout = getViewById(R.id.title_layout);
        tvNofriends = getViewById(R.id.noInfomation);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        nameChangeUtil = new GetCustomerNameSort();
        pinyinComparator = new PinyinComparator();

        sideBar = getViewById(R.id.sidrbar);
        dialog = getViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);


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
        return SourceDateList.get(position).getSortLetters().charAt(0);

    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
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

    /**
     * 加载手机联系人
     */
    private List<CustomBean.ResultBean> loadPhoneContactData() {
        showDialog();

        List<CustomBean.ResultBean> mSortList = new ArrayList<CustomBean.ResultBean>();

        ContentResolver resolver = this.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        CustomBean.ResultBean sort = null;

        String phoneNumber = "";

        String phoneName = "";

        // Long photoID = 0l;

        if (phoneCursor != null) {

            while (phoneCursor.moveToNext()) {

                // get phone number
                phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX).replace(" ", "");

                if (phoneNumber == null || phoneNumber == "") continue;

                phoneName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                sort = new CustomBean.ResultBean();

                sort.setMobile(phoneNumber);
                sort.setSurname(phoneName);

                for (int i=0;i<mCustomBean.getResult().size();i++){
                    if (mCustomBean.getResult().get(0).getMobile().contains(phoneNumber)) {
                        sort.setAdd(true);
                    } else {
                        sort.setAdd(false);
                    }
                }



                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(phoneName);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                sort.setSortLetters(sortString);

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sort.setSortLetters(sortString.toUpperCase());
                } else {
                    sort.setSortLetters("#");
                }

                mSortList.add(sort);
            }

            phoneCursor.close();

        }

        return mSortList;

    }


    private void showContacts(boolean isRefresh) {
        SourceDateList = loadPhoneContactData();

        closeDialog();

        if (isRefresh) {

            if (SourceDateList.size() == 0) {
                tvNofriends.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                tvNofriends.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }


            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new ContactsAdapter(this);
            listView.setAdapter(adapter);
        }
        adapter.addMore(SourceDateList);

    }


    @Override
    public void setListener() {
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


        // 根据输入框输入值的改变来过滤搜索
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                titleLayout.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = searchEditText.getText().toString();
                if (searchContent.length() > 0) {
                    ArrayList<CustomBean.ResultBean> fileterList = (ArrayList<CustomBean.ResultBean>) nameChangeUtil.search_(searchContent, SourceDateList);
                    adapter.updateListView(fileterList);
                } else {
                    adapter.updateListView(SourceDateList);
                }
                listView.setSelection(0);
            }
        });
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        showContacts(true);


    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);

    }


    public void addUserInfo(String surname, String mobile) {
//        if (TextUtils.isEmpty(surname)||TextUtils.isEmpty(mobile)){
//
//            bundle.putSerializable("custome", resultBean);
//            bundle.putString("add", "list");
//            ActivitySkip.forward(CustomeActivity.this, CustomDetailedActivity.class, bundle);
//        }
//


        showDialog();
        HttpParams ajaxParams = new HttpParams();
        //**************************************
        // ajaxParams.put("type_id", SharedPreferencesHelps.getType()+"");//1:销售  2推荐人
        // ajaxParams.put("file", new File(imagePath), mUIProgressResponseCallBack);//客户头像
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("first_name", surname);//姓氏
        ajaxParams.put("surname", surname);//名字
        ajaxParams.put("mid_name", "");//中间名
        ajaxParams.put("en_name", "");//英文名
        ajaxParams.put("birth_date", "");//生日（时间戳格式）
        ajaxParams.put("mobile", mobile);//添加电话
        ajaxParams.put("e_mail", "");//添加邮箱
        ajaxParams.put("wechat_number", "");//添加微信
        ajaxParams.put("qq_number", "");//添加QQ
        ajaxParams.put("customer_type", "");//是否为公司客户     1：个人客户   2：公司客户
        ajaxParams.put("company_name", "");//公司名称
        ajaxParams.put("abn", "");//ABN
        ajaxParams.put("acn", "");//ACN
        ajaxParams.put("company_mobile", "");//公司电话
        ajaxParams.put("company_e_mail", "");//公司邮箱
        ajaxParams.put("company_fax", "");//公司传真
        ajaxParams.put("client_id", "");//负责人ID
        ajaxParams.put("client", "");//负责人ID
        ajaxParams.put("select_self", "");//选择自己   1：选自己  0： 选其他人
        ajaxParams.put("company_country", "");//公司国家
        ajaxParams.put("company_unit_number", "");//公司单元号
        ajaxParams.put("company_street_number", "");//公司街道号码
        ajaxParams.put("company_suburb", "");//公司区
        ajaxParams.put("company_state", "");//公司州
        ajaxParams.put("company_street_address_line_1", "");//公司街道地址一
        ajaxParams.put("company_street_address_line_2", "");//公司街道地址二
        ajaxParams.put("company_postcode", "");//公司邮编
        ajaxParams.put("country", "");//国家
        ajaxParams.put("unit_number", "");//单元号
        ajaxParams.put("street_number", "");//街道号码
        ajaxParams.put("suburb", "");//区
        ajaxParams.put("state", "");//州
        ajaxParams.put("street_address_line_1", "");//街道地址一
        ajaxParams.put("street_address_line_2", "");//街道地址二
        ajaxParams.put("postcode", "");//邮编
        ajaxParams.put("is_firb", "");//FIRB   0：不确定  1：是   2：不是
        ajaxParams.put("job", "");//职业
        ajaxParams.put("income", "");//目前年薪
        ajaxParams.put("card_id", "");//身份证号码
        ajaxParams.put("passport_id", "");//护照号码
        ajaxParams.put("passport_country", "");//护照国别
        ajaxParams.put("family_first_name", "");//亲属姓氏
        ajaxParams.put("family_name", "");//亲属名字
        ajaxParams.put("family_relationship", "");//亲属关系
        ajaxParams.put("family_mobile", "");//亲属手机
        ajaxParams.put("family_email", "");//亲属邮箱
        ajaxParams.put("memo", "");//备注
        ajaxParams.put("push_to", push_to);//1：将客户推荐给上线销售   2：将客户推荐到后台，让管理员分配


        EasyHttp.post(Contants.NEW_CUSTOME).cacheMode(CacheMode.NO_CACHE).headers("Content-Type", "application/x-www-form-urlencoded").params(ajaxParams).timeStamp(true).execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                showDialog();
            }

            @Override
            public void onError(ApiException e) {
                closeDialog();
                ToasShow.showToastCenter(ContactsActivity.this, e.getMessage());
            }

            @Override
            public void onSuccess(String s) {
                Log.e("onSuccess**", "onSuccess:" + s);
                closeDialog();
                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    ToasShow.showToastCenter(ContactsActivity.this, json.getString("msg"));
                    if (json.getInt("code") == 1) {
                        CustomeActivity.customeActivity.handler.sendEmptyMessage(0);
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


}
