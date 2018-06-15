package com.demo.starwars.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

/**
 * Created by Suresh on 6/13/2018.
 */
public class BaseViewModel extends AndroidViewModel {

    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    public final MutableLiveData<String> errorSnackbar = new MutableLiveData<>();
    public final MutableLiveData<String> errorFullScreen = new MutableLiveData<>();


    public BaseViewModel(@NonNull Application application) {
        super(application);
        setIsErrorSnackbar(null);
        setIsErrorFullScreen(null);
    }

    public void setIsLoading(boolean isLoad) {
        isLoading.set(isLoad);
    }

    public void setIsErrorSnackbar(String errorMsg) {
        errorSnackbar.setValue(errorMsg);
    }

    public void setIsErrorFullScreen(String errorMsg) {
        errorFullScreen.setValue(errorMsg);
    }



    @Override
    protected void onCleared() {
        super.onCleared();
    }
}