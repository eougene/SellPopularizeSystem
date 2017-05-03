package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CountrySortAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.clippicture.ClipPictureActivity;
import com.yd.org.sellpopularizesystem.custom.AddrXmlParser;
import com.yd.org.sellpopularizesystem.javaBean.CityInfoModel;
import com.yd.org.sellpopularizesystem.javaBean.CountrySortModel;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.CustomeDetailedBean;
import com.yd.org.sellpopularizesystem.javaBean.DistrictInfoModel;
import com.yd.org.sellpopularizesystem.javaBean.ProvinceInfoModel;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.myView.MyPopupwindow;
import com.yd.org.sellpopularizesystem.myView.WheelView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.CharacterParserUtil;
import com.yd.org.sellpopularizesystem.utils.CountryComparator;
import com.yd.org.sellpopularizesystem.utils.GetCountryNameSort;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.yd.org.sellpopularizesystem.application.ExtraName.CROP_IMAGE;


public class CustomDetailedActivity extends BaseActivity {
    String TAG = CustomDetailedActivity.class.getSimpleName();
    private CircleImageView customeIcon;
    private EditText edCustomeTrueName, edcustmomeDetailedBie, edCustomeMobile,
            edcustmomeDetailedWeChat, edcustmomeDetailedQQ, edcustmomeDetailedCity,
            edcustmomeDetailedAddress, edcustmomeDetailedEmail, edcustmomeDetailedWeJob,
            edcustmomeDetailedSalary, edcustmomeDetailedCard, edcustmomeDetailedPassPort,
            edcustmomeDetailedNationality, edcustmomeDetailedKinsfolk, edcustmomeDetailedRelation,
            edcustmomeDetailedPhone, edZipCode, etNationSearch, etFirb;
    //国家选择列表
    private ListView lvNation;
    private ImageView ivDelete, ivPhone, ivEmail;
    private TextView tvEoi,tvVisit,tvReserver,tvExpandRe,tvPurchase;
    private LinearLayout llOperate;
    private MyPopupwindow myPopupwindow;
    private List<CountrySortModel> mAllCountryList;

    private PopupWindow addrPopWindow, nationSelectPopWindow, firbSelectPopWindow;
    private CustomBean.ResultBean resultBean;
    private String tag, imagePath;
    private CustomeDetailedBean.ResultBean userInfo;
    private String strUrl;
    /**
     * 与选择地址相关
     */
    protected ArrayList<String> mProvinceDatas;
    protected Map<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    protected Map<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName;
    private TextView boxBtnCancel, boxBtnOk;
    //国家选择相关
    private GetCountryNameSort countryChangeUtil;
    private CountrySortAdapter adapter;
    private CharacterParserUtil characterParserUtil;
    private CountryComparator pinyinComparator;

    //城市选择
    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mCountyPicker;
    //private WheelView mDatePicker;
    private View contentView, nationPopWindowView, firbPwView;
    protected boolean isDataLoaded = false;
    private boolean isAddrChoosed = false;
    public static final String UPDATE = "update_custome";
    public static final String ADD = "add_custome";
    /**
     * 与日期选择相关
     */
    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;

    //firb选择相关
    private Button btUnknown, btSure, btFalse;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle=new Bundle();
            bundle.putString("customeId",resultBean.getCustomer_id()+"");
            switch (view.getId()) {
                //日期选择
                case R.id.edcustmomeDetailedBie:
                    pvTime.show();
                    break;
                case R.id.ivPhone:
                    if (TextUtils.isEmpty(edCustomeMobile.getText())) {
                        return;
                    } else {
                        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+edCustomeMobile.getText().toString()));
                        if (ActivityCompat.checkSelfPermission(CustomDetailedActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onReques+tPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intentPhone);
                    }
                    break;
                case R.id.ivEmail:
                    if (TextUtils.isEmpty(edCustomeMobile.getText())){
                        return;
                    }else{
                        Uri uri=Uri.parse("mailto:"+edcustmomeDetailedEmail.getText().toString());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        //intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
                        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
                    }

                    break;
                //城市选择
                case R.id.edcustmomeDetailedCity:
                    if (isDataLoaded) {
                        addrPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    } else {

                    }
                    break;
                //国家选择
                case R.id.edcustmomeDetailedNationality:
                    nationSelectPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.etFirb:
                    firbSelectPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    break;
                //EOI
                case R.id.tvEoi:
                    bundle.putString("custocora","custoeoi");
                    ActivitySkip.forward(CustomDetailedActivity.this,CusOprateRecordActivity.class,bundle);
                    break;
                //拜访
                case R.id.tvVisit:
                    bundle.putString("custocora","custovisit");
                    ActivitySkip.forward(CustomDetailedActivity.this,CusOprateRecordActivity.class,bundle);
                    break;
                //预约
                case  R.id.tvReserve:
                    bundle.putString("custocora","custoreser");
                    ActivitySkip.forward(CustomDetailedActivity.this,CusOprateRecordActivity.class,bundle);
                    break;
                //推广记录
                case R.id.tvExpandRe:
                    bundle.putString("custocora","custoexpand");
                    ActivitySkip.forward(CustomDetailedActivity.this,CusOprateRecordActivity.class,bundle);
                    break;
                //购房记录
                case R.id.tvPurchaseRe:
                    bundle.putString("custocora","custopurchase");
                    ActivitySkip.forward(CustomDetailedActivity.this,CusOprateRecordActivity.class,bundle);
                    break;
            }
        }
    };

    @Override
    protected int setContentView() {
        setBaseLayoutBackground(Color.WHITE);
        return R.layout.activity_custom_detailed;
    }

    @Override
    public void initView() {
        Views();

        Bundle bundle = getIntent().getExtras();

        if (!TextUtils.isEmpty(bundle.getString("add"))) {
            tag = bundle.getString("add");
            if (tag.equals("add")) {
                setTitle(R.string.custome_add);
                llOperate.setVisibility(View.GONE);
            } else {
                setTitle(R.string.customdetaild_title);
                resultBean = (CustomBean.ResultBean) bundle.getSerializable("custome");
                getCustomInfo(resultBean);
            }
        }
        initProviceSelectView();
        setTimePicker();


    }
    OptionsPickerView.Builder builder=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {

        }
    });
    private void setCountry() {
        pvOptions = new OptionsPickerView(builder);
    }


    private void Views() {
        customeIcon = getViewById(R.id.customeIcon);
        edCustomeTrueName = getViewById(R.id.edCustomeTrueName);
        edcustmomeDetailedBie = getViewById(R.id.edcustmomeDetailedBie);
        edCustomeMobile = getViewById(R.id.edCustomeMobile);
        ivPhone = getViewById(R.id.ivPhone);
        edcustmomeDetailedWeChat = getViewById(R.id.edcustmomeDetailedWeChat);
        edcustmomeDetailedQQ = getViewById(R.id.edcustmomeDetailedQQ);
        edcustmomeDetailedCity = getViewById(R.id.edcustmomeDetailedCity);
        edcustmomeDetailedAddress = getViewById(R.id.edcustmomeDetailedAddress);
        edZipCode = getViewById(R.id.edcustmeZipCode);
        edcustmomeDetailedEmail = getViewById(R.id.edcustmomeDetailedEmail);
        ivEmail=getViewById(R.id.ivEmail);
        edcustmomeDetailedWeJob = getViewById(R.id.edcustmomeDetailedWeJob);
        edcustmomeDetailedSalary = getViewById(R.id.edcustmomeDetailedSalary);
        edcustmomeDetailedCard = getViewById(R.id.edcustmomeDetailedCard);
        edcustmomeDetailedPassPort = getViewById(R.id.edcustmomeDetailedPassPort);
        edcustmomeDetailedNationality = getViewById(R.id.edcustmomeDetailedNationality);
        edcustmomeDetailedKinsfolk = getViewById(R.id.edcustmomeDetailedKinsfolk);
        edcustmomeDetailedRelation = getViewById(R.id.edcustmomeDetailedRelation);
        edcustmomeDetailedPhone = getViewById(R.id.edcustmomeDetailedPhone);
        etFirb=getViewById(R.id.etFirb);
        llOperate=getViewById(R.id.llOperate);
        tvEoi=getViewById(R.id.tvEoi);
        tvVisit=getViewById(R.id.tvVisit);
        tvReserver=getViewById(R.id.tvReserve);
        tvExpandRe=getViewById(R.id.tvExpandRe);
        tvPurchase=getViewById(R.id.tvPurchaseRe);
        mAllCountryList = new ArrayList<CountrySortModel>();
        pinyinComparator = new CountryComparator();
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();
        getCountryList();

    }

    private void setTimePicker() {
        TimePickerView.Builder builder=new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                edcustmomeDetailedBie.setText(MyUtils.getTime(date));
            }
        });
        builder.setType(TimePickerView.Type.YEAR_MONTH_DAY);
        //时间选择器
        pvTime = new TimePickerView(builder);

        //控制时间范围
        //Calendar calendar = Calendar.getInstance();
        //pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
//        pvTime.setTime(new Date());
//        pvTime.setCyclic(false);
//        pvTime.setCancelable(true);
//        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//
//            @Override
//            public void onTimeSelect(Date date) {
//
//            }
//        });
    }

    private void getCountryList() {
        String[] countryList = getResources().getStringArray(R.array.country_code_list_ch);

        for (int i = 0, length = countryList.length; i < length; i++) {
            String[] country = countryList[i].split("\\*");

            String countryName = country[0];
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
        adapter = new CountrySortAdapter(CustomDetailedActivity.this, mAllCountryList);
        adapter.updateListView(mAllCountryList);
        Log.e(TAG, "changdu" + mAllCountryList.size());

    }

    private void setInfo(CustomeDetailedBean customeDetailedBean) {
        Picasso.with(this).load(Contants.DOMAIN + "/" + customeDetailedBean.getResult().getHead_img()).into(customeIcon);
        edCustomeTrueName.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getTrue_name()) ? (customeDetailedBean.getResult().getTrue_name() + "") : "");
        edcustmomeDetailedBie.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getBirth_date() + "") ? customeDetailedBean.getResult().getBirth_date() + "" : "");
        edCustomeMobile.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getMobile() + "") ? customeDetailedBean.getResult().getMobile() + "" : "");
        edcustmomeDetailedWeChat.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getWechat_number() + "") ? customeDetailedBean.getResult().getWechat_number() + "" : "");
        edcustmomeDetailedQQ.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getQq_number() + "") ? customeDetailedBean.getResult().getQq_number() + "" : "");
        edcustmomeDetailedCity.setText(customeDetailedBean.getResult().getProvince() + " " + customeDetailedBean.getResult().getCity() + " " + customeDetailedBean.getResult().getArea() + "");
        edcustmomeDetailedAddress.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getAddress() + "") ? customeDetailedBean.getResult().getAddress() + "" : "");
        edcustmomeDetailedEmail.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getE_mail() + "") ? customeDetailedBean.getResult().getE_mail() + "" : "");
        edcustmomeDetailedWeJob.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getJob() + "") ? customeDetailedBean.getResult().getJob() + "" : "");
        edcustmomeDetailedSalary.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getIncome() + "") ? customeDetailedBean.getResult().getIncome() + "" : "");
        edcustmomeDetailedCard.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getCard_validity() + "") ? customeDetailedBean.getResult().getCard_validity() + "" : "");
        edcustmomeDetailedPassPort.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getPassport_id() + "") ? customeDetailedBean.getResult().getPassport_id() + "" : "");
        edcustmomeDetailedNationality.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getPassport_country() + "") ? customeDetailedBean.getResult().getPassport_country() + "" : "");
        edcustmomeDetailedKinsfolk.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_name() + "") ? customeDetailedBean.getResult().getFamily_name() + "" : "");
        edcustmomeDetailedRelation.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_relationship() + "") ? customeDetailedBean.getResult().getFamily_relationship() + "" : "");
        edcustmomeDetailedPhone.setText(!TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_mobile() + "") ? customeDetailedBean.getResult().getFamily_mobile() + "" : "");


    }

    private void initProviceSelectView() {


        //国家选择popWindow视图
        nationPopWindowView = LayoutInflater.from(this).inflate(R.layout.nation_picker, null);
        etNationSearch = (EditText) nationPopWindowView.findViewById(R.id.etNationSearch);
        lvNation = (ListView) nationPopWindowView.findViewById(R.id.lvNation);

        lvNation.setAdapter(adapter);

        //ivDelete = (ImageView) nationPopWindowView.findViewById(R.id.ivDelete);
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
        //设置SelectPicPopupWindow弹出窗体动画效果
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
                /*if (searchContent.equals("")) {
                    ivDelete.setVisibility(View.INVISIBLE);
                } else {
                    ivDelete.setVisibility(View.VISIBLE);
                }*/
                if (searchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
                            .search(searchContent, mAllCountryList);
                    Log.e(TAG, "afterTextChanged: " + fileterList.size());
                    adapter.updateListView(fileterList);
                } else {
                    adapter.updateListView(mAllCountryList);
                }
                lvNation.setSelection(0);
            }
        });

        /*ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNationSearch.setText("");
                adapter.updateListView(mAllCountryList);
            }
        });*/

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
                    countryName = mAllCountryList.get(position).countryName;
                    countryNumber = mAllCountryList.get(position).countryNumber;
                }
                edcustmomeDetailedNationality.setText(countryName);
                nationSelectPopWindow.dismiss();
            }
        });

        //省市县选择视图
        contentView = LayoutInflater.from(this).inflate(
                R.layout.addr_picker, null);
        mProvincePicker = (WheelView) contentView.findViewById(R.id.province);
        mCityPicker = (WheelView) contentView.findViewById(R.id.city);
        mCountyPicker = (WheelView) contentView.findViewById(R.id.county);
        boxBtnCancel = (TextView) contentView.findViewById(R.id.box_btn_cancel);
        boxBtnOk = (TextView) contentView.findViewById(R.id.box_btn_ok);

        addrPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        addrPopWindow.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        addrPopWindow.setBackgroundDrawable(dw);
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    mCurrentCityName = mCityData.get(0);

                    ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mDistrictData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mDistrictData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                String cityText = mCityData.get(id);
                if (!mCurrentCityName.equals(cityText)) {
                    mCurrentCityName = cityText;
                    ArrayList<String> mCountyData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mCountyData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                String districtText = mDistrictData.get(id);
                if (!mCurrentDistrictName.equals(districtText)) {
                    mCurrentDistrictName = districtText;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        boxBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddrChoosed = true;
                String addr = mCurrentProviceName + mCurrentCityName;
                if (!mCurrentDistrictName.equals("其他")) {
                    addr += mCurrentDistrictName;
                }
                edcustmomeDetailedCity.setText(addr);
                addrPopWindow.dismiss();
            }
        });

        // 启动线程读取数据
        new Thread() {
            @Override
            public void run() {
                // 读取数据
                isDataLoaded = readAddrDatas();

                // 通知界面线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProvincePicker.setData(mProvinceDatas);
                        mProvincePicker.setDefault(0);
                        mCurrentProviceName = mProvinceDatas.get(0);

                        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                        mCityPicker.setData(mCityData);
                        mCityPicker.setDefault(0);
                        mCurrentCityName = mCityData.get(0);

                        ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                        mCountyPicker.setData(mDistrictData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mDistrictData.get(0);
                    }
                });
            }
        }.start();

        //FIRB选择视图
        firbPwView=LayoutInflater.from(this).inflate(R.layout.firb_popuwindow,null);
        RelativeLayout rlFirb= (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown= (Button) firbPwView.findViewById(R.id.btUnknown);
        btSure = (Button) firbPwView.findViewById(R.id.btSure);
        btFalse = (Button) firbPwView.findViewById(R.id.btFalse);
        firbSelectPopWindow = new PopupWindow(firbPwView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        firbSelectPopWindow.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable firb = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        firbSelectPopWindow.setBackgroundDrawable(firb);
        rlFirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });
        btUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirb.setText("未知");
                firbSelectPopWindow.dismiss();
            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirb.setText("是");
                firbSelectPopWindow.dismiss();
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirb.setText("否");
                firbSelectPopWindow.dismiss();
            }
        });
    }


    @Override
    public void setListener() {
        edcustmomeDetailedCity.setOnClickListener(onClickListener);
        edcustmomeDetailedBie.setOnClickListener(onClickListener);
        ivPhone.setOnClickListener(onClickListener);
        ivEmail.setOnClickListener(onClickListener);
        edcustmomeDetailedNationality.setOnClickListener(onClickListener);
        etFirb.setOnClickListener(onClickListener);
        //llOperate.setOnClickListener(onClickListener);
        tvEoi.setOnClickListener(onClickListener);
        tvVisit.setOnClickListener(onClickListener);
        tvReserver.setOnClickListener(onClickListener);
        tvExpandRe.setOnClickListener(onClickListener);
        tvPurchase.setOnClickListener(onClickListener);
        if (tag.equals("add")) {
            setRightTitle(R.string.customdetaild_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getEditTextData();
                    updateOrAddUserInfo(ADD);
                }
            });
        } else {
            setRightTitle(R.string.customdetaild_save, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                    getEditTextData();
                    updateOrAddUserInfo(UPDATE);
                }
            });
        }

        customeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPopupwindow == null) {
                    myPopupwindow = new MyPopupwindow(CustomDetailedActivity.this, itemsOnClick);
                }
                myPopupwindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });


    }

    private void updateOrAddUserInfo(String updateOrAdd) {
        FinalHttp http = new FinalHttp();
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
                /*Field[] field = userInfo.getClass().getDeclaredFields();

                for (int i = 0; i < field.length; i++) {
                    Field fs = field[i];
                    fs.setAccessible(true);
                    String type = fs.getType().toString();
                    if (type.endsWith("int")) {
                        try {
                            if(fs.get(userInfo)!=null){
                                ajaxParams.put(fs.getName(), (String) fs.get(userInfo));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    continue;
                }*/

        try {
            File file = new File(imagePath);
            if (updateOrAdd.equals(ADD)) {
                ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
                ajaxParams.put("file", file);
            }
            ajaxParams.put("customer_id", userInfo.getCustomer_id() + "");
            ajaxParams.put("surname", userInfo.getSurname());
            ajaxParams.put("first_name", userInfo.getFirst_name());
            ajaxParams.put("en_name", userInfo.getEn_name());
            ajaxParams.put("birth_date", userInfo.getBirth_date() + "");
            ajaxParams.put("mobile", userInfo.getMobile());
            ajaxParams.put("province", userInfo.getProvince());
            ajaxParams.put("city", userInfo.getCity());
            ajaxParams.put("area", userInfo.getArea());
            ajaxParams.put("address", (String) userInfo.getAddress());
            ajaxParams.put("e_mail", userInfo.getE_mail());
            ajaxParams.put("income", userInfo.getIncome() + "");
            ajaxParams.put("wechat_number", (String) userInfo.getWechat_number());
            ajaxParams.put("qq_number", (String) userInfo.getQq_number());
            ajaxParams.put("job", (String) userInfo.getJob());
            if (updateOrAdd.equals(ADD)) {
                ajaxParams.put("card_id", (String) userInfo.getCard_id());
            }
            ajaxParams.put("passport_id", (String) userInfo.getPassport_id());
            ajaxParams.put("passport_country", (String) userInfo.getPassport_country());
            ajaxParams.put("family_name", (String) userInfo.getFamily_name());
            if (updateOrAdd.equals(ADD)) {
                ajaxParams.put("family_first_name", userInfo.getFamily_first_name());
                ajaxParams.put("family_relationship", (String) userInfo.getFamily_relationship());
                ajaxParams.put("family_mobile", (String) userInfo.getFamily_mobile());
            }
            ajaxParams.put("zip_code", userInfo.getZip_code());
            if (updateOrAdd.equals(ADD)) {
                ajaxParams.put("is_firb", userInfo.getIs_firb() + "");
            }
            if (updateOrAdd.equals(ADD)) {
                strUrl = Contants.NEW_CUSTOME;
            } else {
                strUrl = Contants.UPDATE_CUSTOME;
            }

            http.post(strUrl, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    closeDialog();
                    Log.e("", "onSuccess: " + s);
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    closeDialog();
                    ToasShow.showToastBottom(CustomDetailedActivity.this, "提交失败");
                    Log.e("", "onSuccess: " + errorNo);
                }

            });
        } catch (FileNotFoundException e) {
            Log.e("", "onSuccess: " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void getEditTextData() {
        userInfo = new CustomeDetailedBean.ResultBean();
        userInfo.setTrue_name(!TextUtils.isEmpty(edCustomeTrueName.getText().toString()) ? edCustomeTrueName.getText().toString() : "null");
        userInfo.setBirth_date(!TextUtils.isEmpty(edcustmomeDetailedBie.getText().toString()) ? (Integer.parseInt(edcustmomeDetailedBie.getText().toString())) : 000);
        userInfo.setMobile(!TextUtils.isEmpty(edCustomeMobile.getText().toString()) ? edCustomeMobile.getText().toString() : "null");
        userInfo.setProvince(!TextUtils.isEmpty(edcustmomeDetailedCity.getText().toString()) ? edcustmomeDetailedCity.getText().toString() : "null");
        //String[] str = edcustmomeDetailedCity.getText().toString().split("\\s+");
        userInfo.setProvince("null");
        userInfo.setCity("null");
        userInfo.setArea("null");
        userInfo.setAddress(!TextUtils.isEmpty(edcustmomeDetailedAddress.getText().toString()) ? edcustmomeDetailedAddress.getText().toString() : "null");
        userInfo.setE_mail(!TextUtils.isEmpty(edcustmomeDetailedEmail.getText().toString()) ? edcustmomeDetailedEmail.getText().toString() : "null");
        userInfo.setJob(!TextUtils.isEmpty(edcustmomeDetailedWeJob.getText().toString()) ? edcustmomeDetailedWeJob.getText().toString() : "null");
        userInfo.setIncome(!TextUtils.isEmpty(edcustmomeDetailedSalary.getText().toString()) ? (Integer.parseInt(edcustmomeDetailedSalary.getText().toString())) : 000);
        userInfo.setCard_id(!TextUtils.isEmpty(edcustmomeDetailedCard.getText().toString()) ? edcustmomeDetailedCard.getText().toString() : "null");
        userInfo.setPassport_id(!TextUtils.isEmpty(edcustmomeDetailedPassPort.getText().toString()) ? edcustmomeDetailedPassPort.getText().toString() : "null");
        userInfo.setPassport_country(!TextUtils.isEmpty(edcustmomeDetailedNationality.getText().toString()) ? edcustmomeDetailedNationality.getText().toString() : "null");
        userInfo.setFamily_name(!TextUtils.isEmpty(edcustmomeDetailedKinsfolk.getText().toString()) ? edcustmomeDetailedKinsfolk.getText().toString() : "null");
        userInfo.setFamily_relationship(!TextUtils.isEmpty(edcustmomeDetailedRelation.getText().toString()) ? edcustmomeDetailedRelation.getText().toString() : "null");
        userInfo.setFamily_mobile(!TextUtils.isEmpty(edcustmomeDetailedPhone.getText().toString()) ? edcustmomeDetailedPhone.getText().toString() : "null");
        userInfo.setZip_code(!TextUtils.isEmpty(edZipCode.getText().toString()) ? edZipCode.getText().toString() : "null");

    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photoButton:
                    myPopupwindow.dismiss();
                    BitmapUtil.startImageCapture(CustomDetailedActivity.this, ExtraName.TAKE_PICTURE);
                    break;

                case R.id.cancelButton:
                    myPopupwindow.dismiss();
                    break;
            }
        }

    };


    private void getCustomInfo(CustomBean.ResultBean resultBean) {
        showDialog();
        final FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("customer_id", resultBean.getCustomer_id() + "");
        fh.get(Contants.CUSTOME_DETAILED, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Log.e("客户内容", "s:" + s);
                if (null != s) {
                    Gson gson = new Gson();
                    CustomeDetailedBean customeDetailedBean = gson.fromJson(s, CustomeDetailedBean.class);
                    if (customeDetailedBean.getCode().equals("1")) {
                        setInfo(customeDetailedBean);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
                Toast.makeText(CustomDetailedActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * 拍照上传
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case ExtraName.TAKE_PICTURE:
                    if (resultCode == RESULT_OK) {

                        File cameraFile = new File(BitmapUtil.getCacheDir(this), "camera.jpg");
                        if (cameraFile.exists()) {
                            // copy 照片到指定目录下
                            String path = BitmapUtil.getCacheDir(this);
                            File dir = new File(path, "camera");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File pic = new File(dir, System.currentTimeMillis() + "jpg");
                            try {
                                BitmapUtil.copyStream(new FileInputStream(cameraFile), new FileOutputStream(pic));
                                cameraFile.delete();
                                CropImage(pic.getAbsolutePath());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    break;


                // 裁剪图片
                case ExtraName.CROP_IMAGE:
                    if (resultCode == RESULT_OK && null != data) {
                        imagePath = data.getStringExtra("bitmap");
                        Log.e("图片路径**", "paht:" + imagePath);
                        Picasso.with(this).load("file://" + imagePath).into(customeIcon);
                    } else {
                    }


            }

        }

    }

    // 裁剪图片
    private void CropImage(String path) {
        Intent intent = new Intent(CustomDetailedActivity.this, ClipPictureActivity.class);
        intent.putExtra("image-path", path);
        startActivityForResult(intent, CROP_IMAGE);


    }

    /**
     * 读取地址数据，请使用线程进行调用
     *
     * @return
     */
    protected boolean readAddrDatas() {
        List<ProvinceInfoModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            AddrXmlParser handler = new AddrXmlParser();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityInfoModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictInfoModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            mProvinceDatas = new ArrayList<String>();
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityInfoModel> cityList = provinceList.get(i).getCityList();
                ArrayList<String> cityNames = new ArrayList<String>();
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames.add(cityList.get(j).getName());
                    List<DistrictInfoModel> districtList = cityList.get(j).getDistrictList();
                    ArrayList<String> distrinctNameArray = new ArrayList<String>();
                    DistrictInfoModel[] distrinctArray = new DistrictInfoModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictInfoModel districtModel = new DistrictInfoModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray.add(districtModel.getName());
                    }
                    mDistrictDatasMap.put(cityNames.get(j), distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

}
