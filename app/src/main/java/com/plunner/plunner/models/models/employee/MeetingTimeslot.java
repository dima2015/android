package com.plunner.plunner.models.models.employee;

import com.plunner.plunner.models.adapters.Subscriber;

import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class MeetingTimeslot extends com.plunner.plunner.models.models.general.MeetingTimeslot {

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
}
