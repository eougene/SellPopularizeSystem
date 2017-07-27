package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.LicenceBean;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class MyCertificateActivity extends BaseActivity {
    private TextView stateTextView, dataTextView_01, dataTextView_02, zhTypeTextView, subButton;
    private EditText zhEdTextView;
    private ImageView srcImageView, pointImageView;
    private String picPath = "", licence_type = "1";
    private String type = "0";
    private PopupWindow firbSelectPopWindow;
    private View firbPwView;
    private Button btUnknown, btSure, btFalse;
    private boolean isFile = true;
    private File file = null;

    /**
     * 与日期选择相关
     */
    private TimePickerView pvTime;

    private View.OnClickListener mOn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //日期
                case R.id.dataTextView_01:
                    type = "0";
                    pvTime.show();
                    break;
                //结束时间
                case R.id.dataTextView_02:
                    type = "1";
                    pvTime.show();
                    break;
                //证件类型
                case R.id.zhTypeTextView:
                    firbSelectPopWindow.showAtLocation(MyCertificateActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);


                    break;

                case R.id.pointImageView:
                    firbSelectPopWindow.showAtLocation(MyCertificateActivity.this.findViewById(R.id.activity_custom_detailed), Gravity.BOTTOM, 0, 0);


                    break;

                //选择
                case R.id.srcImageView:
                    startPhoto();
                    break;
                //提交
                case R.id.subButton:
                    commintInfo();
                    break;
            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_my_certificate;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle(getResources().getString(R.string.certificate));
        stateTextView = getViewById(R.id.stateTextView);
        dataTextView_01 = getViewById(R.id.dataTextView_01);
        dataTextView_02 = getViewById(R.id.dataTextView_02);
        zhTypeTextView = getViewById(R.id.zhTypeTextView);
        zhEdTextView = getViewById(R.id.zhEdTextView);
        srcImageView = getViewById(R.id.srcImageView);
        pointImageView = getViewById(R.id.pointImageView);
        subButton = getViewById(R.id.subButton);


        setTimePicker();
        showZh();

        getInfo();


    }

    @Override
    public void setListener() {
        dataTextView_01.setOnClickListener(mOn);
        dataTextView_02.setOnClickListener(mOn);
        zhTypeTextView.setOnClickListener(mOn);
        subButton.setOnClickListener(mOn);
        srcImageView.setOnClickListener(mOn);
        pointImageView.setOnClickListener(mOn);
    }

    private void setTimePicker() {
        TimePickerView.Builder builder = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {


                if (type.equals("0")) {
                    //生效日期
                    dataTextView_01.setText(MyUtils.getTime("yyyy/MM/dd", date));
                } else {

                    //结束日期
                    if (date.getTime() < (new Date()).getTime()) {
                        ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.current_time));
                    } else {
                        dataTextView_02.setText(MyUtils.getTime("yyyy/MM/dd", date));
                    }
                }


            }
        });
        builder.setType(TimePickerView.Type.YEAR_MONTH_DAY);
        //时间选择器
        pvTime = new TimePickerView(builder);

    }

    private void startPhoto() {
        if (Build.VERSION.SDK_INT < 23) {
            BitmapUtil.startImageCapture(MyCertificateActivity.this, ExtraName.TAKE_PICTURE);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new PermissionListener() {
                        @Override
                        public void onGranted() {// 全部授权成功回调
                            // 执行具体业务
                            BitmapUtil.startImageCapture(MyCertificateActivity.this, ExtraName.TAKE_PICTURE);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                            for (String permission : deniedPermissionList) {
                                ToasShow.showToastCenter(MyCertificateActivity.this, permission.toString());
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //拍照
                case ExtraName.TAKE_PICTURE:
                    Uri photoUri = BitmapUtil.imgUri;

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        picPath = BitmapUtil.getImagePath(MyCertificateActivity.this, photoUri, null, null);
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Log.e("招聘***1", "picPath:" + picPath);
                        srcImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapUtil.reviewPicRotate(bitmap, picPath)));
                    } else {
                        Uri imgUri = Uri.parse(BitmapUtil.imgPath);
                        picPath = imgUri.getPath();
                        Log.e("招聘***2", "picPath:" + picPath);
                        srcImageView.setImageBitmap(BitmapUtil.compressBitmap(BitmapFactory.decodeFile(picPath)));
                    }

                    break;

            }
        }
    }

    private void showZh() {
        firbPwView = LayoutInflater.from(this).inflate(R.layout.firb_popuwindow, null);
        RelativeLayout rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.rlFirb);
        btUnknown = (Button) firbPwView.findViewById(R.id.btUnknown);
        btUnknown.setVisibility(View.GONE);
        btSure = (Button) firbPwView.findViewById(R.id.btSure);
        btSure.setText("Certificate of Registration");
        btFalse = (Button) firbPwView.findViewById(R.id.btFalse);
        btFalse.setText("Corporation Licence");
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
                licence_type = "1";
                zhTypeTextView.setText("Certificate of Registration");
                firbSelectPopWindow.dismiss();
            }
        });

        btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                licence_type = "2";
                zhTypeTextView.setText("Corporation Licence");
                firbSelectPopWindow.dismiss();
            }
        });


    }

    private void commintInfo() {


        try {
            String sTime = "", endTime = "", licence_number = "", licence_name = "";


            if ((dataTextView_01.getText().toString()).equals(getString(R.string.commencement_date))) {
                ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.select_valid_date));
                return;

            } else {
                sTime = String.valueOf(MyUtils.getInstance().string2Date("yyyy/MM/dd", dataTextView_01.getText().toString()));

            }

            if ((dataTextView_02.getText().toString()).equals(getString(R.string.maturity_date))) {
                ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.select_due_date));
                return;

            } else {
                endTime = String.valueOf(MyUtils.getInstance().string2Date("yyyy/MM/dd", dataTextView_02.getText().toString()));

            }


            if (TextUtils.isEmpty(zhEdTextView.getText().toString().trim())) {
                ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.Fill_id_number));
                return;
            } else {
                licence_number = zhEdTextView.getText().toString().trim();
            }

            if ((zhTypeTextView.getText().toString().trim()).equals(getString(R.string.certificate_type))) {
                ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.select_certificate_type));
                return;
            } else {
                licence_name = zhTypeTextView.getText().toString().trim();
            }


            if (!picPath.startsWith("https://")) {
                isFile = true;
                if (TextUtils.isEmpty(picPath) || picPath == "") {
                    ToasShow.showToastCenter(MyCertificateActivity.this, getString(R.string.upload_certificate));
                    return;
                } else {
                    file = new File(picPath);
                }

            } else {
                isFile = false;
            }


            showDialog();

            FinalHttp finalHttp = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
            ajaxParams.put("licence_type", licence_type);
            ajaxParams.put("licence_number", licence_number);
            ajaxParams.put("effective_date", sTime.substring(0, sTime.length() - 3));
            ajaxParams.put("expiry_date", endTime.substring(0, endTime.length() - 3));
            if (isFile) {
                ajaxParams.put("file", file);
            }

            ajaxParams.put("licence_name", licence_name);
            ajaxParams.put("abn", "");
            ajaxParams.put("acn", "");
            ajaxParams.put("is_gst", "");

            Log.e("参数**", "ajaxParams:" + ajaxParams.toString());

            finalHttp.post(Contants.UPLOAD_LICENCE, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    closeDialog();
                    Log.e("errorNo***", "errorNo:" + errorNo);
                    ToasShow.showToastCenter(MyCertificateActivity.this, strMsg);
                }

                @Override
                public void onSuccess(String s) {
                    closeDialog();
                    Log.e("s***", "s:" + s);


                    if (null != s) {
                        Gson gson = new Gson();
                        ErrorBean e = gson.fromJson(s, ErrorBean.class);
                        if (e.getCode().equals("1")) {
                            ToasShow.showToastCenter(MyCertificateActivity.this, e.getMsg() + ":" + e.getResult());
                            finish();
                        } else {
                            ToasShow.showToastCenter(MyCertificateActivity.this, e.getMsg());
                        }
                    }
                }


            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("e***", "e:" + e.getMessage());
        }


    }

    private void getInfo() {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        finalHttp.get(Contants.LICENCE_INFO, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
            }

            @Override
            public void onSuccess(String s) {
                closeDialog();
                Log.e("s***", "s:" + s);
                if (null != s) {
                    Gson gson = new Gson();
                    LicenceBean lb = gson.fromJson(s, LicenceBean.class);

                    if (lb.getCode() == 1) {

                        if (lb.getResult().getStatus() == 0) {
                            stateTextView.setText(R.string.state_approval);
                        } else if (lb.getResult().getStatus() == 1) {
                            stateTextView.setText(R.string.valid_state);
                        } else if (lb.getResult().getStatus() == 2) {
                            stateTextView.setText(R.string.state_refused);
                        } else if (lb.getResult().getStatus() == 3) {
                            stateTextView.setText(R.string.State_overdue);
                        }


                        picPath = Contants.DOMAIN + "/" + lb.getResult().getLicence_file();
                        dataTextView_01.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(lb.getResult().getEffective_date() + "000")));
                        dataTextView_02.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(lb.getResult().getExpiry_date() + "000")));
                        zhEdTextView.setText(lb.getResult().getLicence_number());

                        if (lb.getResult().getLicence_type() == 1) {
                            zhTypeTextView.setText("Certificate of Registration");
                        } else {
                            zhTypeTextView.setText("Corporation Licence");
                        }


                        Picasso.with(MyCertificateActivity.this).load(Contants.DOMAIN + "/" + lb.getResult().getLicence_file()).
                                config(Bitmap.Config.RGB_565).into(srcImageView);


                    }
                }
            }
        });
    }


}
