package com.yd.org.sellpopularizesystem.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CountrySortAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CountrySortModel;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.MyUserInfo;
import com.yd.org.sellpopularizesystem.utils.CharacterParserUtil;
import com.yd.org.sellpopularizesystem.utils.CountryComparator;
import com.yd.org.sellpopularizesystem.utils.GetCountryNameSort;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserAdressActivity extends BaseActivity {

    TextView userAdressTv;
    EditText userUnitNumberEd;
    EditText userStreetNumberEd;
    EditText userSuburbNumberEd;
    EditText userStateEd;
    EditText userDetailedAddressEd;
    EditText userDetailedAddressEd2;
    EditText userPostcodeEd;


    private PopupWindow nationSelectPopWindow;
    private EditText etNationSearch;
    private ListView lvNation;
    private View nationPopWindowView;
    private GetCountryNameSort countryChangeUtil;
    private CountrySortAdapter adapter;
    private CharacterParserUtil characterParserUtil;
    private CountryComparator pinyinComparator;
    private List<CountrySortModel> mAllCountryList;


    private MyUserInfo myUserInfo;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_user_adress;
    }

    @Override
    public void initView() {


        myUserInfo = (MyUserInfo) getIntent().getSerializableExtra("userkey");
        type = getIntent().getStringExtra("type");

        if (type.equals("1")) {
            setTitle("我的地址");
        } else {
            setTitle("公司地址");
        }


        getCountryList();
        initProviceSelectView();

        setUserAdress(myUserInfo);

    }

    private void setUserAdress(MyUserInfo myUserInfo) {
        userAdressTv = getViewById(R.id.userAdressTv);
        userUnitNumberEd = getViewById(R.id.userUnitNumberEd);
        userStreetNumberEd = getViewById(R.id.userStreetNumberEd);
        userSuburbNumberEd = getViewById(R.id.userSuburbNumberEd);
        userStateEd = getViewById(R.id.userStateEd);
        userDetailedAddressEd = getViewById(R.id.userDetailedAddressEd);
        userDetailedAddressEd2 = getViewById(R.id.userDetailedAddressEd2);
        userPostcodeEd = getViewById(R.id.userPostcodeEd);


        //国家
        userAdressTv.setText((null == myUserInfo.getResult().getCountry() || TextUtils.isEmpty(myUserInfo.getResult().getCountry())) ? "" : myUserInfo.getResult().getCountry());
        //单元号
        userUnitNumberEd.setText((null == myUserInfo.getResult().getUnit_number() || TextUtils.isEmpty(myUserInfo.getResult().getUnit_number())) ? "" : myUserInfo.getResult().getUnit_number());
        //街道号码
        userStreetNumberEd.setText((null == myUserInfo.getResult().getStreet_number() || TextUtils.isEmpty(myUserInfo.getResult().getStreet_number())) ? "" : myUserInfo.getResult().getStreet_number());
        //区
        userSuburbNumberEd.setText((null == myUserInfo.getResult().getSuburb() || TextUtils.isEmpty(myUserInfo.getResult().getSuburb())) ? "" : myUserInfo.getResult().getSuburb());
        //州
        userStateEd.setText((null == myUserInfo.getResult().getState() || TextUtils.isEmpty(myUserInfo.getResult().getState())) ? "" : myUserInfo.getResult().getState());
        //街道地址1
        userDetailedAddressEd.setText((null == myUserInfo.getResult().getStreet_address_line_1() || TextUtils.isEmpty(myUserInfo.getResult().getStreet_address_line_1())) ? "" : myUserInfo.getResult().getStreet_address_line_1());
        //街道地址2
        userDetailedAddressEd2.setText((null == myUserInfo.getResult().getStreet_address_line_2() || TextUtils.isEmpty(myUserInfo.getResult().getStreet_address_line_2())) ? "" : myUserInfo.getResult().getStreet_address_line_2());
        //邮编
        userPostcodeEd.setText((null == myUserInfo.getResult().getPostcode() || TextUtils.isEmpty(myUserInfo.getResult().getPostcode())) ? "" : myUserInfo.getResult().getPostcode());

    }

    private void saveAdress() {

        String country, unit_number, street_number, suburb, state, street_address_line_1, street_address_line_2, postcode;

        //国际
        if (TextUtils.isEmpty(userAdressTv.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "选择国家或地区");
            return;
        } else {
            country = userAdressTv.getText().toString().trim();
        }

        //单元号
        if (TextUtils.isEmpty(userUnitNumberEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请填写单元号");
            return;
        } else {
            unit_number = userUnitNumberEd.getText().toString().trim();
        }
        //街道号
        if (TextUtils.isEmpty(userStreetNumberEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请填写街道号");
            return;
        } else {
            street_number = userStreetNumberEd.getText().toString().trim();
        }
        //区
        if (TextUtils.isEmpty(userSuburbNumberEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请填写区");
            return;
        } else {
            suburb = userSuburbNumberEd.getText().toString().trim();
        }
        //州
        if (TextUtils.isEmpty(userStateEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请填写州");
            return;
        } else {
            state = userStateEd.getText().toString().trim();
        }
        //街道1
        if (TextUtils.isEmpty(userDetailedAddressEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请选择地区");
            return;
        } else {
            street_address_line_1 = userDetailedAddressEd.getText().toString().trim();
        }
        //街道2
        if (TextUtils.isEmpty(userDetailedAddressEd2.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请选择地区");
            return;
        } else {
            street_address_line_2 = userDetailedAddressEd2.getText().toString().trim();
        }
        //邮编
        if (TextUtils.isEmpty(userPostcodeEd.getText().toString().trim())) {
            ToasShow.showToastCenter(UserAdressActivity.this, "请选择地区");
            return;
        } else {
            postcode = userPostcodeEd.getText().toString().trim();
        }


        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());


        //我的地址
        if (type.equals("1")) {
            httpParams.put("country", country);
            httpParams.put("unit_number", unit_number);
            httpParams.put("street_number", street_number);
            httpParams.put("suburb", suburb);
            httpParams.put("state", state);
            httpParams.put("street_address_line_1", street_address_line_1);
            httpParams.put("street_address_line_2", street_address_line_2);
            httpParams.put("postcode", postcode);

            //公司地址
        } else {

            httpParams.put("account_name", "");
            httpParams.put("account_number", "");
            httpParams.put("bank_name", "");
            httpParams.put("bsb", "");
            httpParams.put("company_country", country);
            httpParams.put("company_unit_number", unit_number);
            httpParams.put("company_street_number", street_number);
            httpParams.put("company_suburb", suburb);
            httpParams.put("company_state", state);
            httpParams.put("company_street_address_line_1", street_address_line_1);
            httpParams.put("company_street_address_line_2", street_address_line_2);
            httpParams.put("company_postcode", postcode);

        }


        EasyHttp.post(Contants.UPDATE_USER)
                .params(httpParams)
                .timeStamp(true)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(UserAdressActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        closeDialog();
                        Log.e("onSuccess***", "onSuccess:" + s);

                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                        ToasShow.showToastCenter(UserAdressActivity.this, errorBean.getMsg());
                        if (errorBean.getCode().equals("1")) {

                            //保存地址信息记录
                            SharedPreferencesHelps.setUserAdress(1);
                            finish();
                        }

                    }
                });

    }

    @Override
    public void setListener() {

        setRightTitle(R.string.customdetaild_save, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAdress();
            }
        });

        userAdressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nationSelectPopWindow.showAtLocation(UserAdressActivity.this.findViewById(R.id.mainLinear), Gravity.BOTTOM, 0, 0);

            }
        });

    }


    private void initProviceSelectView() {


        //国家选择popWindow视图
        nationPopWindowView = LayoutInflater.from(this).inflate(R.layout.nation_picker, null);
        etNationSearch = (EditText) nationPopWindowView.findViewById(R.id.etNationSearch);
        lvNation = (ListView) nationPopWindowView.findViewById(R.id.lvNation);

        lvNation.setAdapter(adapter);
        nationSelectPopWindow = new PopupWindow(nationPopWindowView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        nationSelectPopWindow.setHeight(MyUtils.getInstance().getScreenSize(this) * 2 / 3);
        nationSelectPopWindow.setOutsideTouchable(true);
        nationSelectPopWindow.setTouchable(true);
        nationSelectPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        //设置SelectPicPopu  pWindow弹出窗体动画效果
        nationSelectPopWindow.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable cd = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        nationSelectPopWindow.setBackgroundDrawable(cd);

        etNationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = etNationSearch.getText().toString();
                if (searchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
                            .search(searchContent, mAllCountryList);
                    adapter.updateListView(fileterList);
                } else {
                    adapter.updateListView(mAllCountryList);
                }
                lvNation.setSelection(0);
            }
        });


        lvNation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryName = null;
                String countryNumber = null;
                String etSearchContent = etNationSearch.getText().toString();
                if (etSearchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
                            .search(etSearchContent, mAllCountryList);
                    countryName = fileterList.get(position).countryName;
                    countryNumber = fileterList.get(position).countryNumber;
                } else {
                    // 点击后返回
                    countryName = mAllCountryList.get(position).countryName.trim();
                    countryNumber = mAllCountryList.get(position).countryNumber;
                }
                userAdressTv.setText(countryName);
                nationSelectPopWindow.dismiss();
            }
        });


    }

    private void getCountryList() {
        mAllCountryList = new ArrayList<CountrySortModel>();
        pinyinComparator = new CountryComparator();
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();


        String[] countryList = getResources().getStringArray(R.array.country_code_list_ch);

        for (int i = 0, length = countryList.length; i < length; i++) {
            String[] country = countryList[i].split("\\*");

            String countryName = country[0].trim();
            String countryNumber = country[1];
            String countrySortKey = characterParserUtil.getSelling(countryName);
            CountrySortModel countrySortModel = new CountrySortModel(countryName, countryNumber,
                    countrySortKey);
            String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
            if (sortLetter == null) {
                sortLetter = countryChangeUtil.getSortLetterBySortKey(countryName);
            }

            countrySortModel.sortLetters = sortLetter;
            mAllCountryList.add(countrySortModel);
        }

        Collections.sort(mAllCountryList, pinyinComparator);
        adapter = new CountrySortAdapter(UserAdressActivity.this, mAllCountryList);
        adapter.updateListView(mAllCountryList);

    }


}
