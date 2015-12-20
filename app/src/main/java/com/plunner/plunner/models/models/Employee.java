
package com.plunner.plunner.models.models;

import com.plunner.plunner.activities.interfaces.CanSetModel;
import com.plunner.plunner.models.adapters.Subscriber;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import retrofit.http.GET;
import rx.Observable;

@Generated("org.jsonschema2pojo")
public class Employee extends Model {

    private String id;
    private String name;
    private String email;
    private String company_id;
    private String created_at;
    private String updated_at;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //TODO serialization?

    /**
     * No args constructor for use in serialization
     */
    public Employee() {
    }

    /**
     * @param updated_at
     * @param id
     * @param email
     * @param created_at
     * @param name
     * @param company_id
     */
    public Employee(String id, String name, String email, String company_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company_id = company_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    //TODO chose a different name
    static public rx.Subscription getEmployee(Subscriber subscriber) {
        RestInterface rest = createRetrofit(RestInterface.class);

        Observable<Employee> call = rest.get();

        //TODO check if the the old execution is still in waiting status
        //TODO timeout
        //TODO create an adapter for rest with creation and subscribe
        //TODO insert extends where it is possible
        //TODO interface toi set model into activuty or null on error (with errors?)
        //TODO delete, create and so on
        return subscribe(call, subscriber);
    }

    static public rx.Subscription getEmployee() {
        return getEmployee(new <Employee>Subscriber());
    }

    static public rx.Subscription getEmployee(CanSetModel canSetModel) {
        return getEmployee(new <Employee>Subscriber(canSetModel));
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public Employee setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public Employee setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public Employee setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * @return The company_id
     */
    public String getCompany_id() {
        return company_id;
    }

    /**
     * @param company_id The company_id
     */
    public Employee setCompany_id(String company_id) {
        this.company_id = company_id;
        return this;
    }

    /**
     * @return The created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public Employee setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    /**
     * @return The updated_at
     */
    public String getUpdated_at() {
        return updated_at;
    }

    /**
     * @param updated_at The updated_at
     */
    public Employee setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public Employee setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(email).append(company_id).append(created_at).append(updated_at).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Employee) == false) {
            return false;
        }
        Employee rhs = ((Employee) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(email, rhs.email).append(company_id, rhs.company_id).append(created_at, rhs.created_at).append(updated_at, rhs.updated_at).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    //TODO syncronized??
    //TODO clone

    @Override
    public rx.Subscription fresh(Subscriber subscriber) {
        return subscribe(createRetrofit(RestInterface.class).get(), subscriber);
    }

    @Override
    public rx.Subscription fresh() {
        return fresh(new <Employee>Subscriber() {
            @Override
            public void onNext(Model model) {
                super.onNext(model);
                Employee employee = (Employee) model;
                Employee.this.copy(employee);
            }
        });
    }

    @Override
    public rx.Subscription save(Subscriber subscriber) {
//TODO implement
        return null;
    }

    @Override
    public rx.Subscription save() {
//TODO implement
        return null;
    }

    //TODO is it possible extends a generic interface with standard rest calls?
    static private interface RestInterface {
        @GET("/employees/employee/")
        Observable<Employee> get();
    }

    private Employee copy(Employee employee2)
    {
        id = employee2.id;
        name = employee2.name;
        email = employee2.email;
        company_id = employee2.company_id;
        created_at = employee2.created_at;
        updated_at = employee2.updated_at;
        additionalProperties = employee2.additionalProperties;
        return this;
    }
}