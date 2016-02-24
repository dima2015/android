package com.plunner.plunner.models.login;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.LoginActivity;
import com.plunner.plunner.general.Plunner;
import com.plunner.plunner.models.adapters.HttpException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import retrofit.Response;


/**
 * Created by claudio on 20/12/15.
 */
public class LoginManager {
    public final static String PARAM_USER_PASS = "USER_PASS";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_COMPANY_NAME = "COMPANY_NAME";
    private static LoginManager ourInstance = new LoginManager();
    private AccountManager mAccountManager;
    private String token = null;
    private Context mContext;
    private String accountName = null;
    private boolean onlyInternal = false;

    private LoginManager() {
        mContext = Plunner.getAppContext();
        mAccountManager = AccountManager.get(mContext);
    }

    public static LoginManager getInstance() {
        return ourInstance;
    }

    public boolean isOnlyInternal() {
        return onlyInternal;
    }

    public void setOnlyInternal(boolean onlyInternal) {
        this.onlyInternal = onlyInternal;
    }

    /**
     * Get the stored token or show the login activity<br>
     * <strong>Caution:</strong> it iss an async task
     *
     * @param activity needed to show the login activity
     * @param callback callback used to inform the caller about the request status
     */
    public void storeToken(Activity activity, final storeTokenCallback callback) {
        //TODO better way for auth type
        //TODO use  String blockingGetAuthToken???
        mAccountManager.getAuthTokenByFeatures(mContext.getString(R.string.account_type),
                "Full access token", null, activity, null, null, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                            token = authtoken;
                            Log.v("Login", "Token set correctly via Account manager " +
                                    authtoken.substring(token.length() - 20));
                            if (callback != null)
                                callback.onOk(token);
                        } catch (Throwable e) {
                            Log.e("Login", "Problems during getting authToken");
                            if (callback != null)
                                callback.onError(e);
                        }
                    }
                }, null);
    }

    /**
     * Get the stored token or show the login activity<br>
     * <strong>Caution:</strong> it iss an async task
     *
     * @param activity needed to show the login activity
     */
    public void storeToken(Activity activity) {
        storeToken(activity, null);
    }

    public String getToken() {
        return token;
    }


    /**
     * Stiore token, if onlyInternal is true the token is not stored into accountManager
     *
     * @param token
     */
    public void setToken(final String token) {
        //TODO according to setAuthToken this shoudl be called by the main thread
        this.token = token;
        //TODO better way for auth type
        //TODO use the same way used for the toast notification
        //TODO correct use new Account?
        //update the token stored using the main thread as suggested by javadoc
        if (accountName != null && !onlyInternal)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mAccountManager.setAuthToken(new Account(accountName,
                                    mContext.getString(R.string.account_type)), "Full access token",
                            token);
                }
            });
    }

    public void invalidateToken() {
        //TODO tmp method
        AccountManager.get(mContext).invalidateAuthToken(mContext.getString(R.string.account_type),
                token);
    }

    private LoginManager loginSync(String company, String email, String password) throws LoginException {
        //TODO improve, do we need LoginException?
        Response<Token> response = null;
        try {
            response = new Token().getSync(company, email, password);
        } catch (IOException e) {
            Log.w("Net login error", e.getMessage());
            throw new LoginException("Net login error: " + e.getMessage(), e);
        }
        if (response.isSuccess()) {
            Token token = response.body();
            this.token = "Bearer " + token.getToken();
            return this;
        }
        if (response.code() == 422) {
            try {
                String body = response.errorBody().string().toString();
                Log.w("Login error", Integer.toString(response.code()) + " " + response.message() +
                        " " + body);
                throw new LoginException(Integer.toString(response.code()) + " " +
                        response.message(), new JSONObject(body));
            } catch (IOException e) {
                Log.w("errorBody error", Integer.toString(response.code()) + " " + response.message() + e);
                throw new LoginException("errorBody error: " + Integer.toString(response.code()) + " " + e);
            } catch (JSONException e) {
                Log.w("JSON error", Integer.toString(response.code()) + " " + response.message() + e);
                throw new LoginException("JSON error: " + Integer.toString(response.code()) + " " + e);
            }
        } else {
            Log.w("Login error", Integer.toString(response.code()) + " " + response.message());
            throw new LoginException(Integer.toString(response.code()) + " " + response.message());
        }
    }

    /**
     * get the token and return errors<br>
     * <strong>Caution</strong> errors are already logged
     *
     * @param company
     * @param email
     * @param password
     * @return
     */
    public Bundle getTokenWithErrors(String company, String email, String password) {
        Bundle data = new Bundle();
        try {
            //TODO execute these on the main thread?
            String authtoken = this.loginSync(company, email, password).getToken();
            data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
        } catch (LoginException e) {
            if (e.getJsonErrors() == null) {
                data.putString(KEY_ERROR_MESSAGE, e.getMessage());
            } else {
                JSONObject errors = e.getJsonErrors();
                java.util.Iterator<java.lang.String> keys = errors.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        JSONArray errors2 = errors.getJSONArray(key);
                        String errorsJoined = "";
                        for (int i = 0; i < errors2.length(); i++) {
                            try {
                                errorsJoined += errors2.getString(i) + "\n";
                            } catch (JSONException e3) {
                                Log.w("Login", "errors parsing errors: " + e3);
                                //TODO show the error to user
                            }
                        }
                        if (errorsJoined.length() > 0) {
                            //remove last ", "
                            errorsJoined = errorsJoined.substring(0, errorsJoined.length() - 1);
                            data.putString(key, errorsJoined);
                        }
                    } catch (JSONException e1) {
                        //it is not an array
                        try {
                            data.putString(key, errors.getString(key));
                        } catch (JSONException e2) {
                            Log.w("Login", "errors parsing errors: " + e2);
                            //TODO show the error to user
                        }
                    }
                }
            }
        }
        return data;
    }
    //TODO when the error is not http?


    //TODO static?

    /**
     * this check automatically if a relogin is needed and perform it (invalidating also the old
     * token) in an async way
     *
     * @param e        httpException need to get code of error
     * @param activity needed to show the login view
     * @param callback login callback, if login is not need this is not called
     * @return true if the reLogin was perform (not if it successful since it's async)
     */
    public boolean reLogin(HttpException e, Activity activity, storeTokenCallback callback) {
        //invalidate token
        retrofit.HttpException response = e.getCause();
        //TODO test
        //TODO right && token != null
        if (response.code() == 401 && token != null) {
            Log.i("Login", "401 -> relogin needed");
            this.invalidateToken();
            token = null;
            this.storeToken(activity, callback);
            return true;
            //TODO how to manage request timeout? on error?
        }
        return false;
    }

    /**
     * this check automatically if a relogin is needed and perform it, via a async way
     *
     * @param e        httpException need to get code of error
     * @param activity needed to show the login view
     * @return true if the reLogin was perofrmn (not if it successful since it's async)
     */
    public boolean reLogin(HttpException e, Activity activity) {
        return reLogin(e, activity, null);
    }

    public static class storeTokenCallback {
        public void onOk(String authtoken) {
        }

        ;

        public void onError(Throwable e) {
        }

        ;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     * used by LoginActivity
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Intent> {
        private final LoginActivity loginActivity;

        public UserLoginTask(LoginActivity loginActivity) {
            this.loginActivity = loginActivity;
        }

        @Override
        protected Intent doInBackground(Void... params) {

            //TODO improve
            Log.v("Login", "login background started");

            Bundle data = LoginManager.this.getTokenWithErrors(loginActivity.getCompanyText(),
                    loginActivity.getEmailText(), loginActivity.getPasswordText());

            //no errors
            if (data.get(AccountManager.KEY_AUTHTOKEN) != null) {
                data.putString(AccountManager.KEY_ACCOUNT_NAME, loginActivity.getEmailText());
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, loginActivity.getIntent().
                        getStringExtra(LoginActivity.ARG_ACCOUNT_TYPE)); //TODO why this way?
                data.putString(PARAM_USER_PASS, loginActivity.getPasswordText());
                data.putString(PARAM_COMPANY_NAME, loginActivity.getCompanyText());
            }

            final Intent res = new Intent();
            res.putExtras(data);
            return res;
        }

        @Override
        protected void onPostExecute(final Intent intent) {
            loginActivity.setAuthTask(null);
            loginActivity.setShowProgress(false);

            //TODO remember
            //TODO even generic error called 'error'
            Bundle extras = intent.getExtras();

            if (!intent.hasExtra(AccountManager.KEY_ACCOUNT_NAME)) {
                Map<String, String> errors = bundleStrings(extras);
                if (errors.size() == 0) {
                    Log.e("Login", "errors found but they are not strings");
                } else {
                    loginActivity.onLoginError(errors);
                }
            } else {
                finish(intent);
            }
        }

        @Override
        protected void onCancelled() {
            loginActivity.setAuthTask(null);
            loginActivity.setShowProgress(false);
        }

        private void finish(Intent intent) {
            Log.v("Login", "Store login data");
            //TODO what do we have to do if we are trying to create an account that exists?
            String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
            String companyName = intent.getStringExtra(PARAM_COMPANY_NAME);
            String originalAccountName = loginActivity.getIntent().getStringExtra(loginActivity.ARG_ACCOUNT_NAME);
            final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = loginActivity.getmAuthTokenType();

            //TODO needed getIntent?
            //TODO maybe it's better store this const inside activity
            //originalAccoutnName = null if this is a new account
            //!originalAccountName.equals(accountName) -> new email so new account
            if (originalAccountName == null || !originalAccountName.equals(accountName)) {
                Log.v("login", "Store explicitly");

                //TODO fix
                //remove old acocunt
/*                if(originalAccountName != null)
                {
                    final Account old = new Account(originalAccountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
                    mAccountManager.removeAccountExplicitly(old);
                }*/

                // Creating the account on the device and setting the auth token we got
                // (Not setting the auth token will cause another call to the server to authenticate the user)
                Bundle extras = new Bundle();
                //TODO use const
                extras.putString("company", companyName);
                mAccountManager.addAccountExplicitly(account, accountPassword, extras);
                mAccountManager.setAuthToken(account, authtokenType, authtoken);
            } else {
                Log.v("login", "Store only password");
                //TODO during tests we see 401, why?
                //TODO if I change the email the acocutn keep the same?
                //TODO manage this case for other parameters, errors and otken
                mAccountManager.setPassword(account, accountPassword);
                mAccountManager.setAuthToken(account, authtokenType, authtoken);
                //TODO use const
                mAccountManager.setUserData(account, "company", companyName);
            }

            loginActivity.setAccountAuthenticatorResult(intent.getExtras());
            loginActivity.setResult(loginActivity.RESULT_OK, intent);
            loginActivity.finish();
        }

        private Map<String, String> bundleStrings(Bundle bundle) {
            Map<String, String> ret = new ArrayMap<>();
            Set<String> keys = bundle.keySet();
            String value;
            for (String key : keys) {
                value = bundle.getString(key);
                if (value != null)
                    ret.put(key, value);
            }
            return ret;
        }
    }
}
