package com.plunner.plunner.models.models;


import com.plunner.plunner.models.adapters.Subscriber;

import java.lang.reflect.Field;

/**
 * Created by claudio on 19/12/15.
 */
abstract public class Model {
    public rx.Subscription fresh() {
        return fresh(new Subscriber() {
            @Override
            public void onNext(Model model) {
                try {
                    Model.this.copy(model);
                } catch (ModelException e) {
                    this.onError(e);
                    return;
                }
                super.onNext(model);
            }
        });
    }

    ;
    abstract public rx.Subscription fresh(Subscriber subscriber);

    public rx.Subscription save() {
        return save(new Subscriber());
    }

    ;
    abstract public rx.Subscription save(Subscriber subscriber);

    abstract public rx.Subscription get(String... parameters);

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

    static protected class ModelException extends Exception {
        public ModelException() {
        }

        public ModelException(String detailMessage) {
            super(detailMessage);
        }

        public ModelException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ModelException(Throwable throwable) {
            super(throwable);
        }
    }

}
