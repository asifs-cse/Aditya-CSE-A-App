package com.jntuk.engineers.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jntuk.engineers.DatePicker.DatePickerFragment;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Register.LoginActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private static final int PENDING_INTENT_REQ_CODE = 101;
    private EditText title, sub;
    private String saveCurrentDate, saveCurrentTime, currentUserId;
    private StorageReference pdfRef;
    private DatabaseReference titleRef;
    private ProgressDialog progressDialog;
    private String key, userName ,userRoll;
    private SignInViewModel signInViewModel;
    private UserListViewModel userListViewModel;
    private Boolean isAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        initViewModel();
        checkUser();

        titleRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        pdfRef = FirebaseStorage.getInstance().getReference().child("Admin");

        title = findViewById(R.id.set_materialTitle);
        sub = findViewById(R.id.set_material_subId);

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null){
            currentUserId = User.getUid();
        }else {
            Toast.makeText(this, "Sign in first", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.material_submit_btn).setOnClickListener((View v)->{
            try {
                if (TextUtils.isEmpty(sub.getText().toString()))
                {
                    sub.setError("First Write Material Subject");
                }else if (TextUtils.isEmpty(title.getText().toString()))
                {
                    title.setError("First Write Material Name");
                }else if (isAuth){
                    selectPDFFile();
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        });

    }



    private void selectPDFFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File" ),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm: a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        key =  titleRef.push().getKey();

        progressDialog = new ProgressDialog(UploadActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = pdfRef.child("material/" + data + key + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
                Uri url = uri.getResult();
                uploadInfo(url);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading: "+(int)progress+"%");
            }
        });
    }
    private void uploadInfo(Uri url) {
        HashMap<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("title",title.getText().toString());
        noticeMap.put("subject",sub.getText().toString());
        noticeMap.put("submitter",userName);
        noticeMap.put("submitter_roll",userRoll);
        noticeMap.put("uri", url.toString());
        noticeMap.put("date", saveCurrentDate);

        titleRef.child("Material").child(key).updateChildren(noticeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Material upload successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Material Upload Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(UserListViewModel.class);
    }

    private void showUserStatus() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                userName = signInUsers.get(0).getFirst_name();
                userRoll = signInUsers.get(0).getRoll();
            }
        });
    }
    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    isAuth = true;
                    if (signInUser.isProfile){
                        showUserStatus();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Sign in first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}