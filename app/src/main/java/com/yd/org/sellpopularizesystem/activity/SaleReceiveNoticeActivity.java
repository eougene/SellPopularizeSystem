package com.yd.org.sellpopularizesystem.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.shockwave.pdfium.PdfDocument;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.PDFUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.util.List;

import pdfinterface.OnFileListener;

import static com.android.volley.VolleyLog.TAG;

public class SaleReceiveNoticeActivity extends BaseActivity implements OnPageChangeListener
        , OnLoadCompleteListener {

    private PDFView pdfView;
    private String sale_advice_url, orderId;


    @Override
    protected int setContentView() {
        return R.layout.activity_sale_receive_notice;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.sale_notice));
        hideRightImagview();

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        sale_advice_url = bundle.getString("sale_advice_url");
        pdfView = (PDFView) findViewById(R.id.pdfView);

        displayFromFile1(sale_advice_url, "srna_" + orderId + ".pdf");


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
                            .onPageChange(SaleReceiveNoticeActivity.this)
                            .swipeVertical(true)
                            .showMinimap(false)
                            .enableAnnotationRendering(true)
                            .onLoad(SaleReceiveNoticeActivity.this)
                            .load();
                }
            });
        } catch (Exception e) {

        }

    }


    @Override
    public void setListener() {

    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText(SaleReceiveNoticeActivity.this, page + "/" + pageCount, Toast.LENGTH_SHORT).show();
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


    /**
     * 请求合同
     */
    private void getAskOntract() {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", "");
        ajaxParams.put("remark", "");
        ajaxParams.put("sales_advice_is_true", "");


        finalHttp.post(Contants.ASK_ONTRACT, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();

            }
        });
    }
}
