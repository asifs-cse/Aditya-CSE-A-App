package com.jntuk.engineers.View.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.HomeActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;

public class SplashScreen extends AppCompatActivity {

    private static int splashTimeOut = 3000;
    private TextView text;
    private ImageView image;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initSignInViewModel();
        checkAuth();

        text = findViewById(R.id.splashTextId);
        image = findViewById(R.id.imageId);
        Glide.with(getApplicationContext()).load(R.drawable.eng_genius_logo).into(image);

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },splashTimeOut);
        }catch (Exception e)
        {
            Toast.makeText(this, "This Application Not Support your Device", Toast.LENGTH_SHORT).show();
        }

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.my_splash_animation);
        image.startAnimation(myanim);
        text.startAnimation(myanim);
    }

    private void checkAuth() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser user) {
                if (user.isAuth && user.isProfile){
                    signInViewModel.userInfoData();
                }
            }
        });
    }

    private void initSignInViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignInViewModel.class);
    }
}
