package com.jntuk.engineers.View.BotomSheet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Register.LoginActivity;
import com.jntuk.engineers.View.User.EditProfileActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;

import java.util.List;


public class ShowProfile extends BottomSheetDialogFragment {

    private Button p_editBtn, p_addBtn;
    private TextView userName, userRoll;
    private UserListViewModel userListViewModel;
    private SignInViewModel signInViewModel;
    private Boolean isAuth;
    private String currentUserId;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.show_profile_sample, null);
        dialog.setContentView(view);

        initViewModel();
        currentUser();
        checkUser();

        userName = view.findViewById(R.id.btm_userNameId);
        userRoll = view.findViewById(R.id.btm_userRollId);
        p_addBtn = view.findViewById(R.id.add_profileId);
        p_editBtn = view.findViewById(R.id.edit_profileId);
        p_editBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getContext(), EditProfileActivity.class));
        });
        p_addBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return dialog;
    }

    private void currentUser() {
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null){
            currentUserId = User.getUid();
        }else
            currentUserId = "new";
    }

    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    isAuth = true;
                    p_editBtn.setVisibility(View.VISIBLE);
                    if (signInUser.isProfile){
                        p_addBtn.setVisibility(View.GONE);
                        showUserData();
                    }
                }
            }
        });
    }

    private void showUserData() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                userName.setText(signInUsers.get(0).getFull_name());
                userRoll.setText(signInUsers.get(0).getRoll());
            }
        });
    }

    private void initViewModel() {
        signInViewModel = new  ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(SignInViewModel.class);

        userListViewModel = new  ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(UserListViewModel.class);
    }
}