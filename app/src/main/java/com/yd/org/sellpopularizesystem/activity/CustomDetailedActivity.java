package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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
import com.yd.org.sellpopularizesystem.application.BaseApplication;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.yd.org.sellpopularizesystem.application.ExtraName.CROP_IMAGE;


/**
 * 客户信息详情页面
 */
public class CustomDetailedActivity extends BaseActivity {
    private String TAG = CustomDetailedActivity.class.getSimpleName();
    private CircleImageView customeIcon;
    private EditText edCustomeTrueName, edcustmomeDetailedBie, edCustomeMobile,
            edcustmomeDetailedWeChat, edcustmomeDetailedQQ, edcustmomeDetailedCity,
            edcustmomeDetailedAddress, edcustmomeDetailedEmail, edcustmomeDetailedWeJob,
            edcustmomeDetailedSalary, edcustmomeDetailedCard, edcustmomeDetailedPassPort,
            edcustmomeDetailedNationality, edcustmomeDetailedKinsfolk, edcustmomeDetailedRelation,
            edcustmomeDetailedPhone, edZipCode, etNationSearch, etFirb, etFistName, etEnglishName, etLn, etNation;
    //国家选择列表
    private ListView lvNation;
    private ImageView ivDelete, ivPhone, ivEmail;
    private TextView tvEoi, tvVisit, tvReserver, tvExpandRe, tvPurchase;
    private LinearLayout llOperate;
    private MyPopupwindow myPopupwindow;
    private List<CountrySortModel> mAllCountryList;

    private PopupWindow addrPopWindow, nationSelectPopWindow, firbSelectPopWindow;
    private CustomBean.ResultBean resultBean;
    private String tag, imagePath;

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

    private int flag;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            if (null != resultBean) {
                bundle.putString("customeId", resultBean.getCustomer_id() + "");
            }

            switch (view.getId()) {
                //日期选择
                case R.id.edcustmomeDetailedBie:
                    pvTime.show();
                    break;
                //拍照
                case R.id.ivPhone:
                    if (TextUtils.isEmpty(edCustomeMobile.getText())) {
                        return;
                    } else {
                        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + edCustomeMobile.getText().toString()));
                        if (ActivityCompat.checkSelfPermission(CustomDetailedActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onReques+tPermissionsResult(int requestCode, String[] permissions,
                            //                  int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intentPhone);
                    }
                    break;
                //邮件
                case R.id.ivEmail:
                    if (TextUtils.isEmpty(edCustomeMobile.getText())) {
                        return;
                    } else {
                        Uri uri = Uri.parse("mailto:" + edcustmomeDetailedEmail.getText().toString());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        //intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailtopic)); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.emailcontent)); // 正文
                        startActivity(Intent.createChooser(intent, getString(R.string.chooseemailapp)));
                    }

                    break;
                //城市选择
                case R.id.edcustmomeDetailedCity:
                    if (isDataLoaded) {
                        Log.e(TAG, "onClick: " + edcustmomeDetailedNationality.getText());
                        if (edcustmomeDetailedNationality.getText().toString().equals(getString(R.string.china))) {
                            edcustmomeDetailedCity.setFocusable(false);
                            addrPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                        } else {
                            Log.e(TAG, "onClick***: " + edcustmomeDetailedNationality.getText());
                            edcustmomeDetailedCity.setFocusable(true);
                            edcustmomeDetailedCity.requestFocus();
                        }
                    }
                    break;
                //国家选择
                case R.id.edcustmomeDetailedNationality:
                    flag = 1;
                    nationSelectPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    break;
                //国籍选择
                case R.id.etNation:
                    flag = 2;
                    nationSelectPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.etFirb:
                    firbSelectPopWindow.showAtLocation(CustomDetailedActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);
                    break;
                //EOI
                case R.id.tvEoi:
                    bundle.putString("custocora", "custoeoi");
                    ActivitySkip.forward(CustomDetailedActivity.this, CusOprateRecordActivity.class, bundle);
                    break;
                //拜访
                case R.id.tvVisit:
                    bundle.putString("custocora", "custovisit");
                    ActivitySkip.forward(CustomDetailedActivity.this, CusOprateRecordActivity.class, bundle);
                    break;
                //预约
                case R.id.tvReserve:
                    bundle.putString("custocora", "custoreser");
                    ActivitySkip.forward(CustomDetailedActivity.this, CusOprateRecordActivity.class, bundle);
                    break;
                //推广记录
                case R.id.tvExpandRe:
                    bundle.putString("custocora", "custoexpand");
                    bundle.putSerializable("customer_id", resultBean);
                    ActivitySkip.forward(CustomDetailedActivity.this, PromotionRecordActivity.class, bundle);
                    break;
                //购房记录
                case R.id.tvPurchaseRe:
                    bundle.putString("custocora", "custopurchase");
                    bundle.putSerializable("customer_id", resultBean);
                    ActivitySkip.forward(CustomDetailedActivity.this, PromotionRecordActivity.class, bundle);
                    break;
            }
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_custom_detailed;
    }

    @Override
    public void initView() {
        Views();
        Bundle bundle = getIntent().getExtras();
        if (!TextUtils.isEmpty(bundle.getString("add"))) {
            tag = bundle.getString("add");
            //添加客户
            if (tag.equals("add")) {
                setTitle(R.string.custome_add);
                llOperate.setVisibility(View.GONE);
                //更新客户
            } else {
                setTitle(R.string.customdetaild_title);
                resultBean = (CustomBean.ResultBean) bundle.getSerializable("custome");
                getCustomInfo(resultBean);
            }
        }
        if (bundle.getString(ExtraName.SCALETOCUSTOME) != null) {
            setTitle(R.string.customdetaild_title);
            resultBean = BaseApplication.getInstance().getResultBean();
            getCustomInfo(resultBean);
        }
        initProviceSelectView();
        setTimePicker();


    }

    OptionsPickerView.Builder builder = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {

        }
    });

    private void setCountry() {
        pvOptions = new OptionsPickerView(builder);
    }


    private void Views() {
        etNation = getViewById(R.id.etNation);
        etLn = getViewById(R.id.etLn);
        customeIcon = getViewById(R.id.customeIcon);
        edCustomeTrueName = getViewById(R.id.edCustomeTrueName);
        etFistName = getViewById(R.id.etFistName);
        etEnglishName = getViewById(R.id.etEnglishName);
        edcustmomeDetailedBie = getViewById(R.id.edcustmomeDetailedBie);
        edCustomeMobile = getViewById(R.id.edCustomeMobile);
        ivPhone = getViewById(R.id.ivPhone);
        edcustmomeDetailedWeChat = getViewById(R.id.edcustmomeDetailedWeChat);
        edcustmomeDetailedQQ = getViewById(R.id.edcustmomeDetailedQQ);
        edcustmomeDetailedCity = getViewById(R.id.edcustmomeDetailedCity);
        edcustmomeDetailedAddress = getViewById(R.id.edcustmomeDetailedAddress);
        edZipCode = getViewById(R.id.edcustmeZipCode);
        edcustmomeDetailedEmail = getViewById(R.id.edcustmomeDetailedEmail);
        ivEmail = getViewById(R.id.ivEmail);
        edcustmomeDetailedWeJob = getViewById(R.id.edcustmomeDetailedWeJob);
        edcustmomeDetailedSalary = getViewById(R.id.edcustmomeDetailedSalary);
        edcustmomeDetailedCard = getViewById(R.id.edcustmomeDetailedCard);
        edcustmomeDetailedPassPort = getViewById(R.id.edcustmomeDetailedPassPort);
        edcustmomeDetailedNationality = getViewById(R.id.edcustmomeDetailedNationality);
        edcustmomeDetailedKinsfolk = getViewById(R.id.edcustmomeDetailedKinsfolk);
        edcustmomeDetailedRelation = getViewById(R.id.edcustmomeDetailedRelation);
        edcustmomeDetailedPhone = getViewById(R.id.edcustmomeDetailedPhone);
        etFirb = getViewById(R.id.etFirb);
        llOperate = getViewById(R.id.llOperate);
        tvEoi = getViewById(R.id.tvEoi);
        tvVisit = getViewById(R.id.tvVisit);
        tvReserver = getViewById(R.id.tvReserve);
        tvExpandRe = getViewById(R.id.tvExpandRe);
        tvPurchase = getViewById(R.id.tvPurchaseRe);
        mAllCountryList = new ArrayList<CountrySortModel>();
        pinyinComparator = new CountryComparator();
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();
        getCountryList();

    }

    private void setTimePicker() {
        TimePickerView.Builder builder = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
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
        adapter = new CountrySortAdapter(CustomDetailedActivity.this, mAllCountryList);
        adapter.updateListView(mAllCountryList);
        Log.e(TAG, "changdu" + mAllCountryList.size());

    }


    /**
     * 设置客户信息
     *
     * @param customeDetailedBean
     */
    private void setInfo(CustomeDetailedBean customeDetailedBean) {
        //设置头像
        if (!TextUtils.isEmpty(customeDetailedBean.getResult().getHead_img())) {
            Picasso.with(this).load(Contants.DOMAIN + "/" + customeDetailedBean.getResult().getHead_img()).into(customeIcon);
        }


        //姓
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getSurname())) {
            edCustomeTrueName.setText("");
        } else {
            edCustomeTrueName.setText(customeDetailedBean.getResult().getSurname());

        }

        //名
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getFirst_name())) {
            etFistName.setText("");
        } else {
            etFistName.setText(customeDetailedBean.getResult().getFirst_name());
        }


        //英文名
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getEn_name())) {
            etEnglishName.setText("");
        } else {
            etEnglishName.setText(customeDetailedBean.getResult().getEn_name());
        }

        //生日
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getBirth_date() + "") || String.valueOf(customeDetailedBean.getResult().getBirth_date()).equals("")) {
            edcustmomeDetailedBie.setText("");
        } else {
            edcustmomeDetailedBie.setText(customeDetailedBean.getResult().getBirth_date() + "");
        }

        //电话
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getMobile())) {
            edCustomeMobile.setText("");
        } else {
            edCustomeMobile.setText(customeDetailedBean.getResult().getMobile());
        }

        //邮箱
        if (TextUtils.isEmpty((String)customeDetailedBean.getResult().getE_mail())) {
            edcustmomeDetailedEmail.setText("");
        } else {
            edcustmomeDetailedEmail.setText((String)customeDetailedBean.getResult().getE_mail());
        }

        //微信号
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getWechat_number())) {
            edcustmomeDetailedWeChat.setText("");
        } else {
            edcustmomeDetailedWeChat.setText(customeDetailedBean.getResult().getWechat_number());
        }

        //QQ号
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getQq_number())) {
            edcustmomeDetailedQQ.setText("");
        } else {
            edcustmomeDetailedQQ.setText(customeDetailedBean.getResult().getQq_number());
        }


        //国家地区
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getCountry())) {
            edcustmomeDetailedNationality.setText("");
        } else {
            edcustmomeDetailedNationality.setText(customeDetailedBean.getResult().getCountry());
        }

        //省市区
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getProvince())) {
            edcustmomeDetailedCity.setText("");
        } else {
            edcustmomeDetailedCity.setText(customeDetailedBean.getResult().getProvince());
        }

        //联系地址
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getAddress())) {
            edcustmomeDetailedAddress.setText("");
        } else {
            edcustmomeDetailedAddress.setText(customeDetailedBean.getResult().getAddress());
        }

        //邮编
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getZip_code())) {
            edZipCode.setText("");
        } else {
            edZipCode.setText(customeDetailedBean.getResult().getZip_code());
        }

        //是否是FIRB 0未知 1是  2否
        if (customeDetailedBean.getResult().getIs_firb() == 1) {
            etFirb.setText(R.string.sur);
        } else if (customeDetailedBean.getResult().getIs_firb() == 2) {
            etFirb.setText(R.string.isno);
        } else {
            etFirb.setText(R.string.unknown_);
        }

        //职业
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getJob())) {
            edcustmomeDetailedWeJob.setText("");
        } else {
            edcustmomeDetailedWeJob.setText(customeDetailedBean.getResult().getJob());
        }

        //年薪
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getIncome() + "") || customeDetailedBean.getResult().getIncome() == 0) {
            edcustmomeDetailedSalary.setText("");
        } else {
            edcustmomeDetailedSalary.setText(customeDetailedBean.getResult().getIncome() + "");
        }

        //省份证
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getCard_id())) {
            edcustmomeDetailedCard.setText("");
        } else {
            edcustmomeDetailedCard.setText(customeDetailedBean.getResult().getCard_id());
        }

        //护照
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getPassport_id())) {
            edcustmomeDetailedPassPort.setText("");
        } else {
            edcustmomeDetailedPassPort.setText(customeDetailedBean.getResult().getPassport_id());
        }

        //国籍
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getPassport_country())) {
            etNation.setText("");
        } else {
            etNation.setText(customeDetailedBean.getResult().getPassport_country());
        }

        //亲属姓

        if (TextUtils.isEmpty((String)customeDetailedBean.getResult().getFamily_name())) {
            edcustmomeDetailedKinsfolk.setText("");
        } else {
            edcustmomeDetailedKinsfolk.setText((String)customeDetailedBean.getResult().getFamily_name());
        }
        //亲属名
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_first_name())) {
            etLn.setText("");
        } else {
            etLn.setText(customeDetailedBean.getResult().getFamily_first_name());
        }

        //关系
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_relationship())) {
            edcustmomeDetailedRelation.setText("");
        } else {
            edcustmomeDetailedRelation.setText(customeDetailedBean.getResult().getFamily_relationship());
        }

        //亲属手机号码
        if (TextUtils.isEmpty(customeDetailedBean.getResult().getFamily_mobile())) {
            edcustmomeDetailedPhone.setText("");
        } else {
            edcustmomeDetailedPhone.setText(customeDetailedBean.getResult().getFamily_mobile());
        }


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
                if (flag == 1) {
                    edcustmomeDetailedNationality.setText(countryName);
                } else {
                    etNation.setText(countryName);
                }
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
                String addr = mCurrentProviceName + getString(R.string.single_blank_space) + mCurrentCityName;
                if (!mCurrentDistrictName.equals(getString(R.string.other))) {
                    addr += getString(R.string.single_blank_space) + mCurrentDistrictName;
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
        firbPwView = LayoutInflater.from(this).inflate(R.layout.firb_popuwindow, null);
        RelativeLayout rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown = (Button) firbPwView.findViewById(R.id.btUnknown);
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
                etFirb.setText(getString(R.string.unknown_));
                firbSelectPopWindow.dismiss();
            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirb.setText(getString(R.string.yes));
                firbSelectPopWindow.dismiss();
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirb.setText(getString(R.string.no));
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
        etNation.setOnClickListener(onClickListener);
        etFirb.setOnClickListener(onClickListener);
        //llOperate.setOnClickListener(onClickListener);
        tvEoi.setOnClickListener(onClickListener);
        tvVisit.setOnClickListener(onClickListener);
        tvReserver.setOnClickListener(onClickListener);
        tvExpandRe.setOnClickListener(onClickListener);
        tvPurchase.setOnClickListener(onClickListener);

        //添加数据
        if (tag.equals("add")) {
            setRightTitle(R.string.customdetaild_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getEditTextData(ADD);

                }
            });

            //更新数据
        } else {
            setRightTitle(R.string.customdetaild_save, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getEditTextData(UPDATE);

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

    private void updateOrAddUserInfo(
            final String updateOrAdd, String surname, String first_name, String en_name,
            String birth_date, String mobile, String country, String province, String city, String area,
            String address, String e_mail, String job, String income, String card_id, String passport_id,
            String passport_country, String family_name, String family_first_name, String family_relationship, String family_mobile,
            String zip_code, String is_firb, String wechat_number, String qq_number) {
        showDialog();


        //更新
        if (updateOrAdd.equals(ADD)) {
            strUrl = Contants.NEW_CUSTOME;
        } else {
            strUrl = Contants.UPDATE_CUSTOME;
        }


        FinalHttp http = new FinalHttp();
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        //如果只添加用户,需要userID
        if (updateOrAdd.equals(ADD)) {
            ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        } else {
            ajaxParams.put("customer_id", String.valueOf(resultBean.getCustomer_id()));
        }
        //用户名
        ajaxParams.put("surname", surname);
        ajaxParams.put("first_name", first_name);
        ajaxParams.put("en_name", en_name);
        ajaxParams.put("birth_date", birth_date);
        ajaxParams.put("mobile", mobile);
        ajaxParams.put("country", country);
        ajaxParams.put("province", province);
        ajaxParams.put("city", city);
        ajaxParams.put("area", area);
        ajaxParams.put("address", address);
        ajaxParams.put("e_mail", e_mail);
        ajaxParams.put("job", job);
        ajaxParams.put("income", income);
        ajaxParams.put("card_id", card_id);
        ajaxParams.put("family_first_name", family_first_name);
        ajaxParams.put("family_relationship", family_relationship);
        ajaxParams.put("family_mobile", family_mobile);
        ajaxParams.put("is_firb", is_firb);
        ajaxParams.put("passport_id", passport_id);
        ajaxParams.put("passport_country", passport_country);
        ajaxParams.put("family_name", family_name);
        ajaxParams.put("zip_code", zip_code);
        ajaxParams.put("wechat_number", wechat_number);
        ajaxParams.put("qq_number", qq_number);

        Log.e("生日**", "bir:" + birth_date);
        //Log.e("头像", "file:" + file.getAbsolutePath());
        http.post(strUrl, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                if (updateOrAdd.equals(ADD)) {
                    ToasShow.showToastCenter(CustomDetailedActivity.this, getString(R.string.addsuccess));
                } else {
                    ToasShow.showToastCenter(CustomDetailedActivity.this, getString(R.string.updatesuccess));
                }
                CustomeActivity.customeActivity.handler.sendEmptyMessage(0);
                finish();

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
                ToasShow.showToastBottom(CustomDetailedActivity.this, getString(R.string.submitfail));
            }

        });


    }


    private void getEditTextData(String updateOrAdd) {
        String surname = "", first_name = "", en_name = "", birth_date = "", mobile = "", country = "", province = "", city = "", area = "", address = "", e_mail = "", job = "", income = "", card_id = "", passport_id = "",
                passport_country = "", family_name = "", family_first_name = "", family_relationship = "", family_mobile = "",
                zip_code = "", is_firb = "", wechat_number = "", qq_number = "";
        //姓
        if (!TextUtils.isEmpty(edCustomeTrueName.getText().toString().trim())) {
            surname = edCustomeTrueName.getText().toString().trim();
        } else {
            ToasShow.showToastCenter(this, getString(R.string.fistnamenonull));
            return;
        }

        //名
        if (!TextUtils.isEmpty(etFistName.getText().toString().trim())) {
            first_name = etFistName.getText().toString().trim();
        } else {
            ToasShow.showToastCenter(this, getString(R.string.lastnamenonull));
            return;
        }


        //英文名称
        if (!TextUtils.isEmpty(etEnglishName.getText().toString().trim())) {
            en_name = etEnglishName.getText().toString().trim();
        } else {
            ToasShow.showToastCenter(this, getString(R.string.englishnamenonull));
            return;
        }

        //生日
        if (!TextUtils.isEmpty(edcustmomeDetailedBie.getText().toString().trim())) {
            birth_date = edcustmomeDetailedBie.getText().toString().trim();
        }

        //电话,//邮件
        if (TextUtils.isEmpty(edCustomeMobile.getText().toString().trim()) && TextUtils.isEmpty(edcustmomeDetailedEmail.getText().toString().trim())) {
            ToasShow.showToastCenter(this, getString(R.string.teleonenonull));
            return;
        } else {

            if (!TextUtils.isEmpty(edCustomeMobile.getText().toString().trim())) {
                mobile = edCustomeMobile.getText().toString().trim();
            }

            if (!TextUtils.isEmpty(edcustmomeDetailedEmail.getText().toString().trim())) {
                e_mail = edcustmomeDetailedEmail.getText().toString().trim();
            }

        }


        //国籍或地区
        if (!TextUtils.isEmpty(edcustmomeDetailedNationality.getText().toString().trim())) {
            country = edcustmomeDetailedNationality.getText().toString().trim();
        }

        //省
        if (!TextUtils.isEmpty(edcustmomeDetailedCity.getText().toString())) {
            province = edcustmomeDetailedCity.getText().toString();
            //市区
            city = "";
            area = "";
        }

        //联系地址
        if (!TextUtils.isEmpty(edcustmomeDetailedAddress.getText().toString())) {
            address = edcustmomeDetailedAddress.getText().toString();

        }

        //工作
        if (!TextUtils.isEmpty(edcustmomeDetailedWeJob.getText().toString().trim())) {
            job = edcustmomeDetailedWeJob.getText().toString().trim();
        }

        //收入
        if (!TextUtils.isEmpty(edcustmomeDetailedSalary.getText().toString().trim())) {
            income = edcustmomeDetailedSalary.getText().toString().trim();
        }

        //证件号
        if (!TextUtils.isEmpty(edcustmomeDetailedCard.getText().toString().trim())) {
            card_id = edcustmomeDetailedCard.getText().toString().trim();
        }

        //护照
        if (!TextUtils.isEmpty(edcustmomeDetailedPassPort.getText().toString().trim())) {
            passport_id = edcustmomeDetailedPassPort.getText().toString().trim();
        }

        //国籍
        if (!TextUtils.isEmpty(etNation.getText().toString().trim())) {
            passport_country = etNation.getText().toString().trim();
        }


        //亲属姓
        if (!TextUtils.isEmpty(edcustmomeDetailedKinsfolk.getText().toString().trim())) {
            family_name = edcustmomeDetailedKinsfolk.getText().toString().trim();
        }

        //名
        if (!TextUtils.isEmpty(etLn.getText().toString().trim())) {
            family_first_name = etLn.getText().toString().trim();
        }

        //关系
        if (!TextUtils.isEmpty(edcustmomeDetailedRelation.getText().toString().trim())) {
            family_relationship = edcustmomeDetailedRelation.getText().toString().trim();
        }

        //电话
        if (!TextUtils.isEmpty(edcustmomeDetailedPhone.getText().toString().trim())) {
            family_mobile = edcustmomeDetailedPhone.getText().toString().trim();
        }

        //邮编
        if (!TextUtils.isEmpty(edZipCode.getText().toString().trim())) {
            zip_code = edZipCode.getText().toString().trim();
        } else {
            ToasShow.showToastCenter(this, getString(R.string.nonullpostcode));
            return;
        }


        //是否是FIRB
        if (!TextUtils.isEmpty(etFirb.getText().toString().trim())) {

            if (etFirb.getText().toString().trim().equals(getString(R.string.yes))) {
                is_firb = "1";
            } else if (etFirb.getText().toString().trim().equals(getString(R.string.no))) {
                is_firb = "2";
            } else {
                is_firb = "0";
            }
        }

        //微信
        if (!TextUtils.isEmpty(edcustmomeDetailedWeChat.getText().toString())) {
            wechat_number = edcustmomeDetailedWeChat.getText().toString();
        }
        //qq
        if (!TextUtils.isEmpty(edcustmomeDetailedQQ.getText().toString())) {
            qq_number = edcustmomeDetailedQQ.getText().toString();
        }


        updateOrAddUserInfo(updateOrAdd, surname, first_name, en_name,
                birth_date, mobile, country, province, city, area,
                address, e_mail, job, income, card_id, passport_id,
                passport_country, family_name, family_first_name, family_relationship, family_mobile,
                zip_code, is_firb, wechat_number, qq_number);


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
                case CROP_IMAGE:
                    if (resultCode == RESULT_OK && null != data) {
                        imagePath = data.getStringExtra("bitmap");
                        Picasso.with(this).load("file://" + imagePath).into(customeIcon);
                        if (null != imagePath) {
                            changeHeadImage(imagePath);
                        }
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

    private void changeHeadImage(String imageURL) {

        showDialog();
        try {
            FinalHttp finalHttp = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("customer_id", resultBean.getCustomer_id() + "");
            File file = new File(imageURL);
            ajaxParams.put("file", file);

            finalHttp.post(Contants.UPDATE_HEADIMAGE, ajaxParams, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    closeDialog();
                }

                @Override
                public void onSuccess(String s) {
                    closeDialog();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
