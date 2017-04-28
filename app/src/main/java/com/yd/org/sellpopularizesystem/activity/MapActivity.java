package com.yd.org.sellpopularizesystem.activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends BaseActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;
    private View mMakerView;
    private TextView tvDes;
    private List<ProductListBean.ResultBean> productData=new ArrayList<>();
    BitmapDescriptor bd= BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

    private BitmapDescriptor getBitmapDescriptor(int i) {
        tvDes.setText( productData.get(i).getProduct_name());
        //启用绘图缓存
        mMakerView.setDrawingCacheEnabled(true);
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        mMakerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //这个方法也非常重要，设置布局的尺寸和位置
        //LogUtils.e("测量后的尺寸：" + mViewInflater.getMeasuredWidth() + "....." + mViewInflater.getMeasuredHeight());
        mMakerView.layout(0, 0, mMakerView.getMeasuredWidth(), mMakerView.getMeasuredHeight());
        //获得绘图缓存中的Bitmap
        mMakerView.buildDrawingCache();
        Bitmap mCacheBitmap = mMakerView.getDrawingCache();
        Bitmap mBitmap = Bitmap.createBitmap(mCacheBitmap);
        BitmapDescriptor bdBitMap=BitmapDescriptorFactory.fromBitmap(mBitmap);
        return bdBitMap;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        setBackImageView(R.mipmap.wback_1);
        setTitle(R.string.home_scale);
        setColor(Color.WHITE);
        setBaseLayoutBackground(Color.BLACK);
        productData= (List<ProductListBean.ResultBean>) getIntent().getSerializableExtra("data");
        Log.e("TAG", "initView: "+ productData.size());
        mMakerView= LayoutInflater.from(this).inflate(R.layout.map_maker_view,null);
        tvDes= (TextView) mMakerView.findViewById(R.id.tvDescription);
        mMapView=  getViewById(R.id.mv_bmap);
        mBaiduMap=mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mBaiduMap.getMinZoomLevel());
        mBaiduMap.setMapStatus(msu);
        initOverlay();

    }

    private void initOverlay() {
        // 定义Maker坐标点
        for (int i = 0; i < productData.size(); i++) {
            LatLng lat=new LatLng(productData.get(i).getLatitude(),productData.get(i).getLongitude());
            ///构建MarkerOption，用于在地图上添加
            MarkerOptions mo=new MarkerOptions().position(lat).icon(getBitmapDescriptor(i)).zIndex(9).draggable(true);
            mBaiduMap.addOverlay(mo);

        }
    }

    @Override
    public void setListener() {
        clickRightImageView(R.mipmap.maplist, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.reverse_anim);
            }
        });

    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
        bd.recycle();
    }
}
