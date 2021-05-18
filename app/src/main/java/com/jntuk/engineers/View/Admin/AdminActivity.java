package com.jntuk.engineers.View.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jntuk.engineers.DatePicker.DatePickerFragment;
import com.jntuk.engineers.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private static final int PENDING_INTENT_REQ_CODE = 101;
    private EditText title;
    private String saveCurrentDate, saveCurrentTime, key;
    private StorageReference pdfRef;
    private DatabaseReference titleRef;
    private  ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private Button getDate;
    private TextView date;
    String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        titleRef = FirebaseDatabase.getInstance("https://engineers-f801e-default-rtdb.firebaseio.com/").getReference().child("Admin");
        key =  titleRef.push().getKey();
        pdfRef = FirebaseStorage.getInstance().getReference().child("Admin");

        title = findViewById(R.id.set_noticeTitle);
        date = findViewById(R.id.dateTv);

        findViewById(R.id.notice_submit_btn).setOnClickListener((View v)->{
            try {
                if (TextUtils.isEmpty(title.getText().toString()))
                {
                    title.setError("First Write Notice Name");
                }else {
                    selectPDFFile();
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        });

        getDate = findViewById(R.id.notice_add_date);

        getDate.setOnClickListener((View view1)->{
            addDate();
        });

        //updateToken(FirebaseInstanceId.getInstance().getToken());
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

        //noticeRendomKey = saveCurrentDate + saveCurrentTime;

        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = pdfRef.child("Notice/" + data + key + ".pdf");
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
        noticeMap.put("uri", url.toString());
        noticeMap.put("time", saveCurrentTime);
        noticeMap.put("date", date.getText().toString());

        titleRef.child("Notice").child(key).updateChildren(noticeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Notice upload successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Notice Upload Fail", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void addDate() {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };
}