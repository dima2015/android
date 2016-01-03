package com.plunner.plunner.models.login;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.plunner.plunner.activities.activities.LoginActivity;
import com.plunner.plunner.general.Plunner;
import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit.Response;


/**
 * Created by claudio on 20/12/15.
 */
public class LoginManager implements CallOnHttpError {
    public final static String PARAM_USER_PASS = "USER_PASS";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    private static LoginManager ourInstance = new LoginManager();
    private AccountManager mAccountManager;
    private String token;
    private boolean tokenCanBeSet = true;

    private LoginManager() {
        mAccountManager = AccountManager.get(Plunner.getAppContext());
    }

    public static LoginManager getInstance() {
        return ourInstance;
    }

    //TODO not static
    public static rx.Subscription loginByData(String company, String email, String password) {
        final LoginManager loginManager = getInstance();
        loginManager.tokenCanBeSet = false;
        return (new Token()).get(company, email, password, new Subscriber<Token>() {
            @Override
            public void onNext(Token token) {
                super.onNext(token);
                loginManager.tokenCanBeSet = true;
                loginManager.token = "Bearer " + token.getToken();
            }
        });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (tokenCanBeSet) {
            this.token = token;
        }
    }

    public LoginManager withToken(String token) {
        if (tokenCanBeSet) {
            this.token = token;
        }
        return this;
    }

    private LoginManager loginSync(String company, String email, String password) throws LoginException {
        Response<Token> response = null;
        try {
            response = new Token().getSync(company, email, password);
        } catch (IOException e) {
            Log.w("Net login error", e.getMessage());
            throw new LoginException("Net login error: " + e.getMessage(), e);
        }
        if (response.isSuccess()) {
            Token token = response.body();
            this.tokenCanBeSet = true;
            this.token = "Bearer " + token.getToken();
            return this;
        }
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

    }

    @Override
    public void onHttpError(HttpException e) {
        //TODO implement CallOnHttpError
    }
    //TODO when the error is not http?


    //TODO static?

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
            // TODO: attempt authentication against a network service.

            Log.v("Login", "login background started");

            Bundle data = new Bundle();
            try {
                String authtoken = null;
                //TODO execute these on the main thread?
                authtoken = LoginManager.this.loginSync(loginActivity.getCompanyText(),
                        loginActivity.getEmailText(), loginActivity.getPasswordText()).getToken();

                data.putString(AccountManager.KEY_ACCOUNT_NAME, loginActivity.getEmailText());
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, loginActivity.getIntent().
                        getStringExtra(LoginActivity.ARG_ACCOUNT_TYPE));
                data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                data.putString(PARAM_USER_PASS, loginActivity.getPasswordText());
                //TODO insert also company and test re-login

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
                    //data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }
            }

            final Intent res = new Intent();
            res.putExtras(data);
            return res;
        }

        @Override
        protected void onPostExecute(final Intent intent) {
            loginActivity.setAuthTask(null);
            loginActivity.setShowProgress(false);

            //intent.getExtras().
            //for(String error : )
            //if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
            //TODO catch all and fix focus
            //TODO even password and not thrown like remember
            //TODO even generic error called 'error'
            if (intent.hasExtra("email")) {
                //TODO company errors
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                loginActivity.setPasswordError(intent.getStringExtra("email"));
                loginActivity.requestPasswordFocus();
                Log.v("Login", "errors: " + intent.getStringExtra("email"));
            } else if (intent.hasExtra("company")) {
                loginActivity.setCompanyError(intent.getStringExtra("company"));
                loginActivity.requestCompanyFocus();
                Log.v("Login", "errors: " + intent.getStringExtra("company"));
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
            String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
            final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

            //TODO needed getIntent?
            if (loginActivity.getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
                Log.v("login", "Store explicitly");
                String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                String authtokenType = loginActivity.getmAuthTokenType();

                // Creating the account on the device and setting the auth token we got
                // (Not setting the auth token will cause another call to the server to authenticate the user)
                mAccountManager.addAccountExplicitly(account, accountPassword, null);
                mAccountManager.setAuthToken(account, authtokenType, authtoken);
            } else {
                Log.v("login", "Store only password");
                mAccountManager.setPassword(account, accountPassword);
            }

            loginActivity.setAccountAuthenticatorResult(intent.getExtras());
            loginActivity.setResult(loginActivity.RESULT_OK, intent);
            loginActivity.finish();
        }
    }
}
