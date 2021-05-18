package com.jntuk.engineers.View.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Profile.UserProfile;
import com.jntuk.engineers.databinding.ActivityTeacherBinding;

public class TeacherActivity extends AppCompatActivity {

    private ActivityTeacherBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_teacher);

        binding.hbsBtnId.setOnClickListener((View view)->{
            Intent intent = new Intent(getApplicationContext(), TeacherDetailsActivity.class);
            intent.putExtra("dept","HBS");
            startActivity(intent);
        });
        binding.cseBtnId.setOnClickListener((View view)->{
            Intent intent = new Intent(getApplicationContext(), TeacherDetailsActivity.class);
            intent.putExtra("dept","CSE");
            startActivity(intent);
        });

    }
}