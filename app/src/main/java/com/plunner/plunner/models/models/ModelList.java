package com.plunner.plunner.models.models;

import com.plunner.plunner.models.adapters.Subscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class ModelList<T extends Model> extends Model {
    private List<T> models = new ArrayList<T>();

    public ModelList(List<T> models) {
        this.models = models;
    }

    public ModelList() {
    }

    public List<T> getModels() {
        return models;
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null; //TODO implement
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null; //TODO implement
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        return null; //TODO implement
    }
}
