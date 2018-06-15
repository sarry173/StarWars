package com.demo.starwars.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.demo.starwars.R;
import com.demo.starwars.base.BaseViewModel;
import com.demo.starwars.model.character.CharacterResponseBean;
import com.demo.starwars.repository.DataRepository;
import com.demo.starwars.repository.DataWrapper;

import java.util.concurrent.TimeoutException;

/**
 * Created by Suresh on 6/13/2018.
 */

public class CharacterViewModel extends BaseViewModel
{
    private final DataRepository repository;
    MutableLiveData<DataWrapper<CharacterResponseBean>> data = new MutableLiveData<>();

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        repository = DataRepository.getInstance(application);
        data.setValue(null);
    }


    public void fetchNextCharactersData(int currentPage)
    {
//        if (data==null)
        data = repository.fetchAllCharacters(currentPage);
    }

    public LiveData<DataWrapper<CharacterResponseBean>> getAllCharacterDataObservable()
    {
        return data;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getApplication().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getApplication().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getApplication().getString(R.string.error_msg);
        }

        return errorMsg;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplication(). getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

