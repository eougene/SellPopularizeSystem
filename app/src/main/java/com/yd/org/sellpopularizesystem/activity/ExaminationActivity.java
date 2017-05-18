package com.yd.org.sellpopularizesystem.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.fragment.ExamineFragment;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.OptionBean;
import com.yd.org.sellpopularizesystem.javaBean.PagerDetailsBean;
import com.yd.org.sellpopularizesystem.javaBean.QuestionBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.XMLFormatter;

public class ExaminationActivity extends BaseActivity {
    private List<QuestionBean> questions = new ArrayList<>();

    private LinearLayout test_layout;
    private PagerDetailsBean pagerDetail;
    // 选项列表
    private List<OptionBean> the_answer_list;
    // 问题所在的View
    private View que_view;
    private LayoutInflater mInflater;
    private JSONArray jsonArray;


    @Override
    protected int setContentView() {
        return R.layout.activity_examination;
    }

    @Override
    public void initView() {

        setTitle(R.string.check);
        hideRightImagview();
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 提交按钮
        Button button = (Button) findViewById(R.id.btnSubmit);
        initData(7 + "");
        button.setOnClickListener(new ExaminationActivity.submitOnClickListener());

    }

    @Override
    public void setListener() {

    }

    private void initData(String paperId) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("paper_id", paperId);
        showDialog();
        http.get(Contants.PAPER_DETAILS, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("试题数据", "s:" + s);
                super.onSuccess(s);
                if (null != s) {
                    jsonParse(s);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void jsonParse(String json) {
        Gson gson = new Gson();
        pagerDetail = gson.fromJson(json, PagerDetailsBean.class);
        if (pagerDetail.getCode().equals("1")) {
            questions = pagerDetail.getResult();
            addViews(questions);
            closeDialog();
        }
    }

    private void addViews(List<QuestionBean> questions) {
        Log.e("questions*", "questions:" + questions.size());


        test_layout = (LinearLayout) findViewById(R.id.examinaltionLinearLayout);

        for (int i = 0; i < questions.size(); i++) {
            //Map<String,String> map=
            que_view = mInflater.inflate(R.layout.radiobuttonittem, null);
            TextView questionTitle = (TextView) que_view.findViewById(R.id.examinaltionTextView);
            LinearLayout option_layout = (LinearLayout) que_view.findViewById(R.id.childLinearLayout);
            //Answer answer = new Answer();
            //answer.setAnswerContent(set);
            //多选和单选
            if (questions.get(i).getType() == 3) {
                questionTitle.setText((i + 1) + "." + questions.get(i).getCheck_title() + getString(R.string.mutilatequestion));
            } else if (questions.get(i).getType() == 1) {
                questionTitle.setText((i + 1) + "." + questions.get(i).getCheck_title() + getString(R.string.yesornoquestion));
            } else if (questions.get(i).getType() == 2) {
                questionTitle.setText((i + 1) + "." + questions.get(i).getCheck_title() + getString(R.string.singlequestion));
            }
            the_answer_list = questions.get(i).getOptions();
            for (int j = 0; j < the_answer_list.size(); j++) {
                the_answer_list.get(j).setStatus(0);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setTag(i * 10 + j + "");
                radioButton.setPadding(5, 10, 0, 10);
                radioButton.setButtonDrawable(ContextCompat.getDrawable(this, R.drawable.examination_checkradio));
                radioButton.setText(the_answer_list.get(j).getOption_name());
                option_layout.addView(radioButton);
                radioButton.setOnClickListener(new answerItemOnClickListener(i, j, the_answer_list, option_layout, radioButton));
            }
            test_layout.addView(que_view);
        }

    }

    class answerItemOnClickListener implements View.OnClickListener {
        private int i;
        private int j;
        private LinearLayout linearLayout;
        private RadioButton radioButton;
        private List<OptionBean> options;

        public answerItemOnClickListener(int i, int j, List<OptionBean> options, LinearLayout linearLayout, RadioButton radioButton) {
            this.i = i;
            this.j = j;
            this.options = options;
            this.linearLayout = linearLayout;
            this.radioButton = radioButton;

        }

        // 实现点击选项后改变选中状态以及对应图片
        @Override
        public void onClick(View arg0) {
            // 判断当前问题是单选还是多选
            if (questions.get(i).getType() == 3) {
                //Log.e("多选", "ff");
                // 多选未选中
                if (options.get(j).getStatus() == 0) {
                    radioButton.setButtonDrawable(ContextCompat.getDrawable(ExaminationActivity.this, R.mipmap.selectedx));
                    Log.e("多选", "选中");
                    // 如果未被选中
                    radioButton.setChecked(true);
                    options.get(j).setStatus(1);
                    //多选选中状态
                } else if (options.get(j).getStatus() == 1) {
                    Log.e("1f2", "onClick: " + "第" + i + "题状态值为" + questions.get(i).getStatus());
                    radioButton.setButtonDrawable(ContextCompat.getDrawable(ExaminationActivity.this, R.mipmap.unselected));
                    radioButton.setChecked(false);
                    options.get(j).setStatus(0);
                }

                int falseCount = 0;
                for (int k = 0; k < questions.get(i).getOptions().size(); k++) {
                    Log.e("111", "onClick: " + "第" + i + "题状态值为" + options.get(k).getStatus());
                    if (options.get(k).getStatus() == 1) {
                        setStatuToTrue(i, questions);
                    } else {
                        falseCount++;
                        //setStatuToFalse(i,questions);
                    }
                }
                if (falseCount == questions.get(i).getOptions().size()) {
                    setStatuToFalse(i, questions);
                }
                Log.e("222", "onClick:" + questions.get(i).getStatus());
            } else {
                Log.e("单选", "ff");
                // 单选
                /*for (int z = 0; z < options.size(); z++) {
                    options.get(z).setStatus(0);
                }*/
                // 单选未选中
                if (options.get(j).getStatus() == 0) {
                    Log.e("单选", "已选中");
                    radioButton.setChecked(true);
                    radioButton.setButtonDrawable(ContextCompat.getDrawable(ExaminationActivity.this, R.mipmap.selectedx));
                    // 如果当前未被选中
                    //imglist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
                    options.get(j).setStatus(1);
                    questions.get(i).setStatus(1);
                    for (int k = 0; k < linearLayout.getChildCount(); k++) {
                        View view = linearLayout.getChildAt(k);
                        if (k != j) {
                            if (view instanceof RadioButton) {
                                ((RadioButton) view).setChecked(false);
                                ((RadioButton) view).setButtonDrawable(ContextCompat.getDrawable(ExaminationActivity.this, R.mipmap.unselected));
                                options.get(k).setStatus(0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setStatuToTrue(int i, List<QuestionBean> questions) {
        questions.get(i).setStatus(1);
    }

    private void setStatuToFalse(int i, List<QuestionBean> questions) {
        questions.get(i).setStatus(0);
    }

    class submitOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            // 判断是否答完题
            boolean isState = true;
            // 最终要的json数组
            jsonArray = new JSONArray();
            // 点击提交的时候，先判断状态，如果有未答完的就提示，如果没有再把每条答案提交（包含问卷ID 问题ID 及答案ID）
            // 注：不用管是否是一个问题的答案，就以答案的个数为准来提交上述格式的数据
            for (int i = 0; i < questions.size(); i++) {
                //the_answer_list = questions.get(i).getOptions();
                // 判断是否有题没答完
                // Log.e("hh**1", "Status" + questions.get(i).getStatus());
                if (questions.get(i).getStatus() == 0) {
                    Toast.makeText(getApplicationContext(), "您" + (i + 1) + "题没有答完", Toast.LENGTH_LONG).show();
                    jsonArray = null;
                    isState = false;
                    break;
                }

            }
            if (isState) {

                String str = null;
                String s = null;
                for (int i = 0; i < questions.size(); i++) {
                    JSONObject json = new JSONObject();
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < questions.get(i).getOptions().size(); j++) {
                        if (questions.get(i).getOptions().get(j).getStatus() == 1) {
                            if (questions.get(i).getType() == 3) {
                                sb.append(questions.get(i).getOptions().get(j).getOption_id() + ",");
                                str = sb.toString().substring(0, sb.length() - 1);
                            } else if (questions.get(i).getType() == 1 || questions.get(i).getType() == 2) {
                                s = questions.get(i).getOptions().get(j).getOption_id() + "";
                            }
                        }
                    }
                    try {
                        json.put("check_id", questions.get(i).getCheck_id());
                        if (questions.get(i).getType() == 3) {
                            json.put("user_answer", str);
                        } else {
                            json.put("user_answer", s);
                        }
                        jsonArray.put(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("提交数据**", "jsonArray:" + jsonArray.toString());
                commit();
            }
        }
    }

    private void commit() {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.addHeader("Content-Type", "application/x-www-form-urlencoded");

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("paper_id", 7 + "");
        ajaxParams.put("answer", jsonArray.toString());
        ajaxParams.put("add_ip", "");

        finalHttp.post(Contants.SUBMIT_PAPER, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                closeDialog();
                if (null!=s){
                    Gson gson=new Gson();
                    ErrorBean errorBean=gson.fromJson(s,ErrorBean.class);
                    if (errorBean.getCode().equals("1")){
                        ExamineFragment.examineFragment.handler.sendEmptyMessage(1);
                        ToasShow.showToastBottom(ExaminationActivity.this,errorBean.getMsg());
                        finish();
                    }else {
                        ToasShow.showToastBottom(ExaminationActivity.this,errorBean.getMsg());
                    }
                }
                Log.e("提交成功", "s:" + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("提交失败", "s:" + errorNo + "strMsg:" + strMsg);
                closeDialog();
                ToasShow.showToastBottom(ExaminationActivity.this, getString(R.string.submitfail));
            }
        });

    }

}
