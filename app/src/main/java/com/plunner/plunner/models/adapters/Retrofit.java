package com.plunner.plunner.models.adapters;

import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.Model;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by claudio on 20/12/15.
 */
public class Retrofit {

    static final protected String BASE_URL = "http://api.plunner.com";
    static final private int TIMEOUT = 30;

    /**
     * @param interfaceClass
     * @param <T>
     * @return
     */
    static public <T> T createRetrofit(Class<T> interfaceClass) {
        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        client.interceptors().add(new InterceptorClass());
        client.networkInterceptors().add(new StethoInterceptor());//TODO remove

        return new retrofit.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(interfaceClass);
    }

    /**
     * @param call
     * @param subscriber
     * @param <T>
     * @return
     */
    static public <T extends Model> rx.Subscription subscribe(Observable<T> call, rx.Subscriber<T> subscriber) {
        return call.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    static private class InterceptorClass implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token;
            LoginManager loginManager = LoginManager.getInstance();
            Request newRequest = chain.request();
            if (loginManager.getToken() != null) {
                token = loginManager.getToken();
                //for security reason show only the start of the token
                Log.v("Token set in connection", token.substring(0, 20));
                newRequest = newRequest.newBuilder().addHeader("Authorization", token).build();
            }
            Response response = chain.proceed(newRequest);
            token = response.header("Authorization");
            if (token != null) {
                //for security reason show only the start of the token
                Log.v("Fresh token stored", token.substring(0, 20));
                loginManager.setToken(token);
            }
            return response;
        }
    }
}
