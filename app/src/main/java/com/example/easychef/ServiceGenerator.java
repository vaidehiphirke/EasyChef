package com.example.easychef;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.easychef.activities.MainActivity.getOldestCacheTime;

public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final long CACHE_SIZE_5_MB = 5 * 1024 * 1024;

    public static final int NANOSECONDS_IN_A_SECOND = 1000 * 1000 * 1000;
    private static final int MAX_STALE_OFFLINE_CALL_DAYS = 7;

    private static final Cache API_CACHE = new Cache(new File(EasyChefApplication.getInstance().getCacheDir(), "apiCache"),
            CACHE_SIZE_5_MB);

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .cache(API_CACHE)
            .addNetworkInterceptor(networkInterceptor())
            .addInterceptor(offlineInterceptor())
            .build();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OK_HTTP_CLIENT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static Interceptor offlineInterceptor() {
        return new OfflineInterceptor();
    }

    private static Interceptor networkInterceptor() {
        return new NetworkInterceptor();
    }

    public static FoodAPI getFoodAPI() {
        return RETROFIT.create(FoodAPI.class);
    }

    private static class NetworkInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Log.d(TAG, "network interceptor called");
            final Response response = chain.proceed(chain.request());

            final int currentTime = (int) (System.nanoTime() / NANOSECONDS_IN_A_SECOND);

            final CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(currentTime - getOldestCacheTime(), TimeUnit.SECONDS)
                    .build();

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        }
    }

    private static class OfflineInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Log.d(TAG, "offline interceptor called");
            if (EasyChefApplication.hasNetwork()) {
                return chain.proceed(chain.request());
            }
            final CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(MAX_STALE_OFFLINE_CALL_DAYS, TimeUnit.DAYS)
                    .build();

            final Request request = chain.request()
                    .newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);
        }
    }
}
