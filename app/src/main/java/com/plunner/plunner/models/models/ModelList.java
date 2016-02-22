package com.plunner.plunner.models.models;

import com.plunner.plunner.models.adapters.Subscriber;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class ModelList<T extends Model & Listable> extends Model<T> implements Cloneable {
    //TODO exteds Model<T>  Is <T> needed?
    private List<T> models = new ArrayList<T>();
    private T instance = null;

    public ModelList(List<T> models) {
        this.models = models;
        //TODO set an instance
    }

    public ModelList(T instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(models).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModelList) == false) {
            return false;
        }
        ModelList rhs = ((ModelList) other);
        return new EqualsBuilder().append(models, rhs.models).isEquals();
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
        return instance.getList(subscriber, parameters);
    }

    @Override
    public ModelList<T> clone() throws CloneNotSupportedException {
        ModelList<T> ret = (ModelList<T>) super.clone();
        ret.models = new ArrayList<T>(models);
        return ret;
    }
}
