package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.myView.ShareDialog;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ProductItemDetailActivity extends BaseActivity {
    private TextView tvId, tvProdes, tvIsSalingNum, tvHasSaledNum, tvFirbNum, tvEoiTime,
            tvSaleDeadTime, tvCloseDate, tvMemo, tvProjectPro, tvSupplier, tvLawyer,
            tvBuilder, tvDespositHolder, tvForeignMoney, tvCashDesposit, tvSubscription,
            tvIntroduce, tvVideo, tvOrder, tvFloor, tvContract, tvFile;
    private ImageView ivCart;
    private RollPagerView rpv;
    private ProductListBean.ResultBean resultBean;
    ProductDetailBean.ResultBean prs;
    private List childs = new ArrayList();
    private UMShareListener mUmShareListener;
    private ShareAction mShareAction;
    private String string;
    private static Bitmap bitmap;
    public int images;
    private Bundle bun = new Bundle();
    ;
    private String product_id;

    @Override
    protected int setContentView() {
        //hideRightImagview();
        return R.layout.activity_product_item_des;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        string = bundle.getString("productName");
        resultBean = (ProductListBean.ResultBean) bundle.getSerializable("bean");
        product_id = String.valueOf(resultBean.getProduct_id());
        childs.addAll(resultBean.getChilds());
        setTitle(string);
        initViews();
        clickRightImageView(R.mipmap.share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareDialog();
            }
        });
        initData();
    }

    private void openShareDialog() {
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
             Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE,
             Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
           // ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
        new ShareDialog(ProductItemDetailActivity.this, new ShareDialog.onClickback() {
            @Override
            public void onShare(int id) {
                switch (id) {
                    case 1://微信
                        shareToMedia(SHARE_MEDIA.WEIXIN);
                        //mShareAction.open();
                        break;
                    case 2://微信朋友圈
                        shareToMedia(SHARE_MEDIA.WEIXIN_CIRCLE);
                        //mShareAction.open();
                        break;
                    case 3://facebook
                        shareToMedia(SHARE_MEDIA.FACEBOOK);
                }
            }
        }).show();
    }

    private void shareToMedia(SHARE_MEDIA share_MEDIA) {
        final UMWeb web = new UMWeb("http://www.maclandgroup.com/");
        web.setTitle(string);
        web.setDescription(resultBean.getProduct_name());
        Picasso.with(ProductItemDetailActivity.this).load(resultBean.getThumb()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ProductItemDetailActivity.bitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        web.setThumb(new UMImage(ProductItemDetailActivity.this, Contants.DOMAIN + "/" + resultBean.getThumb()));
        Log.e("TAG", "shareToMedia: " + Contants.DOMAIN + resultBean.getThumb());
        mShareAction.setPlatform(share_MEDIA).setCallback(mUmShareListener).withMedia(web).share();
    }

    private void initViews() {
        // ivCart= (ImageView) findViewById(R.id.ivCustomePhoto);
        //轮播图控件
        rpv = getViewById(R.id.rpv);
        //设置轮播时间间隔
        rpv.setPlayDelay(3000);
        //设置轮播动画持续时间
        rpv.setAnimationDurtion(500);
        //自定义指示器图片
        //rpv.setHintView(new IconHintView(this,R.mipmap.dian_true,R.mipmap.dian_false));
        rpv.setHintView(new ColorPointHintView(this, Color.WHITE, Color.parseColor("#7F7F7F")));
        rpv.setOnItemClickListener(mOnItemClickListener);
        tvId = getViewById(R.id.tvId);
        tvProdes = getViewById(R.id.tvProdes);
        tvIsSalingNum = getViewById(R.id.tvIsSalingNum);
        tvHasSaledNum = getViewById(R.id.tvHasSaledNum);
        tvFirbNum = getViewById(R.id.tvFirbNum);
        tvEoiTime = getViewById(R.id.tvEoiTime);
        tvSaleDeadTime = getViewById(R.id.tvSaleDeadTime);
        tvCloseDate = getViewById(R.id.tvCloseDate);
        tvMemo = getViewById(R.id.tvMemo);
        tvProjectPro = getViewById(R.id.tvProjectPro);
        tvSupplier = getViewById(R.id.tvSupplier);
        tvBuilder = getViewById(R.id.tvBuilder);
        tvLawyer = getViewById(R.id.tvLawyer);
        tvDespositHolder = getViewById(R.id.tvDespositHolder);
        tvForeignMoney = getViewById(R.id.tvForeignMoney);
        tvCashDesposit = getViewById(R.id.tvCashDesposit);
        tvSubscription = getViewById(R.id.tvSubscription);
        tvIntroduce = getViewById(R.id.tvIntroduce);
        tvVideo = getViewById(R.id.tvVideo);
        tvOrder = getViewById(R.id.tvOrder);
        tvFloor = getViewById(R.id.tvFloor);
        tvContract = getViewById(R.id.tvContract);
        tvFile = getViewById(R.id.tvFile);
        mUmShareListener = new CustomShareListener(this);
        mShareAction = new ShareAction(ProductItemDetailActivity.this);

    }

    private void initData() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("product_id", resultBean.getProduct_id() + "");
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        fh.get(Contants.PRODUCT_DETAIL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                Gson gson = new Gson();
                ProductDetailBean pdb = gson.fromJson(s, ProductDetailBean.class);
                if (pdb.getCode().equals("1")) {
                    prs = pdb.getResult();
                    BaseApplication.getInstance().setPrs(prs);
                    //轮播控件适配器
                    rpv.setAdapter(new NormalAdapter(rpv));
                    tvId.setText(prs.getProduct_id() + "");
                    tvProdes.setText(prs.getDescription());
                    tvIsSalingNum.setText(prs.getSell_number() + "");
                    tvHasSaledNum.setText(prs.getSign_number() + "");
                    tvFirbNum.setText(prs.getFirb_number() + "");
                    tvEoiTime.setText(prs.getEoi_open_time() + "");
                    tvSaleDeadTime.setText(prs.getStop_sales_time() + "");
                    tvCloseDate.setText(prs.getSettlement_time() + "");
                    tvMemo.setText(prs.getPreview_memo());
                    tvProjectPro.setText(prs.getProduct_type());
                    tvSupplier.setText(prs.getVendor());
                    tvLawyer.setText(prs.getVendor_lawyer());
                    tvBuilder.setText(prs.getBuilder());
                    tvDespositHolder.setText(prs.getDesposit_holder());
                    tvForeignMoney.setText(prs.getExchange_deposit());
                    tvCashDesposit.setText(prs.getFirb_exchange_deposit());
                    tvSubscription.setText(prs.getMin_reservation_fee());
                    controlColor();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                showDialog();
                ToasShow.showToastCenter(ProductItemDetailActivity.this, strMsg);
            }
        });
    }

    private void controlColor() {
        if (prs.getDescription_url() != null) {
            tvIntroduce.setAlpha(1.0f);
        }
        if (prs.getVideo_url() != null) {
            tvVideo.setAlpha(1.0f);
        }
        if (prs.getImg_content() != null && prs.getImg_content().size() > 0) {
            tvFloor.setAlpha(1.0f);
        }
        if (prs.getContract_url() != null && !prs.getContract_url().equals("")) {
            tvContract.setAlpha(1.0f);
        }
        if (prs.getFile_content() != null && prs.getFile_content().size() > 0) {
            tvFile.setAlpha(1.0f);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                //介绍
                case R.id.tvIntroduce:

                    break;
                //视频
                case R.id.tvVideo:
                    if (tvVideo.getAlpha() == 1.0f) {
                        bun.putSerializable("prs", prs);
                        ActivitySkip.forward(ProductItemDetailActivity.this, VideoActivity.class, bun);
                    }
                    break;
                //预定
                case R.id.tvOrder:
                    if (resultBean != null) {
                        bun.putSerializable("prs", prs);
                        bun.putString("productId", product_id == null ? "" : product_id);
                        bun.putString("title", resultBean.getProduct_name());
                        bun.putString("pidatopsla", "pidatopsla");
                        //Log.e("prs", "onClick: " + prs.getImg_content().size());
                        ActivitySkip.forward(ProductItemDetailActivity.this, ProductSubunitListActivity.class, bun);
                    }
                    break;
                //平面图
                case R.id.tvFloor:
                    if (resultBean != null) {
                        if (tvFloor.getAlpha() == 1.0f) {
                            bun.putSerializable("floorListData", (Serializable) prs.getImg_content());
                            ActivitySkip.forward(ProductItemDetailActivity.this, BuildingPlanActivity.class, bun);
                        }
                    }
                    break;
                //合同
                case R.id.tvContract:
                    if (tvContract.getAlpha() == 1.0f) {
                        bun.putSerializable("file", prs);
                        bun.putString("key", "Contract");
                        ActivitySkip.forward(ProductItemDetailActivity.this, FileActivity.class, bun);
                    }
                    break;
                //文件
                case R.id.tvFile:
                    if (tvFile.getAlpha() == 1.0f) {
                        bun.putString("key", "File");
                        bun.putSerializable("file", prs);
                        ActivitySkip.forward(ProductItemDetailActivity.this, FileActivity.class, bun);
                    }
                    break;
            }
        }
    };
    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            bun = new Bundle();
            bun.putSerializable("prs", prs);
            ActivitySkip.forward(ProductItemDetailActivity.this, ScaleDeltaileActivity.class, bun);
        }
    };

    @Override
    public void setListener() {
        /*ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("bean", (Serializable) childs);
                ActivitySkip.forward(ProductItemDetailActivity.this,ProductSubunitListActivity.class,bundle);
            }
        });*/
        tvIntroduce.setOnClickListener(mOnClickListener);
        tvVideo.setOnClickListener(mOnClickListener);
        tvOrder.setOnClickListener(mOnClickListener);
        tvFloor.setOnClickListener(mOnClickListener);
        tvContract.setOnClickListener(mOnClickListener);
        tvFile.setOnClickListener(mOnClickListener);
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<ProductItemDetailActivity> mActivity;

        private CustomShareListener(ProductItemDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToasShow.showToastCenter(ProductItemDetailActivity.this, getString(R.string.sharesuccess));
            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                String expName = t.getMessage();
                if (expName.contains(getString(R.string.noinstallapp))) {
                    ToasShow.showToastCenter(ProductItemDetailActivity.this, getString(R.string.clientnoinstallapp));
                } else {
                    ToasShow.showToastCenter(ProductItemDetailActivity.this, getString(R.string.sharefail));
                    Log.e("tag", "onError: " + t.getMessage());
                    ;
                }

            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + getString(R.string.sharecancel), Toast.LENGTH_SHORT).show();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private class NormalAdapter extends LoopPagerAdapter {
        public NormalAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Picasso.with(ProductItemDetailActivity.this).load(Contants.DOMAIN + "/" + ProductItemDetailActivity.this.prs.getImg_content().get(position).getThumbURL()).into(view);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return ProductItemDetailActivity.this.prs.getImg_content().size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
