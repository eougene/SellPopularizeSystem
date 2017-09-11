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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.MyUserInfo;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.myView.HeadZoomScrollView;
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

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的信息
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    CircleImageView myHeadIm;
    EditText myFirstNameEdit;
    EditText myLastNameEdit;
    EditText myPhoneEdit;
    EditText myEmailEdit;
    TextView myAdressTv;
    TextView myBankTV;
    TextView myCompanyAdressTV;
    TextView myCertificate;
    TextView myCertificateTV;
    RelativeLayout wechatRelative;
    ImageView wechatImageView;


    private String imagePath = "", wechat_qrcode = "";
    private MyUserInfo myUserInfo;
    private int type = 0;//0销售头像,1微信二维码


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
        myCompanyAdressTV = getViewById(R.id.myCompanyAdressTV);
        myCertificate = getViewById(R.id.myCertificate);
        myCertificateTV = getViewById(R.id.myCertificateTV);

        wechatRelative = getViewById(R.id.wechatRelative);
        wechatImageView = getViewById(R.id.wechatImageView);


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
        wechatRelative.setOnClickListener(this);


        setRightTitle(R.string.customdetaild_save, getResources().getColor(R.color.scale_tab5), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUserInfo();


            }
        });
    }

    private void getInfo() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());

        EasyHttp.get(Contants.USER_INFO)
                .cacheMode(CacheMode.NO_CACHE)
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
                            if (myUserInfo.getResult() != null) {
                                setUseInfo(myUserInfo);
                            }

                        } else {
                            ToasShow.showToastCenter(MyInfoActivity.this, myUserInfo.getMsg());
                        }


                    }
                });
    }

    //设置信息
    private void setUseInfo(MyUserInfo myUserInfo) {
        BaseApplication.getInstance().myUserInfo = myUserInfo;



        //更新头像
        if (!TextUtils.isEmpty(myUserInfo.getResult().getHead_img())) {
            SharedPreferencesHelps.setUserImage(myUserInfo.getResult().getHead_img());

        }

        //姓名
        if (!TextUtils.isEmpty(myUserInfo.getResult().getFirst_name())) {
            SharedPreferencesHelps.setFirstName(myUserInfo.getResult().getFirst_name());

        }
        //姓名
        if (!TextUtils.isEmpty(myUserInfo.getResult().getSurname())) {
            SharedPreferencesHelps.setSurName(myUserInfo.getResult().getSurname());

        }


        //设置头像
        if (!TextUtils.isEmpty(myUserInfo.getResult().getHead_img())) {

            Picasso.with(this).load(Contants.DOMAIN + "/" + myUserInfo.getResult().getHead_img()).
                    config(Bitmap.Config.RGB_565).into(myHeadIm);
        }

        //设置微信二维码
        if (!TextUtils.isEmpty(myUserInfo.getResult().getWechat_qrcode())) {
            Picasso.with(this).load(Contants.DOMAIN + "/" + myUserInfo.getResult().getWechat_qrcode()).
                    config(Bitmap.Config.RGB_565).into(wechatImageView);
        }

        //姓
        myFirstNameEdit.setText(myUserInfo.getResult().getSurname());
        //名
        myLastNameEdit.setText(myUserInfo.getResult().getFirst_name());
        //手机号
        myPhoneEdit.setText(myUserInfo.getResult().getMobile());
        //邮箱
        myEmailEdit.setText(myUserInfo.getResult().getE_mail());

    }


    private void updateUserInfo() {
        UIProgressResponseCallBack mUIProgressResponseCallBack = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);


            }
        };


        String first_name, surname, mobile, e_mail;


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


        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());
        httpParams.put("first_name", first_name);
        httpParams.put("surname", surname);
        httpParams.put("mobile", mobile);
        httpParams.put("e_mail", e_mail);


        //头像
        if (!TextUtils.isEmpty(imagePath)) {
            httpParams.put("head_img", new File(imagePath), mUIProgressResponseCallBack);
        }


        //微信二维码
        if (!TextUtils.isEmpty(imagePath)) {
            httpParams.put("wechat_qrcode", new File(wechat_qrcode), mUIProgressResponseCallBack);
        }

        //公司logi
        if (!TextUtils.isEmpty(BaseApplication.getInstance().myUserInfo.getResult().getSales_logo())) {
            if (!BaseApplication.getInstance().myUserInfo.getResult().getSales_logo().contains("public/uploads/user_logo/")) {
                httpParams.put("logo", new File(BaseApplication.getInstance().myUserInfo.getResult().getSales_logo()), mUIProgressResponseCallBack);
            }
        }


        //----------------------我的地址----------------------
        httpParams.put("country", BaseApplication.getInstance().myUserInfo.getResult().getCountry());
        httpParams.put("unit_number", BaseApplication.getInstance().myUserInfo.getResult().getUnit_number());
        httpParams.put("street_number", BaseApplication.getInstance().myUserInfo.getResult().getStreet_number());
        httpParams.put("suburb", BaseApplication.getInstance().myUserInfo.getResult().getSuburb());
        httpParams.put("state", BaseApplication.getInstance().myUserInfo.getResult().getState());
        httpParams.put("street_address_line_1", BaseApplication.getInstance().myUserInfo.getResult().getStreet_address_line_1());
        httpParams.put("street_address_line_2", BaseApplication.getInstance().myUserInfo.getResult().getStreet_address_line_2());
        httpParams.put("postcode", BaseApplication.getInstance().myUserInfo.getResult().getPostcode());

        //--------------------公司地址------------
        httpParams.put("company_country", BaseApplication.getInstance().myUserInfo.getResult().getCompany_country());
        httpParams.put("company_unit_number", BaseApplication.getInstance().myUserInfo.getResult().getCompany_unit_number());
        httpParams.put("company_street_number", BaseApplication.getInstance().myUserInfo.getResult().getCompany_street_number());
        httpParams.put("company_suburb", BaseApplication.getInstance().myUserInfo.getResult().getCompany_suburb());
        httpParams.put("company_state", BaseApplication.getInstance().myUserInfo.getResult().getCompany_state());
        httpParams.put("company_street_address_line_1", BaseApplication.getInstance().myUserInfo.getResult().getCompany_street_address_line_1());
        httpParams.put("company_street_address_line_2", BaseApplication.getInstance().myUserInfo.getResult().getCompany_street_address_line_2());
        httpParams.put("company_postcode", BaseApplication.getInstance().myUserInfo.getResult().getCompany_postcode());

        //---------------公司信息
        httpParams.put("business_name", BaseApplication.getInstance().myUserInfo.getResult().getBusiness_name());
        httpParams.put("abn", BaseApplication.getInstance().myUserInfo.getResult().getAbn());
        httpParams.put("acn", BaseApplication.getInstance().myUserInfo.getResult().getAcn());


        //-------------------银行信息--------
        httpParams.put("account_name", BaseApplication.getInstance().myUserInfo.getResult().getAccount_name());
        httpParams.put("account_number", BaseApplication.getInstance().myUserInfo.getResult().getAccount_number());
        httpParams.put("bank_name", BaseApplication.getInstance().myUserInfo.getResult().getBank_name());
        httpParams.put("bsb", BaseApplication.getInstance().myUserInfo.getResult().getBsb());

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
                /*case ExtraName.TAKE_PICTURE:
                    Uri photoUri = BitmapUtil.imgUri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {


                        if (photoUri != null) {
                            if (type == 0) {
                                imagePath = BitmapUtil.getImagePath(MyInfoActivity.this, photoUri, null, null);
                            } else {
                                wechat_qrcode = BitmapUtil.getImagePath(MyInfoActivity.this, photoUri, null, null);
                            }

                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (type == 0) {
                                myHeadIm.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, imagePath)));

                            } else {
                                wechatImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, wechat_qrcode)));

                            }
                        }

                    } else {
                        Uri imgUri = Uri.parse(BitmapUtil.imgPath);
                        if (imgUri != null) {
                            if (type == 0) {
                                imagePath = imgUri.getPath();
                                myHeadIm.setImageBitmap(BitmapUtil.compressBitmap(BitmapFactory.decodeFile(imagePath)));
                            } else {
                                wechat_qrcode = imgUri.getPath();
                                wechatImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapFactory.decodeFile(wechat_qrcode)));
                            }

                        }

                    }*/
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    //String string=data.getStringExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d("TAG", "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;

            }

        }

    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("000000")){
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        String picPath=imagePaths.get(0);
        Log.e("TAG", "loadAdpater: "+ picPath);
        if (type == 0) {
            //myHeadIm.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, imagePath)));
            Glide.with(MyInfoActivity.this)
                    .load(picPath)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                            myHeadIm.setImageDrawable(resource);
                        }
                    });
        } else {
            //wechatImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, wechat_qrcode)));
            Glide.with(MyInfoActivity.this)
                    .load(picPath)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(wechatImageView);
        }
        try{
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的头像
            case R.id.myHeadIm:
                type = 0;
                getImagePath();
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
                ActivitySkip.forward(MyInfoActivity.this, ComPanyActivity.class, bundle1);

                break;
            //我的证书
            case R.id.myCertificateTV:
                ActivitySkip.forward(MyInfoActivity.this, MyCertificateActivity.class);
                break;


            //上传微信二维码
            case R.id.wechatRelative:
                type = 1;
                getImagePath();
                break;
        }
    }

    private static final int REQUEST_CAMERA_CODE = 10;
    private ArrayList<String> imagePaths=new ArrayList<>();
    private void getImagePath() {
       /* if (Build.VERSION.SDK_INT < 23) {
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
        }*/
        PhotoPickerIntent intent = new PhotoPickerIntent(MyInfoActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true); // 是否显示拍照
        // intent.setMaxTotal(6); // 最多选择照片数量，默认为6
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }
}
