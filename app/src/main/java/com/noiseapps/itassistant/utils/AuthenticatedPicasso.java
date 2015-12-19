package com.noiseapps.itassistant.utils;

import android.content.Context;

import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.connector.Consts;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class AuthenticatedPicasso {

    private static final long CACHE_SIZE = 50 * 1024 * 1024 * 8;

    private static Picasso INSTANCE;

    public static void setConfig(Context context, final BaseAccount config) {
        getPicasso(context, config);
    }

    public static Picasso getAuthPicasso(Context context, final BaseAccount config) {
        if (INSTANCE == null) {
            getPicasso(context, config);
        }
        return INSTANCE;
    }

    private static void getPicasso(Context context, BaseAccount config) {
        final OkHttpClient picassoClient = new OkHttpClient();
        final Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);
        picassoClient.setCache(cache);
        picassoClient.interceptors().add(chain -> {
            String basicAuthEncoded = config.getToken();
            basicAuthEncoded = basicAuthEncoded.replaceAll("\n", "");
            Request newRequest = chain.request().newBuilder()
                    .addHeader(Consts.AUTH_HEADER, String.format(Consts.AUTH_HEADER_VALUE, basicAuthEncoded))
                    .build();
            return chain.proceed(newRequest);
        });

        final Picasso.Builder builder = new Picasso.Builder(context);
        if (BuildConfig.DEBUG) {
            builder.indicatorsEnabled(true);
            builder.loggingEnabled(true);
            builder.listener((picasso, uri, exception) -> Logger.e(exception, exception.getMessage()));
        }
        builder.downloader(new OkHttpDownloader(picassoClient));
        INSTANCE = builder.build();
    }
}
