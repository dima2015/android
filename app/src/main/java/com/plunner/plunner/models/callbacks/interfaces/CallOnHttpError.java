package com.plunner.plunner.models.callbacks.interfaces;

import retrofit.HttpException;

/**
 * Created by claudio on 20/12/15.
 */
public interface CallOnHttpError extends Callable{
    void onHttpError(HttpException e);
}
