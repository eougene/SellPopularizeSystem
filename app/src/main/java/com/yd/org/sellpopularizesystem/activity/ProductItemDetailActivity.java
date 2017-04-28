package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.myView.ShareDialog;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ProductItemDetailActivity extends BaseActivity {
    private TextView tvId,tvProdes,tvIsSalingNum,tvHasSaledNum,tvFirbNum,tvEoiTime,
            tvSaleDeadTime,tvCloseDate,tvMemo,tvProjectPro,tvSupplier,tvLawyer,
            tvBuilder,tvDespositHolder,tvForeignMoney,tvCashDesposit,tvSubscription,
            tvIntroduce,tvVideo,tvOrder,tvFloor,tvContract,tvFile;
    private ImageView ivCart;
    private ProductListBean.ResultBean resultBean;
    ProductDetailBean.ResultBean prs;
    private List childs=new ArrayList();
    private UMShareListener mUmShareListener;
    private ShareAction mShareAction;
    private String string;
    private static Bitmap bitmap;

    @Override
    protected int setContentView() {
        //hideRightImagview();
        setBaseLayoutBackground(Color.WHITE);
        return R.layout.activity_product_item_des;
    }

    @Override
    public void initView() {
        Bundle bundle=getIntent().getExtras();
        string = bundle.getString("productName");
        resultBean = (ProductListBean.ResultBean) bundle.getSerializable("bean");
        childs.addAll(resultBean.getChilds());
        setTitle(string);
        initViews();
        clickRightImageView(R.mipmap.share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开分享面板（自己定制界面，不使用友盟默认）
               /*new Thread(new Runnable() {
                   @Override
                   public void run() {

                   }
               });*/
                openShareDialog();
            }
        });
        initData();
    }

    private void openShareDialog() {
        new ShareDialog(ProductItemDetailActivity.this, new ShareDialog.onClickback() {
            @Override
            public void onShare(int id) {
                switch (id){
                    case 1://微信
                        shareToMedia(SHARE_MEDIA.WEIXIN);
                        //mShareAction.open();
                        break;
                    case 2://微信朋友圈
                        shareToMedia(SHARE_MEDIA.WEIXIN_CIRCLE);
                        //mShareAction.open();
                        break;
                }
            }
        }).show();
    }

    private void shareToMedia(SHARE_MEDIA share_MEDIA) {
                    final UMWeb web=new UMWeb("http://www.maclandgroup.com/");
                    web.setTitle(string);
                    web.setDescription(resultBean.getProduct_name());
                    Picasso.with(ProductItemDetailActivity.this).load(resultBean.getThumb()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ProductItemDetailActivity.bitmap=bitmap;
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                    web.setThumb(new UMImage(ProductItemDetailActivity.this, Contants.DOMAIN + "/" +resultBean.getThumb()));
                    Log.e("TAG", "shareToMedia: "+Contants.DOMAIN +resultBean.getThumb());
                    mShareAction.setPlatform(share_MEDIA).setCallback(mUmShareListener).withMedia(web).share();
    }

    private void initViews() {
       // ivCart= (ImageView) findViewById(R.id.ivCustomePhoto);
        tvId= getViewById(R.id.tvId);
        tvProdes=getViewById(R.id.tvProdes);
        tvIsSalingNum= getViewById(R.id.tvIsSalingNum);
        tvHasSaledNum= getViewById(R.id.tvHasSaledNum);
        tvFirbNum= getViewById(R.id.tvFirbNum);
        tvEoiTime= getViewById(R.id.tvEoiTime);
        tvSaleDeadTime= getViewById(R.id.tvSaleDeadTime);
        tvCloseDate= getViewById(R.id.tvCloseDate);
        tvMemo= getViewById(R.id.tvMemo);
        tvProjectPro= getViewById(R.id.tvProjectPro);
        tvSupplier= getViewById(R.id.tvSupplier);
        tvBuilder= getViewById(R.id.tvBuilder);
        tvLawyer= getViewById(R.id.tvLawyer);
        tvDespositHolder= getViewById(R.id.tvDespositHolder);
        tvForeignMoney= getViewById(R.id.tvForeignMoney);
        tvCashDesposit= getViewById(R.id.tvCashDesposit);
        tvSubscription= getViewById(R.id.tvSubscription);
        tvIntroduce=getViewById(R.id.tvIntroduce);
        tvVideo=getViewById(R.id.tvVideo);
        tvOrder=getViewById(R.id.tvOrder);
        tvFloor=getViewById(R.id.tvFloor);
        tvContract=getViewById(R.id.tvContract);
        tvFile=getViewById(R.id.tvFile);
        mUmShareListener=new CustomShareListener(this);
        mShareAction=new ShareAction(ProductItemDetailActivity.this);

    }

    private void initData() {
        showDialog();
        FinalHttp fh=new FinalHttp();
        AjaxParams ajaxParams=new AjaxParams();
        ajaxParams.put("product_id",resultBean.getProduct_id()+"");
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        fh.get(Contants.PRODUCT_DETAIL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Gson gson = new Gson();
                ProductDetailBean pdb= gson.fromJson(s,ProductDetailBean.class);
                if (pdb.getCode().equals("1")){
                    prs=pdb.getResult();
                    tvId.setText(prs.getProduct_id()+"");
                    tvProdes.setText(prs.getDescription());
                    tvIsSalingNum.setText(prs.getSell_number()+"");
                    tvHasSaledNum.setText(prs.getSign_number()+"");
                    tvFirbNum.setText(prs.getFirb_number()+"");
                    tvEoiTime.setText(prs.getEoi_open_time()+"");
                    tvSaleDeadTime.setText(prs.getStop_sales_time()+"");
                    tvCloseDate.setText(prs.getSettlement_time()+"");
                    tvMemo.setText(prs.getPreview_memo());
                    tvProjectPro.setText(prs.getProduct_type());
                    tvSupplier.setText(prs.getVendor());
                    tvLawyer.setText(prs.getVendor_lawyer());
                    tvBuilder.setText(prs.getBuilder());
                    tvDespositHolder.setText(prs.getDesposit_holder());
                    tvForeignMoney.setText(prs.getExchange_deposit());
                    tvCashDesposit.setText(prs.getFirb_exchange_deposit());
                    tvSubscription.setText(prs.getMin_reservation_fee());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

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

    }

    private  class CustomShareListener implements UMShareListener {

        private WeakReference<ProductItemDetailActivity> mActivity;

        private CustomShareListener(ProductItemDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToasShow.showToastCenter(ProductItemDetailActivity.this,"分享成功");
            if (bitmap!=null){
                bitmap.recycle();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {

                ToasShow.showToastCenter(ProductItemDetailActivity.this,"抱歉，分享失败");
            }
            if (bitmap!=null){
                bitmap.recycle();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            if (bitmap!=null){
                bitmap.recycle();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
