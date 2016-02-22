package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.employee.Employee;

import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class Planner extends Employee<Planner> {
    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null;
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null;
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        return null;
    }
    //TODO check if it is a planner

}
