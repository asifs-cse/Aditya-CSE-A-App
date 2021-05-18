package com.jntuk.engineers.View.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jntuk.engineers.Model.Teachers;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.ViewHolder.TeacherViewHolder;
import com.jntuk.engineers.ViewModel.TeacherViewModel;
import com.jntuk.engineers.databinding.ActivityTeacherDetailsBinding;

import java.util.List;

public class TeacherDetailsActivity extends AppCompatActivity {

    private ActivityTeacherDetailsBinding binding;
    private TeacherViewModel teacherViewModel;
    private String dept;
    private TeacherAdapter teacherAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_teacher_details);

        initViewModel();
        progressDialog = new ProgressDialog(TeacherDetailsActivity.this);

        dept = getIntent().getStringExtra("dept");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.teacherRecyclerId.setLayoutManager(manager);

        binding.teacherDept.setText(dept+" Department");

        teacherList(dept);
    }

    private void openWhatsApp(String number) {
        String message="Hello! ";
        String url = "https://api.whatsapp.com/send?phone=" + "+91"+number + "&text="+message;
        try {
            PackageManager pm = getApplicationContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(TeacherDetailsActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void teacherList(String dept) {

        progressDialog.setTitle("Check Teachers...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        teacherViewModel.getTeacherInfo(dept);
        teacherViewModel.teachersLiveData.observe(this, new Observer<List<Teachers>>() {
            @Override
            public void onChanged(List<Teachers> teachers) {
                teacherAdapter = new TeacherAdapter(getApplicationContext(), teachers);
                binding.teacherRecyclerId.setAdapter(teacherAdapter);
                progressDialog.dismiss();
            }
        });
    }

    private void initViewModel() {
        teacherViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
        .getInstance(this.getApplication())).get(TeacherViewModel.class);
    }
    public class TeacherAdapter extends RecyclerView.Adapter<TeacherViewHolder>{

        private Context context;
        private List<Teachers> teachersList;

        public TeacherAdapter(Context context, List<Teachers> teachersList) {
            this.context = context;
            this.teachersList = teachersList;
        }

        @NonNull
        @Override
        public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.teacher_list_sample, parent, false);
            TeacherViewHolder holder = new TeacherViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
            holder.teacher_name.setText(teachersList.get(position).getName());
            holder.teacher_sub.setText(teachersList.get(position).getSub());
            holder.teacher_rank.setText(teachersList.get(position).getRank());
            holder.teacher_degree.setText(teachersList.get(position).getDegree());

            String number, gmail;
            number = teachersList.get(position).getPhone();
            gmail = teachersList.get(position).getEmail();

            Glide.with(getApplicationContext()).load(teachersList.get(position).getImage()).into(holder.teacher_image);

            holder.teacher_email.setOnClickListener((View view)->{
                try{
                    Intent intent = new Intent (Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{gmail});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Help");
                    intent.setPackage("com.google.android.gm");
                    if (intent.resolveActivity(getPackageManager())!=null)
                        startActivity(intent);
                    else
                        Toast.makeText(getApplicationContext(),"Gmail App is not installed", Toast.LENGTH_SHORT).show();
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }
            });
            holder.teacher_wap.setOnClickListener((View view)->{
                openWhatsApp(number);
            });
        }

        @Override
        public int getItemCount() {
            return teachersList.size();
        }
    }
}