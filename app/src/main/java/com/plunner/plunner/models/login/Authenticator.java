package com.plunner.plunner.models.login;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.plunner.plunner.activities.activities.LoginActivity;

/**
 * Created by claudio on 31/12/15.
 */
public class Authenticator extends AbstractAccountAuthenticator {
    private final Context mContext;
    private final AccountManager mAccountManager;

    public Authenticator(Context context) {
        super(context);
        this.mContext = context;
        mAccountManager = AccountManager.get(mContext);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.v("login", "addAccount called");

        //only one account check
        /*
        if (mAccountManager.getAccountsByType(mContext.getString(R.string.account_type)).length >= 1) {
            Log.v("login", "error more than one account");
            final Bundle result = new Bundle();
            //TODO right error code
            result.putInt(AccountManager.KEY_ERROR_CODE, 1);//AccountManager.ERROR_CODE_ERROR_CODE_ONE_ACCOUNT_ALLOWED);
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Only one account allowed");

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Only one account allowed", Toast.LENGTH_SHORT).show();
                }
            });

            return result;
        }*/

        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);


        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.v("login", "getAuthToken called");

        // If the caller requested an authToken type we don't support, then
        // return an error
        //TODO fix this check?
        /*if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }*/

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);


        //TODo fix with company and with our syncLogin

        //TODO test
        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                //TODO use const
                Log.v("login", "re-authenticating with the existing password, since it was not" +
                        " possible restore token form cache");
                Bundle data = LoginManager.getInstance().getTokenWithErrors(
                        am.getUserData(account, "company").toString(), account.name, password);

                //no errors
                if (data.get(AccountManager.KEY_AUTHTOKEN) != null) {
                    authToken = data.getString(AccountManager.KEY_AUTHTOKEN);
                }
                //errors are already logged
            }
        } else {
            Log.v("login", "token restored from cache");
        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        //TODO remvoe accunts if they are not logged

        //TODO call addAccount to check automaticlaly if an acocunt is present or not????

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        Log.v("login", "set new credentials, since other methods fails");
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.ARG_ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    //TODO write this method
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {

        final Bundle result = super.getAccountRemovalAllowed(response, account);

        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);

        return result;
    }

    //TODO remove
}
