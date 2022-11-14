package com.example.designmode.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceApi {

    @GET("TestServlet")
    Call<UserLogResult> useLogin (
            @Query("username") String username,
            @Query("password") String password);

}
