package com.yd.org.sellpopularizesystem.viewpage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends BaseFragmentView {
    private PhotoView photoIm;
    private String url, title;
    private WebView pdfView;

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

        Log.e("url***", "url:" + url);

        if (url.endsWith(".pdf") || url.endsWith(".ppt") || url.endsWith(".pptx")||url.endsWith(".PDF") || url.endsWith(".PPT") || url.endsWith(".PPTX")) {
            title = getArguments().getString("title");
            pdfView.setVisibility(View.VISIBLE);
            photoIm.setVisibility(View.GONE);


            //正式域名
            if (Contants.DOMAIN.equals("https://www.wingaid.com")) {
                MyUtils.getInstance().showWebView(getActivity(), pdfView, Contants.PDF + url);

                //测试域名
            } else {
                MyUtils.getInstance().showWebView(getActivity(), pdfView, Contants.PDF_TEST + url);

            }


        } else if (url.endsWith(".png") || url.endsWith(".jpg")) {
            photoIm.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
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
        BitmapUtil.loadImageView(getActivity(),url,photoIm);


    }


}


