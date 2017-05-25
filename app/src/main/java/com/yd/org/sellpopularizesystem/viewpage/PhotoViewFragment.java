package com.yd.org.sellpopularizesystem.viewpage;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;
import com.yd.org.sellpopularizesystem.utils.PDFUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.io.File;

import pdfinterface.OnFileListener;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends BaseFragmentView implements OnPageChangeListener
        , OnLoadCompleteListener {
    private PhotoView photoIm;
    private String url, title;
    private PDFView pdfView;


    public static PhotoViewFragment getInstnce(String url, String title) {
        PhotoViewFragment photoViewFragment = new PhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        photoViewFragment.setArguments(bundle);
        return photoViewFragment;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_photo_view);
        photoIm = getViewById(R.id.photoIm);
        pdfView = getViewById(R.id.pdfView);

        url = getArguments().getString("url");

        Log.e("url***","url:"+url);

        if (url.endsWith(".pdf")) {
            showLoadingDialog();
            title = getArguments().getString("title");
            photoIm.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);

            displayFromFile1(url, "pvf_" + title + ".pdf");

        } else if (url.endsWith(".png") || url.endsWith(".jpg")) {
            pdfView.setVisibility(View.GONE);
            photoIm.setVisibility(View.VISIBLE);
            init();
        }

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void init() {
        Log.e("图片地址**", "url:" + url);
        Picasso.with(getContext()).load(url).into(photoIm);

    }


    /**
     * 获取打开网络的pdf文件
     *
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1(String fileUrl, String fileName) {
        try {
            showLoadingDialog();
            PDFUtils.fileFromLocalStorage(fileUrl, fileName, new OnFileListener() {
                @Override
                public void setFile(File file) {
                    pdfView.fromUri(Uri.fromFile(file))
                            .defaultPage(1)
                            .onPageChange(PhotoViewFragment.this)
                            .swipeVertical(true)
                            .showMinimap(false)
                            .enableAnnotationRendering(true)
                            .onLoad(PhotoViewFragment.this)
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
        ToasShow.showToastBottom(getActivity(), page +
                "/" + pageCount);
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        dismissLoadingDialog();
    }


}


