package pdfinterface;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by e-dot on 2017/5/24.
 */

public interface ApiManagerService {
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    @Streaming
    @GET
    Observable<ResponseBody> downloadPptxFromNet(@Url String pptxfileUrl);
}
