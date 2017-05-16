package com.yd.org.sellpopularizesystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.activity.InvestigationActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hejin on 2017/2/28.
 */

public class LastFragmentView extends BaseFragmentView {
    public TextView tvStart;
    public TextView tvEnd;
    private String type = null;
    private String studyId ;
    public static LastFragmentView getInstnce(String type,String studyId) {
        LastFragmentView photoViewFragment = new LastFragmentView();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("study_id", studyId);
        photoViewFragment.setArguments(bundle);
        return photoViewFragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.last_pager_layout);
        type =  getArguments().getString("type");
        studyId=getArguments().getString("study_id");
        tvStart = getViewById(R.id.tvStartInvestigation);
        tvEnd = getViewById(R.id.tvEndInvestigation);
        if (type.equals(ExtraName.INVISIBILITY)){
            controlViewVisibility();
        }
        setListener();
    }
    Intent intent;
    @Override
    protected void setListener() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //开始调研
                if(type.equals(ExtraName.VISIBILITY)){
                    intent = new Intent(getActivity(), InvestigationActivity.class);
                    getActivity().startActivity(intent);

                    //提交完成学习
                }else{
                    submit();

                }

            }
        });

        //结束推广
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActiviyt.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void submit() {
        showLoadingDialog();
        FinalHttp http=new FinalHttp();
        http.addHeader("Content-Type","application/x-www-form-urlencoded");
        AjaxParams ajaxParams=new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("study_id",studyId.equals("")?"":studyId);
        Log.e(TAG, "submit: "+studyId);
        http.post(Contants.STUDY_COMPLETE, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                dismissLoadingDialog();
                try {
                    JSONObject json=new JSONObject(s);
                    if(json.getString("code").equals("1")){
                        ToasShow.showToastBottom(getActivity(),json.getString("msg"));
                        /*intent = new Intent(getActivity(), LearningGardenActivity.class);
                        getActivity().startActivity(intent);*/
                        getActivity().finish();
                    }else{
                        ToasShow.showToastBottom(getActivity(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


//    private void setListener() {
//        tvStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), InvestigationActivity.class);
//                getActivity().startActivity(intent);
//            }
//        });
//        tvEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ScaleActivity.class);
//                getActivity().startActivity(intent);
//                getActivity().finish();
//            }
//        });
//    }

    public void controlViewVisibility() {
        if (tvEnd != null && tvStart != null) {
            tvEnd.setVisibility(View.GONE);
            tvStart.setText("完成学习");
        } else {
            Toast.makeText(getActivity(),"控件未初始化", Toast.LENGTH_SHORT).show();
        }
    }
}
