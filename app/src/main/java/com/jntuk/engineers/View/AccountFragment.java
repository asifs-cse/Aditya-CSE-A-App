package com.jntuk.engineers.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.TempUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Admin.AdminActivity;
import com.jntuk.engineers.View.Profile.UserProfile;
import com.jntuk.engineers.View.Register.LoginActivity;
import com.jntuk.engineers.View.Routine.DailyRoutineActivity;
import com.jntuk.engineers.View.Teacher.TeacherActivity;
import com.jntuk.engineers.View.User.EditProfileActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    private SignInViewModel signInViewModel;

    private String roll, user_name, department, user_mail, currentUserId, picture, email;
    private TextView name, dept, gmail;
    private RelativeLayout userSignup, admin;
    //private AdView mAdView;
    private CardView register_btn, profile, go_students_card;
    private CircleImageView profile_picture;
    private boolean isAuth, isProfile, isInternet;
    private CardView usersignIn;

    private SwitchCompat mode_switch, notify_switch;

    private UserListViewModel userListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_account, container, false);


        initSignInViewModel();
        checkUser();


        profile = root.findViewById(R.id.go_profile_cardId);
        go_students_card = root.findViewById(R.id.student_list_card);

        SharedPreferences login_pre = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        email = login_pre.getString("email","");

        name = root.findViewById(R.id.info_name);
        dept = root.findViewById(R.id.info_tech);
        gmail = root.findViewById(R.id.info_gmail);
        admin = root.findViewById(R.id.admin_panel);
        profile_picture = root.findViewById(R.id.account_pictureId);
        usersignIn = root.findViewById(R.id.user_signin_card);

        profile.setOnClickListener((View view)->{
            if (!isAuth){
                startActivity(new Intent(getContext(), LoginActivity.class));
            }else if (!isProfile){
                startActivity(new Intent(getContext(), EditProfileActivity.class));
                Toast.makeText(getActivity(), "Your profile isn't complete, Please complete your profile", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getActivity(), UserProfile.class);
                intent.putExtra("user_id",currentUserId);
                startActivity(intent);
            }
        });

        root.findViewById(R.id.user_details_card).setOnClickListener((View view)->{
            if (!isAuth){
                startActivity(new Intent(getContext(), LoginActivity.class));
            }else if (!isProfile){
                startActivity(new Intent(getContext(), EditProfileActivity.class));
                Toast.makeText(getActivity(), "Your profile isn't complete, Please complete your profile", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(getContext(),UserDetailsActivity.class));
            }
        });
        go_students_card.setOnClickListener((View view)->{
            startActivity(new Intent(getContext(),UserListActivity.class));
        });
        root.findViewById(R.id.time_table_card).setOnClickListener((View view)->{
            startActivity(new Intent(getContext(), DailyRoutineActivity.class));
        });
        root.findViewById(R.id.teacher_list_card).setOnClickListener((View view)->{
            startActivity(new Intent(getContext(), TeacherActivity.class));
        });
        root.findViewById(R.id.material_up_card).setOnClickListener((View view)->{
            if (!isAuth){
                startActivity(new Intent(getContext(),LoginActivity.class));
            }else {
                startActivity(new Intent(getContext(), UploadActivity.class));
            }
        });
        usersignIn.setOnClickListener((View view)->{
            startActivity(new Intent(getContext(),LoginActivity.class));
        });
        admin.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), AdminActivity.class));
        });

        return root;
    }

    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(getActivity(), new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    usersignIn.setVisibility(View.INVISIBLE);
                    isAuth = true;
                    currentUserId = signInUser.getuId();
                    if (signInUser.isProfile){
                        isProfile = true;
                        showProfile();
                    }
                    if (currentUserId.equals("HD9EmOZBY8gOVoJXZMrcRizYv7J2")){
                        admin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    private void showProfile() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                name.setText(signInUsers.get(0).getFull_name());
                dept.setText(signInUsers.get(0).getDepartment());
                gmail.setText(signInUsers.get(0).getEmail());
                Glide.with(getActivity()).load(signInUsers.get(0).getUser_image()).placeholder(R.drawable.user).into(profile_picture);
            }
        });
    }
    private void initSignInViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(UserListViewModel.class);
    }

    private void restartApp() {
        Intent i = new Intent(getContext(),HomeActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}
