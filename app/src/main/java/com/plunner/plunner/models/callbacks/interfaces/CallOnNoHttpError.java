package com.plunner.plunner.models.callbacks.interfaces;


import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.adapters.NoHttpException;


/**
 * Created by claudio on 20/12/15.
 */
public interface CallOnNoHttpError<T extends Model> extends Callable<T> {
    void onNoHttpError(NoHttpException e);
}
