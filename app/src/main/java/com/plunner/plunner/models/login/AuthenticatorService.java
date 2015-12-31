package com.plunner.plunner.models.login;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    public AuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Authenticator(this).getIBinder();
    }
}
