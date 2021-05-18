package com.jntuk.engineers.View.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.User.EditProfileActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private SignInViewModel signInViewModel;

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.colorWhite);

        Glide.with(getApplicationContext()).load(R.drawable.eng_genius_logo).into(binding.signupLogo);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        initSigninViewModel();
        binding.signupBtn.setOnClickListener((View v)->{
            CheckAccount();
        });
        binding.backLoginId.setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
//        binding.signupGoogleLogin.setOnClickListener((View view)->{
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        });
    }
    private void initSigninViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignInViewModel.class);
    }

    private void CheckAccount() {

        String email = binding.edtSignuEmailId.getText().toString().trim();
        String password = binding.edtSignupPasswordId.getText().toString().trim();
        String confirm_pass = binding.edtSignupCPasswordId.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter Your Email...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Enter Password...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(confirm_pass)){
            Toast.makeText(getApplicationContext(), "Enter Confirm Password...", Toast.LENGTH_SHORT).show();
        }else if(password.equals(confirm_pass)){
            ValidatePhoneNumber(email, password);
        }else {
            Toast.makeText(getApplicationContext(), "Password Doesn't same! Please Enter same password", Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidatePhoneNumber(String email, String password)
    {
        progressDialog.setTitle("Registration...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        signInViewModel.signUp(email, password);
        signInViewModel.signUpLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                finish();
            }
        });
    }
}