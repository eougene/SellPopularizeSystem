package com.yd.org.sellpopularizesystem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.myView.imageshow.TouchImageView;
import com.yd.org.sellpopularizesystem.utils.Options;

import java.util.List;


/**
 * 图片浏览的PagerAdapter
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imgsUrl;
    private LayoutInflater inflater = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    //view内控件
    private TouchImageView full_image;


    public ImagePagerAdapter(Context context, List<String> imgsUrl) {
        this.context = context;
        this.imgsUrl = imgsUrl;
        this.inflater = LayoutInflater.from(context);
        this.options = Options.getListOptions();
    }

    /**
     * 动态加载数据
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        //  ((ImageShowViewPager) container).mCurrentView = ((TouchImageView) ((View) object).findViewById(R.id.full_image));
    }

    @Override
    public int getCount() {
        return imgsUrl == null ? 0 : imgsUrl.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.from(context).inflate(R.layout.details_imageshow_item, null);
        full_image = (TouchImageView) view.findViewById(R.id.full_image);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(imgsUrl.get(position), full_image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                full_image.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

                full_image.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                full_image.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

                full_image.setVisibility(View.VISIBLE);


            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
