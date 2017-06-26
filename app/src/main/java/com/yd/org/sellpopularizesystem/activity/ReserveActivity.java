package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.clippicture.ClipPictureActivity;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.Lawyer;
import com.yd.org.sellpopularizesystem.javaBean.LawyerBean;
import com.yd.org.sellpopularizesystem.javaBean.ProSubunitListBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yd.org.sellpopularizesystem.application.ExtraName.CROP_IMAGE;

public class ReserveActivity extends BaseActivity {
    private TextView tvProName, tvRePrice, tvRetype, tvReFirb, tvReSale, tvValidity,
            tvReCus, tvReLawyer, tvReGoal, tvRePay, tvRePayType, tvCertificate, tvReCusAdd;
    private TextView tvTitleDes, tvMoneyNum, tvPayMethod, tvEoiSubmit;
    private ImageView ivReLawyer, ivCertificate, ivCash, ivIdCard, ivAlipay, ivWechatPay;
    private RelativeLayout rlReGoal, rlPayType, rlPop, rlPayTypePop, rlReLawyer, rlRecus;
    private ProSubunitListBean.ResultBean.PropertyBean bean;
    private LawyerBean.ResultBean lawBean;
    private int lawyer_id = -1;
    private View mView, mPayTypeView, msetPhotoView, mCusSelectView;
    private Button btReUnknow, btReinv, btReSelf, btRefoin, btReCancel, btCash, btCredit, btDesposit, btTransfer,
            btCheck, btReTypeCancel, btReSubmit, btFromCamera, btFromAlbum, btPhotoCancel, btSelecCus, btEditCusInfo, btCusCancel;
    private CustomePopuWindow mCustomePopuWindow;
    private CommonPopuWindow rePayTypePopuWindow, setPhotoPopuWindow, cusSelectPop;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String imagePath;
    private String picPath = "";
    private String customeId;
    private Dialog payMethodDialog;
    private LinearLayout llCertificate;
    private String payment_method;


    @Override
    protected int setContentView() {
        return R.layout.activity_reserve;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        bean = (ProSubunitListBean.ResultBean.PropertyBean) bundle.get("item");
        lawBean = (LawyerBean.ResultBean) bundle.get("custome");
        hideRightImagview();
        setTitle(R.string.reserver);
        tvProName = getViewById(R.id.tvReNameOne);
        tvProName.setText(bean.getProduct_name() + "-" + bean.getProduct_childs_unit_number());
        tvRePrice = getViewById(R.id.tvRePrice);
        tvRetype = getViewById(R.id.tvRetype);
        tvReFirb = getViewById(R.id.tvReFirb);
        tvReSale = getViewById(R.id.tvReSale);
        tvValidity = getViewById(R.id.tvValidity);
        tvReCus = getViewById(R.id.tvReCus);
        tvReCusAdd = getViewById(R.id.tvReCusAdd);
        ivReLawyer = getViewById(R.id.ivReLawyer);
        //ivCertificate = (ImageView) getViewById(R.id.ivCertificate);
        rlRecus = getViewById(R.id.rlRecus);
        rlReLawyer = getViewById(R.id.rlReLawyer);
        tvReLawyer = getViewById(R.id.tvReLawyer);
        rlReGoal = getViewById(R.id.rlReGoal);
        tvReGoal = getViewById(R.id.tvReGoal);
        tvRePay = getViewById(R.id.tvRePay);
        tvRePayType = getViewById(R.id.tvRePayType);
        rlPayType = getViewById(R.id.rlPayType);
        tvCertificate = getViewById(R.id.tvCertificate);

        mCustomePopuWindow = new CustomePopuWindow(ReserveActivity.this);
        mView = mCustomePopuWindow.getContentView();
        cusSelectPop = new CommonPopuWindow(ReserveActivity.this) {
            @Override
            protected int getLayoutId() {
                return R.layout.reserve_activity_cusselect_pop;
            }
        };
        rePayTypePopuWindow = new CommonPopuWindow(ReserveActivity.this) {
            @Override
            protected int getLayoutId() {
                return R.layout.reserver_paytype_popwindow;
            }
        };
        initPayMethodDialog();

        setPhotoPopuWindow = new CommonPopuWindow(ReserveActivity.this) {
            @Override
            protected int getLayoutId() {
                return R.layout.reserve_activity_setphoto_popwindow;
            }
        };
        mCusSelectView = cusSelectPop.getContentView();
        msetPhotoView = setPhotoPopuWindow.getContentView();
        mPayTypeView = rePayTypePopuWindow.getContentView();

        btSelecCus = (Button) mCusSelectView.findViewById(R.id.btSelecCus);
        btEditCusInfo = (Button) mCusSelectView.findViewById(R.id.btEditCusInfo);
        btCusCancel = (Button) mCusSelectView.findViewById(R.id.btCusCancel);

        rlPop = (RelativeLayout) mView.findViewById(R.id.rlPop);
        btReUnknow = (Button) mView.findViewById(R.id.btReUnknow);
        btReinv = (Button) mView.findViewById(R.id.btReinv);
        btReSelf = (Button) mView.findViewById(R.id.btReSelf);
        btRefoin = (Button) mView.findViewById(R.id.btRefoin);
        btReCancel = (Button) mView.findViewById(R.id.btReCancel);

        rlPayTypePop = (RelativeLayout) mPayTypeView.findViewById(R.id.rlPayTypePop);
        btCash = (Button) mPayTypeView.findViewById(R.id.btCash);
        btCredit = (Button) mPayTypeView.findViewById(R.id.btCredit);
        btDesposit = (Button) mPayTypeView.findViewById(R.id.btDesposit);
        btTransfer = (Button) mPayTypeView.findViewById(R.id.btTransfer);
        btCheck = (Button) mPayTypeView.findViewById(R.id.btCheck);
        btReTypeCancel = (Button) mPayTypeView.findViewById(R.id.btReTypeCancel);
        btReSubmit = getViewById(R.id.btReSubmit);

        btFromCamera = (Button) msetPhotoView.findViewById(R.id.btFromCamera);
        btFromAlbum = (Button) msetPhotoView.findViewById(R.id.btFromAlbum);
        btPhotoCancel = (Button) msetPhotoView.findViewById(R.id.btPhotoCancel);
        initData();
    }

    private void initPayMethodDialog() {
        payMethodDialog = new Dialog(this);
        payMethodDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payMethodDialog.setContentView(R.layout.eoi_operate_view);
        Window dialogWindow = payMethodDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = MyUtils.getStatusBarHeight(this);
        dialogWindow.setAttributes(lp);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        initPayMethodDialogViews();
    }

    private void initPayMethodDialogViews() {
        tvTitleDes = (TextView) payMethodDialog.findViewById(R.id.tvTitleDes);
        tvTitleDes.setText(R.string.zhifu);
        tvMoneyNum = (TextView) payMethodDialog.findViewById(R.id.tvMoneyNum);
        tvPayMethod = (TextView) payMethodDialog.findViewById(R.id.tvPayMethod);
        tvEoiSubmit = (TextView) payMethodDialog.findViewById(R.id.tvEoiSubmit);
        ivCash = (ImageView) payMethodDialog.findViewById(R.id.ivCash);
        ivIdCard = (ImageView) payMethodDialog.findViewById(R.id.ivIdCard);
        ivAlipay = (ImageView) payMethodDialog.findViewById(R.id.ivAlipay);
        ivWechatPay = (ImageView) payMethodDialog.findViewById(R.id.ivWeixinPay);
        llCertificate = (LinearLayout) payMethodDialog.findViewById(R.id.llCertificate);
        ivCertificate = (ImageView) payMethodDialog.findViewById(R.id.ivCertificate);
        ivCash.setOnClickListener(mOnClickListener);
        ivIdCard.setOnClickListener(mOnClickListener);
        ivAlipay.setOnClickListener(mOnClickListener);
        ivWechatPay.setOnClickListener(mOnClickListener);
        tvEoiSubmit.setOnClickListener(mOnClickListener);
        ivCertificate.setOnClickListener(mOnClickListener);
    }

    private void initData() {
        tvRePrice.setText("$" + getString(R.string.single_blank_space) + MyUtils.addComma(bean.getPrice().split("\\.")[0]));
        tvRetype.setText(bean.getCate_name());
        if (BaseApplication.getInstance().getResultBean() != null) {
            if (BaseApplication.getInstance().getIs_firb() == 0) {
                tvReFirb.setText(R.string.bushi);
            } else {
                if (BaseApplication.getInstance().getFirb_number() != 0) {
                    tvReFirb.setText(getString(R.string.bushi));
                }
            }
            if (BaseApplication.getInstance().getResultBean() != null) {
                tvReCusAdd.setText(BaseApplication.getInstance().getResultBean().getAddress() + BaseApplication.getInstance().getResultBean().getZip_code());
            }
            tvReSale.setText(SharedPreferencesHelps.getSurName() + "  " + SharedPreferencesHelps.getFirstName());
            tvValidity.setText(bean.getPrice());
            //String en_name = BaseApplication.getInstance().getResultBean().getEn_name();
            customeId = BaseApplication.getInstance().getResultBean().getCustomer_id() + "";
            if (BaseApplication.getInstance().getResultBean().getEn_name() != null) {
                tvReCus.setText(BaseApplication.getInstance().getResultBean().getSurname() + BaseApplication.getInstance().getResultBean().getFirst_name());
                if (judgeCusInfo(BaseApplication.getInstance().getResultBean())) {
                    tvReCus.setTextColor(Color.BLUE);
                } else {
                    tvReCus.setTextColor(Color.RED);
                }

            }
        }
    }

    @Override
    public void setListener() {
        btSelecCus.setOnClickListener(mOnClickListener);
        btEditCusInfo.setOnClickListener(mOnClickListener);
        btCusCancel.setOnClickListener(mOnClickListener);
        tvReCus.setOnClickListener(mOnClickListener);
        ivReLawyer.setOnClickListener(mOnClickListener);
        tvReGoal.setOnClickListener(mOnClickListener);
        //tvRePayType.setOnClickListener(mOnClickListener);
        // ivCertificate.setOnClickListener(mOnClickListener);
        rlRecus.setOnClickListener(mOnClickListener);
        rlReLawyer.setOnClickListener(mOnClickListener);
        tvReLawyer.setOnClickListener(mOnClickListener);
        rlPop.setOnClickListener(mOnClickListener);
        rlReGoal.setOnClickListener(mOnClickListener);
        btReUnknow.setOnClickListener(mOnClickListener);
        btReinv.setOnClickListener(mOnClickListener);
        btReSelf.setOnClickListener(mOnClickListener);
        btRefoin.setOnClickListener(mOnClickListener);
        btReCancel.setOnClickListener(mOnClickListener);
        rlPayType.setOnClickListener(mOnClickListener);
        rlPayTypePop.setOnClickListener(mOnClickListener);
        btCash.setOnClickListener(mOnClickListener);
        btCredit.setOnClickListener(mOnClickListener);
        btDesposit.setOnClickListener(mOnClickListener);
        btTransfer.setOnClickListener(mOnClickListener);
        btCheck.setOnClickListener(mOnClickListener);
        btReTypeCancel.setOnClickListener(mOnClickListener);
        btReSubmit.setOnClickListener(mOnClickListener);
        btFromCamera.setOnClickListener(mOnClickListener);
        btFromAlbum.setOnClickListener(mOnClickListener);
        btPhotoCancel.setOnClickListener(mOnClickListener);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
            } else if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToasShow.showToastCenter(ReserveActivity.this, getString(R.string.camera_hint));
            }
        }
    }*/

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        private Bundle bun = new Bundle();

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                //客户选择对话框
                case R.id.rlRecus:
                    cusSelectPop.showAtLocation(ReserveActivity.this.getViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    break;
                //选择客户
                case R.id.btSelecCus:
                    bun.putString(ExtraName.SCALETOCUSTOME, ExtraName.TORESVER_TOCUSTOME);
                    ActivitySkip.forward(ReserveActivity.this, CustomeActivity.class, ExtraName.RESERVE_TO_CUSTOME, bun);
                    overridePendingTransition(R.anim.enter_anim, 0);
                    cusSelectPop.dismiss();
                    break;
                //编辑客户信息
                case R.id.btEditCusInfo:
                    BaseApplication app = BaseApplication.getInstance();
                    app.getResultBean();
                    bun.putSerializable("cun", app.getResultBean());
                    bun.putString("add", "completeinfo");
                    ActivitySkip.forward(ReserveActivity.this, CustomDetailedActivity.class, bun);
                    overridePendingTransition(R.anim.enter_anim, 0);
                    cusSelectPop.dismiss();
                    break;
                //取消客户选择
                case R.id.btCusCancel:
                    cusSelectPop.dismiss();
                    break;
                //选择律师
                case R.id.rlReLawyer:
                    bun.putString(ExtraName.RESVERTOLAWYER, ExtraName.TORESVER);
                    ActivitySkip.forward(ReserveActivity.this, LawyerActivity.class, ExtraName.RESERVE_TO_LAWYER, bun);
                    overridePendingTransition(R.anim.enter_anim, 0);
                    break;
                //购房目的弹出框
                case R.id.rlReGoal:
                    mCustomePopuWindow.showAtLocation(ReserveActivity.this.getViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    break;
                //支付方式
                case R.id.rlPayType:
                    // rePayTypePopuWindow.showAtLocation(ReserveActivity.this.getViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    payMethodDialog.show();
                    break;
                case R.id.rlPop:
                    mCustomePopuWindow.dismiss();
                    break;
                //未知
                case R.id.btReUnknow:
                    tvReGoal.setText(btReUnknow.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                //投资
                case R.id.btReinv:
                    tvReGoal.setText(btReinv.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                //自住
                case R.id.btReSelf:
                    tvReGoal.setText(btReSelf.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                //海外投资
                case R.id.btRefoin:
                    tvReGoal.setText(btRefoin.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                //取消
                case R.id.btReCancel:
                    mCustomePopuWindow.dismiss();
                    break;
                //支付方式弹出框
                case R.id.rlPayTypePop:
                    rePayTypePopuWindow.dismiss();
                    break;
                //现金
                case R.id.btCash:
                    tvRePayType.setText(btCash.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                //信用卡
                case R.id.btCredit:
                    tvRePayType.setText(btCredit.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                //银行卡
                case R.id.btDesposit:
                    tvRePayType.setText(btDesposit.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                //转账
                case R.id.btTransfer:
                    tvRePayType.setText(btTransfer.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                //支票
                case R.id.btCheck:
                    tvRePayType.setText(btCheck.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                //取消
                case R.id.btReTypeCancel:
                    rePayTypePopuWindow.dismiss();
                    break;

                //提交预定
                case R.id.btReSubmit:
                    getInfo();
                    break;


                //现金
                case R.id.ivCash:
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    tvMoneyNum.setText("$ 300.00");
                    tvPayMethod.setText(getString(R.string.recash));
                    payment_method = "1";
                    break;
                //银行转账
                case R.id.ivIdCard:
                    if (llCertificate.getVisibility() == View.GONE) {
                        llCertificate.setVisibility(View.VISIBLE);
                    }
                    tvMoneyNum.setText("$ 300.00");
                    tvPayMethod.setText(getString(R.string.transfer));
                    payment_method = "4";
                    break;
                //支付宝
                case R.id.ivAlipay:
                    if (llCertificate.getVisibility() == View.VISIBLE) {
                        llCertificate.setVisibility(View.GONE);
                    }
                    tvPayMethod.setText(R.string.alipay);
                    tvMoneyNum.setText("￥ 2000.00");
                    payment_method = "6";
                    break;
                //微信
                case R.id.ivWeixinPay:
                    if (llCertificate.getVisibility() == View.VISIBLE) {
                        llCertificate.setVisibility(View.GONE);
                    }
                    tvPayMethod.setText(R.string.wechatpay);
                    tvMoneyNum.setText("￥ 2000.00");
                    payment_method = "7";
                    break;
                //开启相机
                case R.id.ivCertificate:
                    if (Build.VERSION.SDK_INT < 23) {
                        BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {// 全部授权成功回调
                                        // 执行具体业务
                                        BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                                        for (String permission : deniedPermissionList) {
                                            ToasShow.showToastCenter(ReserveActivity.this, permission.toString());
                                        }
                                    }
                                });

                        /*boolean bCamera = ActivityCompat.checkSelfPermission(ReserveActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                        boolean bFile = ActivityCompat.checkSelfPermission(ReserveActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (bCamera && bFile) {
                            BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
                        } else if (!bCamera){
                            //申请相机权限
                            ActivityCompat.requestPermissions(ReserveActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }else if(!bFile){
                            //申请WRITE_EXTERNAL_STORAGE权限
                            ActivityCompat.requestPermissions(ReserveActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }*/
                    }
                    break;

                //提交支付
                case R.id.tvEoiSubmit:
                    if (tvMoneyNum.getText().equals("-")) {
                        ToasShow.showToastCenter(ReserveActivity.this, getString(R.string.paytype));
                    } else if (llCertificate.getVisibility() == View.VISIBLE) {
                        if (picPath == null) {
                            ToasShow.showToastCenter(ReserveActivity.this, getString(R.string.picpath));
                        } else {
                            tvRePay.setText(tvMoneyNum.getText().toString());
                            tvRePayType.setText(tvPayMethod.getText());
                            payMethodDialog.dismiss();
                        }
                    } else {
                        tvRePay.setText(tvMoneyNum.getText().toString());
                        tvRePayType.setText(tvPayMethod.getText());
                        payMethodDialog.dismiss();
                    }
                    break;
                //相机拍照
                case R.id.btFromCamera:
                    setPhotoPopuWindow.dismiss();
                    BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
                    break;
                //从相册中选择
                case R.id.btFromAlbum:
                    setPhotoPopuWindow.dismiss();
                    BitmapUtil.gotoSysPic(ReserveActivity.this, ExtraName.ALBUM_PICTURE);
                    break;
                //取消选择
                case R.id.btPhotoCancel:
                    setPhotoPopuWindow.dismiss();
                    break;

            }
        }
    };

    private void getInfo() {
        if (lawyer_id == -1) {
            ToasShow.showToastBottom(this, getString(R.string.lawyer_id));
            return;
        } else if (tvRePayType.getText().equals(getString(R.string.pay_method))) {

            ToasShow.showToastBottom(this, getString(R.string.pay_method));

            return;

        } else if (TextUtils.isEmpty(picPath) && !payment_method.equals("6") && !payment_method.equals("7")) {

            ToasShow.showToastBottom(this, getString(R.string.picpath));
            Log.e("payment_method1**", "payment_method:" + payment_method);
            return;

        } else if (!judgeCusInfo(BaseApplication.getInstance().getResultBean())) {
            return;
        } else {
            //显示提示框
            showAlertDialog();
            //commit(payment_method);
        }


    }

    private void showAlertDialog() {
        new AlertDialog.Builder(ReserveActivity.this)
                .setMessage(R.string.order_prompt)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit(payment_method);
                        //finish();
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
    }

    private void commit(final String payment_method) {

        try {
            showDialog();
            FinalHttp http = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("order_type", "1");
            ajaxParams.put("property_id", bean.getProduct_childs_id() + "");
            ajaxParams.put("client", customeId);
            ajaxParams.put("sales_id", SharedPreferencesHelps.getUserID());
            ajaxParams.put("lawyer_id", lawyer_id + "");
            ajaxParams.put("payment_method", payment_method);
            ajaxParams.put("payment_amount", getDigitalFromString(tvRePay.getText().toString()));
            if (null != picPath && !picPath.equals("")) {
                File picFile = new File(picPath);
                ajaxParams.put("file", picFile);
            }
            ajaxParams.put("pay_time", System.currentTimeMillis() + "");
            ajaxParams.put("currency", bean.getCurrency());
            ajaxParams.put("purchaseReason", (String) tvReGoal.getText());
            ajaxParams.put("eoi_id", bean.getIs_eoi() + "");

            Log.e("参数**", "ajaxParams:" + ajaxParams.toString());
            http.post(Contants.CREAT_ORDER, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    closeDialog();
                    super.onSuccess(s);
                    Log.e("TAG", "onSuccess: " + s);
                    try {
                        JSONObject json = new JSONObject(s);
                        if (json.getString("code").equals("1")) {
                            ToasShow.showToastCenter(ReserveActivity.this, json.getString("msg"));
                            if (payment_method.equals("6") || payment_method.equals("7")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("payurlId", json.getString("trust_account_id"));
                                bundle.putString("payment_method", payment_method);
                                ActivitySkip.forward(ReserveActivity.this, PaymentQrActivity.class, bundle);
                            }
                            finish();
                        } else {
                            ToasShow.showToastCenter(ReserveActivity.this, json.getString("msg"));
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private String getDigitalFromString(String s) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }

    class CustomePopuWindow extends CommonPopuWindow {

        public CustomePopuWindow(Activity context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.reserver_goal_popwindow;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean mCrop = false;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //选择律师
                case ExtraName.RESERVE_TO_LAWYER:
                    Bundle lawBean = data.getExtras();
                    Lawyer.ResultBean.LawyerListBean lawyerBean = (Lawyer.ResultBean.LawyerListBean) lawBean.getSerializable("lawyer");
                    ivReLawyer.setVisibility(View.GONE);
                    tvReLawyer.setVisibility(View.VISIBLE);
                    tvReLawyer.setText(lawyerBean.getFirst_name() + lawyerBean.getSurname());
                    tvReLawyer.setTextColor(Color.BLUE);
                    lawyer_id = lawyerBean.getLawyer_id();
                    break;
                //选择客户
                case ExtraName.RESERVE_TO_CUSTOME:
                    lawBean = data.getExtras();
                    CustomBean.ResultBean cun = (CustomBean.ResultBean) lawBean.getSerializable("custome");
                    tvReCus.setText(cun.getEn_name());
                    if (cun.getAddress() != null && cun.getZip_code() != null) {
                        tvReCusAdd.setText(cun.getAddress() + cun.getZip_code());
                    } else {
                        tvReCusAdd.setText("");
                    }
                    customeId = cun.getCustomer_id() + "";
                    //判断用户信息是否完整
                    if (!judgeCusInfo(cun)) {
                        tvReCus.setTextColor(Color.RED);
                        Bundle bundle = new Bundle();
                        bundle.putString("add", "completeinfo");
                        bundle.putSerializable("cun", cun);
                        ActivitySkip.forward(ReserveActivity.this, CustomDetailedActivity.class, bundle);
                    } else {
                        tvReCus.setTextColor(Color.BLUE);
                    }
                    break;

                //拍照上传
                case ExtraName.TAKE_PICTURE:
                    /*File cameraFile = new File(BitmapUtil.getCacheDir(this), "camera.jpg");
                    if (cameraFile.exists()) {
                        // copy 照片到指定目录下
                        String path = BitmapUtil.getCacheDir(this);
                        File dir = new File(path, "camera");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File picFile = new File(dir, System.currentTimeMillis() + ".jpg");
                        Log.e("picFile", picFile.getAbsolutePath());
                        try {
                            BitmapUtil.copyStream(new FileInputStream(cameraFile), new FileOutputStream(picFile));
                            cameraFile.delete();
                            if (mCrop) {
                                picPath = picFile.getAbsolutePath();
                                CropImage(picPath);
                            } else {
                                Picasso.with(this).load(picFile).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                    /*if (takePhotoUri!=null){
                        if (Build.VERSION.SDK_INT < 23){
                            Picasso.with(this).load(takePhotoUri).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
                        }else {

                        }
                    }*/

                    Uri photoUri = BitmapUtil.imgUri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        picPath = BitmapUtil.getImagePath(ReserveActivity.this, photoUri, null, null);
                        Log.e("imgPath", "onActivityResult: " + picPath);
                        //picPath=BitmapUtil.imgPath;
                        Bitmap bitmap = null;
                        try {
                            //picPath: onActivityResult: /storage/emulated/0/Pictures/1497846519571.jpg
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //Picasso.with(this).load("file://"+BitmapUtil.imgPath)./*resize(ivCertificate.getWidth(), ivCertificate.getHeight()).*/into(ivCertificate);
                        ivCertificate.setImageBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath));
                    }else {
                        Uri imgUri = Uri.parse(BitmapUtil.imgPath);
                        picPath = imgUri.getPath();
                        ivCertificate.setImageBitmap(BitmapFactory.decodeFile(picPath));
                    }
                    break;

                //从相册选取图片
                case ExtraName.ALBUM_PICTURE:
                    Uri selectedPhotoUri = null;
                    selectedPhotoUri = data.getData();
                    picPath = BitmapUtil.getImagePath(ReserveActivity.this, selectedPhotoUri, null, null);
                    if (mCrop) {
                        CropImage(picPath);
                    } else {
                        Picasso.with(this).load(selectedPhotoUri).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
                    }
                    // 裁剪图片
                case ExtraName.CROP_IMAGE:
                    if (resultCode == RESULT_OK && null != data) {
                        imagePath = data.getStringExtra("bitmap");
                        Picasso.with(this).load("file://" + imagePath).into(ivCertificate);
                        tvCertificate.setVisibility(View.GONE);
                    } else {

                    }
            }
        }
    }

    private boolean judgeCusInfo(CustomBean.ResultBean cun) {
        if (cun.getSurname() != null && cun.getFirst_name() != null &&
                cun.getMobile() != null && cun.getE_mail() != null &&
                cun.getCountry() != null && cun.getProvince() != null &&
                cun.getAddress() != null && cun.getZip_code() != null) {
            if (!cun.getSurname().equals("") && !cun.getFirst_name().equals("") &&
                    !cun.getMobile().equals("") && !cun.getE_mail().equals("") &&
                    !cun.getCountry().equals("") && !cun.getProvince().equals("") &&
                    !cun.getAddress().equals("") && !cun.getZip_code().equals("")) {
                return true;
            } else {
                ToasShow.showToastCenter(ReserveActivity.this, getString(R.string.complete_cusinfo));
                return false;
            }
        } else {
            ToasShow.showToastCenter(ReserveActivity.this, getString(R.string.complete_cusinfo));
            return false;
        }

    }

    // 裁剪图片
    private void CropImage(String absolutePath) {
        Intent intent = new Intent(ReserveActivity.this, ClipPictureActivity.class);
        intent.putExtra("image-path", absolutePath);
        startActivityForResult(intent, CROP_IMAGE);
    }
}
