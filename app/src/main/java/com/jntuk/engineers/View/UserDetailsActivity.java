package com.jntuk.engineers.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.User.EditProfileActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;
import com.jntuk.engineers.databinding.ActivityUserDetailsBinding;

import java.util.List;


public class UserDetailsActivity extends AppCompatActivity {

    private SignInViewModel signInViewModel;
    private ActivityUserDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private UserListViewModel userListViewModel;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_details);

        initSignInViewModel();
        initGoogleSignInClint();

        Glide.with(getApplicationContext()).load(R.drawable.back).into(binding.detailsBackBtn);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true)
        {
            checkUser();
            binding.editProfileId.setOnClickListener((View view)->{
                try {
                    startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "You are offline! Check Internet Connection...", Snackbar.LENGTH_LONG);
            snackbar.setDuration(5000);
            snackbar.show();
            binding.editProfileId.setOnClickListener((View view)->{
                Toast.makeText(getApplicationContext(), "You are offline! Check Internet Connection...", Toast.LENGTH_SHORT).show();
            });
        }

        binding.userLogoutCardId.setOnClickListener((View view)->{
            logout();
        });
        binding.detailsBackBtn.setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });
    }


    private void initSignInViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(UserListViewModel.class);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
    private void deleteAccount() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    finish();
                }
            }
        });
    }
    private void initGoogleSignInClint() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    currentUserId = signInUser.getuId();
                    if (signInUser.isProfile){
                        onlineInfo();
                    }else {
                        Toast.makeText(UserDetailsActivity.this, "Please Sign in first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected void onlineInfo() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                binding.accountNameId.setText(signInUsers.get(0).getFull_name());
                binding.accountRollId.setText(signInUsers.get(0).getRoll());
                binding.accountRegiId.setText(signInUsers.get(0).getRegistration());
                binding.accountDepartmentId.setText(signInUsers.get(0).getDepartment());
                binding.accountSemesterId.setText(signInUsers.get(0).getSemester());
                binding.accountPhoneId.setText(signInUsers.get(0).getPhone());
                binding.accountEmailId.setText(signInUsers.get(0).getEmail());
                binding.accountInstituteId.setText(signInUsers.get(0).getInstitute());
                binding.accountBloodId.setText(signInUsers.get(0).getBlood());
                binding.accountDateOfBirthId.setText(signInUsers.get(0).getBirthday());
            }
        });

    }
}
