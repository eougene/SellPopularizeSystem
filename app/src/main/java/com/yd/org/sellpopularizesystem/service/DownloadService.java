package com.yd.org.sellpopularizesystem.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.download.DownloadProgressListener;
import com.yd.org.sellpopularizesystem.javaBean.DownloadInfo;
import com.yd.org.sellpopularizesystem.utils.DownloadApi;
import com.yd.org.sellpopularizesystem.utils.StringUtils;
import com.yd.org.sellpopularizesystem.viewpage.PhotoViewFragment;

import java.io.File;


import pdfinterface.OnFileListener;
import rx.Subscriber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by JokAr on 16/7/5.
 */
public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    int downloadCount = 0;
    private OnFileListener onFileListener;
    private String fileUrl;
    private String title;
    private String filePath;

    public DownloadService() {
        super("DownloadService");
    }

    private File outputFile;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 该方法中的代码将会在工作线程中执行
     * 每当调用startService启动IntentService后，
     * IntentService将会把OnHandlerIntent中的
     * 业务逻辑放入消息队列等待执行。
     * 当工作线程轮循到该消息对象时，将会
     * 执行该方法。
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        fileUrl=intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download(onFileListener);
    }
    //执行下载
    private void download(OnFileListener onFileListener) {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //不频繁发送通知，防止通知栏下拉卡顿
                int progress = (int) ((bytesRead * 100) / contentLength);
                if ((downloadCount == 0) || progress > downloadCount) {
                    DownloadInfo download = new DownloadInfo();
                    download.setTotalFileSize(contentLength);
                    download.setCurrentFileSize(bytesRead);
                    download.setProgress(progress);
                    sendNotification(download);
                }
            }
        };
        if (fileUrl.contains(".ppt")){
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ppt/"+title+".ppt";
            outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ppt/",title+".ppt");
        }else if (fileUrl.contains(".pptx")){
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ppt/"+title+".pptx";
            outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ppt/",title+".pptx");
        }
        if (outputFile.exists()) {
            onFileListener.setFile(outputFile);
        }else {
            String baseUrl = StringUtils.getHostName(fileUrl);
            downloadFile(baseUrl, listener,onFileListener);
        }
    }

    private void downloadFile(String baseUrl, final DownloadProgressListener listener, final OnFileListener onFileListener) {
        new DownloadApi(baseUrl, listener).downloadAPK(fileUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
                //下载完成
                onFileListener.setFile(outputFile);
                downloadCompleted();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                downloadCompleted();
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void downloadCompleted() {
        DownloadInfo download = new DownloadInfo();
        download.setProgress(100);
        sendIntent(download);
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

        if (fileUrl.endsWith(".apk")){
            //安装apk
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
            startActivity(intent);
        }

    }

    private void sendNotification(DownloadInfo download) {

        sendIntent(download);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(DownloadInfo download) {

        Intent intent = new Intent(PhotoViewFragment.DOWNLOAD_COMPLETED);
        intent.putExtra("download", download);
        intent.putExtra("filepath",filePath);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
