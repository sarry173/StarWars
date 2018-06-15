package com.demo.starwars.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.demo.starwars.api.ApiInterface;
import com.demo.starwars.api.RestClient;
import com.demo.starwars.model.character.CharacterResponseBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suresh on 6/13/2018.
 */

public class DataRepository
{
    private static final String LOG_TAG = DataRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    private static DataRepository INSTANCE = null;


    public DataRepository(Context context) {
        this.apiInterface = new RestClient().getApiInterface();
    }

    public static synchronized DataRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(context);
        }
        return INSTANCE;
    }

    public MutableLiveData<DataWrapper<CharacterResponseBean>>  fetchAllCharacters(int currentPage)
    {

        MutableLiveData<DataWrapper<CharacterResponseBean>> data = new MutableLiveData<>();
        apiInterface.getListCharacters(""+currentPage).enqueue(new Callback<CharacterResponseBean>() {
            @Override
            public void onResponse(Call<CharacterResponseBean> call, Response<CharacterResponseBean> response)
            {
                Log.e(LOG_TAG,""+ response.body());
                data.postValue(new DataWrapper(response.body()));
            }

            @Override
            public void onFailure(Call<CharacterResponseBean> call, Throwable t)
            {
                Log.e(LOG_TAG, "Network error "+t.getLocalizedMessage());
                data.setValue(new DataWrapper(t));
            }
        });
        return data;
    }



}
