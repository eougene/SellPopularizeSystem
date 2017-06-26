package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.myView.CustomProgressDialog;
import com.yd.org.sellpopularizesystem.utils.ACache;
import com.yd.org.sellpopularizesystem.utils.ActivityCollector;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by bai on 2017/1/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected ImageView backLinearLayou, rightSearchLinearLayout,ivShare;
    protected TextView tvTitle, rightRtitle;
    private LinearLayout llBaseLayout;
    private CustomProgressDialog loading_Dialog;
    private ACache aCache;
    private View baseView;
    /**
     * 回调接口实例
     */
    private static PermissionListener mlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache = ACache.get(this);
        setContentView(R.layout.activity_base);


        loading_Dialog = new CustomProgressDialog(this, R.style.customLoadDialog);

        baseView = getViewById(R.id.baseView);

        // 标题
        tvTitle = getViewById(R.id.tvTitle);
        llBaseLayout = getViewById(R.id.titleViewParent_ll);
        rightRtitle = getViewById(R.id.rightTitle);
        //左边图片
        backLinearLayou = getViewById(R.id.backLinearLayout);
        // 右边图片
        rightSearchLinearLayout = getViewById(R.id.rightSearchLinearLayout);
        ivShare= getViewById(R.id.ivShare);
        //左边文字按钮事件处理
        backLinearLayou.setOnClickListener(clickback);


        //初始化布局文件
        if (setContentView() != 0) {
            BaseSetContentView(setContentView());
            setImmerseLayout(getViewById(R.id.base_header_layout));
            initView();
            setListener();

        }
        ActivityCollector.addActivity(this);
    }

    public void hideBaseView() {
        baseView.setVisibility(View.GONE);
    }

    public ACache getaCache() {
        return aCache;
    }

    public void setaCache(ACache aCache) {
        this.aCache = aCache;
    }


    /**
     * 设置布局
     * <p/>
     * 子类要实现的方法
     */
    protected abstract int setContentView();

    /**
     * 初始化
     * <p/>
     * 子类要实现的方法
     */
    public abstract void initView();

    /**
     * 监听
     * <p/>
     * 子类要实现的方法
     */
    public abstract void setListener();


    /**
     * 隐藏整个标题栏
     */
    public void hideBaseTab() {
        getViewById(R.id.base_header_layout).setVisibility(View.GONE);
    }


    /**
     * 设置标题/通过资源找到
     */
    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTitle(String resId) {
        tvTitle.setText(resId);
    }

    /**
     * 设置标题颜色
     */
    public void setColor(int resId) {
        tvTitle.setTextColor(resId);
    }

    /**
     * 设置基本布局背景色
     */
    public void setBaseLayoutBackground(int resId) {
        llBaseLayout.setBackgroundColor(resId);

    }

    /**
     * 设置左边图片/通过资源找到---带监听 resId==0时默认是分享图标
     */
    public void setBackImageView(int resId) {
        backLinearLayou.setImageResource(resId);

    }

    /**
     * 设置右边图片/通过资源找到---带监听 resId==0时默认是分享图标
     */
    public void clickRightImageView(int resId, View.OnClickListener onClickListener) {
        rightSearchLinearLayout.setImageResource(resId);
        rightSearchLinearLayout.setOnClickListener(onClickListener);
    }

    //设置右上角标题
    public void setRightTitle(int resId, View.OnClickListener onClickListener) {
        rightSearchLinearLayout.setVisibility(View.GONE);
        rightRtitle.setVisibility(View.VISIBLE);
        rightRtitle.setText(resId);
        rightRtitle.setOnClickListener(onClickListener);
    }

    //设置右上角标题背景色
    public void setRightTitleBackground(int drawableId, int colorId) {
        rightSearchLinearLayout.setVisibility(View.GONE);
        rightRtitle.setVisibility(View.VISIBLE);
        rightRtitle.setTextColor(colorId);
        rightRtitle.setBackgroundResource(drawableId);
    }

    //设置右上角标题背景色
    public void setRightTitleBackground(Drawable drawable, int colorId) {
        rightSearchLinearLayout.setVisibility(View.GONE);
        rightRtitle.setVisibility(View.VISIBLE);
        rightRtitle.setTextColor(colorId);
        rightRtitle.setBackground(drawable);
    }

    public void changeLeftImageView(int resId, View.OnClickListener onClickListener) {
        backLinearLayou.setImageResource(resId);
        backLinearLayou.setOnClickListener(onClickListener);
    }

    /**
     * 隐藏右边图标
     */
    public void hideRightImagview() {
        getViewById(R.id.rightSearchLinearLayout).setVisibility(View.GONE);
    }

    /**
     * 正常返回按钮事件
     */
    View.OnClickListener clickback = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };


    /**
     * 初始化父类布局
     *
     * @param layoutID
     */
    public void BaseSetContentView(int layoutID) {
        FrameLayout llcontent = getViewById(R.id.llcontent);
        llcontent.addView(LayoutInflater.from(this).inflate(layoutID, null));

    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    public <VT extends View> VT getViewById(@IdRes int id) {


        return (VT) findViewById(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        /*if (loading_Dialog != null && loading_Dialog.isShowing()) {
            loading_Dialog.dismiss();
        }*/
        super.onDestroy();
    }

    /**
     * 显示数据加载对话框
     */
    public void showDialog() {
            loading_Dialog.show();
    }

    /**
     * 关闭数据加载对话框
     */
    public void closeDialog() {
        if (loading_Dialog != null && loading_Dialog.isShowing()) {
            loading_Dialog.dismiss();
        }


    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = MyUtils.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    /**
     * 运行时权限执行方法（static修饰的必要性--在非Activity类中也可以调用）
     * @param permissions 需要授权的权限
     * @param mlistener 接口回调实例（运行时权限处理结果）
     */
    public static void requestPermissions(String[] permissions, PermissionListener mlistener) {

        // 获得栈顶Activity作为授权时的参数
        Activity topActivity = ActivityCollector.getTopActivity();
        if (topActivity == null) {
            return;
        }

        List<String> unGrantedPermissionsList = new ArrayList<String>(); // 未授权权限的集合

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissionsList.add(permission);
            }
        }
        // 授权业务
        if (!unGrantedPermissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, unGrantedPermissionsList.
                    toArray(new String[unGrantedPermissionsList.size()]), 1);
        } else { // 为空说明已全部授权，执行后续业务
            // TODO Something
            mlistener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissionList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permission);
                        }
                    }
                    if (deniedPermissionList.isEmpty()) {
                        mlistener.onGranted();
                    } else {
                        mlistener.onDenied(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }
}
