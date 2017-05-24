package com.yd.org.sellpopularizesystem.utils;

import okhttp3.ResponseBody;
import pdfinterface.ApiManagerService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by e-dot on 2017/5/24.
 */

public class ApiManager {


    private static final String BASE_URL = "http://file/";
    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static final ApiManagerService apiManager = sRetrofit.create(ApiManagerService.class);

    public static Observable<ResponseBody> downloadPicFromNet(String fileUrl) {
        return apiManager.downloadPicFromNet(fileUrl);
    }
}
