package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.WheelOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.GradeBean;
import com.yd.org.sellpopularizesystem.javaBean.VisitRecord;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CusOprateRecordActivity extends BaseActivity {
    private String customeId;
    private Bundle bundle;
    private String flag;
    private TextView tvMoneyNum, tvPayMethod, tvEoiSubmit;
    private EditText etCertificateTime;
    private Button btDoacash, btDoaTransfer, btDoaCancel, btFromCamera, btFromAlbum, btPhotoCancel;
    private ImageView ivCertificate;
    private String picPath;
    private LinearLayout llSpace, llCertificate, llChooseSpace;
    private Dialog dialog, methodDialog, optionDialog;
    private List weeks;
    private List hours;
    private List minutes;
    private CommonAdapter visitAdapter;
    private OptionsPickerView pvCustomTime;

    @Override
    protected int setContentView() {
        setBaseLayoutBackground(Color.WHITE);
        return R.layout.activity_cus_oprate_record;
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        flag = bundle.getString("custocora");
        customeId = (String) bundle.get("customeId");
        Log.e("TAG", "initView: " + customeId);
        if (flag.equals("custovisit") || flag.equals("custoreser")) {
            if (flag.equals("custovisit")) {
                setTitle(getString(R.string.visit));
                getVisitData();
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
        } else if (flag.equals("custoexpand") || flag.equals("custopurchase")) {
            hideRightImagview();
            if (flag.equals("custoexpand")) {
                setTitle(getString(R.string.expandre));
                getExpandReData();
            } else if (flag.equals("custopurchase")) {
                setTitle(getString(R.string.housepurchase));
                getHousePurData();
            }
        } else {
            setTitle("EOI");
            getEoiData();
            setRightTitle(R.string.recharge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(CusOprateRecordActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.eoi_operate_view);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.x = MyUtils.getStatusBarHeight(CusOprateRecordActivity.this);
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
                    dialog.show();
                    initDialogViews(dialog);
                }
            });

        }
    }

    private void initDialogViews(Dialog dialog) {
        tvMoneyNum = (TextView) dialog.findViewById(R.id.tvMoneyNum);
        tvPayMethod = (TextView) dialog.findViewById(R.id.tvPayMethod);
        tvEoiSubmit = (TextView) dialog.findViewById(R.id.tvEoiSubmit);
        llCertificate = (LinearLayout) dialog.findViewById(R.id.llCertificate);
        etCertificateTime = (EditText) dialog.findViewById(R.id.etUploadTime);
        ivCertificate = (ImageView) dialog.findViewById(R.id.ivCertificate);
        tvPayMethod.setOnClickListener(mOnClickListener);
        tvEoiSubmit.setOnClickListener(mOnClickListener);
        etCertificateTime.setOnClickListener(mOnClickListener);
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
        }).setTitleText("凭证上传时间").setTitleColor(R.color.black)
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
                tvTitle.setText("凭证上传时间");
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
        if (str1.equals("今天")) {
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
                    initPayMethodDialog();
                    //payMethodPop.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent),Gravity.BOTTOM, 0,0);
                    break;
                case R.id.tvEoiSubmit:
                    if (picPath != null) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, "请上传支付凭证");
                    } else if (TextUtils.isEmpty(etCertificateTime.getText())) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, "请填写凭证转账时间");
                    } else {
                        submitEoi();
                    }
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
                case R.id.etUploadTime:
                    pvCustomTime.show();
                    break;
                case R.id.ivCertificate:
                    initOptionDialog();
                    break;
                case R.id.btFromCamera:
                    optionDialog.dismiss();
                    BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                    break;
                case R.id.btFromAlbum:
                    optionDialog.dismiss();
                    BitmapUtil.gotoSysPic(CusOprateRecordActivity.this, ExtraName.ALBUM_PICTURE);
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
            }
        }
    };

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
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        try {
            ajaxParams.put("client", customeId);
            ajaxParams.put("sales_id", SharedPreferencesHelps.getUserID());
            ajaxParams.put("payment_method", "1");
            ajaxParams.put("payment_amount", tvMoneyNum.getText().toString());
            File picFile = new File(picPath);
            ajaxParams.put("file", picFile);
            ajaxParams.put("pay_time", etCertificateTime.getText().toString());
            ajaxParams.put("currency", tvMoneyNum.getText().toString());
            ajaxParams.put("purchaseReason", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fh.post(Contants.EOI_RECHARGE, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastCenter(CusOprateRecordActivity.this, json.getString("msg"));
                    } else {
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

    private void getEoiData() {
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", "1");
        ajaxParams.put("number", "10");
        ajaxParams.put("company_id", BaseApplication.getInstance().getResultBean().getCompany_id() + "");
        ajaxParams.put("client", BaseApplication.getInstance().getResultBean().getCustomer_id() + "");
        ajaxParams.put("property_id", "");
        ajaxParams.put("is_use", "");
        ajaxParams.put("house", "");
        fh.get(Contants.EOI_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    @Override
    public void setListener() {

    }

    private void getVisitData() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id",SharedPreferencesHelps.getUserID());
        ajaxParams.put("customer_id",customeId);
        ajaxParams.put("page","");
        ajaxParams.put("number","");
        fh.get(Contants.VISIT_RECORD_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                super.onSuccess(s);
                Gson gson=new Gson();
                VisitRecord visitRecord=gson.fromJson(s,VisitRecord.class);
                if (visitRecord.getResult().size()>0){
                    List<VisitRecord.ResultBean> vrrb=new ArrayList<VisitRecord.ResultBean>();
                    vrrb=visitRecord.getResult();
                    visitAdapter=new CommonAdapter<VisitRecord.ResultBean>(CusOprateRecordActivity.this,vrrb,R.layout.visit_listview_item_layout) {


                        @Override
                        public void convert(ViewHolder holder, VisitRecord.ResultBean item) {

                        }
                    };
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void getReservertData() {

    }

    private void getExpandReData() {

    }

    private void getHousePurData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
                            picPath = picFile.getAbsolutePath();
                            Picasso.with(this).load(picFile).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
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
                        /*Bitmap mBitmap = BitmapUtil.getThumbnail(this, selectedPhotoUri, ivCertificate.getWidth(), ivCertificate.getHeight());
                        ivCertificate.setImageBitmap(mBitmap);*/
                    picPath = BitmapUtil.getImagePath(CusOprateRecordActivity.this, selectedPhotoUri, null, null);
                    Picasso.with(this).load(selectedPhotoUri).resize(ivCertificate.getWidth(), ivCertificate.getHeight()).into(ivCertificate);
            }
        }
    }
}
