package com.demo.starwars.api;

import com.demo.starwars.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Suresh on 6/13/2018.
 */

public class RestClient {

    private static Retrofit retrofit = null;
    private ApiInterface apiService;

    public RestClient()
    {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        apiService = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApiInterface()
    {
        return apiService;
    }
}
