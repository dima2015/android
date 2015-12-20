package com.plunner.plunner.models.models;


import com.facebook.stetho.okhttp.StethoInterceptor;
import com.plunner.plunner.models.adapters.Subscriber;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by claudio on 19/12/15.
 */
abstract public class Model {

    static final protected String BASE_URL = "http://api.plunner.com";

    //TODO fine a better solution
    static public String AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoiZW4iLCJzdWIiOiIzNCIsImlzcyI6Imh0dHA6XC9cL2FwaS5wbHVubmVyLmNvbVwvZW1wbG95ZWVzXC9ncm91cHMiLCJpYXQiOiIxNDUwNDgwNjA2IiwiZXhwIjoiMTQ1MDQ4NzIxNCIsIm5iZiI6IjE0NTA0ODM2MTQiLCJqdGkiOiIyZjgwMzU3MzU5MjAzMmM4YjIyODIzMWJmM2U0Nzc1ZSJ9.-FHEGifidaZ0EvY-W5DWGImcoWNKjPE_SHcI1I6W0YU";

    /**
     *
     * @param interfaceClass
     * @param <T>
     * @return
     */
    static protected<T> T createRetrofit(Class<T> interfaceClass) {
        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new InterceptorClass());
        client.networkInterceptors().add(new StethoInterceptor());//TODO remove

        return new Retrofit.Builder()
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
    static protected <T extends Model> rx.Subscription subscribe(Observable<T> call, Subscriber<T> subscriber) {
        return call.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    abstract public rx.Subscription fresh();
    abstract public rx.Subscription fresh(Subscriber subscriber);
    abstract public rx.Subscription save();
    abstract public rx.Subscription save(Subscriber subscriber);

    static private class InterceptorClass implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //TODO token refresh
            Request newRequest = chain.request().newBuilder().addHeader("Authorization", AUTH_TOKEN).build();
            return chain.proceed(newRequest);
        }
    }
}
