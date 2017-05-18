package com.yd.org.sellpopularizesystem.viewpage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

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

        if (url.endsWith(".pdf")) {
            showLoadingDialog();
            title = getArguments().getString("title");
            photoIm.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);

            displayFromFile1(url, title + ".pdf");

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


