package com.jntuk.engineers.View.Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.HomeActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private static final int RC_SIGN_IN = 1;
    private SignInViewModel signInViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        }catch (Exception e){
            Log.e("Binding_error", "onCreateView", e);
            throw e;
        }


        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.colorWhite);

        Glide.with(getApplicationContext()).load(R.drawable.eng_genius_logo).into(binding.loginLogo);

        initViewModel();
        signinMethod();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        showEmail();

        binding.signinBtn.setOnClickListener((View view) -> {
            LoginAccount();
        });
        binding.forgetPasswordId.setOnClickListener((View view) -> {
            forgetMail(getApplicationContext());
        });

//        binding.googleSignupBtn.setOnClickListener((View view) -> {
//            GoogleLogin();
//        });
        binding.signupBtn.setOnClickListener((View view) -> {
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            finish();
        });
    }
    private void initViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this
                .getApplication())).get(SignInViewModel.class);
    }

    private void signinMethod() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account!=null){
                    getGoogleAuthCredential(account);
                }

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getGoogleAuthCredential(GoogleSignInAccount account) {
        String googleTokenId = account.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInViewModel.isAuthentication(credential);
        signInViewModel.authLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }


    private void GoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showEmail() {
        SharedPreferences infoPre = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        binding.edtLoginEmailId.setText(infoPre.getString("email", ""));
    }

    private void LoginAccount() {
        String email = binding.edtLoginEmailId.getText().toString().trim();
        String password = binding.edtLoginPasswordId.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please write your email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            AllowAccessToAccount(email, password);
        }
    }

    private void AllowAccessToAccount(String email, String password) {
        progressDialog.setTitle("Login...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        signInViewModel.emailAuthentication(email, password);
        signInViewModel.emailAuthLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
                signInViewModel.checkAuthFirebase();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    private void forgetMail(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.forget_mail_sample, null);

        Button cancel = (Button) view.findViewById(R.id.for_email_cancelId);
        Button submit = (Button) view.findViewById(R.id.for_email_submitId);
        EditText emailEdt = (EditText) view.findViewById(R.id.for_emailId);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener((View v)->{
            alertDialog.dismiss();
        });
        submit.setOnClickListener((View v)->{
            String email = emailEdt.getText().toString();
            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), "Email not found, Please try again", Toast.LENGTH_SHORT).show();
            }else  {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                try {
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Please Check your email Address", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }
}