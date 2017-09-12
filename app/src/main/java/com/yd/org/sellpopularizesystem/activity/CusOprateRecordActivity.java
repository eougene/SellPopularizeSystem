package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenu;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuCreator;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuItem;
import com.yd.org.sellpopularizesystem.internal.SwipeListview.SwipeMenuListView;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.SubscribeListBean;
import com.yd.org.sellpopularizesystem.javaBean.VisitRecord;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ObjectSaveUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.File;
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
    private List<String> weeks = new ArrayList<>();
    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private CommonAdapter visitAdapter, eoiAdapter, subscribeAdapter;
    private OptionsPickerView pvCustomTime;
    private SwipeMenuListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private Dialog visitDilog;
    private VisitRecord.ResultBean visitRecord;
    public static CusOprateRecordActivity cusOprateRecordActivity;
    private List<VisitRecord.ResultBean> vrrb = new ArrayList<>();
    private String payment_method;
    private Uri imgUri;
    private List<SubscribeListBean.ResultBean> rbList = new ArrayList<>();
    private List<EoilistBean.ResultBean> eoiList = new ArrayList<>();
    private PopupWindow firbSelectPopWindow, firbSelectPopWindowCancel;
    private View firbPwView;
    private Button btUnknown, btSure, btFalse;
    private String eoi_ID = "", payMe = "";
    private int type = 0;



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
        showZh();
        cancelEOI();
        if (flag.equals("custovisit") || flag.equals("custoreser")) {
            if (flag.equals("custovisit")) {
                setTitle(getString(R.string.visit));
                initVisitDilaog();
                getVisitData(page, true);
            } else if (flag.equals("custoreser")) {
                setTitle(getString(R.string.yuyuerecord));
                getReservertData(page, true);
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
                }
            });
        } else {
            setTitle("EOI");
            getEoiData(page, true);
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
                getVisitData(page, true);
            } else if (msg.what == ExtraName.UPDATE && flag.equals("custoreser")) {
                getReservertData(page, true);
            } else if (msg.what == ExtraName.SUCCESS) {
                getEoiData(page, true);
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
        MyUtils.getInstance().getOptionData(CusOprateRecordActivity.this, weeks, hours, minutes);
        //初始化自定义选择器
        initOptionPicker();
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
        String str1 = weeks.get(options1);
        String str2 = hours.get(options2);
        String str3 = minutes.get(options3);
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
                    if (Build.VERSION.SDK_INT < 23) {
                        //BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                        takePhoto();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {// 全部授权成功回调
                                        // 执行具体业务
                                        //BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                                        takePhoto();
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                                        for (String permission : deniedPermissionList) {
                                            ToasShow.showToastCenter(CusOprateRecordActivity.this, permission.toString());
                                        }
                                    }
                                });
                    }
                    //BitmapUtil.gotoSysPic(CusOprateRecordActivity.this,ExtraName.ALBUM_PICTURE);
                    break;
                //提交eoi
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
                //提交拜访记录
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
        EasyHttp.post(Contants.CHANGE_PASSWORD)
                .cacheMode(CacheMode.NO_CACHE)
                .params("v_log_id", visitRecord.getV_log_id() + "")
                .params("customer_id", customeId)
                .params("title", etVistTitle.getText().toString())
                .params("content", etVistContent.getText().toString())
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {

                        closeDialog();
                        ToasShow.showToast(CusOprateRecordActivity.this, getResources().getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();

                        Gson gs = new Gson();
                        ErrorBean result = gs.fromJson(json, ErrorBean.class);
                        if (result.getCode().equals("1")) {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, result.getMsg());
                            handler.sendEmptyMessage(ExtraName.UPDATE);
                            visitDilog.dismiss();

                        } else {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, result.getMsg());
                            visitDilog.dismiss();
                        }

                    }
                });


    }


    private void submitEoi_01() {


        UIProgressResponseCallBack mUIProgressResponseCallBack = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);



            }
        };
        HttpParams httpParams = new HttpParams();
        httpParams.put("eoi_id", eoi_ID);
        httpParams.put("pay_time", (System.currentTimeMillis() / 1000 + ""));
        httpParams.put("payment_method", payMe);

        if (!picPath.equals("")) {
            File picFile = new File(picPath);
            httpParams.put("file", picFile, mUIProgressResponseCallBack);
        }

        EasyHttp.post(Contants.UPLOAD_EOI_MONEY)
                .cacheMode(CacheMode.NO_CACHE)
                .params(httpParams)
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {

                        closeDialog();
                        ToasShow.showToast(CusOprateRecordActivity.this, getResources().getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();

                        Gson gs = new Gson();
                        ErrorBean result = gs.fromJson(json, ErrorBean.class);
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, result.getMsg());

                    }
                });


    }

    private void submitEoi() {


        UIProgressResponseCallBack mUIProgressResponseCallBack = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);



            }
        };

        HttpParams httpParams = new HttpParams();
        httpParams.put("client", customeId);
        httpParams.put("sales_id", SharedPreferencesHelps.getUserID());
        httpParams.put("payment_method", payment_method);
        httpParams.put("payment_amount", tvMoneyNum.getText().toString());
        httpParams.put("pay_time", "");
        httpParams.put("currency", tvMoneyNum.getText().toString().startsWith("$") ? "au" : "RMB");


        if (!picPath.equals("")) {
            File picFile = new File(picPath);
            httpParams.put("file", picFile, mUIProgressResponseCallBack);
        }

        EasyHttp.post(Contants.EOI_RECHARGE)
                .cacheMode(CacheMode.NO_CACHE)
                .params(httpParams)
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {

                        closeDialog();
                        ToasShow.showToast(CusOprateRecordActivity.this, getResources().getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();
                        Gson gs = new Gson();
                        ErrorBean result = gs.fromJson(json, ErrorBean.class);
                        if (result.getCode().equals("1")) {
                            eoiDialog.dismiss();
                            handler.sendEmptyMessage(ExtraName.SUCCESS);
                            if (payment_method.equals("6") || payment_method.equals("7")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("payurlId", result.getMsg());
                                bundle.putString("payment_method", payment_method);
                                ActivitySkip.forward(CusOprateRecordActivity.this, PaymentQrActivity.class, bundle);
                            }

                        } else {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, result.getMsg());
                        }
                    }
                });


    }

    private void getEoiData(int page, final boolean isRel) {
        EasyHttp.get(Contants.EOI_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName()+"1")
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("page", String.valueOf(page))
                .params("number", "100")
                .params("company_id", ((CustomBean.ResultBean) ObjectSaveUtil.readObject(CusOprateRecordActivity.this, "custome")).getCompany_id() + "")
                .params("client", ((CustomBean.ResultBean) ObjectSaveUtil.readObject(CusOprateRecordActivity.this, "custome")).getCustomer_id() + "")
                .params("property_id", "")
                .params("is_use", "")
                .params("house", "")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        closeDialog();
                        Gson gson = new Gson();
                        EoilistBean eoilistBean = gson.fromJson(json, EoilistBean.class);
                        eoiList = eoilistBean.getResult();


                        if (isRel) {

                            if (eoiList.size() == 0) {
                                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }

                            eoiAdapter = new CommonAdapter<EoilistBean.ResultBean>(CusOprateRecordActivity.this, eoiList, R.layout.eoi_listview_item_layout) {

                                @Override
                                public void convert(ViewHolder holder, EoilistBean.ResultBean item) {
                                    holder.setText(R.id.tvEoiNum, item.getProduct_eois_id() + " - ");


                                    //尚未付款,未使用
                                    if (item.getEoi_money_status() == 1 && item.getIs_use() != 1) {


                                        //凭证已上传
                                        if (item.getEoi_moneycheck_time().equals("") && (item.getPayment_method() == 1 || item.getPayment_method() == 4)) {
                                            holder.setText(R.id.tvEoiStatusDes, getString(R.string.stillneedcheck));
                                            //未付款
                                        } else {
                                            holder.setText(R.id.tvEoiStatusDes, getString(R.string.nopay));
                                        }

                                        //已付款,是否使用
                                    } else if (item.getEoi_money_status() == 2 || item.getEoi_money_status() == 1) {

                                        //未使用
                                        if (item.getIs_use() == 0) {
                                            holder.setLinear_Gone(R.id.promtionLinear);
                                            holder.setText(R.id.tvEoiStatusDes, getString(R.string.nouse));

                                            //已使用
                                        } else {
                                            holder.setLinear(R.id.promtionLinear);
                                            holder.setText(R.id.tvProm01, "0");
                                            holder.setText(R.id.tvProm02, "0");
                                            holder.setText(R.id.tvProm03, "0");
                                            holder.setText(R.id.tvEoiStatusDes, getString(R.string.isuse));
                                        }

                                        //请重新上传凭证
                                    } else if (item.getEoi_money_status() == 3) {
                                        type = 1;
                                        holder.setText(R.id.tvEoiStatusDes, getString(R.string.evidence));

                                    }


                                }
                            };

                            listView.setAdapter(eoiAdapter);
                        } else {
                            eoiAdapter.addMore(eoiList);
                        }
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

                    /**
                     * eoi_money_status=1  EOI已充值，待审核
                     * eoi_money_status=2  EOI充值已审核通过
                     * eoi_money_status=3  EOI充值未审核通过
                     */


                    EoilistBean.ResultBean eoilistBean = (EoilistBean.ResultBean) eoiAdapter.getItem(position);
                    eoi_ID = eoilistBean.getProduct_eois_id() + "";
                    payMe = eoilistBean.getPayment_method() + "";
                    if (eoilistBean.getEoi_money_status() == 1 && eoilistBean.getPayment_method() == 6 || eoilistBean.getPayment_method() == 7) {
                        Bundle bundle = new Bundle();
                        bundle.putString("payurlId", eoilistBean.getEoi_money_url());
                        bundle.putString("payment_method", eoilistBean.getPayment_method() + "");
                        ActivitySkip.forward(CusOprateRecordActivity.this, PaymentQrActivity.class, bundle);

                        //申请退款
                    } else if (eoilistBean.getEoi_money_status() == 2 && eoilistBean.getIs_use() == 0) {

                        firbSelectPopWindow.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM, 0, 0);

                        //重新上传凭证
                    } else if (eoilistBean.getEoi_money_status() == 3) {
                        type = 1;
                        if (Build.VERSION.SDK_INT < 23) {
                            takePhoto();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    new PermissionListener() {
                                        @Override
                                        public void onGranted() {// 全部授权成功回调
                                            // 执行具体业务
                                            takePhoto();
                                        }

                                        @Override
                                        public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                                            for (String permission : deniedPermissionList) {
                                                ToasShow.showToastCenter(CusOprateRecordActivity.this, permission.toString());
                                            }
                                        }
                                    });
                        }


                        //已使用,申请取消
                    } else if (eoilistBean.getEoi_money_status() == 2 && eoilistBean.getIs_use() == 1) {
                        firbSelectPopWindowCancel.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM, 0, 0);

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
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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

    private void getVisitData(int page, final boolean isRel) {

        EasyHttp.get(Contants.VISIT_RECORD_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName()+"2")
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", customeId)
                .params("page", String.valueOf(page))
                .params("number", "100")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {

                        closeDialog();
                        Gson gson = new Gson();
                        VisitRecord visitRecord = gson.fromJson(json, VisitRecord.class);
                        vrrb = visitRecord.getResult();

                        if (isRel) {

                            if (vrrb.size() == 0) {
                                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }

                            visitAdapter = new CommonAdapter<VisitRecord.ResultBean>(CusOprateRecordActivity.this, vrrb, R.layout.visit_listview_item_layout) {
                                @Override
                                public void convert(ViewHolder holder, VisitRecord.ResultBean item) {
                                    holder.setText(R.id.tvVisitTime, MyUtils.date2String("yyyy/MM/dd", Long.valueOf(item.getAdd_time() + "000")));
                                    holder.setText(R.id.tvVisitTitle, item.getTitle());
                                    holder.setText(R.id.tvVisitContent, item.getContent());
                                }
                            };
                            listView.setAdapter(visitAdapter);
                            initMenuListView();


                        } else {
                            //ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            visitAdapter.addMore(vrrb);
                        }

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
                                    tvDes.setText(getString(R.string.noinformation));
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
                                    tvDes.setText(getString(R.string.noinformation));
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
        String strUrl = "";
        String key = null;
        if (flag.equals("custovisit")) {
            key = "v_log_id";

            strUrl = Contants.REMOVE_VISIT_RECORD;
        } else if (flag.equals("custoreser")) {

            key = "o_log_id";
            strUrl = Contants.REMOVE_RESERVER_RECORD;
        }


        EasyHttp.post(strUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .params(key, id + "")
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();

                        if (!TextUtils.isEmpty(json)) {
                            Gson gson = new Gson();
                            ErrorBean errorBean = gson.fromJson(json, ErrorBean.class);
                            if (errorBean.getCode().equals("1")) {
                                ToasShow.showToastCenter(CusOprateRecordActivity.this, errorBean.getMsg());
                            }

                        }


                    }
                });


    }

    private void getReservertData(int page, final boolean isRel) {
        EasyHttp.get(Contants.RESERVER_RECORDER_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName()+"3")
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", customeId)
                .params("page", String.valueOf(page))
                .params("number", "100")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {

                        closeDialog();
                        Gson gson = new Gson();
                        SubscribeListBean slb = gson.fromJson(json, SubscribeListBean.class);
                        rbList = slb.getResult();

                        if (isRel) {
                            if (rbList.size() == 0) {
                                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }
                            subscribeAdapter = new CommonAdapter<SubscribeListBean.ResultBean>(CusOprateRecordActivity.this, rbList, R.layout.reserver_listview_item_layout) {
                                @Override
                                public void convert(ViewHolder holder, SubscribeListBean.ResultBean item) {
                                    holder.setText(R.id.tvSubscribeTime, MyUtils.date2String("MM/dd HH:mm", item.getOrder_time() * 1000));
                                    holder.setText(R.id.tvSubscribeContent, item.getContent());
                                }
                            };

                            listView.setAdapter(subscribeAdapter);
                            initMenuListView();
                        } else {
                            subscribeAdapter.addMore(rbList);
                        }
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
                    try {
                        if (null != data && null != data.getData()) {
                            picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, data.getData(), null, null);
                            if (type == 0) {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                                ivCertificate.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath)));
                            }

                        } else {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                            picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, imgUri, null, null);
                            if (type == 0) {
                                ivCertificate.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath)));

                            }
                        }

                        //重新上传凭证
                        if (type == 1) {
                            submitEoi_01();
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
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;

        if (flag.equals("custovisit")) {
            getVisitData(page, true);
        } else if (flag.equals("custoreser")) {
            getReservertData(page, true);
        } else {
            getEoiData(page, true);
        }

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        if (flag.equals("custovisit")) {
            getVisitData(page, false);
        } else if (flag.equals("custoreser")) {
            getReservertData(page, false);
        } else {
            getEoiData(page, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDialog();
    }


    /**
     * 申请退款
     */
    private void showZh() {
        firbPwView = LayoutInflater.from(this).inflate(R.layout.firb_popuwindow, null);
        RelativeLayout rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown = (Button) firbPwView.findViewById(R.id.btUnknown);
        btUnknown.setVisibility(View.GONE);
        btSure = (Button) firbPwView.findViewById(R.id.btSure);
        btSure.setText(getString(R.string.eoi_refund));
        btFalse = (Button) firbPwView.findViewById(R.id.btFalse);
        btFalse.setText(getString(R.string.cancel));
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

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
                cancel_eoi(eoi_ID);
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });


    }


    /**
     * 取消排队
     */
    private void cancelEOI() {


        firbPwView = LayoutInflater.from(this).inflate(R.layout.firb_popuwindow, null);
        RelativeLayout rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown = (Button) firbPwView.findViewById(R.id.btUnknown);
        btUnknown.setVisibility(View.GONE);
        btSure = (Button) firbPwView.findViewById(R.id.btSure);
        btSure.setText(R.string.cncel_line);
        btFalse = (Button) firbPwView.findViewById(R.id.btFalse);
        btFalse.setText(getString(R.string.cancel));
        firbSelectPopWindowCancel = new PopupWindow(firbPwView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        firbSelectPopWindowCancel.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable firb = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        firbSelectPopWindowCancel.setBackgroundDrawable(firb);
        rlFirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindowCancel.dismiss();
            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindowCancel.dismiss();
                quxioaEOI(eoi_ID);
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindowCancel.dismiss();
            }
        });

    }


    /**
     * 退款
     *
     * @param eoi_id
     */
    private void cancel_eoi(String eoi_id) {

        EasyHttp.post(Contants.CANCEL_EOI)
                .cacheMode(CacheMode.NO_CACHE)
                .params("eoi_id", eoi_id)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);

                        closeDialog();
                        if (!TextUtils.isEmpty(json)) {
                            Gson gson = new Gson();
                            ErrorBean userBean = gson.fromJson(json, ErrorBean.class);
                            ToasShow.showToastCenter(cusOprateRecordActivity, userBean.getMsg());
                        }


                    }
                });


    }


    /**
     * 取消排队
     */
    private void quxioaEOI(String eoi) {

        EasyHttp.post(Contants.CANCEL_EOI_SORT)
                .cacheMode(CacheMode.NO_CACHE)
                .params("eoi_id", eoi)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);

                        closeDialog();
                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(json, ErrorBean.class);
                        if (errorBean.getCode().equals("1")) {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, errorBean.getMsg());
                        } else {
                            ToasShow.showToastCenter(CusOprateRecordActivity.this, errorBean.getMsg());
                        }


                    }
                });


    }

}
