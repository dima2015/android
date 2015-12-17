package com.plunner.plunner;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by claudio on 16/12/15.
 */
public interface GitHubInterface {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Repo>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
