package com.plunner.plunner.models.models;


import com.plunner.plunner.models.adapters.Subscriber;

/**
 * Created by claudio on 19/12/15.
 */
abstract public class Model {
    abstract public rx.Subscription fresh();
    abstract public rx.Subscription fresh(Subscriber subscriber);
    abstract public rx.Subscription save();
    abstract public rx.Subscription save(Subscriber subscriber);


}
