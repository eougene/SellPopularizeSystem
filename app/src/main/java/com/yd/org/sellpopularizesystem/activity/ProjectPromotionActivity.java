package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.ProSubunitListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class ProjectPromotionActivity extends AppCompatActivity{
    private LinearLayout llAll;
    private TextView tvHotSale,tvLookHouse,tvMore;
    private GridView gvHouse;
    private Toolbar mToolbar;
    private CommonAdapter mCommonAdapter;
    private List datas=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_promotion);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        mToolbar= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
    }

    private void initView() {
        llAll= (LinearLayout) findViewById(R.id.llAll);
        tvHotSale= (TextView) findViewById(R.id.tvHotSale);
        tvLookHouse= (TextView) findViewById(R.id.tvLookHouse);
        tvMore= (TextView) findViewById(R.id.tvMore);
        gvHouse= (GridView) findViewById(R.id.gvHouse);
        setAdapter();
    }

    private void setAdapter() {
        mCommonAdapter=new CommonAdapter<ProductListBean.ResultBean>(this,datas, R.layout.gv_item_house_suggest) {
            @Override
            public void convert(ViewHolder holder, ProductListBean.ResultBean item) {
                holder.setImageByUrl(R.id.ivHousePic, Contants.DOMAIN + "/"+item.getThumb(),mOnClickListener);
                holder.setText(R.id.tvName,item.getProduct_name());
                holder.setText(R.id.tvLocation,item.getAddress());
                holder.setText(R.id.tvHousePrice,item.getMin_reservation_fee());
            }
        };
        gvHouse.setAdapter(mCommonAdapter);
    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


}
