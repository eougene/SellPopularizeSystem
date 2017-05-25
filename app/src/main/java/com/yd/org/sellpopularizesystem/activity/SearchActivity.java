package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    private ListView lv;
    private EditText et_search;
    private List<String> listStrings = new ArrayList<String>();
    private TextView tvSearchCancel, tvSearchTitle, tvCancel;
    private ImageView ivBack;
    private RelativeLayout rlNoResullt;
    private SharedPreferences sp;
    private CommonAdapter adapter;
    private String history;
    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          switch (v.getId()){
              case R.id.tvCancel:
                  cleanHistory();
                  tvCancel.setText(R.string.historyclear);
                  break;
              case  R.id.tvSearchCancel:
                  finish();
                  break;
          }
      }
  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.searchListView);
        lv.setVisibility(View.VISIBLE);
        Log.d("tag", "listView: "+lv.getVisibility());
        et_search = (EditText) findViewById(R.id.edSearch);
        tvSearchCancel = (TextView) findViewById(R.id.tvSearchCancel);
        tvSearchCancel.setOnClickListener(mOnClickListener);

        tvSearchTitle = (TextView) findViewById(R.id.tvSearchTitle);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(mOnClickListener);
        //ivBack = (ImageView) findViewById(R.id.ivBack);
        rlNoResullt = (RelativeLayout) findViewById(R.id.rlNoResult);
        getHistory();

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 隐藏软键盘
                    save();
                    tvSearchTitle.setText(R.string.project);
                    tvCancel.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    rlNoResullt.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_search.getText().toString())) {
                        lv.setAdapter(adapter);

                }
            }
        });

        /*ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }

    private void getHistory() {
        history=SharedPreferencesHelps.getHistory();
        /*sp = getSharedPreferences("search_history", 0);
        Log.d("TAG", "history: "+sp.hashCode());
        history = sp.getString("history", "");
        Log.d("TAG", "history: "+history);*/
        // 用逗号分割内容返回数组
        if (history.length() != 0 && !TextUtils.isEmpty(history)) {
            String[] history_arr = history.split(",");
            for (int i = 0; i < history_arr.length; i++) {
                if (!TextUtils.isEmpty(history_arr[i])) {
                    listStrings.add(history_arr[i]);
                }
            }

        //Log.e("获取历史记录","listStrings:"+listStrings.size()+":::"+listStrings.get(0));
        adapter = new CommonAdapter<String>(this, listStrings, R.layout.item_search_history) {

            @Override
            public void convert(ViewHolder holder, final String item) {
                holder.setText(R.id.tvHistory, item);
            }
        };
        lv.setAdapter(adapter);
        } else {
            tvCancel.setText(R.string.nohistoryrecord);
            //tvCancel.setVisibility(View.GONE);
            tvCancel.setClickable(false);

        }
    }

    public void save() {
        Log.e("保存信息", "");
        // 获取搜索框信息
        String text = et_search.getText().toString();
        Log.d("text", "text: "+text);
        String old_text=SharedPreferencesHelps.getHistory();
        //String old_text = sp.getString("history", "");
        Log.d("old_text", "old_text: "+old_text);
        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(old_text);
        builder.insert(0,text + ",");
        //builder.append(text + ",");
        String str=builder.toString();
        Log.d("str", "str: "+str);
        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if (!old_text.contains(text + ",")) {
            SharedPreferencesHelps.editHistory(str,true);
            //String sptr=getSharedPreferences("search_history",0).getString("history","没有值可取");
            //Log.d("sptr", "sptr: "+sptr+"-"+getSharedPreferences("search_history",0).hashCode());
        }

    }

    // 清除搜索记录
    public void cleanHistory() {
        SharedPreferencesHelps.editHistory("",false);
        /*SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();*/
        listStrings.clear();
        adapter.notifyDataSetChanged();
        tvCancel.setClickable(false);
        Toast.makeText(this, R.string.clearsuccess, Toast.LENGTH_SHORT).show();
    }




}
