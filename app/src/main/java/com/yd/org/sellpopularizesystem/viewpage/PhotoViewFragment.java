package com.yd.org.sellpopularizesystem.viewpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.olivephone.office.TempFileManager;
import com.olivephone.office.powerpoint.DocumentSession;
import com.olivephone.office.powerpoint.DocumentSessionBuilder;
import com.olivephone.office.powerpoint.DocumentSessionStatusListener;
import com.olivephone.office.powerpoint.IMessageProvider;
import com.olivephone.office.powerpoint.ISystemColorProvider;
import com.olivephone.office.powerpoint.android.AndroidMessageProvider;
import com.olivephone.office.powerpoint.android.AndroidSystemColorProvider;
import com.olivephone.office.powerpoint.android.AndroidTempFileStorageProvider;
import com.olivephone.office.powerpoint.view.SlideShowNavigator;
import com.olivephone.office.ui.PresentationView;
import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.BaseFragmentView;
import com.yd.org.sellpopularizesystem.service.DownloadService;
import com.yd.org.sellpopularizesystem.utils.PDFUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.io.File;

import pdfinterface.OnFileListener;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewFragment extends BaseFragmentView implements OnPageChangeListener
        , OnLoadCompleteListener, DocumentSessionStatusListener, OnFileListener {
    private PhotoView photoIm;
    private String url, title;
    private PDFView pdfView;
    private PresentationView pptView;
    public static final String DOWNLOAD_COMPLETED = "download_completed";
    public static final String MESSAGE_PROGRESS = "message_progress";
    private int currentSlideNumber;
    private SlideShowNavigator navigator;
    private DocumentSession session;
    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;
    private String filePath;
    public static File file;

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
        pptView = getViewById(R.id.pptView);
        url = getArguments().getString("url");

        Log.e("url***", "url:" + url);

        if (url.endsWith(".pdf")) {
            showLoadingDialog();
            title = getArguments().getString("title");
            photoIm.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pptView.setVisibility(View.GONE);
            displayFromFile1(url, "pvf_" + title + ".pdf");

        } else if (url.endsWith(".png") || url.endsWith(".jpg")) {
            pdfView.setVisibility(View.GONE);
            photoIm.setVisibility(View.VISIBLE);
            pptView.setVisibility(View.GONE);
            init();
        } else if (url.endsWith(".ppt") || url.endsWith(".pptx")) {
            showLoadingDialog();
            title = getArguments().getString("title");
            photoIm.setVisibility(View.GONE);
            pdfView.setVisibility(View.GONE);
            pptView.setVisibility(View.VISIBLE);
            //displayFromFile1(url, "pvf_" + title + ".pdf");
            displayPptFromFile(url, title);

        }

    }

    private void displayPptFromFile(String url, String title) {
        //file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf/" + title);

        Intent intent = new Intent(getActivity(), DownloadService.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        getContext().startService(intent);

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

    @Override
    public void onSessionStarted() {

    }


    @Override
    public void onDocumentReady() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigator = new SlideShowNavigator(session.getPPTContext());
                currentSlideNumber = navigator.getFirstSlideNumber() - 1;
            }
        });
    }

    @Override
    public void onDocumentException(Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToasShow.showToastCenter(getActivity(), "onDocumentException");
            }
        });
    }

    @Override
    public void onSessionEnded() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(DOWNLOAD_COMPLETED);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //收到广播后所作的操作
                if (intent.getAction().equals(DOWNLOAD_COMPLETED)) {
                    filePath = intent.getStringExtra("filepath");
                    file = new File(filePath);
                    showPpt(context, file);
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void showPpt(Context context, File file) {
        IMessageProvider msgProvider = new AndroidMessageProvider(context);
        TempFileManager tmpFileManager = new TempFileManager(
                new AndroidTempFileStorageProvider(context));
        ISystemColorProvider sysColorProvider = new AndroidSystemColorProvider();
        session = new DocumentSessionBuilder(file)
                .setMessageProvider(msgProvider)
                .setTempFileManager(tmpFileManager)
                .setSystemColorProvider(sysColorProvider)
                .setSessionStatusListener(PhotoViewFragment.this).build();
        session.startSession();
    }

    @Override
    public void onStart() {
        super.onStart();
        //pptView.setC
    }

    @Override
    public void onDestroy() {
        if (this.session != null) {
            this.session.endSession();
        }
        //解除注册时，使用注册时的manager解绑
        broadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void setFile(File file) {
        this.file = file;
        showPpt(PhotoViewFragment.this.getContext(), file);
    }
}


