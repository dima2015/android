package com.plunner.plunner.retrofit;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.plunner.plunner.BuildConfig;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.NoHttpException;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.Model;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.URI;

/**
 * Created by claudio on 22/02/16.
 */
public class RetrofitTest extends ApplicationTestCase<Application> {
    final protected LockClass lockClass = new LockClass();

    public RetrofitTest() {
        super(Application.class);
    }

    protected InterceptorClient initialise(String response) {
        InterceptorClient interceptorClient = new InterceptorClient(response);
        Retrofit.getInstance().additionalInterceptors().add(interceptorClient); //TODO new object each time??
        LoginManager login = LoginManager.getInstance();
        login.setOnlyInternal(true);
        return interceptorClient;
    }

    static protected class InterceptorClient implements Interceptor {

        private String responseString;
        private URI uri;

        public InterceptorClient(String responseString) {
            this.responseString = responseString;
        }

        public URI getUri() {
            return uri;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Response response = null;
            if (BuildConfig.DEBUG) {

                uri = chain.request().uri();
                response = new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                        .addHeader("content-type", "application/json")
                        .addHeader("Authorization", "Bearer xxxxxxxxxxxxxxxxxx")
                        .build();

            } else {
                //perform request
                response = chain.proceed(chain.request());
            }

            return response;
        }
    }

    protected class LockClass {
        private Object lock = new Object();

        public void lock() throws InterruptedException {
            synchronized (lock) {
                lock.wait();
            }
        }

        public void unLock() {
            synchronized (lock)

            {
                lock.notify();
            }
        }
    }

    protected class Execution<T extends Model> {
        private boolean executed = false;
        private boolean error = false;
        private T model = null;
        private int status = 0;
        private EmployeeCallback callback = new EmployeeCallback();

        private void reset() {
            executed = false;
            error = false;
            model = null;
        }

        public EmployeeCallback getCallback() {
            return callback;
        }

        public boolean isExecuted() {
            return executed;
        }

        public boolean isError() {
            return error;
        }

        public T getModel() {
            return model;
        }

        public int getStatus() {
            return status;
        }

        public class EmployeeCallback implements
                CallOnHttpError<T>, CallOnNext<T>, CallOnNoHttpError<T> {

            @Override
            public void onHttpError(HttpException e) {
                Execution.this.reset();
                Execution.this.executed = true;
                Execution.this.error = true;
                Execution.this.status = 1;
                RetrofitTest.this.lockClass.unLock();
            }

            @Override
            public void onNext(T model) {
                Execution.this.reset();
                Execution.this.executed = true;
                Execution.this.model = model;
                RetrofitTest.this.lockClass.unLock();
            }

            @Override
            public void onNoHttpError(NoHttpException e) {
                Execution.this.reset();
                Execution.this.executed = true;
                Execution.this.error = true;
                Execution.this.status = 2;
                RetrofitTest.this.lockClass.unLock();
            }
        }
    }
}
