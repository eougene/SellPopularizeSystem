package com.yd.org.sellpopularizesystem.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import pdfinterface.OnFileListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by e-dot on 2017/5/24.
 */

public class PDFUtils {

    /**
     * http://file.chmsp.com.cn/colligate/file/000000224821.pdf
     *
     * @param fileUrl
     */
    public static void fileFromLocalStorage(String fileUrl, final String fileName, final OnFileListener listener) throws IOException {
        final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdf/";
        final File file = new File(SDPath, fileName);
        if (file.exists()) {//文件已经存在，直接获取本地文件打开，否则从网络现在文件，文件下载成功之后再打开
            listener.setFile(file);

        } else {


            ApiManager.downloadPicFromNet(fileUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseBody>() {
                        @Override
                        public void call(ResponseBody responseBody) {
                            InputStream is = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            try {
                                is = responseBody.byteStream();
                                long total = responseBody.contentLength();
                                File file1 = new File(SDPath);
                                if (!file1.exists()) {
                                    file1.mkdirs();
                                }
                                File fileN = new File(SDPath, fileName);
                                if (!fileN.exists()) {
                                    boolean mkdir = fileN.createNewFile();
                                    Log.d("mkdir", "call: " + mkdir);
                                }
                                fos = new FileOutputStream(fileN);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    Log.d("h_bl", "progress=" + progress);
                                }
                                fos.flush();
                                Log.d("h_bl", "文件下载成功");
                                listener.setFile(fileN);
                            } catch (Exception e) {
                                Log.d("h_bl", "文件下载失败");
                            } finally {
                                try {
                                    if (is != null)
                                        is.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null)
                                        fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.d("h_bl", "文件下载失败");
                        }
                    });
        }
    }

}
