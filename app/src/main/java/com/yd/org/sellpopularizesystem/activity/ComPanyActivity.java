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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.MyUserInfo;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.io.FileNotFoundException;
import java.util.List;

public class ComPanyActivity extends BaseActivity {

    private EditText companyEdit, abnEdit, acnEdit;
    private LinearLayout acomAdrLinnear;
    private RelativeLayout compaanyRelative;
    private ImageView logoImageView;
    private MyUserInfo myUserInfo;
    private String imagePath = "";

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //公司地址
                case R.id.acomAdrLinnear:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", "2");
                    bundle1.putSerializable("userkey", myUserInfo);
                    ActivitySkip.forward(ComPanyActivity.this, UserAdressActivity.class, bundle1);
                    break;


                //公司logo
                case R.id.compaanyRelative:
                    if (Build.VERSION.SDK_INT < 23) {
                        BitmapUtil.startImageCapture(ComPanyActivity.this, ExtraName.TAKE_PICTURE);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {// 全部授权成功回调
                                        // 执行具体业务
                                        BitmapUtil.startImageCapture(ComPanyActivity.this, ExtraName.TAKE_PICTURE);
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                                        for (String permission : deniedPermissionList) {
                                            ToasShow.showToastCenter(ComPanyActivity.this, permission.toString());
                                        }
                                    }
                                });
                    }
                    break;

            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_com_pany;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle("公司信息");

        myUserInfo = (MyUserInfo) getIntent().getSerializableExtra("userkey");

        companyEdit = getViewById(R.id.companyEdit);
        abnEdit = getViewById(R.id.abnEdit);
        acnEdit = getViewById(R.id.acnEdit);
        acomAdrLinnear = getViewById(R.id.acomAdrLinnear);
        compaanyRelative = getViewById(R.id.compaanyRelative);
        logoImageView = getViewById(R.id.logoImageView);
        setInfo(myUserInfo);

    }

    private void setInfo(MyUserInfo myUserInfo) {

        //公司名称
        companyEdit.setText((null == myUserInfo.getResult().getBusiness_name() || TextUtils.isEmpty(myUserInfo.getResult().getBusiness_name())) ? "" : myUserInfo.getResult().getBusiness_name());
        abnEdit.setText((null == myUserInfo.getResult().getAbn() || TextUtils.isEmpty(myUserInfo.getResult().getAbn())) ? "" : myUserInfo.getResult().getAbn());
        acnEdit.setText((null == myUserInfo.getResult().getAcn() || TextUtils.isEmpty(myUserInfo.getResult().getAcn())) ? "" : myUserInfo.getResult().getAcn());


        //公司logo
        if (!TextUtils.isEmpty(myUserInfo.getResult().getSales_logo())) {
            Picasso.with(this).load(Contants.DOMAIN + "/" + myUserInfo.getResult().getSales_logo()).
                    config(Bitmap.Config.RGB_565).into(logoImageView);
        }

    }

    @Override
    public void setListener() {
        acomAdrLinnear.setOnClickListener(mOnClick);
        compaanyRelative.setOnClickListener(mOnClick);

    }

    private void getInfo() {

        String business_name, abn, acn;

        //公司名称
        business_name = companyEdit.getText().toString();
        BaseApplication.getInstance().myUserInfo.getResult().setBusiness_name(business_name);
        //abn
        abn = abnEdit.getText().toString();
        BaseApplication.getInstance().myUserInfo.getResult().setAbn(abn);
        //acn
        acn = acnEdit.getText().toString();
        BaseApplication.getInstance().myUserInfo.getResult().setAcn(acn);
        //logo
        BaseApplication.getInstance().myUserInfo.getResult().setSales_logo(imagePath);




    }

    @Override
    protected void onPause() {
        super.onPause();
        getInfo();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //拍照
                case ExtraName.TAKE_PICTURE:
                    Uri photoUri = BitmapUtil.imgUri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        if (photoUri != null) {
                            imagePath = BitmapUtil.getImagePath(ComPanyActivity.this, photoUri, null, null);
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            logoImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, imagePath)));
                        }

                    } else {
                        Uri imgUri = Uri.parse(BitmapUtil.imgPath);
                        if (imgUri != null) {
                            imagePath = imgUri.getPath();
                            logoImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapFactory.decodeFile(imagePath)));
                        }

                    }

                    break;

            }

        }

    }
}
