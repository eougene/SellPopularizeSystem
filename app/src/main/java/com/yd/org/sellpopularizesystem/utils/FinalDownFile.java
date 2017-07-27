package com.yd.org.sellpopularizesystem.utils;

import android.Manifest;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.RxDownload;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadStatus;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by e-dot on 2017/7/26.
 */

public class FinalDownFile {

    private String defaultPath = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
    private Subscription subscription;
    private RxDownload mRxDownload;


    public FinalDownFile(final Activity activity, final String url, final String saveName, final WebView view) {

        mRxDownload = RxDownload.getInstance()
                .maxThread(10)
                .context(activity);

        SVProgressHUD.showWithProgress(activity, 0 + "KB", SVProgressHUD.SVProgressHUDMaskType.Black);

        subscription = RxPermissions.getInstance(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .compose(mRxDownload.transform(url, saveName, null))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DownloadStatus>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.e("onStart**", "onStart");
                    }

                    @Override
                    public void onCompleted() {
                        if (SVProgressHUD.isShowing(activity)) {
                            SVProgressHUD.dismiss(activity);

                        }
                        Log.e("onCompleted**", "onCompleted:" + defaultPath + File.separator + saveName);
                        String fileName = defaultPath + File.separator + saveName;
                        if (url.endsWith(".ppt") || url.endsWith(".pptx") || url.endsWith(".docx")
                                || url.endsWith(".xls") || url.endsWith(".doc") || url.endsWith(".PPT") || url.endsWith(".PPTX") || url.endsWith(".DOCX")
                                || url.endsWith(".XLS") || url.endsWith(".DOC")) {

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (SVProgressHUD.isShowing(activity)) {
                            SVProgressHUD.dismiss(activity);

                        }
                        Log.e("onError**", "onError");

                    }

                    @Override
                    public void onNext(final DownloadStatus status) {
                        Log.e("status**", "status:");
                        SVProgressHUD.getProgressBar(activity).setMax((int) status.getTotalSize() / (1024));
                        SVProgressHUD.getProgressBar(activity).setProgress((int) status.getDownloadSize() / (1024));
                        SVProgressHUD.setText(activity, (int) status.getDownloadSize() / (1024) + "KB");


                    }
                });

    }


}
