package com.plunner.plunner.models;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by claudio on 19/12/15.
 */
abstract class Model {

    static final protected String BASE_URL = "http://api.plunner.com";

    static public String AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoiZW4iLCJzdWIiOiIzNCIsImlzcyI6Imh0dHA6XC9cL2FwaS5wbHVubmVyLmNvbVwvZW1wbG95ZWVzXC9ncm91cHMiLCJpYXQiOiIxNDUwNDgwNjA2IiwiZXhwIjoiMTQ1MDQ4NzIxNCIsIm5iZiI6IjE0NTA0ODM2MTQiLCJqdGkiOiIyZjgwMzU3MzU5MjAzMmM4YjIyODIzMWJmM2U0Nzc1ZSJ9.-FHEGifidaZ0EvY-W5DWGImcoWNKjPE_SHcI1I6W0YU";

    static protected<T> T createRetrofit(Class<T> interfaceClass) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", AUTH_TOKEN).build();
                return chain.proceed(newRequest);
            }
        };

        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(interfaceClass);
    }

    abstract public void fresh();
    abstract public void save();
}
