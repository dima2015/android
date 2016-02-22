package com.plunner.plunner.models.models;

import com.plunner.plunner.models.adapters.Subscriber;

/**
 * Created by claudio on 22/02/16.
 */
public interface Listable {
    rx.Subscription getList(Subscriber subscriber, String... parameters);
}
