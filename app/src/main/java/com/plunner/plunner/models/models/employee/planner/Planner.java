package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.utility.LoadResource;

import java.lang.reflect.Field;

import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class Planner extends Employee<Planner> {

    private LoadResource<ModelList<Group>> groupsManaged = new LoadResource<ModelList<Group>>(new ModelList<Group>(new Group()));

    public Planner(Employee employee) throws ModelException {
        Field[] fields = employee.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(this, field.get(employee));
            } catch (IllegalAccessException e) {
                throw new ModelException("parameters error during copy", e);
            }
        }
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null;
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null;
    }
    //TODO check if it is a planner

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        return null;
    }

    public LoadResource<ModelList<Group>> getGroupsManaged() {
        return groupsManaged;
    }

}
