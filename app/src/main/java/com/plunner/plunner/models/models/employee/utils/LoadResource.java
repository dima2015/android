package com.plunner.plunner.models.models.employee.utils;

import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.models.FatherParameters;
import com.plunner.plunner.models.models.Model;

/**
 * Created by claudio on 21/02/16.
 */
public class LoadResource<S extends Model> {

    String[] parameters = null;
    private S instance = null;

    public LoadResource(S instance, String... parameters) {
        this.instance = instance;
        this.parameters = parameters;
    }

    /**
     * Get the instance of th resource, before the load it is an empty object of the same type of the resource requested then it is the instance of the resource received
     *
     * @return
     */
    public S getInstance() {
        return instance;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String... parameters) {
        this.parameters = parameters;
    }

    public rx.Subscription load(LoadSubscriber subscriber) {
        //TODO check parameters
        return instance.get(subscriber, parameters);
    }

    public rx.Subscription load(Callable<S> callable) {
        return load(new LoadSubscriber(callable));
    }

    public rx.Subscription load() {
        return load(new LoadSubscriber());
    }

    public class LoadSubscriber extends Subscriber<S> {
        public LoadSubscriber(Callable callable) {
            super(callable);
        }

        public LoadSubscriber() {
            super();
        }

        @Override
        public void onNext(S resource) {
            LoadResource.this.instance = resource;
            if (resource instanceof FatherParameters)
                ((FatherParameters) resource).setFatherParameters(parameters);
            super.onNext(resource);
        }
    }
}

