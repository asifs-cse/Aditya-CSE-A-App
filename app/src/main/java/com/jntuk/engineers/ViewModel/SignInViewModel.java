package com.jntuk.engineers.ViewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.TempUser;
import com.jntuk.engineers.Ripository.SignInRepository;

import java.util.List;


public class SignInViewModel extends AndroidViewModel {

    private SignInRepository repository;
    public LiveData<SignInUser> checkAuthLiveData;
    public LiveData<List<SignInUser>> userDetailLiveData;
    public LiveData<SignInUser> checkInternetLiveData;
    public LiveData<TempUser> userTempLiveData;
    public LiveData<String> authLiveData;
    public LiveData<String> emailAuthLiveData;
    public LiveData<String> createAccountLiveData;
    public LiveData<String> signUpLiveData;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository();
    }
    public void checkAuthFirebase(){
        checkAuthLiveData = repository.checkAuthFirebase();
    }
    public void userInfoData(){
        userDetailLiveData = repository.getFirebaseUserData();
    }
    public void userTempData(){
        userTempLiveData = repository.collectUserTempData();
    }
    public void isAuthentication(AuthCredential credential){
        authLiveData = repository.firebaseSignInWithGoogle(credential);
    }
    public void emailAuthentication(String email, String password){
        emailAuthLiveData = repository.firebaseSignInWithEmail(email, password);
    }

    public void createAccount(SignInUser user, Uri imageUri){
        createAccountLiveData = repository.createOrEditAccount(user, imageUri);
    }
    public void checkInternet(Context context){
        checkInternetLiveData = repository.checkInternet(context);
    }
    public void signUp(String email, String password){
        signUpLiveData = repository.firebaseSignUpMutableLiveData(email, password);
    }
}
