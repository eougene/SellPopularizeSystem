package com.yd.org.sellpopularizesystem.activity;

import android.net.Uri;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.utils.PDFUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.io.File;

import pdfinterface.OnFileListener;

public class PDFActivity extends BaseActivity implements OnPageChangeListener
        , OnLoadCompleteListener {
    private PDFView pdfView;
    private FileContent fileContent;


    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        hideRightImagview();

        fileContent = (FileContent) getIntent().getSerializableExtra("pdf");
        setTitle(fileContent.getDetail_name());

        pdfView = (PDFView) findViewById(R.id.pdfView);
        displayFromFile1(Contants.DOMAIN + "/" + fileContent.getUrl(), "pdfa_"+ fileContent.getProduct_id() + ".pdf");


    }

    @Override
    public void setListener() {

    }

    /**
     * 获取打开网络的pdf文件
     *
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1(String fileUrl, String fileName) {
        try {
            showDialog();
            PDFUtils.fileFromLocalStorage(fileUrl, fileName, new OnFileListener() {
                @Override
                public void setFile(File file) {
                    pdfView.fromUri(Uri.fromFile(file))
                            .defaultPage(1)
                            .onPageChange(PDFActivity.this)
                            .swipeVertical(true)
                            .showMinimap(false)
                            .enableAnnotationRendering(true)
                            .onLoad(PDFActivity.this)
                            .load();
                }
            });
        } catch (Exception e) {

        }

    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        ToasShow.showToastBottom(PDFActivity.this, page +
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
