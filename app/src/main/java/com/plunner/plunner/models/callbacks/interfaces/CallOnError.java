package com.plunner.plunner.models.callbacks.interfaces;

import com.plunner.plunner.models.models.Model;

/**
 * Created by claudio on 20/12/15.
 */
public interface CallOnError<T extends Model> extends Callable<T> {
    void onError(Throwable e);
}
