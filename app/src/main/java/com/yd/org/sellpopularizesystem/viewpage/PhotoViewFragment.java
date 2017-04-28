package com.yd.org.sellpopularizesystem.viewpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends Fragment {
    private PhotoView photoIm;
    private View View;
    private String url;

   /* public PhotoViewFragment(String url) {
        this.url = url;
    }*/

    public static PhotoViewFragment getInstnce(String url){
        PhotoViewFragment photoViewFragment=new PhotoViewFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        photoViewFragment.setArguments(bundle);
        return photoViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=savedInstanceState){
            url =(String) getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.fragment_photo_view, container, false);
        photoIm = (PhotoView) View.findViewById(R.id.photoIm);
        init();
        return View;
    }

    private void init() {

        Picasso.with(getContext()).load(url).into(photoIm);

    }


}


