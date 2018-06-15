package com.demo.starwars.api;

import com.demo.starwars.model.character.CharacterResponseBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Suresh on 6/13/2018.
 */

public interface ApiInterface {

    // for getting character Data
    @GET("people")
    Call<CharacterResponseBean> getCharacters();

    @GET("people")
    Call<CharacterResponseBean> getListCharacters(@Query("page") String perPage);
}
