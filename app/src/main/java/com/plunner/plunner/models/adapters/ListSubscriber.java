package com.plunner.plunner.models.adapters;

import com.plunner.plunner.models.models.Listable;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelList;

import java.util.List;

/**
 * Created by claudio on 22/12/15.
 */
public class ListSubscriber<T extends Model & Listable> extends rx.Subscriber<List<T>> {
    private Subscriber subscriber;
    private T instance;

    public ListSubscriber(Subscriber subscriber, T instance) {
        this.subscriber = subscriber;
        this.instance = instance;
    }

    @Override
    final public void onCompleted() {
        subscriber.onCompleted();
    }

    @Override
    final public void onError(Throwable e) {
        subscriber.onError(e);
    }

    @Override
    final public void onNext(List<T> models) {
        subscriber.onNext(new ModelList<T>(models, instance));
    }
}
