package com.yd.org.sellpopularizesystem.activity;

import android.util.Log;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.CommissionBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

public class CommissionDetailsActivity extends BaseActivity {
    private CommissionBean.ResultBean resultBean;
    private TextView tvcdTitle, tvFirstCom, tvFirstDate, tvSecondtCom, tvSecondtDate, tvThreeCom, tvThreeDate, tvSunCom;


    @Override
    protected int setContentView() {
        return R.layout.activity_commission_details;
    }

    @Override

    public void initView() {
        hideRightImagview();
        setTitle(getString(R.string.commissiondetails));

        //获取数据
        resultBean = (CommissionBean.ResultBean) getIntent().getSerializableExtra("commission");
        Log.e("resultBean**", "resultBean:" + resultBean.getProduct_childs_unit_number());

        tvcdTitle = getViewById(R.id.tvcdTitle);
        tvFirstCom = getViewById(R.id.tvFirstCom);
        tvFirstDate = getViewById(R.id.tvFirstDate);
        tvSecondtCom = getViewById(R.id.tvSecondtCom);
        tvSecondtDate = getViewById(R.id.tvSecondtDate);
        tvThreeCom = getViewById(R.id.tvThreeCom);
        tvThreeDate = getViewById(R.id.tvThreeDate);
        tvSunCom = getViewById(R.id.tvSunCom);


        tvcdTitle.setText(resultBean.getProduct_name() + " / " + resultBean.getProduct_childs_lot_number() + " / " + resultBean.getProduct_childs_unit_number());
        tvFirstCom.setText("1. " + resultBean.getFirst_commossion() + " / - /");
        tvFirstDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd HH:mm", Long.parseLong(resultBean.getFirst_time() + "000")));

        tvSecondtCom.setText("2. " + resultBean.getSecond_commossion() + " / - / ");
        tvSecondtDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd HH:mm", Long.parseLong(resultBean.getSecond_time() + "000")));


        tvThreeCom.setText("3. " + resultBean.getThird_commossion() + " / - / ");
        tvThreeDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd HH:mm", Long.parseLong(resultBean.getThird_time() + "000")));

        tvSunCom.setText(getString(R.string.total) + resultBean.getFirst_total() + " / " + resultBean.getSecond_total() + " / " + resultBean.getThird_total());


    }

    @Override
    public void setListener() {

    }
}
