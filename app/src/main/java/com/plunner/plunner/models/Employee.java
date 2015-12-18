
package com.plunner.plunner.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Header;

@Generated("org.jsonschema2pojo")
public class Employee {

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
     *
     */
    public Employee() {
    }

    /**
     *
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
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     *
     * @param companyId
     *     The company_id
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     *
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     *     The updated_at
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
     *
     * @return
     */
    static public Employee getEmployee() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.plunner.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        RestInterface rest = retrofit.create(RestInterface.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<Employee> call = rest.get("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoiZW4iLCJzdWIiOiIzNCIsImlzcyI6Imh0dHA6XC9cL2FwaS5wbHVubmVyLmNvbVwvZW1wbG95ZWVzXC9jYWxlbmRhcnNcLzEwMFwvdGltZXNsb3RzIiwiaWF0IjoiMTQ1MDQ3NjMxNiIsImV4cCI6IjE0NTA0Nzk5NTgiLCJuYmYiOiIxNDUwNDc2MzU4IiwianRpIjoiNjVkMjgxYzI4ZjlmOTNkYWQ4NjdlYWEzZDY2OTI2NGYifQ.6cwWcclXdt2jqeRpc6EEyb9sv2irEAb-o0mT4fbAEbw");

        // Fetch and print a list of the contributors to the library.
        Employee employee = null;
        try {
            employee = call.execute().body();
            //TODO check if it is null
            System.out.println(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employee;
    }

    //TODO private?
    static interface RestInterface
    {
        @GET("/employees/employee/")
        Call<Employee> get(@Header("Authorization") String authorization);
    }

}