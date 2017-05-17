package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.FileAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends BaseActivity implements OnPageChangeListener
        , OnLoadCompleteListener {
    private ListView listView;
    private ProductDetailBean.ResultBean prs;
    private List<FileContent> file_content = new ArrayList<>();
    private FileAdapter fileAdapter;
    private String key;


    //合同
    private PDFView pdfView;


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
        pdfView = (PDFView) findViewById(R.id.pdfView);
        if (key.equals("File")) {
            setTitle(getString(R.string.file_act));
            pdfView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            file_content = prs.getFile_content();
            fileAdapter = new FileAdapter(this, file_content);
            listView.setAdapter(fileAdapter);
        } else {
            showDialog();
            setTitle(getString(R.string.contract));

            pdfView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            displayFromFile1(Contants.DOMAIN + "/" + prs.getContract_url(), prs.getProduct_id()+prs.getProduct_name() + ".pdf");


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

    /**
     * 获取打开网络的pdf文件
     *
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1(String fileUrl, String fileName) {

        pdfView.fileFromLocalStorage(this, this, fileUrl, fileName);   //设置pdf文件地址

    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        ToasShow.showToastBottom(FileActivity.this, page +
                "/" + pageCount);
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        closeDialog();
    }
}
