package com.yd.org.sellpopularizesystem.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.shockwave.pdfium.PdfDocument;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.utils.PDFUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.io.File;
import java.util.List;

import pdfinterface.OnFileListener;

import static com.android.volley.VolleyLog.TAG;

public class PDFActivity extends BaseActivity implements OnPageChangeListener
        , OnLoadCompleteListener {
    private PDFView pdfView;
    private FileContent fileContent;
    private String sale_advice_url, orderId;

    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        hideRightImagview();
        pdfView = (PDFView) findViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        sale_advice_url = bundle.getString("sale_advice_url");
        if (orderId==null || sale_advice_url==null){
            fileContent = (FileContent) getIntent().getSerializableExtra("pdf");
            setTitle(fileContent.getDetail_name());
            displayFromFile1(Contants.DOMAIN + "/" + fileContent.getUrl(), "pdfa_"+ fileContent.getProduct_id() + ".pdf");
        }else {
            setTitle(getString(R.string.sale_notice));
            displayFromFile1(sale_advice_url, "srna_" + orderId + ".pdf");
        }



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
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
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
