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
import java.util.HashMap;
import java.util.Map;

import okio.Buffer;

/**
 * Created by claudio on 22/02/16.
 */
public class RetrofitTest extends ApplicationTestCase<Application> {
    final private LockClass lockClass = new LockClass();
    private InterceptorClient interceptorClient = null;
    private Execution execution;

    //TODO improve calling one method that perform all checks

    public RetrofitTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initialise();
    }

    protected void lock() throws InterruptedException {
        lockClass.lock();
    }

    private void initialise() {
        LoginManager login = LoginManager.getInstance();
        login.setOnlyInternal(true);
        interceptorClient = new InterceptorClient();
        //Retrofit.getInstance().additionalInterceptors().clear();
        Retrofit.getInstance().additionalInterceptors().add(interceptorClient);
    }

    protected void assertUrl(String URL) {
        assertEquals(URL, interceptorClient.getUri().getPath());
    }

    protected void assertMethod(String method) {
        assertEquals(method, interceptorClient.getMethod());
    }

    protected void assertRequestBody(String requestBody) {
        assertEquals(requestBody, interceptorClient.getRequestBody());
    }

    protected void assertRequestBody(Map<String, String> data) {
        Map<String, String> body = new HashMap<>();
        for (String element : interceptorClient.getRequestBody().split("&")) {
            String[] tmp = element.split("=");
            body.put(tmp[0], tmp[1]);
        }
        assertBothMapsEquals(data, body);
    }

    /**
     * this check isn based on both maps
     *
     * @param map1
     * @param map2
     * @return
     */
    private void assertBothMapsEquals(Map<String, String> map1, Map<String, String> map2) {
        assertFirstMapsEquals(map1, map2);
        assertFirstMapsEquals(map2, map1);
    }

    /**
     * this check is based only on the first map (list only it)
     *
     * @param map1
     * @param map2
     * @return
     */
    private void assertFirstMapsEquals(Map<String, String> map1, Map<String, String> map2) {
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            assertTrue(map2.containsKey(entry.getKey()));
            assertEquals(map2.get(entry.getKey()), entry.getValue());
        }
    }

    protected void assertOK() {
        assertTrue(execution.isExecuted());
        assertFalse(execution.isError());
    }

    /**
     * <strong>CAUTION:</strong> if the answer is ok the status is 0 not 200
     *
     * @param status
     */
    protected void assertStatus(int status) {
        assertTrue(execution.isExecuted());
        assertEquals(status, execution.getHTTPStatus());
    }


    protected void setResponse(String response) {
        //if we don't create a new object we have problems
        InterceptorClient old = interceptorClient;
        interceptorClient = new InterceptorClient();
        interceptorClient.setResponseString(response);
        interceptorClient.setStatus(old.getStatus());
        Retrofit.getInstance().additionalInterceptors().clear();
        Retrofit.getInstance().additionalInterceptors().add(interceptorClient);
    }

    protected void setStatus(int status) {
        //if we don't create a new object we have problems
        InterceptorClient old = interceptorClient;
        interceptorClient = new InterceptorClient();
        interceptorClient.setResponseString(old.getResponseString());
        interceptorClient.setStatus(status);
        Retrofit.getInstance().additionalInterceptors().clear();
        Retrofit.getInstance().additionalInterceptors().add(interceptorClient);
    }

    static private class InterceptorClient implements Interceptor {

        private String responseString = "";
        private URI uri;
        private int status = 200;
        private String method;
        private String requestBody;

        public InterceptorClient() {
        }

        public String getResponseString() {
            return responseString;
        }

        public void setResponseString(String responseString) {
            this.responseString = responseString;
        }

        public String getMethod() {
            return method;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public URI getUri() {
            return uri;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Response response = null;
            if (BuildConfig.DEBUG) {
                method = chain.request().method();
                uri = chain.request().uri();
                if (chain.request().body() != null) {
                    Buffer buffer = new Buffer();
                    chain.request().body().writeTo(buffer);
                    requestBody = buffer.readUtf8();
                }
                response = new Response.Builder()
                        .code(status)
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
        private int HTTPStatus = 0;
        private EmployeeCallback callback = new EmployeeCallback();
        private NoHttpException noHttpException = null;

        public Execution() {
            RetrofitTest.this.execution = this;
        }

        public NoHttpException getNoHttpException() {
            return noHttpException;
        }

        private void reset() {
            executed = false;
            error = false;
            model = null;
            HTTPStatus = 0;
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

        public int getHTTPStatus() {
            return HTTPStatus;
        }

        public T getModel() {
            return model;
        }

        public class EmployeeCallback implements
                CallOnHttpError<T>, CallOnNext<T>, CallOnNoHttpError<T> {

            @Override
            public void onHttpError(HttpException e) {
                Execution.this.reset();
                Execution.this.executed = true;
                Execution.this.error = true;
                Execution.this.HTTPStatus = e.getCause().code();
                RetrofitTest.this.execution = Execution.this;
                RetrofitTest.this.lockClass.unLock();
            }

            @Override
            public void onNext(T model) {
                Execution.this.reset();
                Execution.this.executed = true;
                Execution.this.model = model;
                RetrofitTest.this.execution = Execution.this;
                RetrofitTest.this.lockClass.unLock();
            }

            @Override
            public void onNoHttpError(NoHttpException e) {
                Execution.this.reset();
                Execution.this.noHttpException = e;
                Execution.this.executed = true;
                Execution.this.error = true;
                RetrofitTest.this.execution = Execution.this;
                RetrofitTest.this.lockClass.unLock();
            }
        }
    }
}
