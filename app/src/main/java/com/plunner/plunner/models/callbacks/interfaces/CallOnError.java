package com.plunner.plunner.models.callbacks.interfaces;

/**
 * Created by claudio on 20/12/15.
 */
public interface CallOnError extends Callable{
    void onError(Throwable e);
}