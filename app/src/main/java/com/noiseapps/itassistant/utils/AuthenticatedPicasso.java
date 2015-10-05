package com.noiseapps.itassistant.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.io.IOException;

import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.connector.Consts;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


public class AuthenticatedPicasso {

    public static Picasso getAuthPicasso(Context context, final BaseAccount config) {
        final OkHttpClient picassoClient = new OkHttpClient();
        picassoClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                String basicAuthEncoded = getBasicAuth(config);
                basicAuthEncoded = basicAuthEncoded.replaceAll("\n", "");
                Request newRequest = chain.request().newBuilder()
                        .addHeader(Consts.AUTH_HEADER, String.format(Consts.AUTH_HEADER_VALUE, basicAuthEncoded))
                        .build();
                return chain.proceed(newRequest);
            }
        });

        final Picasso.Builder builder = new Picasso.Builder(context);
        if(BuildConfig.DEBUG) {
            builder.indicatorsEnabled(true);
            builder.loggingEnabled(true);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Logger.e(exception, exception.getMessage());
                }
            });
        }
        builder.downloader(new OkHttpDownloader(picassoClient));
        return builder.build();
    }

    private static String getBasicAuth(BaseAccount currentConfig) {
        final String usernameString = currentConfig.getUsername() + ":" + currentConfig.getPassword();
        return Base64.encodeToString(usernameString.getBytes(), Base64.DEFAULT);
    }
}
