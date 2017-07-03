package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.FileAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends BaseActivity {
    private ListView listView;
    private ProductDetailBean.ResultBean prs;
    private List<FileContent> file_content = new ArrayList<>();
    private FileAdapter fileAdapter;
    private String key;


    //合同
    private WebView pdfView;


    @Override
    protected int setContentView() {
        return R.layout.activity_file;
    }

    @Override
    public void initView() {
        hideRightImagview();
        key = getIntent().getStringExtra("key");
        prs = (ProductDetailBean.ResultBean) getIntent().getSerializableExtra("file");
        listView = getViewById(R.id.fileListView);
        pdfView = getViewById(R.id.pdfView);
        if (key.equals("File")) {
            setTitle(getString(R.string.file_act));
            pdfView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            file_content = prs.getFile_content();
            fileAdapter = new FileAdapter(this, file_content);
            listView.setAdapter(fileAdapter);
        } else {
            setTitle(getString(R.string.contract));
            pdfView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            MyUtils.getInstance().showWebView(FileActivity.this,pdfView,Contants.PDF_TEST +Contants.DOMAIN + "/" + prs.getContract_url());


        }

    }

    @Override
    public void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileAdapter.ViewHolder viewHolder = (FileAdapter.ViewHolder) view.getTag();
                FileContent fileContent = viewHolder.fileContent;
                Bundle bundle = new Bundle();
                bundle.putSerializable("pdf", fileContent);
                ActivitySkip.forward(FileActivity.this, PDFActivity.class, bundle);


            }
        });

    }


}
