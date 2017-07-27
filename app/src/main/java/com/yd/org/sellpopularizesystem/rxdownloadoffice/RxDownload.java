package com.yd.org.sellpopularizesystem.rxdownloadoffice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yd.org.sellpopularizesystem.rxdownloadoffice.db.DataBaseHelper;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadEvent;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadMission;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadRecord;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadStatus;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadType;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.entity.DownloadTypeFactory;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.function.DownloadHelper;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.function.DownloadService;
import com.yd.org.sellpopularizesystem.rxdownloadoffice.function.Utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.CompositeException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout.TAG;
import static com.yd.org.sellpopularizesystem.rxdownloadoffice.function.DownloadHelper.TEST_RANGE_SUPPORT;


/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/19
 * Time: 10:46
 * RxDownload
 */
public class RxDownload {
    private static DownloadService mDownloadService;
    private static boolean bound = false;

    private DownloadHelper mDownloadHelper;
    private DownloadTypeFactory mFactory;

    private Context mContext;

    private boolean mAutoInstall;
    private int MAX_DOWNLOAD_NUMBER = 5;

    private RxDownload() {
        mDownloadHelper = new DownloadHelper();
        mFactory = new DownloadTypeFactory(mDownloadHelper);
    }

    public static RxDownload getInstance() {
        return new RxDownload();
    }

    /**
     * 普通下载时不需要context, 使用Service下载时需要context;
     *
     * @param context context
     * @return RxDownload
     */
    public RxDownload context(Context context) {
        this.mContext = context;
        return this;
    }

    public RxDownload defaultSavePath(String savePath) {
        mDownloadHelper.setDefaultSavePath(savePath);
        return this;
    }

    public RxDownload retrofit(Retrofit retrofit) {
        mDownloadHelper.setRetrofit(retrofit);
        return this;
    }

    public RxDownload maxThread(int max) {
        mDownloadHelper.setMaxThreads(max);
        return this;
    }

    public RxDownload maxRetryCount(int max) {
        mDownloadHelper.setMaxRetryCount(max);
        return this;
    }

    public RxDownload maxDownloadNumber(int max) {
        this.MAX_DOWNLOAD_NUMBER = max;
        return this;
    }

    public RxDownload autoInstall(boolean flag) {
        this.mAutoInstall = flag;
        return this;
    }

    /**
     * Receive the download address for the url download event and download status.
     * 接收下载地址为url的下载事件和下载状态.
     * <p>
     * Note that only receive the download address for the URL.
     * 注意只接收下载地址为url的事件和状态.
     *
     * @param url download url
     * @return Observable<DownloadStatus>
     */
    public Observable<DownloadEvent> receiveDownloadStatus(final String url) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                if (!bound) {
                    startBindServiceAndDo(new ServiceConnectedCallback() {
                        @Override
                        public void call() {
                            subscriber.onNext(null);
                        }
                    });
                } else {
                    subscriber.onNext(null);
                }
            }
        }).flatMap(new Func1<Object, Observable<DownloadEvent>>() {
            @Override
            public Observable<DownloadEvent> call(Object o) {
                return mDownloadService.getSubject(url).asObservable().onBackpressureLatest();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Read all the download record from the database
     * 从数据库中读取所有的下载记录
     *
     * @return Observable<List<DownloadRecord>>
     */
    public Observable<List<DownloadRecord>> getTotalDownloadRecords() {
        if (mContext == null) {
            return Observable.error(new Throwable("Context is NULL! You should call " +
                    "#RxDownload.context(Context context)# first!"));
        }
        DataBaseHelper dataBaseHelper = DataBaseHelper.getSingleton(mContext);
        return dataBaseHelper.readAllRecords();
    }

    /**
     * Read single download record with url.
     * 从数据库中读取下载地址为url的下载记录
     *
     * @param url download url
     * @return Observable<DownloadStatus>
     */
    public Observable<DownloadRecord> getDownloadRecord(String url) {
        if (mContext == null) {
            return Observable.error(new Throwable("Context is NULL! You should call " +
                    "#RxDownload.context(Context context)# first!"));
        }
        DataBaseHelper dataBaseHelper = DataBaseHelper.getSingleton(mContext);
        return dataBaseHelper.readRecord(url);
    }

    /**
     * Suspended download address for the url download task in Service.
     * 暂停Service中下载地址为url的下载任务.
     * <p>
     * Book the download records in the tag database are paused.
     * 同时标记数据库中的下载记录为暂停状态.
     *
     * @param url download url
     */
    public Observable<?> pauseServiceDownload(final String url) {
        return Observable.just(null).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (!bound) {
                    startBindServiceAndDo(new ServiceConnectedCallback() {
                        @Override
                        public void call() {
                            mDownloadService.pauseDownload(url);
                        }
                    });
                } else {
                    mDownloadService.pauseDownload(url);
                }
            }
        });
    }

    /**
     * 取消Service中下载地址为url的下载任务.
     * <p>
     * 同时标记数据库中的下载记录为取消状态.
     * 不会删除已经下载的文件.
     *
     * @param url download url
     */
    public Observable<?> cancelServiceDownload(final String url) {
        return Observable.just(null).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (!bound) {
                    startBindServiceAndDo(new ServiceConnectedCallback() {
                        @Override
                        public void call() {
                            mDownloadService.cancelDownload(url);
                        }
                    });
                } else {
                    mDownloadService.cancelDownload(url);
                }
            }
        });
    }

    /**
     * 删除Service中下载地址为url的下载任务.
     * <p>
     * 同时从数据库中删除该下载记录.
     * 不会删除已经下载的文件.
     *
     * @param url download url
     */
    public Observable<?> deleteServiceDownload(final String url) {
        return Observable.just(null).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (!bound) {
                    startBindServiceAndDo(new ServiceConnectedCallback() {
                        @Override
                        public void call() {
                            mDownloadService.deleteDownload(url);
                        }
                    });
                } else {
                    mDownloadService.deleteDownload(url);
                }
            }
        });
    }

    /**
     * Using Service to download. Just download, can't receive download status.
     * 使用Service下载. 仅仅开始下载, 不会接收下载进度.
     * <p>
     * Un subscribe will not pause download.
     * 取消订阅不会停止下载.
     * <p>
     * If you want receive download status, see {@link #receiveDownloadStatus(String)}
     * <p>
     * If you want pause download, see {@link #pauseServiceDownload(String)}
     * <p>
     * Also save the download records in the database, if you want get record from database,
     * see  {@link #getDownloadRecord(String)}
     *
     * @param url      download file Url
     * @param saveName download file SaveName
     * @param savePath download file SavePath. If NULL, using default save path {@code /storage/emulated/0/Download/}
     * @return Observable<DownloadStatus>
     */
    public Observable<Object> serviceDownload(@NonNull final String url,
                                              @NonNull final String saveName,
                                              @Nullable final String savePath) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                if (!bound) {
                    startBindServiceAndDo(new ServiceConnectedCallback() {
                        @Override
                        public void call() {
                            try {
                                addDownloadTask(url, saveName, savePath);
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                            } catch (IOException e) {
                                subscriber.onError(e);
                            }
                        }
                    });
                } else {
                    try {
                        addDownloadTask(url, saveName, savePath);
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    /**
     * Normal download.
     * <p>
     * Un subscribe will  pause download.
     * <p>
     * Do not save the download records in the database.
     *
     * @param url      download file Url
     * @param saveName download file SaveName
     * @param savePath download file SavePath. If NULL, using default save path {@code /storage/emulated/0/Download/}
     * @return Observable<DownloadStatus>
     */
    public Observable<DownloadStatus> download(@NonNull final String url,
                                               @NonNull final String saveName,
                                               @Nullable final String savePath) {
        return downloadDispatcher(url, saveName, savePath);
    }

    /**
     * Normal download version of the Transformer.
     * <p>
     * Provide RxJava Compose operator use.
     *
     * @param url      download file Url
     * @param saveName download file SaveName
     * @param savePath download file SavePath. If NULL, using default save path {@code /storage/emulated/0/Download/}
     * @param <T>      T
     * @return Transformer
     */
    public <T> Observable.Transformer<T, DownloadStatus> transform(@NonNull final String url,
                                                                   @NonNull final String saveName,
                                                                   @Nullable final String savePath) {
        return new Observable.Transformer<T, DownloadStatus>() {
            @Override
            public Observable<DownloadStatus> call(Observable<T> observable) {
                return observable.flatMap(new Func1<T, Observable<DownloadStatus>>() {
                    @Override
                    public Observable<DownloadStatus> call(T t) {
                        return download(url, saveName, savePath);
                    }
                });
            }
        };
    }

    /**
     * Service download without status version of the Transformer.
     * <p>
     * Provide RxJava Compose operator use.
     *
     * @param url      download file Url
     * @param saveName download file SaveName
     * @param savePath download file SavePath. If NULL, using default save path {@code /storage/emulated/0/Download/}
     * @param <T>      T
     * @return Transformer
     */
    public <T> Observable.Transformer<T, Object> transformService(@NonNull final String url,
                                                                  @NonNull final String saveName,
                                                                  @Nullable final String savePath) {
        return new Observable.Transformer<T, Object>() {
            @Override
            public Observable<Object> call(Observable<T> observable) {
                return observable.flatMap(new Func1<T, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(T t) {
                        return serviceDownload(url, saveName, savePath);
                    }
                });
            }
        };
    }

    public String[] getRealFileSavePaths(String savePath) {
        return mDownloadHelper.getFileSavePaths(savePath);
    }

    public File[] getRealFiles(String saveName, String savePath) {
        String[] filePaths = mDownloadHelper.getRealFilePaths(saveName, savePath);
        return new File[]{new File(filePaths[0]), new File(filePaths[1]), new File(filePaths[2])};
    }

    private void addDownloadTask(@NonNull String url, @NonNull String saveName,
                                 @Nullable String savePath) throws IOException {
        mDownloadService.addDownloadMission(
                new DownloadMission.Builder()
                        .setRxDownload(RxDownload.this)
                        .setUrl(url)
                        .setSaveName(saveName)
                        .setSavePath(savePath)
                        .build());
    }

    private Observable<DownloadStatus> downloadDispatcher(final String url,
                                                          final String saveName,
                                                          final String savePath) {
        if (mDownloadHelper.isRecordExists(url)) {
            return Observable.error(new Throwable("This url download task already exists, so do nothing."));
        }
        try {
            mDownloadHelper.addDownloadRecord(url, saveName, savePath);
        } catch (IOException e) {
            return Observable.error(e);
        }
        return getDownloadType(url)
                .flatMap(new Func1<DownloadType, Observable<DownloadStatus>>() {
                    @Override
                    public Observable<DownloadStatus> call(DownloadType type) {
                        try {
                            type.prepareDownload();
                            return type.startDownload();
                        } catch (IOException | ParseException e) {
                            return Observable.error(e);
                        }
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        try {
                            //等待1.5秒,以确保文件写入到磁盘中.
                            Thread.sleep(1500);
                            if (mAutoInstall) {
                                if (mContext == null) {
                                    throw new IllegalStateException("Context is NULL! You should call " +
                                            "#RxDownload.context(Context context)# first!");
                                }
                                Utils.installApk(mContext, getRealFiles(saveName, savePath)[0]);
                            }
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                        mDownloadHelper.deleteDownloadRecord(url);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof CompositeException) {
                            //避免打印CompositeException内的所有异常信息
                            Log.w(TAG, throwable.getMessage());
                        } else {
                            Log.w(TAG, throwable);
                        }
                        mDownloadHelper.deleteDownloadRecord(url);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mDownloadHelper.deleteDownloadRecord(url);
                    }
                });
    }

    private Observable<DownloadType> getDownloadType(String url) {
        if (mDownloadHelper.downloadFileExists(url)) {
            try {
                return getWhenFileExists(url);
            } catch (IOException e) {
                return getWhenFileNotExists(url);
            }
        } else {
            return getWhenFileNotExists(url);
        }
    }

    private Observable<DownloadType> getWhenFileNotExists(final String url) {
        return mDownloadHelper.getDownloadApi()
                .getHttpHeader(TEST_RANGE_SUPPORT, url)
                .map(new Func1<Response<Void>, DownloadType>() {
                    @Override
                    public DownloadType call(Response<Void> response) {
                        if (Utils.notSupportRange(response)) {
                            return mFactory.url(url)
                                    .fileLength(Utils.contentLength(response))
                                    .lastModify(Utils.lastModify(response))
                                    .buildNormalDownload();
                        } else {
                            return mFactory.url(url)
                                    .lastModify(Utils.lastModify(response))
                                    .fileLength(Utils.contentLength(response))
                                    .buildMultiDownload();
                        }
                    }
                }).retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        return mDownloadHelper.retry(integer, throwable);
                    }
                });
    }

    private Observable<DownloadType> getWhenFileExists(final String url) throws IOException {
        return mDownloadHelper.getDownloadApi()
                .getHttpHeaderWithIfRange(TEST_RANGE_SUPPORT, mDownloadHelper.getLastModify(url), url)
                .map(new Func1<Response<Void>, DownloadType>() {
                    @Override
                    public DownloadType call(Response<Void> resp) {
                        if (Utils.serverFileNotChange(resp)) {
                            return getWhenServerFileNotChange(resp, url);
                        } else if (Utils.serverFileChanged(resp)) {
                            return getWhenServerFileChanged(resp, url);
                        } else {
                            throw new RuntimeException("unknown error");
                        }
                    }
                }).retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        return mDownloadHelper.retry(integer, throwable);
                    }
                });
    }

    private DownloadType getWhenServerFileChanged(Response<Void> resp, String url) {
        if (Utils.notSupportRange(resp)) {
            return mFactory.url(url)
                    .fileLength(Utils.contentLength(resp))
                    .lastModify(Utils.lastModify(resp))
                    .buildNormalDownload();
        } else {
            return mFactory.url(url)
                    .fileLength(Utils.contentLength(resp))
                    .lastModify(Utils.lastModify(resp))
                    .buildMultiDownload();
        }
    }

    private DownloadType getWhenServerFileNotChange(Response<Void> resp, String url) {
        if (Utils.notSupportRange(resp)) {
            return getWhenNotSupportRange(resp, url);
        } else {
            return getWhenSupportRange(resp, url);
        }
    }

    private DownloadType getWhenSupportRange(Response<Void> resp, String url) {
        long contentLength = Utils.contentLength(resp);
        try {
            if (mDownloadHelper.needReDownload(url, contentLength)) {
                return mFactory.url(url)
                        .fileLength(contentLength)
                        .lastModify(Utils.lastModify(resp))
                        .buildMultiDownload();
            }
            if (mDownloadHelper.downloadNotComplete(url)) {
                return mFactory.url(url)
                        .fileLength(contentLength)
                        .lastModify(Utils.lastModify(resp))
                        .buildContinueDownload();
            }
        } catch (IOException e) {
            Log.w(TAG, "download record file may be damaged,so we will re download");
            return mFactory.url(url)
                    .fileLength(contentLength)
                    .lastModify(Utils.lastModify(resp))
                    .buildMultiDownload();
        }
        return mFactory.fileLength(contentLength).buildAlreadyDownload();
    }

    private DownloadType getWhenNotSupportRange(Response<Void> resp, String url) {
        long contentLength = Utils.contentLength(resp);
        if (mDownloadHelper.downloadNotComplete(url, contentLength)) {
            return mFactory.url(url)
                    .fileLength(contentLength)
                    .lastModify(Utils.lastModify(resp))
                    .buildNormalDownload();
        } else {
            return mFactory.fileLength(contentLength).buildAlreadyDownload();
        }
    }

    private void startBindServiceAndDo(final ServiceConnectedCallback callback) {
        if (mContext == null) {
            throw new RuntimeException("Context is NULL! You should call " +
                    "#RxDownload.context(Context context)# first!");
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(DownloadService.INTENT_KEY, MAX_DOWNLOAD_NUMBER);
        mContext.startService(intent);
        mContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                DownloadService.DownloadBinder downloadBinder = (DownloadService.DownloadBinder) binder;
                mDownloadService = downloadBinder.getService();
                mContext.unbindService(this);
                bound = true;
                callback.call();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //注意!!这个方法只会在系统杀掉Service时才会调用!!
                bound = false;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    private interface ServiceConnectedCallback {
        void call();
    }
}
