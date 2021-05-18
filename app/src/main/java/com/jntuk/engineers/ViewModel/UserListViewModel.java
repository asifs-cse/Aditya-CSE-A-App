package com.jntuk.engineers.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.SliderClass;
import com.jntuk.engineers.Model.UserTitle;
import com.jntuk.engineers.Ripository.SignInRepository;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private SignInRepository repository;
    public LiveData<List<UserTitle>> getUserInfoLiveData;
    public LiveData<List<SignInUser>> getUserDetailsLiveData;
    public LiveData<List<SliderClass>> getSliderLiveData;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository();
    }

    public void getUserInfo(){
        getUserInfoLiveData = repository.userTitleMutableLiveData();
    }
    public void getUserDetails(String roll){
        getUserDetailsLiveData = repository.userDetailsMutableLiveData(roll);
    }

    public void getSliderUrl(){
        getSliderLiveData = repository.sliderMutableLiveData();
    }
}
