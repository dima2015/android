package com.plunner.plunner.activities.interfaces;

import com.plunner.plunner.models.models.Model;

/**
 * Created by claudio on 20/12/15.
 */
public interface CallOnNext<T extends Model> extends Callable {
    void onNext(T t);
}
