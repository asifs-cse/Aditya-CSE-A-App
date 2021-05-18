package com.jntuk.engineers.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.UserTitle;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Profile.UserProfile;
import com.jntuk.engineers.View.Register.LoginActivity;
import com.jntuk.engineers.View.ViewHolder.UserListViewHolder;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;
import com.jntuk.engineers.databinding.ActivityUserListBinding;

import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private UserListViewModel userListViewModel;
    private ActivityUserListBinding binding;
    private UserListAdapter userListAdapter;
    private SignInViewModel signInViewModel;
    private boolean isAuth, isProfile;
    private String currentUserId;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_list);

        progressDialog = new ProgressDialog(UserListActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.userRecyclerId.setLayoutManager(manager);

        initViewModel();
        checkUser();
        seeUserList();
    }

    private void initViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(UserListViewModel.class);
    }

    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    isAuth = true;
                    currentUserId = signInUser.getuId();
                    if (signInUser.isProfile){
                        isProfile = true;
                    }
                }
            }
        });
    }

    private void seeUserList() {
        progressDialog.setTitle("Checking Students...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userListViewModel.getUserInfo();
        userListViewModel.getUserInfoLiveData.observe(this, new Observer<List<UserTitle>>() {
            @Override
            public void onChanged(List<UserTitle> userTitles) {
                userListAdapter = new UserListAdapter(getApplicationContext(), userTitles);
                binding.userRecyclerId.setAdapter(userListAdapter);
                progressDialog.dismiss();
            }
        });
    }

    public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder>{

        private Context context;
        private List<UserTitle> userTitles;

        public UserListAdapter(Context context, List<UserTitle> userTitles) {
            this.context = context;
            this.userTitles = userTitles;
        }

        @NonNull
        @Override
        public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_list_sample, parent, false);
            UserListViewHolder holder = new UserListViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
            Glide.with(getApplicationContext()).load(userTitles.get(position).getUser_image()).placeholder(R.drawable.user).into(holder.user_image);
            holder.user_name.setText(userTitles.get(position).getFull_name());
            holder.user_roll.setText(userTitles.get(position).getRoll());
            holder.user_semester.setText(userTitles.get(position).getSemester());
            holder.user_department.setText(userTitles.get(position).getDepartment());
            holder.user_institute.setText(userTitles.get(position).getInstitute());

            holder.userCard.setOnClickListener((View v)->{
                if (isAuth){
                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                    intent.putExtra("user_id",userTitles.get(position).getuId());
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Please Sign in first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

            });
        }

        @Override
        public int getItemCount() {
            return userTitles.size();
        }
    }
}