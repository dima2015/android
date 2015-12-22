package com.plunner.plunner.models.login;

import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
class Token extends Model {

    private String token;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Token() {
    }

    /**
     * @param token
     */
    public Token(String token) {
        this.token = token;
    }

    public Subscription get(String company, String email, String password, Subscriber subscriber) {
        return get(subscriber, company, email, password);
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public Token withToken(String token) {
        this.token = token;
        return this;
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

    public Token withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(token).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Token) == false) {
            return false;
        }
        Token rhs = ((Token) other);
        return new EqualsBuilder().append(token, rhs.token).append(additionalProperties, rhs.additionalProperties).isEquals();
    }


    @Override
    public Subscription fresh(Subscriber subscriber) {
        return null; //TODO implement, we need route for refresh
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null; //TODO implement
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        if (parameters.length != 3)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 3)"));
        return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).
                login(parameters[0], parameters[1], parameters[2]), subscriber);
    }

    private static interface RestInterface {
        @FormUrlEncoded
        @POST("/employees/auth/login")
        Observable<Token> login(@Field("company") String company, @Field("email") String email,
                                @Field("password") String password);
    }
}