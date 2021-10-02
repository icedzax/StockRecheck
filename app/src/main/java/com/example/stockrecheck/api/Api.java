package com.example.stockrecheck.api;

import com.example.stockrecheck.dao.PlanItems;
import com.example.stockrecheck.dao.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    String BASE_URL = "https://hook.zubbsteel.com/line-ci/cstock/v1/";
    @GET("getPlan")
    Call<List<Results>> getPlanList();


    @GET("getPlan_item/{find}")
    Call<List<PlanItems>> listPlanDoc(@Path("find") String find);


}
