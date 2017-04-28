package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.clippicture.ClipPictureActivity;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.Lawyer;
import com.yd.org.sellpopularizesystem.javaBean.LawyerBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductChildBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.yd.org.sellpopularizesystem.application.ExtraName.CROP_IMAGE;

public class ReserveActivity extends BaseActivity {
    private TextView tvRePrice, tvRetype, tvReFirb, tvReSale, tvValidity,
            tvReCus, tvReLawyer, tvReGoal, tvRePay, tvRePayType, tvCertificate;
    private ImageView ivReLawyer, ivCertificate;
    private RelativeLayout rlReGoal, rlPayType, rlPop, rlPayTypePop, rlReLawyer, rlRecus;
    private ProductChildBean bean;
    private LawyerBean.ResultBean lawBean;
    private int lawyer_id = -1;
    private View mView, mPayTypeView, msetPhotoView;
    private Button btReUnknow, btReinv, btReSelf, btRefoin, btReCancel, btCash, btCredit, btDesposit,
            btTransfer, btCheck, btReTypeCancel, btReSubmit, btFromCamera, btFromAlbum, btPhotoCancel;
    private CustomePopuWindow mCustomePopuWindow;
    private CommonPopuWindow rePayTypePopuWindow, setPhotoPopuWindow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String imagePath;
    private String picPath;
    private String customeId;

    @Override
    protected int setContentView() {
        return R.layout.activity_reserve;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        bean = (ProductChildBean) bundle.get("item");
        lawBean = (LawyerBean.ResultBean) bundle.get("custome");
        hideRightImagview();
        setTitle(R.string.reserver);
        tvRePrice = (TextView) findViewById(R.id.tvRePrice);
        tvRetype = (TextView) findViewById(R.id.tvRetype);
        tvReFirb = (TextView) findViewById(R.id.tvReFirb);
        tvReSale = (TextView) findViewById(R.id.tvReSale);
        tvValidity = (TextView) findViewById(R.id.tvValidity);
        tvReCus = (TextView) findViewById(R.id.tvReCus);
        ivReLawyer = (ImageView) findViewById(R.id.ivReLawyer);
        ivCertificate = (ImageView) findViewById(R.id.ivCertificate);
        rlRecus = (RelativeLayout) findViewById(R.id.rlRecus);
        rlReLawyer = (RelativeLayout) findViewById(R.id.rlReLawyer);
        tvReLawyer = (TextView) findViewById(R.id.tvReLawyer);
        rlReGoal = (RelativeLayout) findViewById(R.id.rlReGoal);
        tvReGoal = (TextView) findViewById(R.id.tvReGoal);
        tvRePay = (TextView) findViewById(R.id.tvRePay);
        tvRePayType = (TextView) findViewById(R.id.tvRePayType);
        rlPayType = (RelativeLayout) findViewById(R.id.rlPayType);
        tvCertificate = (TextView) findViewById(R.id.tvCertificate);

        mCustomePopuWindow = new CustomePopuWindow(ReserveActivity.this, mOnClickListener);
        mView = mCustomePopuWindow.getContentView();
        rePayTypePopuWindow = new CommonPopuWindow(ReserveActivity.this, mOnClickListener) {
            @Override
            protected int getLayoutId() {
                return R.layout.reserver_paytype_popwindow;
            }
        };
        setPhotoPopuWindow = new CommonPopuWindow(ReserveActivity.this, mOnClickListener) {
            @Override
            protected int getLayoutId() {
                return R.layout.reserve_activity_setphoto_popwindow;
            }
        };
        msetPhotoView = setPhotoPopuWindow.getContentView();
        mPayTypeView = rePayTypePopuWindow.getContentView();

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
        btReSubmit = (Button) findViewById(R.id.btReSubmit);

        btFromCamera = (Button) msetPhotoView.findViewById(R.id.btFromCamera);
        btFromAlbum = (Button) msetPhotoView.findViewById(R.id.btFromAlbum);
        btPhotoCancel = (Button) msetPhotoView.findViewById(R.id.btPhotoCancel);
        initData();
    }

    private void initData() {
        tvRePrice.setText(bean.getPrice());
        tvRetype.setText(bean.getFloor_type());
        //tvReFirb.setText(bean.get);
        tvReSale.setText(bean.getSales_commission_type() + "");
        tvValidity.setText(bean.getPrice());
        String true_name = BaseApplication.getInstance().getResultBean().getTrue_name();
        customeId = BaseApplication.getInstance().getResultBean().getCustomer_id() + "";
        if (true_name != null) {
            tvReCus.setText(true_name);
        }

    }

    @Override
    public void setListener() {
        tvReCus.setOnClickListener(mOnClickListener);
        ivReLawyer.setOnClickListener(mOnClickListener);
        tvReGoal.setOnClickListener(mOnClickListener);
        tvRePayType.setOnClickListener(mOnClickListener);
        ivCertificate.setOnClickListener(mOnClickListener);
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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        private Bundle bun = new Bundle();
        ;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.rlRecus:
                    bun.putString(ExtraName.SCALETOCUSTOME, ExtraName.TORESVER_TOCUSTOME);
                    ActivitySkip.forward(ReserveActivity.this, CustomeActivity.class, ExtraName.RESERVE_TO_CUSTOME, bun);
                    overridePendingTransition(R.anim.enter_anim, 0);
                    break;
                case R.id.rlReLawyer:
                    bun.putString(ExtraName.RESVERTOLAWYER, ExtraName.TORESVER);
                    // bun.putString("id", String.valueOf(BaseApplication.getInstance().getResultBean().getCustomer_id()));
                    //ActivitySkip.forward(ReserveActivity.this, CustomeActivity.class, ExtraName.RESERVE_TO_LAWYER, bun);
                    ActivitySkip.forward(ReserveActivity.this,LawyerActivity.class,ExtraName.RESERVE_TO_LAWYER, bun);
                    overridePendingTransition(R.anim.enter_anim, 0);
                    break;
                case R.id.rlReGoal:
                    mCustomePopuWindow.showAtLocation(ReserveActivity.this.findViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.rlPayType:
                    rePayTypePopuWindow.showAtLocation(ReserveActivity.this.findViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.rlPop:
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.btReUnknow:
                    tvReGoal.setText(btReUnknow.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.btReinv:
                    tvReGoal.setText(btReinv.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.btReSelf:
                    tvReGoal.setText(btReSelf.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.btRefoin:
                    tvReGoal.setText(btRefoin.getText());
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.btReCancel:
                    mCustomePopuWindow.dismiss();
                    break;
                case R.id.rlPayTypePop:
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btCash:
                    tvRePayType.setText(btCash.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btCredit:
                    tvRePayType.setText(btCredit.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btDesposit:
                    tvRePayType.setText(btDesposit.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btTransfer:
                    tvRePayType.setText(btTransfer.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btCheck:
                    tvRePayType.setText(btCheck.getText());
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btReTypeCancel:
                    rePayTypePopuWindow.dismiss();
                    break;
                case R.id.btReSubmit:
                    getInfo();
                    break;
                case R.id.ivCertificate:
                    setPhotoPopuWindow.showAtLocation(ReserveActivity.this.findViewById(R.id.rlReserver), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.btFromCamera:
                    setPhotoPopuWindow.dismiss();
                    BitmapUtil.startImageCapture(ReserveActivity.this, ExtraName.TAKE_PICTURE);
                    break;
                case R.id.btFromAlbum:
                    setPhotoPopuWindow.dismiss();
                    BitmapUtil.gotoSysPic(ReserveActivity.this, ExtraName.ALBUM_PICTURE);
                    break;
                case R.id.btPhotoCancel:
                    setPhotoPopuWindow.dismiss();
                    break;

            }
        }
    };

    private void getInfo() {

        String payment_method = (String) tvRePayType.getText();

        if (lawyer_id == -1) {
            ToasShow.showToastBottom(this, getString(R.string.lawyer_id));
            return;
        } else if (payment_method.equals("请选择支付方式")) {
            ToasShow.showToastBottom(this, getString(R.string.pay_method));
            return;
        } else if (TextUtils.isEmpty(picPath)) {
            ToasShow.showToastBottom(this, getString(R.string.picpath));
            return;
        } else {
            commit(payment_method);
        }


    }

    private void commit(String payment_method) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        try {
            ajaxParams.put("order_type", "1");
            ajaxParams.put("property_id", bean.getProduct_childs_id() + "");
            ajaxParams.put("client", customeId);
            ajaxParams.put("sales_id", SharedPreferencesHelps.getUserID());
            ajaxParams.put("lawyer_id", lawyer_id + "");
            ajaxParams.put("payment_method", payment_method);
            ajaxParams.put("payment_amount", bean.getPrice());
            File picFile = new File(picPath);
            ajaxParams.put("file", picFile);
            ajaxParams.put("currency", bean.getCurrency());
            ajaxParams.put("purchaseReason", (String) tvReGoal.getText());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        http.post(Contants.CREAT_ORDER, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                super.onSuccess(s);
                Log.e("TAG", "onSuccess: " + s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastBottom(ReserveActivity.this, json.getString("msg"));
                        finish();
                    } else {
                        ToasShow.showToastBottom(ReserveActivity.this, json.getString("msg"));
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

    class CustomePopuWindow extends CommonPopuWindow {

        public CustomePopuWindow(Activity context, View.OnClickListener itemsOnClick) {
            super(context, itemsOnClick);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.reserver_goal_popwindow;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean mCrop = true;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ExtraName.RESERVE_TO_LAWYER:
                    Bundle lawBean = data.getExtras();
                    Lawyer.ResultBean.LawyerListBean lawyerBean = (Lawyer.ResultBean.LawyerListBean) lawBean.getSerializable("lawyer");
                    ivReLawyer.setVisibility(View.GONE);
                    tvReLawyer.setVisibility(View.VISIBLE);
                    tvReLawyer.setText(lawyerBean.getFirst_name()+lawyerBean.getSurname());
                    tvReLawyer.setTextColor(Color.BLUE);
                    lawyer_id = lawyerBean.getLawyer_id();
                    break;
                case ExtraName.RESERVE_TO_CUSTOME:
                    lawBean = data.getExtras();
                    CustomBean.ResultBean cun = (CustomBean.ResultBean) lawBean.getSerializable("custome");
                    tvReCus.setText(cun.getTrue_name());
                    customeId = cun.getCustomer_id() + "";
                    tvReCus.setTextColor(Color.RED);
                    break;
                //拍照上传
                case ExtraName.TAKE_PICTURE:
                    File cameraFile = new File(BitmapUtil.getCacheDir(this), "camera.jpg");
                    if (cameraFile.exists()) {
                        // copy 照片到指定目录下
                        String path = BitmapUtil.getCacheDir(this);
                        File dir = new File(path, "camera");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File picFile = new File(dir, System.currentTimeMillis() + "jpg");
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
                    }

                    break;
                //从相册选取图片
                case ExtraName.ALBUM_PICTURE:
                    Uri selectedPhotoUri = null;
                    //File picFile= null;
                        selectedPhotoUri = data.getData();
                    Log.e("tag", "onActivityResult: "+selectedPhotoUri.toString());
                        picPath=BitmapUtil.getImagePath(ReserveActivity.this,selectedPhotoUri,null,null);
                    Log.e("tag", "onActivityResult: "+picPath);
                    if (mCrop) {
                        CropImage(picPath);
                    } else {
                        /*Bitmap mBitmap = BitmapUtil.getThumbnail(this, selectedPhotoUri, ivCertificate.getWidth(), ivCertificate.getHeight());
                        ivCertificate.setImageBitmap(mBitmap);*/
                        Picasso.with(this).load(selectedPhotoUri).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
                    }
                    // 裁剪图片
                case ExtraName.CROP_IMAGE:
                    if (resultCode == RESULT_OK && null != data) {
                        imagePath = data.getStringExtra("bitmap");
                        Log.e("图片路径**", "paht:" + imagePath);
                        Picasso.with(this).load("file://" + imagePath).into(ivCertificate);
                        tvCertificate.setVisibility(View.GONE);
                    } else {

                    }
            }
        }
    }
    // 裁剪图片
    private void CropImage(String absolutePath) {
        Intent intent = new Intent(ReserveActivity.this, ClipPictureActivity.class);
        intent.putExtra("image-path", absolutePath);
        startActivityForResult(intent, CROP_IMAGE);
    }
}
