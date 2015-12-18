
package com.plunner.plunner.models;

import android.util.Log;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import retrofit.GsonConverterFactory;
import retrofit.HttpException;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Generated("org.jsonschema2pojo")
public class Employee extends Model {

    private String id;
    private String name;
    private String email;
    private String companyId;
    private String createdAt;
    private String updatedAt;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //TODO serialization?

    /**
     * No args constructor for use in serialization
     */
    public Employee() {
    }

    /**
     * @param updatedAt
     * @param id
     * @param email
     * @param createdAt
     * @param name
     * @param companyId
     */
    public Employee(String id, String name, String email, String companyId, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.companyId = companyId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public void setId(String id) {
        this.id = id;
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
    public void setName(String name) {
        this.name = name;
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
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId The company_id
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(email).append(companyId).append(createdAt).append(updatedAt).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(email, rhs.email).append(companyId, rhs.companyId).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    /**
     * @return
     */
    static public void getEmployee() {
        RestInterface rest = createRetrofit(RestInterface.class);

        // Create a call instance for looking up Retrofit contributors.
        Observable<Employee> call = rest.get("Bearer aeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoiZW4iLCJzdWIiOiIzNCIsImlzcyI6Imh0dHA6XC9cL2FwaS5wbHVubmVyLmNvbVwvZW1wbG95ZWVzXC9tZWV0aW5ncyIsImlhdCI6IjE0NTA0ODA2MDYiLCJleHAiOiIxNDUwNDg0MjA2IiwibmJmIjoiMTQ1MDQ4MDYwNiIsImp0aSI6ImRiMjE3MTdjMWE0YmIwNWZlZTBlZWZmZWIzNDc1YWRhIn0.R-WWewXBJ3NI0PbRc0p90jPCrGfl0ALnR2INx3wzKzg");

        //TODO check if the the odl execution is still in waiting status
        //TODO timeout
        Subscription subscription = call.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Employee>() {
            @Override

            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                // cast to retrofit.HttpException to get the response code
                if (e instanceof HttpException) {
                    HttpException response = (HttpException) e;
                    int code = response.code();
                    Log.d("net error", Integer.toString(code));
                }
                //TODO else
            }

            @Override
            public void onNext(Employee employee) {
                Log.d("employee", employee.toString());
            }

        });
    }

    //TODO syncronized??
    //TODO clone

    //TODO is it possible extends a generic interface with standard rest calls?
    static private interface RestInterface {
        @GET("/employees/employee/")
        Observable<Employee> get(@Header("Authorization") String authorization);
    }

    @Override
    public void fresh() {

    }

    @Override
    public void save() {

    }
}