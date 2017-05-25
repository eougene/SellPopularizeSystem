package com.yd.org.sellpopularizesystem.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenu;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuCreator;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuItem;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuListView;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.javaBean.SubscribeListBean;
import com.yd.org.sellpopularizesystem.javaBean.VisitRecord;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CusOprateRecordActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private String customeId;
    private Bundle bundle;
    private String flag;
    private TextView tvMoneyNum, tvPayMethod, tvEoiSubmit, tvDes, tvNoMessage, tvVisitTile, tvVisitSubmit, tvVisitTime;
    private EditText etCertificateTime, etVistTitle, etVistContent;
    private Button btDoacash, btDoaTransfer, btDoaCancel, btFromCamera, btFromAlbum, btPhotoCancel;
    private ImageView ivCertificate, ivCash, ivIdCard, ivAlipay, ivWechatPay;
    private String picPath = "";
    private LinearLayout llSpace, llCertificate, llChooseSpace;
    private Dialog eoiDialog, methodDialog, optionDialog;
    private List<String> weeks;
    private List<String> hours;
    private List<String> minutes;
    private CommonAdapter visitAdapter, eoiAdapter, subscribeAdapter;
    private OptionsPickerView pvCustomTime;
    private SwipeMenuListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private String number = "20";
    private Dialog visitDilog;
    private VisitRecord.ResultBean visitRecord;
    public static CusOprateRecordActivity cusOprateRecordActivity;
    private List<VisitRecord.ResultBean> vrrb;
    private String payment_method;
    private Uri imgUri;
    private List<SubscribeListBean.ResultBean> rbList;

    @Override
    protected int setContentView() {
        cusOprateRecordActivity = this;
        return R.layout.activity_cus_oprate_record;
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        flag = bundle.getString("custocora");
        customeId = (String) bundle.get("customeId");
        initWidgets();
        Log.e("TAG", "initView: " + customeId);
        if (flag.equals("custovisit") || flag.equals("custoreser")) {
            if (flag.equals("custovisit")) {
                setTitle(getString(R.string.visit));
                initVisitDilaog();
                getVisitData(page);
            } else if (flag.equals("custoreser")) {
                setTitle(getString(R.string.yuyue));
                getReservertData();
            }
            clickRightImageView(R.mipmap.addim, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag.equals("custovisit")) {
                        bundle.putString("cora", "visit");
                        ActivitySkip.forward(CusOprateRecordActivity.this, DialogOptionActivity.class, bundle);
                    } else if (flag.equals("custoreser")) {
                        bundle.putString("cora", "reserver");
                        ActivitySkip.forward(CusOprateRecordActivity.this, DialogOptionActivity.class, bundle);
                    }
                    //addOpratePopWin.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM,0,0);
                }
            });
        } else {
            setTitle("EOI");
            getEoiData();
            setRightTitle(R.string.recharge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eoiDialog = new Dialog(CusOprateRecordActivity.this);
                    eoiDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    eoiDialog.setContentView(R.layout.eoi_operate_view);
                    Window dialogWindow = eoiDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.x = MyUtils.getStatusBarHeight(CusOprateRecordActivity.this);
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
                    eoiDialog.show();
                    initDialogViews(eoiDialog);
                }
            });

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ExtraName.UPDATE && flag.equals("custovisit")) {
                getVisitData(page);
            } else if (msg.what == ExtraName.UPDATE && flag.equals("custoreser")) {
                getReservertData();
            } else if (msg.what == ExtraName.SUCCESS) {
                getEoiData();
            }
        }
    };

    private void initWidgets() {
        listView = getViewById(R.id.content_view);
        tvDes = getViewById(R.id.tvDes);
        tvNoMessage = getViewById(R.id.noInfomation);
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);

    }

    private void initDialogViews(Dialog dialog) {
        tvMoneyNum = (TextView) dialog.findViewById(R.id.tvMoneyNum);
        tvPayMethod = (TextView) dialog.findViewById(R.id.tvPayMethod);
        tvEoiSubmit = (TextView) dialog.findViewById(R.id.tvEoiSubmit);
        ivCash = (ImageView) dialog.findViewById(R.id.ivCash);
        ivIdCard = (ImageView) dialog.findViewById(R.id.ivIdCard);
        ivAlipay = (ImageView) dialog.findViewById(R.id.ivAlipay);
        ivWechatPay = (ImageView) dialog.findViewById(R.id.ivWeixinPay);
        llCertificate = (LinearLayout) dialog.findViewById(R.id.llCertificate);
        // etCertificateTime = (EditText) dialog.findViewById(R.id.tvUploadTime);
        ivCertificate = (ImageView) dialog.findViewById(R.id.ivCertificate);
        ivCash.setOnClickListener(mOnClickListener);
        ivIdCard.setOnClickListener(mOnClickListener);
        ivAlipay.setOnClickListener(mOnClickListener);
        ivWechatPay.setOnClickListener(mOnClickListener);
        //tvPayMethod.setOnClickListener(mOnClickListener);
        tvEoiSubmit.setOnClickListener(mOnClickListener);
        //etCertificateTime.setOnClickListener(mOnClickListener);
        ivCertificate.setOnClickListener(mOnClickListener);
        //初始化自定义选择器的数据
        initOptionData();
        //初始化自定义选择器
        initOptionPicker();
    }

    private void initOptionData() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMon = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, mYear);
        weeks = new ArrayList<String>();
        hours = new ArrayList<String>();
        minutes = new ArrayList<String>();
        for (int i = 1; i < 13; i++) {
            ca.set(Calendar.MONTH, i);
            int days = 0;
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
                days = 32;
            } else if (i == 2) {
                if (mYear % 4 != 0 && mYear % 400 != 0) {
                    days = 29;
                } else {
                    days = 30;
                }
            } else {
                days = 31;
            }
            for (int j = 1; j < days; j++) {
                if (i == mMon && j == mDay) {
                    weeks.add("今天");
                } else {
                    String dayOfWeek = getdayOfWeek(ca, i, j);
                    String str = i + "月" + j + "日" + "周" + dayOfWeek;
                    weeks.add(str);
                }

            }
        }
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format("%02d", i));
        }
    }

    //根据日期判断星期几
    private String getdayOfWeek(Calendar ca, int i, int j) {
        ca.set(Calendar.MONTH, i - 1);
        ca.set(Calendar.DAY_OF_MONTH, j);
        int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
        String str = "null";
        switch (dayOfWeek) {
            case 1:
                str = "日";
                break;
            case 2:
                str = "一";
                break;
            case 3:
                str = "二";
                break;
            case 4:
                str = "三";
                break;
            case 5:
                str = "四";
                break;
            case 6:
                str = "五";
                break;
            case 7:
                str = "六";
                break;
        }
        return str;
    }

    private Calendar cal;

    private void initOptionPicker() {
        cal = Calendar.getInstance();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                setText(cal, options1, options2, options3);
            }
        }).setTitleText(getString(R.string.uploadetime)).setTitleColor(R.color.black)
                .setCyclic(true, true, true)/*.setOutSideCancelable(false)*/
                .setSelectOptions(weeks.indexOf("今天"), hours.indexOf(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))), minutes.indexOf(String.format("%02d", cal.get(Calendar.MINUTE))))
                .isDialog(true);

        builder.setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvFinish = (TextView) v.findViewById(R.id.tv_finish);
                TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                ImageView ivUp = (ImageView) v.findViewById(R.id.ivUp);
                ImageView ivDown = (ImageView) v.findViewById(R.id.ivDown);
                tvTitle.setText(getString(R.string.uploadetime));
                ivUp.setVisibility(View.GONE);
                ivDown.setVisibility(View.GONE);
                tvFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomTime.returnData();
                    }
                });

            }
        });
        pvCustomTime = new OptionsPickerView(builder);
        View view = LayoutInflater.from(this).inflate(R.layout.pickerview_custom_time, null);
        LinearLayout llOptionPicker = (LinearLayout) view.findViewById(R.id.optionspicker);
        pvCustomTime.setNPicker(weeks, hours, minutes);
    }

    private void setText(Calendar cal, int options1, int options2, int options3) {
        Calendar ca = Calendar.getInstance();
        String str1 = (String) weeks.get(options1);
        String str2 = (String) hours.get(options2);
        String str3 = (String) minutes.get(options3);
                /*Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(str);
                m.find();
                String newStr = m.group();*/
        String[] nums = new String[2];
        if (str1.equals(getString(R.string.today))) {
            nums[0] = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            nums[1] = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        } else {
            nums = str1.split("\\D+");
        }
        etCertificateTime.setText(String.format("%02d", Integer.parseInt(nums[0])) + "/"
                + String.format("%02d", Integer.parseInt(nums[1])) + getString(R.string.blank_space) + str2 + ":" + str3);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvPayMethod:
                    // initPayMethodDialog();
                    //payMethodPop.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent),Gravity.BOTTOM, 0,0);
                    break;
                case R.id.btDoaCash:
                    tvPayMethod.setText(btDoacash.getText());
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    methodDialog.dismiss();
                    break;
                case R.id.btDoaTransfer:
                    tvPayMethod.setText(btDoaTransfer.getText());
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    methodDialog.dismiss();
                    break;
                /*case R.id.etUploadTime:
                    pvCustomTime.show();
                    break;*/
                //eoi充值
                case R.id.ivCash:
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    tvMoneyNum.setText("$ 300.00");
                    tvPayMethod.setText(getString(R.string.recash));
                    payment_method = "1";
                    break;
                case R.id.ivIdCard:
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    tvMoneyNum.setText("$ 300.00");
                    tvPayMethod.setText(getString(R.string.transfer));
                    payment_method = "4";
                    break;
                case R.id.ivAlipay:
                    if (llCertificate.getVisibility() == View.VISIBLE) {
                        llCertificate.setVisibility(View.GONE);
                    }
                    tvPayMethod.setText(R.string.alipay);
                    tvMoneyNum.setText("￥ 2000.00");
                    payment_method = "6";
                    break;
                case R.id.ivWeixinPay:
                    if (llCertificate.getVisibility() == View.VISIBLE) {
                        llCertificate.setVisibility(View.GONE);
                    }
                    tvPayMethod.setText(R.string.wechatpay);
                    tvMoneyNum.setText("￥ 2000.00");
                    payment_method = "7";
                    break;
                case R.id.ivCertificate:
                    //initOptionDialog();
                    //BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                    takePhoto();
                    //BitmapUtil.gotoSysPic(CusOprateRecordActivity.this,ExtraName.ALBUM_PICTURE);
                    break;
                case R.id.tvEoiSubmit:
                    Log.e("submitEoi", "onClick: " + "submitEoi");
                    if (llCertificate.getVisibility() == View.VISIBLE) {
                        if (picPath == "") {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, getString(R.string.picpath));
                        }
                    }
                    if (tvMoneyNum.getText().equals("-")) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, getString(R.string.pay_method));
                    } else {
                        //提交eoi
                        Log.e("submitEoi", "onClick: " + "submitEoi");
                        submitEoi();
                    }
                    break;

               /* case R.id.btFromCamera:
                    optionDialog.dismiss();
                    BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                    break;*/
                /*case R.id.btFromAlbum:
                    optionDialog.dismiss();
                    BitmapUtil.gotoSysPic(CusOprateRecordActivity.this, ExtraName.ALBUM_PICTURE);
                    break;*/
                case R.id.btPhotoCancel:
                    optionDialog.dismiss();
                    break;
                case R.id.btDoaCancel:
                    methodDialog.dismiss();
                    break;
                case R.id.llSpace:
                    methodDialog.dismiss();
                    break;
                case R.id.llChooseSpace:
                    optionDialog.dismiss();
                    break;
                case R.id.tvVisitSubmit:
                    if (etVistTitle.getText().toString().equals(visitRecord.getTitle().toString()) && etVistContent.getText().toString().equals(visitRecord.getContent().toString())) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, getString(R.string.tips));
                    } else {
                        updateVisit();
                    }
                    break;
            }
        }
    };

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            ContentValues contentValues = new ContentValues(2);
            //如果想拍完存在系统相机的默认目录,改为
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, UUID.randomUUID().toString() + ".jpg");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.Images.Media.SIZE, 1024 * 200);
            imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(cameraIntent, ExtraName.TAKE_PICTURE);
        }

    }

    private void updateVisit() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("v_log_id", visitRecord.getV_log_id() + "");
        ajaxParams.put("customer_id", customeId);
        ajaxParams.put("title", etVistTitle.getText().toString());
        ajaxParams.put("content", etVistContent.getText().toString());
        fh.post(Contants.UPDATE_VISIT_RECORDER, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                        handler.sendEmptyMessage(ExtraName.UPDATE);
                        visitDilog.dismiss();
                    } else {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                        visitDilog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
            }
        });
    }

    private void initOptionDialog() {
        optionDialog = new Dialog(CusOprateRecordActivity.this, R.style.ActionSheetDialogStyle);
        optionDialog.setContentView(R.layout.eoi_choose_oprate_dialog);
        optionDialog.setCanceledOnTouchOutside(true);
        btFromCamera = (Button) optionDialog.findViewById(R.id.btFromCamera);
        btFromAlbum = (Button) optionDialog.findViewById(R.id.btFromAlbum);
        btPhotoCancel = (Button) optionDialog.findViewById(R.id.btPhotoCancel);
        llChooseSpace = (LinearLayout) optionDialog.findViewById(R.id.llChooseSpace);
        btFromCamera.setOnClickListener(mOnClickListener);
        btFromAlbum.setOnClickListener(mOnClickListener);
        btPhotoCancel.setOnClickListener(mOnClickListener);
        llChooseSpace.setOnClickListener(mOnClickListener);
        //获取当前Activity所在的窗体
        Window dialogWindow = optionDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        // 将属性设置给窗体
        dialogWindow.setAttributes(lp);
        optionDialog.show();
    }

    private void initPayMethodDialog() {
        methodDialog = new Dialog(CusOprateRecordActivity.this, R.style.ActionSheetDialogStyle);
        //methodDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        methodDialog.setContentView(R.layout.doa_paymetod_pop);
        btDoacash = (Button) methodDialog.findViewById(R.id.btDoaCash);
        btDoaTransfer = (Button) methodDialog.findViewById(R.id.btDoaTransfer);
        btDoaCancel = (Button) methodDialog.findViewById(R.id.btDoaCancel);
        llSpace = (LinearLayout) methodDialog.findViewById(R.id.llSpace);
        btDoacash.setOnClickListener(mOnClickListener);
        btDoaTransfer.setOnClickListener(mOnClickListener);
        btDoaCancel.setOnClickListener(mOnClickListener);
        llSpace.setOnClickListener(mOnClickListener);
        //获取当前Activity所在的窗体
        Window dialogWindow = methodDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        // 将属性设置给窗体
        dialogWindow.setAttributes(lp);
        methodDialog.show();
    }

    private void submitEoi() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        try {
            ajaxParams.put("client", customeId);
            ajaxParams.put("sales_id", SharedPreferencesHelps.getUserID());
            ajaxParams.put("payment_method", payment_method);
            ajaxParams.put("payment_amount", tvMoneyNum.getText().toString());
            if (!picPath.equals("")) {
                File picFile = new File(picPath);
                ajaxParams.put("file", picFile);
            }
            ajaxParams.put("pay_time", "");
            ajaxParams.put("currency", tvMoneyNum.getText().toString().startsWith("$") ? "au" : "RMB");
            ajaxParams.put("purchaseReason", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fh.post(Contants.EOI_RECHARGE, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {

                        eoiDialog.dismiss();
                        handler.sendEmptyMessage(ExtraName.SUCCESS);

                        if (payment_method.equals("6") || payment_method.equals("7")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("payurlId", json.getString("trust_account_id"));
                            bundle.putString("payment_method", payment_method);
                            ActivitySkip.forward(CusOprateRecordActivity.this, PaymentQrActivity.class, bundle);
                        } else {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                        }
                    } else {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
            }
        });

    }

    private void getEoiData() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", "1");
        ajaxParams.put("number", "20");
        ajaxParams.put("company_id", BaseApplication.getInstance().getResultBean().getCompany_id() + "");
        ajaxParams.put("client", BaseApplication.getInstance().getResultBean().getCustomer_id() + "");
        ajaxParams.put("property_id", "");
        ajaxParams.put("is_use", "");
        ajaxParams.put("house", "");
        fh.get(Contants.EOI_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Gson gson = new Gson();
                EoilistBean eoilistBean = gson.fromJson(s, EoilistBean.class);
                if (eoilistBean.getResult().size() > 0) {
                    List<EoilistBean.ResultBean> eoiList = eoilistBean.getResult();
                    eoiAdapter = new CommonAdapter<EoilistBean.ResultBean>(CusOprateRecordActivity.this, eoiList, R.layout.eoi_listview_item_layout) {

                        @Override
                        public void convert(ViewHolder holder, EoilistBean.ResultBean item) {
                            holder.setText(R.id.tvEoiNum, item.getProduct_eois_id() + " - ");
                            if (item.getEoi_moneycheck_time().equals("") && (item.getPayment_method() == 6 || item.getPayment_method() == 7)) {
                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.nopay));
                            } else if (item.getEoi_moneycheck_time().equals("") && (item.getPayment_method() == 1 || item.getPayment_method() == 4)) {
                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.stillneedcheck));
                            } else if (item.getCancel_apply_status() == 1) {
                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.refund));
                            } else {
                                holder.setText(R.id.tvEoiStatusDes, getString(R.string.nopay));
                            }
                        }
                    };
                    listView.setAdapter(eoiAdapter);
                    if (tvDes.getVisibility() == View.VISIBLE) {
                        tvDes.setVisibility(View.GONE);
                    }
                } else {
                    tvDes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
            }
        });
    }

    @Override
    public void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag.equals("custovisit")) {
                    visitRecord = (VisitRecord.ResultBean) visitAdapter.getItem(position);
                    visitDilog.show();
                    tvVisitTile.setText(getString(R.string.details));
                    etVistTitle.setText(visitRecord.getTitle());
                    tvVisitTime.setText(MyUtils.date2String("MM/dd HH:mm", visitRecord.getAdd_time() * 1000));
                    etVistContent.setText(visitRecord.getContent());
                    tvVisitSubmit.setText(getString(R.string.update));
                    tvVisitSubmit.setOnClickListener(mOnClickListener);
                } else if (flag.equals("custoreser")) {
                    Bundle bun = new Bundle();
                    bun.putSerializable("subrb", rbList.get(position));
                    ActivitySkip.forward(CusOprateRecordActivity.this, DialogOptionActivity.class, bun);
                } else {
                    EoilistBean.ResultBean eoilistBean = (EoilistBean.ResultBean) eoiAdapter.getItem(position);
                    Log.e("支付**", "play:" + eoilistBean.getEoi_money_url());
                    if (eoilistBean.getPayment_method() == 6 || eoilistBean.getPayment_method() == 7) {
                        Bundle bundle = new Bundle();
                        bundle.putString("payurlId", eoilistBean.getEoi_money_url());
                        bundle.putString("payment_method", eoilistBean.getPayment_method() + "");
                        ActivitySkip.forward(CusOprateRecordActivity.this, PaymentQrActivity.class, bundle);
                    }
                }
            }
        });


    }

    private void initVisitDilaog() {
        visitDilog = new Dialog(CusOprateRecordActivity.this);
        visitDilog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        visitDilog.setContentView(R.layout.visit_operate_view);
        Window dialogWindow = visitDilog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = MyUtils.getStatusBarHeight(CusOprateRecordActivity.this);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        initVisitDilaogViews(visitDilog);
    }

    private void initVisitDilaogViews(Dialog visitDilog) {
        tvVisitTile = (TextView) visitDilog.findViewById(R.id.tvVisitTile);
        etVistTitle = (EditText) visitDilog.findViewById(R.id.etVistTitle);
        tvVisitTime = (TextView) visitDilog.findViewById(R.id.tvVisitTime);
        etVistContent = (EditText) visitDilog.findViewById(R.id.etVistContent);
        tvVisitSubmit = (TextView) visitDilog.findViewById(R.id.tvVisitSubmit);
    }

    private void getVisitData(int page) {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("customer_id", customeId);
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", number);
        fh.get(Contants.VISIT_RECORD_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                super.onSuccess(s);
                Log.e("tag", "onSuccess: " + s);
                Gson gson = new Gson();
                VisitRecord visitRecord = gson.fromJson(s, VisitRecord.class);
                if (visitRecord.getResult().size() > 0) {
                    vrrb = new ArrayList<VisitRecord.ResultBean>();
                    vrrb = visitRecord.getResult();
                    visitAdapter = new CommonAdapter<VisitRecord.ResultBean>(CusOprateRecordActivity.this, vrrb, R.layout.visit_listview_item_layout) {
                        @Override
                        public void convert(ViewHolder holder, VisitRecord.ResultBean item) {
                            holder.setText(R.id.tvVisitTime, MyUtils.date2String("yyyy/MM/dd", Long.valueOf(item.getAdd_time() + "000")));
                            holder.setText(R.id.tvVisitTitle, item.getTitle());
                            holder.setText(R.id.tvVisitContent, item.getContent());
                        }
                    };
                    //visitAdapter=new SlidingListviewAdapter(CusOprateRecordActivity.this,vrrb,listView.getRightViewWidth());
                    listView.setAdapter(visitAdapter);
                    if (tvDes.getVisibility() == View.VISIBLE) {
                        tvDes.setVisibility(View.GONE);
                    }
                    initMenuListView();
                } else {
                    tvDes.setVisibility(View.VISIBLE);
                    tvDes.setText(getString(R.string.noinformation));
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void initMenuListView() {
        //创建一个SwipeMenuCreator供ListView使用
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建一个侧滑菜单
                SwipeMenuItem deleteItem = new SwipeMenuItem(CusOprateRecordActivity.this);
                //给该侧滑菜单设置背景
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                //设置宽度
                deleteItem.setWidth(MyUtils.dp2px(CusOprateRecordActivity.this, 80));
                //设置文字
                deleteItem.setTitle(getString(R.string.delete));
                //字体大小
                deleteItem.setTitleSize(16);
                //字体颜色
                deleteItem.setTitleColor(Color.WHITE);
                //加入到侧滑菜单中
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        //侧滑菜单的相应事件
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://第一个添加的菜单的响应时间(打开)
                        if (flag.equals("custovisit")) {
                            if (vrrb.size() > 0) {
                                int vlog_id = vrrb.get(position).getV_log_id();
                                vrrb.remove(position);
                                visitAdapter.notifyDataSetChanged();
                                if (vrrb.size() == 0) {
                                    tvDes.setVisibility(View.VISIBLE);
                                }
                                removeVistOrResRecord(vlog_id);
                            }
                        } else if (flag.equals("custoreser")) {
                            if (rbList.size() > 0) {
                                Log.e("size", "onMenuItemClick: " + rbList.size() + "\n" + position);
                                int log_id = rbList.get(position).getO_log_id();
                                rbList.remove(position);
                                subscribeAdapter.notifyDataSetChanged();
                                if (rbList.size() == 0) {
                                    tvDes.setVisibility(View.VISIBLE);
                                }
                                removeVistOrResRecord(log_id);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void removeVistOrResRecord(int id) {
        showDialog();
        String strUrl = "";
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        if (flag.equals("custovisit")) {
            ajaxParams.put("v_log_id", id + "");
            strUrl = Contants.REMOVE_VISIT_RECORD;
        } else if (flag.equals("custoreser")) {
            ajaxParams.put("o_log_id", id + "");
            strUrl = Contants.REMOVE_RESERVER_RECORD;
        }

        fh.post(strUrl, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void getReservertData() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("customer_id", customeId);
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", number);
        fh.get(Contants.RESERVER_RECORDER_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Gson gson = new Gson();
                SubscribeListBean slb = gson.fromJson(s, SubscribeListBean.class);
                if (slb.getCode().equals("1")) {
                    if (slb.getResult().size() > 0) {
                        rbList = slb.getResult();
                        subscribeAdapter = new CommonAdapter<SubscribeListBean.ResultBean>(CusOprateRecordActivity.this, rbList, R.layout.reserver_listview_item_layout) {
                            @Override
                            public void convert(ViewHolder holder, SubscribeListBean.ResultBean item) {
                                holder.setText(R.id.tvSubscribeTime, MyUtils.date2String("MM/dd HH:mm", item.getOrder_time() * 1000));
                                holder.setText(R.id.tvSubscribeContent, item.getContent());
                            }
                        };
                        listView.setAdapter(subscribeAdapter);
                        if (tvDes.getVisibility() == View.VISIBLE) {
                            tvDes.setVisibility(View.GONE);
                        }
                        initMenuListView();
                    } else {
                        tvDes.setVisibility(View.VISIBLE);
                        tvDes.setText(getString(R.string.noinformation));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照上传
                case ExtraName.TAKE_PICTURE:
                   /* File cameraFile = new File(BitmapUtil.getCacheDir(this), "camera.jpg");
                    if (cameraFile.exists()) {
                        // copy 照片到指定目录下
                        String path = BitmapUtil.getCacheDir(this);
                        File dir = new File(path, "camera");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                       File picFile = new File(dir, System.currentTimeMillis() + ".jpg");

                        try {
                           // BitmapUtil.copyStream(new FileInputStream(cameraFile), new FileOutputStream(picFile));
                            //cameraFile.delete();
                            picPath = picFile.getAbsolutePath();
                            Log.e("picFile", picPath);
                            Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").resize(100, 100).into(ivCertificate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
                    try {
                        if (null != data && null != data.getData()) {
                            picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, data.getData(), null, null);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                            ivCertificate.setImageBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath));
                        } else {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                            Log.e("Authority", "onActivityResult: " + imgUri + "\n" + imgUri.getAuthority());
                            picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, imgUri, null, null);
                            Log.e("picPath", "onActivityResult: " + picPath);
                            ivCertificate.setImageBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath));
                            /*Picasso.with(this).load(picPath).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    ivCertificate.setImageBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });*/
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Log.e("imgUri2", "onActivityResult: "+imgUri+"\n"+imgUri.getPath()+"\n"+data.getData()+"\n"+data.getData().getPath());
                    break;
                //从相册选取图片
                case ExtraName.ALBUM_PICTURE:
                    Uri selectedPhotoUri = null;
                    //File picFile= null;
                    selectedPhotoUri = data.getData();
                        /*Bitmap mBitmap = BitmapUtil.getThumbnail(this, selectedPhotoUri, ivCertificate.getWidth(), ivCertificate.getHeight());
                        ivCertificate.setImageBitmap(mBitmap);*/
                    picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, selectedPhotoUri, null, null);
                    Picasso.with(this).load(selectedPhotoUri).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        getVisitData(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getVisitData(page);
    }

}
