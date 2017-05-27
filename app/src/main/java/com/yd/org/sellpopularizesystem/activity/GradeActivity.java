package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.GradeAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ExamlineBean;
import com.yd.org.sellpopularizesystem.javaBean.GradeBean;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.DecimalFormat;

public class GradeActivity extends BaseActivity {
    private ListView lvTestResult;
    private TextView tvCount, tvSureCount, tvAccuracy;
    private GradeBean gradeBean;
    //private CommonAdapter mCommonAdapter;
    private GradeAdapter gradeAdapter;

    private ExamlineBean.ResultBean resultBean;

    @Override
    protected int setContentView() {
        return R.layout.activity_grade;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.check));
        hideRightImagview();
        initViews();
        getData();

    }

    private void initViews() {

        Bundle b = getIntent().getExtras();
        resultBean = (ExamlineBean.ResultBean) b.getSerializable("key");
        lvTestResult = getViewById(R.id.lvResult);
        tvCount = getViewById(R.id.tvCount);
        tvSureCount = getViewById(R.id.tvSureCount);
        tvAccuracy = getViewById(R.id.tvAccuracy);
    }

    @Override
    public void setListener() {

    }

    private void getData() {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("answer_id", resultBean.getAnswer_id() + "");

        Log.e("answer_id", "answer_id:" + resultBean.getAnswer_id());

        finalHttp.get(Contants.GET_TEST_RESULT, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e("数据", "s:" + s);
                closeDialog();
                if (null != s) {
                    jsonParse(s);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
            }
        });

    }

    private void jsonParse(String json) {
        Gson gson = new Gson();
        gradeBean = gson.fromJson(json, GradeBean.class);
        if (gradeBean.getCode().equals("1")) {
            tvCount.setText(gradeBean.getTotal_answer() + getString(R.string.topic));
            tvSureCount.setText(gradeBean.getTotal_right_answer() +getString(R.string.topic));
            DecimalFormat df = new DecimalFormat("0.00");
            tvAccuracy.setText(df.format(((Float.valueOf(gradeBean.getTotal_right_answer()) / Float.valueOf(gradeBean.getTotal_answer())) * 100)) + " %");
            gradeAdapter = new GradeAdapter(this, gradeBean.getResult());

            lvTestResult.setAdapter(gradeAdapter);


//
//            //设置适配器
//            mCommonAdapter = new CommonAdapter<GradeBean.ResultBean>(GradeActivity.this, gradeBean.getResult(), R.layout.grade_display_listview_item) {
//                @Override
//                public void convert(ViewHolder holder, final GradeBean.ResultBean fitem) {
//
//                    holder.setText(R.id.questionTitle, (holder.getPosition() + 1) + "." + fitem.getCheck_title() + "");
//
//                    ChildListView itemListView = (ChildListView) holder.setListView(R.id.itemListView);
//
//                    CommonAdapter mItemCommonAdapter = new CommonAdapter<GradeBean.ResultBean.OptionsBean>(GradeActivity.this, fitem.getOptions(), R.layout.item_search_history) {
//                        @Override
//                        public void convert(ViewHolder holder, GradeBean.ResultBean.OptionsBean item) {
//                            holder.setText(R.id.tvHistory, item.getOption_name());
//                            if (!TextUtils.isEmpty(fitem.getUser_answer())) {
//                                String all[] = fitem.getUser_answer().split("\\,");
//                                Log.e("all**", "all:" + all.length);
//                                for (int i = 0; i < all.length; i++) {
//                                    if (Integer.parseInt(all[i]) == item.getOption_id()) {
//                                        Log.e("适配**", "适配:" + Integer.parseInt(all[i]));
//                                        holder.setTextColor(R.id.tvHistory, Color.RED);
//                                    } else {
//                                        holder.setTextColor(R.id.tvHistory, Color.BLACK);
//                                    }
//                                }
//                            }
//
//
//                        }
//                    };
//
//                    itemListView.setAdapter(mItemCommonAdapter);
//
//                }
//            };
//            lvTestResult.setAdapter(mCommonAdapter);
//
        }
    }

}
