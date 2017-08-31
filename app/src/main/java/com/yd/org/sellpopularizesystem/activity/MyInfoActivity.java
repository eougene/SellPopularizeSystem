package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.MyUserInfo;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    CircleImageView myHeadIm;
    EditText myFirstNameEdit;
    EditText myLastNameEdit;
    EditText myPhoneEdit;
    EditText myEmailEdit;
    TextView myAdressTv;
    TextView myBankTV;
    EditText myCompanyEdit;
    TextView myCompanyAdressTV;
    TextView myCertificate;
    TextView myCertificateTV;


    private String imagePath = "";
    private MyUserInfo myUserInfo;


    @Override
    protected int setContentView() {
        return R.layout.activity_my_info;

    }

    @Override
    public void initView() {
        setTitle(getString(R.string.myinfo));

        myHeadIm = getViewById(R.id.myHeadIm);
        myFirstNameEdit = getViewById(R.id.myFirstNameEdit);
        myLastNameEdit = getViewById(R.id.myLastNameEdit);
        myPhoneEdit = getViewById(R.id.myPhoneEdit);
        myEmailEdit = getViewById(R.id.myEmailEdit);
        myAdressTv = getViewById(R.id.myAdressTv);
        myBankTV = getViewById(R.id.myBankTV);
        myCompanyEdit = getViewById(R.id.myCompanyEdit);
        myCompanyAdressTV = getViewById(R.id.myCompanyAdressTV);
        myCertificate = getViewById(R.id.myCertificate);
        myCertificateTV = getViewById(R.id.myCertificateTV);


        if (SharedPreferencesHelps.getType() == 1) {
            myCertificate.setText(R.string.mycertificate);
        } else if (SharedPreferencesHelps.getType() == 2) {
            myCertificate.setText(R.string.besale);

        }
        getInfo();
    }

    @Override
    public void setListener() {
        myHeadIm.setOnClickListener(this);
        myAdressTv.setOnClickListener(this);
        myBankTV.setOnClickListener(this);
        myCompanyAdressTV.setOnClickListener(this);
        myCertificateTV.setOnClickListener(this);


        setRightTitle(R.string.customdetaild_save, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断我的地址信息是否完整,,,==0不完整,
                if (SharedPreferencesHelps.getUserAdress() == 0) {
                    ToasShow.showToastBottom(MyInfoActivity.this, getString(R.string.completel_add));
                    return;

                    //判断银行卡信息是否完整,,,==0不完整,
                } else if (SharedPreferencesHelps.getUserBank() == 0) {
                    ToasShow.showToastBottom(MyInfoActivity.this, getString(R.string.complete_bank));
                    return;
                } else {
                    updateUserInfo();
                }


            }
        });
    }

    private void getInfo() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());

        EasyHttp.get(Contants.USER_INFO)
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .cacheKey(this.getClass().getSimpleName())
                .params(httpParams)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(MyInfoActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getMessage());

                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.e("onSuccess***", "onSuccess:" + s);
                        closeDialog();
                        Gson gson = new Gson();
                        myUserInfo = gson.fromJson(s, MyUserInfo.class);
                        if (myUserInfo.getCode().equals("1")) {
                            setUseInfo(myUserInfo);
                        } else {
                            ToasShow.showToastCenter(MyInfoActivity.this, myUserInfo.getMsg());
                        }


                    }
                });
    }

    //设置信息
    private void setUseInfo(MyUserInfo myUserInfo) {

        //设置头像
        if (!TextUtils.isEmpty(myUserInfo.getResult().getSales_logo())) {
            Picasso.with(this).load(Contants.DOMAIN + "/" + myUserInfo.getResult().getSales_logo()).
                    config(Bitmap.Config.RGB_565).into(myHeadIm);
        }

        //姓
        myFirstNameEdit.setText(myUserInfo.getResult().getSurname());
        //名
       myLastNameEdit.setText(myUserInfo.getResult().getFirst_name());
        //手机号
        myPhoneEdit.setText(myUserInfo.getResult().getMobile());
        //邮箱
        myEmailEdit.setText(myUserInfo.getResult().getE_mail());
        //公司名称
        myCompanyEdit.setText(myUserInfo.getResult().getBusiness_name());
    }


    private void updateUserInfo() {
        UIProgressResponseCallBack mUIProgressResponseCallBack = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);


            }
        };


        String first_name, surname, mobile, e_mail, business_name;


        //姓
        if (TextUtils.isEmpty(myFirstNameEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(MyInfoActivity.this, getString(R.string.xingshi_empty));
            return;
        } else {
            surname = myFirstNameEdit.getText().toString().trim();
        }

        //名
        if (TextUtils.isEmpty(myLastNameEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(MyInfoActivity.this, getString(R.string.name_empty));
            return;
        } else {
            first_name = myLastNameEdit.getText().toString().trim();
        }

        //电话
        mobile = myPhoneEdit.getText().toString();


        //邮箱

        if (TextUtils.isEmpty(myEmailEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(MyInfoActivity.this, getString(R.string.email_hint));
            return;
        } else {
            e_mail = myEmailEdit.getText().toString().trim();
        }


        //名
        if (TextUtils.isEmpty(myCompanyEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(MyInfoActivity.this, getString(R.string.companyname_empty));
            return;
        } else {
            business_name = myCompanyEdit.getText().toString().trim();
        }


        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());
        httpParams.put("first_name", first_name);
        httpParams.put("surname", surname);
        httpParams.put("mobile", mobile);
        httpParams.put("e_mail", e_mail);
        httpParams.put("business_name", business_name);


        if (!TextUtils.isEmpty(imagePath)) {
            httpParams.put("file", new File(imagePath), mUIProgressResponseCallBack);
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
                        ToasShow.showToastCenter(MyInfoActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        closeDialog();
                        Log.e("onSuccess***", "onSuccess:" + s);

                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                        ToasShow.showToastCenter(MyInfoActivity.this, errorBean.getMsg());
                        if (errorBean.getCode().equals("1")) {
                            finish();
                        }

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
                //拍照
                case ExtraName.TAKE_PICTURE:
                    Uri photoUri = BitmapUtil.imgUri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        if (photoUri != null) {
                            imagePath = BitmapUtil.getImagePath(MyInfoActivity.this, photoUri, null, null);
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            myHeadIm.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, imagePath)));
                        }

                    } else {
                        Uri imgUri = Uri.parse(BitmapUtil.imgPath);
                        if (imgUri != null) {
                            imagePath = imgUri.getPath();
                            myHeadIm.setImageBitmap(BitmapUtil.compressBitmap(BitmapFactory.decodeFile(imagePath)));
                        }

                    }

                    break;

            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的头像
            case R.id.myHeadIm:
                if (Build.VERSION.SDK_INT < 23) {
                    BitmapUtil.startImageCapture(MyInfoActivity.this, ExtraName.TAKE_PICTURE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {// 全部授权成功回调
                                    // 执行具体业务
                                    BitmapUtil.startImageCapture(MyInfoActivity.this, ExtraName.TAKE_PICTURE);
                                }

                                @Override
                                public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                                    for (String permission : deniedPermissionList) {
                                        ToasShow.showToastCenter(MyInfoActivity.this, permission.toString());
                                    }
                                }
                            });
                }
                break;
            //我的地址
            case R.id.myAdressTv:
                Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                bundle.putSerializable("userkey", myUserInfo);
                ActivitySkip.forward(MyInfoActivity.this, UserAdressActivity.class, bundle);

                break;
            //银行账号
            case R.id.myBankTV:
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("userkey", myUserInfo);
                ActivitySkip.forward(MyInfoActivity.this, BankActivity.class, bundle2);

                break;
            //公司地址
            case R.id.myCompanyAdressTV:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "2");
                bundle1.putSerializable("userkey", myUserInfo);
                ActivitySkip.forward(MyInfoActivity.this, UserAdressActivity.class, bundle1);

                break;
            //我的证书
            case R.id.myCertificateTV:
                ActivitySkip.forward(MyInfoActivity.this, MyCertificateActivity.class);
                break;
        }
    }
}
