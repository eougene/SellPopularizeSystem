package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.FileAdapter;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends BaseActivity {
    private ListView listView;
    private ProductDetailBean.ResultBean prs;
    private List<FileContent> file_content = new ArrayList<>();
    private FileAdapter fileAdapter;
    private String key;


    @Override
    protected int setContentView() {
        return R.layout.activity_file;
    }

    @Override
    public void initView() {
        hideRightImagview();
        key = getIntent().getStringExtra("key");
        prs = (ProductDetailBean.ResultBean) getIntent().getSerializableExtra("file");

        if (key.equals("File")){
            setTitle(getString(R.string.file_act));
            listView = getViewById(R.id.fileListView);
            file_content = prs.getFile_content();
            fileAdapter = new FileAdapter(this, file_content);
            listView.setAdapter(fileAdapter);
        }else {
            setTitle(getString(R.string.contract));
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
