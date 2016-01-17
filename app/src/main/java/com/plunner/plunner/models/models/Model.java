package com.plunner.plunner.models.models;


import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.Callable;

import java.lang.reflect.Field;

/**
 * Created by claudio on 19/12/15.
 */
abstract public class Model {

    protected Callable callable;

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    /**
     * fresh the proprieties of the object
     *
     * @return
     */
    public rx.Subscription fresh() {
        return fresh(new FreshSubscriber(callable));
    }

    /**
     * fresh the proprieties of the object
     *
     * @param callable the callable instance to call callback
     * @return
     */
    public rx.Subscription fresh(Callable callable) {
        this.callable = callable;
        return fresh(new FreshSubscriber(callable));
    }

    /**
     * fresh the proprieties of the object
     *
     * @param subscriber
     * @return
     */
    abstract public rx.Subscription fresh(Subscriber subscriber);

    public rx.Subscription save() {
        return save(new Subscriber(callable));
    }

    /**
     * @param callable the callable instance to call callback
     * @return
     */
    public rx.Subscription save(Callable callable) {
        this.callable = callable;
        return save(new Subscriber(callable));
    }

    abstract public rx.Subscription save(Subscriber subscriber);

    /**
     * @param subscriber
     * @param parameters the parameters to perform the get unequivocally
     * @return
     */
    abstract public rx.Subscription get(Subscriber subscriber, String... parameters);

    /**
     * @param parameters the parameters to perform the get unequivocally
     * @return
     */
    public rx.Subscription get(String... parameters) {
        return get(new Subscriber(callable), parameters);
    }

    /**
     * @param callable   the callable instance to call callback
     * @param parameters the parameters to perform the get univocally
     * @return
     */
    public rx.Subscription get(Callable callable, String... parameters) {
        this.callable = callable;
        return get(new Subscriber(callable), parameters);
    }

    /**
     * Copy proprieties of another model inside this model
     *
     * @param model from which copy data
     * @return this
     * @throws ModelException on errors due to incorrect access to proprieties
     */
    protected Model copy(Model model) throws ModelException {
        if (this.getClass().getName() != model.getClass().getName())
            throw new ModelException("this and class given are different");

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(this, field.get(model));
            } catch (IllegalAccessException e) {
                throw new ModelException("parameters error during copy", e);
            }
        }
        return this;
    }

    /**
     * FreshSubscriber that perform the proprieties fresh and manage errors
     *
     * @param <T>
     */
    public class FreshSubscriber<T extends Model> extends Subscriber<T> {
        public FreshSubscriber(Callable callable) {
            super(callable);
        }

        public FreshSubscriber() {
        }

        @Override
        public void onNext(T model) {
            try {
                Model.this.copy(model);
            } catch (ModelException e) {
                this.onError(e);
                return;
            }
            super.onNext(model);
        }
    }
}