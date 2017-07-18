package com.yd.org.sellpopularizesystem.activity;

import com.yd.org.sellpopularizesystem.R;

/**
 * 发票
 */

public class InvoiceActivity extends BaseActivity {


    @Override
    protected int setContentView() {
        return R.layout.activity_invoice;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle(getString(R.string.invoice));

    }

    @Override
    public void setListener() {

    }
}
