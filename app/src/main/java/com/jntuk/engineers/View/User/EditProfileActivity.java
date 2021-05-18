package com.jntuk.engineers.View.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jntuk.engineers.DatePicker.DatePickerFragment;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.TempUser;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.HomeActivity;
import com.jntuk.engineers.View.Register.LoginActivity;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;
import com.jntuk.engineers.databinding.ActivityEditProfileBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    private String select_semi,currentUserId, old_image, select_gender, select_blood, select_roll;
    private ProgressDialog progressDialog;
    private static final int GalleryPic = 1 ;
    private Uri imageUri = null;
    private StorageReference userImageRef;
    private String check_image, dateOfBirth;
    private byte[] final_image;
    private SignInViewModel signInViewModel;
    private UserListViewModel userListViewModel;
    private ActivityEditProfileBinding binding;
    private boolean signIn, isProfile;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        initSignInViewModel();
        checkAuth();

        userImageRef = FirebaseStorage.getInstance().getReference().child("Status_Images");

        progressDialog = new ProgressDialog(EditProfileActivity.this);

        List<String> semester = new ArrayList<>();
        semester.add("1st Semester");
        semester.add("2nd Semester");
        semester.add("3rd Semester");
        semester.add("4th Semester");
        semester.add("5th Semester");
        semester.add("6th Semester");
        semester.add("7th Semester");
        semester.add("8th Semester");

        List<String> roll_number = new ArrayList<>();
        roll_number.add("20MH1A0501");
        roll_number.add("20MH1A0502");
        roll_number.add("20MH1A0503");
        roll_number.add("20MH1A0504");
        roll_number.add("20MH1A0505");
        roll_number.add("20MH1A0506");
        roll_number.add("20MH1A0507");
        roll_number.add("20MH1A0508");
        roll_number.add("20MH1A0509");
        roll_number.add("20MH1A0510");
        roll_number.add("20MH1A0511");
        roll_number.add("20MH1A0512");
        roll_number.add("20MH1A0513");
        roll_number.add("20MH1A0514");
        roll_number.add("20MH1A0515");
        roll_number.add("20MH1A0516");
        roll_number.add("20MH1A0517");
        roll_number.add("20MH1A0518");
        roll_number.add("20MH1A0519");
        roll_number.add("20MH1A0520");
        roll_number.add("20MH1A0521");
        roll_number.add("20MH1A0522");
        roll_number.add("20MH1A0523");
        roll_number.add("20MH1A0524");
        roll_number.add("20MH1A0525");
        roll_number.add("20MH1A0526");
        roll_number.add("20MH1A0527");
        roll_number.add("20MH1A0528");
        roll_number.add("20MH1A0529");
        roll_number.add("20MH1A0530");
        roll_number.add("20MH1A0531");
        roll_number.add("20MH1A0532");
        roll_number.add("20MH1A0533");
        roll_number.add("20MH1A0534");
        roll_number.add("20MH1A0535");
        roll_number.add("20MH1A0536");
        roll_number.add("20MH1A0537");
        roll_number.add("20MH1A0538");
        roll_number.add("20MH1A0539");
        roll_number.add("20MH1A0540");
        roll_number.add("20MH1A0541");
        roll_number.add("20MH1A0542");
        roll_number.add("20MH1A0543");
        roll_number.add("20MH1A0544");
        roll_number.add("20MH1A0545");
        roll_number.add("20MH1A0546");
        roll_number.add("20MH1A0547");
        roll_number.add("20MH1A0548");
        roll_number.add("20MH1A0549");
        roll_number.add("20MH1A0550");
        roll_number.add("20MH1A0551");
        roll_number.add("20MH1A0552");
        roll_number.add("20MH1A0553");
        roll_number.add("20MH1A0554");
        roll_number.add("20MH1A0555");
        roll_number.add("20MH1A0556");
        roll_number.add("20MH1A0557");
        roll_number.add("20MH1A0558");
        roll_number.add("20MH1A0559");
        roll_number.add("20MH1A0560");
        roll_number.add("20MH1A0561");
        roll_number.add("20MH1A0562");
        roll_number.add("20MH1A0563");
        roll_number.add("20MH1A0564");
        roll_number.add("20MH1A0565");
        roll_number.add("20MH1A0566");


        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");

        List<String> department = new ArrayList<>();
        department.add("COMPUTER SCIENCE & ENGINEERING");
        department.add("Others");

        List<String> blood = new ArrayList<>();
        blood.add("A+");
        blood.add("O+");
        blood.add("B+");
        blood.add("AB+");
        blood.add("A-");
        blood.add("O-");
        blood.add("B-");
        blood.add("AB-");

        binding.semesterSpinnerId.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semester));
        binding.semesterSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_semi = parent.getItemAtPosition(position).toString();
                binding.semesterTVId.setText(select_semi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.rollSpinnerId.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roll_number));
        binding.rollSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_roll = parent.getItemAtPosition(position).toString();
                binding.rollTVId.setText(select_roll);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.bloodSpinnerId.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, blood));
        binding.bloodSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_blood = parent.getItemAtPosition(position).toString();
                binding.bloodTVId.setText(select_blood);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.genderSpinnerId.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender));
        binding.genderSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_gender = parent.getItemAtPosition(position).toString();
                binding.genderTVId.setText(select_gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnRegistration.setOnClickListener((View v)->{
            check();
        });
        binding.selectImgId.setOnClickListener((View v)->{
            checkPermission();
        });
        binding.dateOfBirth.setOnClickListener((View view)->{
            addDateOfBirth();
        });
    }

    private void checkAuth() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser user) {
                if (user.isAuth){
                    signIn = true;
                    currentUser();
                    if (user.isProfile){
                        isProfile =true;
                        showOldData();
                    }
                }
            }
        });
    }

    private void currentUser() {
        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = userId.getUid();
    }

    private void showOldData() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUser) {
                if (signInUser.get(0).getFirst_name()!=null){
                    binding.edtFirstNameId.setText(signInUser.get(0).getFirst_name());
                    binding.edtLastNameId.setText(signInUser.get(0).getLast_name());
                    binding.edtBioId.setText(signInUser.get(0).getBio());
                    binding.rollTVId.setText(signInUser.get(0).getRoll());
                    binding.edtRegistrationId.setText(signInUser.get(0).getRegistration());
                    binding.edtMobileId.setText(signInUser.get(0).getPhone());
                    binding.edtEmailId.setText(signInUser.get(0).getEmail());
                    binding.bloodTVId.setText(signInUser.get(0).getBlood());
                    binding.addDateOfBirthId.setText(signInUser.get(0).getBirthday());
                    binding.edtPresentAddressId.setText(signInUser.get(0).getPresent_address());
                    binding.edtPermanentAddressId.setText(signInUser.get(0).getPermanent_address());
                    binding.semesterTVId.setText(signInUser.get(0).getSemester());
                    binding.addDateOfBirthId.setText(signInUser.get(0).getBirthday());
                    Glide.with(getApplicationContext()).load(signInUser.get(0).getUser_image()).into(binding.selectImgId);

                    old_image = signInUser.get(0).getUser_image();
                }
            }
        });
    }
    private void showTempProfile() {
        signInViewModel.userTempData();
        signInViewModel.userTempLiveData.observe(this, new Observer<TempUser>() {
            @Override
            public void onChanged(TempUser tempUser) {
                if (tempUser.getuId()!=null){
                    binding.edtFirstNameId.setText(tempUser.getName());
                    binding.edtEmailId.setText(tempUser.getEmail());
                    Glide.with(getApplicationContext()).load(tempUser.getImage()).into(binding.selectImgId);
                }
            }
        });
    }

    private void initSignInViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
        .getInstance(this.getApplication())).get(UserListViewModel.class);
    }

    private void addDateOfBirth() {
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
            dateOfBirth = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year);
            binding.addDateOfBirthId.setText(dateOfBirth);
        }
    };

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {
                imagePick();
            }
        }else {
            imagePick();
        }
    }

    private void imagePick() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imageUri = result.getUri();
                File actualImage = new File(Objects.requireNonNull(imageUri.getPath()));
                try {
                    Bitmap compressedImage = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(300)
                            .setQuality(60)
                            .compressToBitmap(actualImage);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    final_image = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
            if (imageUri!=null){
                Glide.with(getApplicationContext()).load(imageUri).into(binding.selectImgId);
                check_image = imageUri.toString();
            }
        }
    }
    private void check() {
        if (!signIn){
            Toast.makeText(this, "Your aren't sign in user, Please Sign In first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }else if (TextUtils.isEmpty(binding.edtFirstNameId.getText().toString())){
            binding.wrapperFNameId.setError("Enter first name");
        }else if (TextUtils.isEmpty(binding.edtLastNameId.getText().toString())){
            binding.wrapperLNameId.setError("Enter last name");
        }else if (select_roll==null){
            binding.rollTVId.setError("Enter your roll");
            Toast.makeText(this, "Please Select Your Roll!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(binding.edtRegistrationId.getText().toString())){
            binding.wrapper3.setError("Enter your registration");
        }else if (TextUtils.isEmpty(binding.edtMobileId.getText().toString())){
            binding.edtMobileId.setText("Optional");
        }else if (TextUtils.isEmpty(binding.edtEmailId.getText().toString())) {
            binding.wrapper4.setError("Enter your gmail");
        }else if(binding.addDateOfBirthId.getText().toString().equals("Date of Birth")){
            Toast.makeText(this, "Enter your Birthday", Toast.LENGTH_SHORT).show();
        }else if (select_blood==null) {
            Toast.makeText(this, "Please Select Your Blood Group!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(binding.edtPresentAddressId.getText().toString())) {
            binding.wrapperPresentAddress.setError("Enter your present address");
        }else if (TextUtils.isEmpty(binding.edtPermanentAddressId.getText().toString())) {
            binding.wrapperPermanentAddress.setError("Enter your permanent address");
        }else if(select_semi==null){
            Toast.makeText(this, "Please Select Your Semester!", Toast.LENGTH_SHORT).show();
        }else if(check_image==null){
            if (old_image==null){
                Toast.makeText(this, "Please Select Your Profile Picture!", Toast.LENGTH_SHORT).show();
            }else {
                check_image = old_image;
                updateAccount();
            }
        }else {
            if (isProfile){
                updateAccount();
            }else {
                createAccount();
            }
        }
    }

    private void createAccount() {
        progressDialog.setTitle("Profile Creating...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String time, date;
        Calendar calDateTime = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calDateTime.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calDateTime.getTime());

        String first_name, last_name, full_name, bio, gender, presentAddress, permanentAddress, roll, regi, dept, semi, email, institute, blood, phone, bithday;

        first_name = binding.edtFirstNameId.getText().toString();
        last_name = binding.edtLastNameId.getText().toString();
        bio = binding.edtBioId.getText().toString();
        gender = select_gender;
        presentAddress = binding.edtPresentAddressId.getText().toString();
        permanentAddress = binding.edtPermanentAddressId.getText().toString();
        roll = select_roll;
        regi = binding.edtRegistrationId.getText().toString();
        dept = "COMPUTER SCIENCE & ENGINEERING";
        phone = binding.edtMobileId.getText().toString();
        email = binding.edtEmailId.getText().toString();
        semi = select_semi;
        institute = "Aditya College of Engineering";
        blood = select_blood;
        bithday = binding.addDateOfBirthId.getText().toString();
        full_name = binding.edtFirstNameId.getText().toString()+" "+binding.edtLastNameId.getText().toString();

        if (old_image==null){
            old_image = "null";
        }

        // uId, first_name, last_name, full_name, roll, registration, phone, department, semester, institute, blood, email, time, date, user_image, birthday, present_address, permanent_address, bio;

        SignInUser user = new SignInUser(currentUserId, first_name, last_name, full_name, roll, regi, phone, dept, semi, institute, blood, email, time, date, old_image, bithday, gender, presentAddress, permanentAddress, bio);
        signInViewModel.createAccount(user, imageUri);
        signInViewModel.createAccountLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                saveInfo();
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }
    private void updateAccount() {
        progressDialog.setTitle("Profile Creating...");
        progressDialog.setMessage("Please wait, while we are checking the credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String time, date;
        Calendar calDateTime = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calDateTime.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calDateTime.getTime());

        String first_name, last_name, full_name, bio, gender, presentAddress, permanentAddress, roll, regi, dept, semi, email, institute, blood, phone, bithday;

        first_name = binding.edtFirstNameId.getText().toString();
        last_name = binding.edtLastNameId.getText().toString();
        bio = binding.edtBioId.getText().toString();
        gender = binding.genderTVId.getText().toString();
        presentAddress = binding.edtPresentAddressId.getText().toString();
        permanentAddress = binding.edtPermanentAddressId.getText().toString();
        roll = binding.rollTVId.getText().toString();
        regi = binding.edtRegistrationId.getText().toString();
        dept = "COMPUTER SCIENCE & ENGINEERING";
        phone = binding.edtMobileId.getText().toString();
        email = binding.edtEmailId.getText().toString();
        semi = binding.semesterTVId.getText().toString();
        institute = "Aditya College of Engineering";
        blood = binding.bloodTVId.getText().toString();
        bithday = binding.addDateOfBirthId.getText().toString();
        full_name = binding.edtFirstNameId.getText().toString()+" "+binding.edtLastNameId.getText().toString();

        if (old_image==null){
            old_image = "null";
        }

        // uId, first_name, last_name, full_name, roll, registration, phone, department, semester, institute, blood, email, time, date, user_image, birthday, present_address, permanent_address, bio;

        SignInUser user = new SignInUser(currentUserId, first_name, last_name, full_name, roll, regi, phone, dept, semi, institute, blood, email, time, date, old_image, bithday, gender, presentAddress, permanentAddress, bio);
        signInViewModel.createAccount(user, imageUri);
        signInViewModel.createAccountLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                saveInfo();
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

    private void saveInfo() {
        SharedPreferences preferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("name", binding.edtFirstNameId.getText().toString());
        editor.putString("roll", binding.rollTVId.getText().toString());
        editor.putString("regi", binding.edtRegistrationId.getText().toString());
        editor.putString("dept", "COMPUTER SCIENCE & ENGINEERING");
        editor.putString("semi", select_semi);
        editor.putString("institute", "Aditya College of Engineering");
        editor.putString("blood", select_blood);
        editor.putString("phone", binding.edtMobileId.getText().toString());
        editor.putString("email", binding.edtEmailId.getText().toString());
        editor.putString("birthday", binding.addDateOfBirthId.getText().toString());
        editor.putString("profile_picture", check_image);

        editor.apply();
    }
}