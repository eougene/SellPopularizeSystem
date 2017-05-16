package com.yd.org.sellpopularizesystem.viewpage;

import android.os.Bundle;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends BaseFragmentView {
    private PhotoView photoIm;
    private String url;


    public static PhotoViewFragment getInstnce(String url) {
        PhotoViewFragment photoViewFragment = new PhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        photoViewFragment.setArguments(bundle);
        return photoViewFragment;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_photo_view);
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void init() {
        url = getArguments().getString("url");
        Log.e("图片地址**", "url:" + url);
        photoIm = getViewById(R.id.photoIm);
        Picasso.with(getContext()).load(url).into(photoIm);

    }


}


