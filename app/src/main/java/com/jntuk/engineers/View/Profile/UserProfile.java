package com.jntuk.engineers.View.Profile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jntuk.engineers.Interface.ItemClickListener;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Teacher.TeacherDetailsActivity;
import com.jntuk.engineers.View.User.EditProfileActivity;
import com.jntuk.engineers.View.UserDetailsActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;
import com.jntuk.engineers.databinding.ActivityUserProfileBinding;

import java.util.List;

public class UserProfile extends AppCompatActivity {

    private DatabaseReference followReference, infoRef;
    private Boolean followChecker = false;
    private String currentUserId, postKey, user_id, phone;
    public ItemClickListener listener;
    private ActivityUserProfileBinding binding;
    private SignInViewModel signInViewModel;
    private UserListViewModel userListViewModel;
    private boolean  thisUser, isUser, isProfile;
    private int followerCound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        user_id = getIntent().getStringExtra("user_id");
        currentUserId = FirebaseAuth.getInstance().getUid();

        if (user_id.equals(currentUserId)){
            binding.infoEditLayout.setVisibility(View.VISIBLE);
            binding.infoMessagelayout.setVisibility(View.INVISIBLE);
        }else {
            binding.infoMessagelayout.setVisibility(View.VISIBLE);
            binding.infoEditLayout.setVisibility(View.INVISIBLE);
        }

        initSignInViewModel();
        getUserInfo(user_id);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        binding.userPostRecyclerId.setLayoutManager(mLayoutManager);
        binding.userPostRecyclerId.setHasFixedSize(true);
        binding.userPostRecyclerId.setItemViewCacheSize(20);
        binding.userPostRecyclerId.setDrawingCacheEnabled(true);
        binding.userPostRecyclerId.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.profileEditBtn2Id.setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
        });
        binding.profileEditBtnId.setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
        });
        binding.profileReportBtnId.setOnClickListener((View view)->{
            try{
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"asifs.cse@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "report");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(),"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }catch(ActivityNotFoundException e){
                //TODO smth
            }
        });
        binding.profileMessageBtnId.setOnClickListener((View view)->{
            openWhatsApp();
        });

    }

    private void getUserInfo(String uId) {
        userListViewModel.getUserDetails(uId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                Glide.with(getApplicationContext()).load(signInUsers.get(0).getUser_image()).into(binding.profileImageId);
                binding.profileNameId.setText(signInUsers.get(0).getFull_name());
                binding.profileCollapsToolLayoutId.setTitle(signInUsers.get(0).getFull_name());
                binding.profileBioId.setText(signInUsers.get(0).getBio());
                binding.profileInstituteId.setText("Studying at "+signInUsers.get(0).getInstitute());
                binding.profileCurrentAddressId.setText("Current Address "+signInUsers.get(0).getPresent_address());
                binding.profileHomeAddressId.setText("Permanent Address "+signInUsers.get(0).getPermanent_address());
                binding.profileEmailId.setText(signInUsers.get(0).getEmail());
                binding.profileBloodId.setText("Blood Group "+signInUsers.get(0).getBlood());
                binding.profileJoinId.setText("Joined "+signInUsers.get(0).getDate());
                binding.profileWhatsappId.setText("Phone "+signInUsers.get(0).getPhone());
                phone = signInUsers.get(0).getPhone();
            }
        });
    }

    private void initSignInViewModel(){
        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
        .getInstance(this.getApplication())).get(UserListViewModel.class);
    }
    private void openWhatsApp() {
        String message="Hello! ";
        String url = "https://api.whatsapp.com/send?phone=" + "+91"+phone + "&text="+message;
        try {
            PackageManager pm = getApplicationContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(UserProfile.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //Create method appInstalledOrNot
    private boolean appInstalledOrNot(String url){
        PackageManager packageManager =getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }

}