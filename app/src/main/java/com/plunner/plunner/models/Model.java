package com.plunner.plunner.models;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by claudio on 19/12/15.
 */
abstract class Model {

    static final protected String BASE_URL = "http://api.plunner.com";

    static protected<T> T createRetrofit(Class<T> interfaceClass) {
        return new Retrofit.Builder()
                .baseUrl("http://api.plunner.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(interfaceClass);
    }
}
