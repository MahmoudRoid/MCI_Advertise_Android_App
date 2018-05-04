package ir.mahmoud.app.Interfaces;

import retrofit2.Retrofit;

/**
 * Created by soheilsystem on 3/9/2018.
 */

public class MCIApiClient {
    public static final String BASE_URL = "http://api.shodanilanding.ir/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
