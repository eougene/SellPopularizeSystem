package com.yd.org.sellpopularizesystem.viewpage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;
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

        if (url.endsWith(".pdf") || url.endsWith(".ppt") || url.endsWith(".pptx")) {
            title = getArguments().getString("title");
            pdfView.setVisibility(View.VISIBLE);
            photoIm.setVisibility(View.GONE);
            MyUtils.getInstance().showWebView(getActivity(), pdfView, "http://dcsapi.com?k=140337680&url=" + url);


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
        Picasso.with(getActivity()).load(url).into(photoIm);

    }


}


