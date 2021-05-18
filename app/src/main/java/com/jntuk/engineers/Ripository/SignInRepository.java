package com.jntuk.engineers.Ripository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.SingleUser;
import com.jntuk.engineers.Model.SliderClass;
import com.jntuk.engineers.Model.TempUser;
import com.jntuk.engineers.Model.UserTitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignInRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private SignInUser user = new SignInUser();
    private DatabaseReference userData;
    private String cUser;
    FirebaseUser currentUser;

    public MutableLiveData<SignInUser> checkAuthFirebase(){
        MutableLiveData<SignInUser> isAuthLiveData = new MutableLiveData<>();

        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser==null){
            user.isAuth = false;
            user.isProfile = false;
        }else {
            user.setuId(currentUser.getUid());
            user.isAuth = true;

            DocumentReference docRef = firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        user.isProfile = documentSnapshot.getString("uId")!=null;
                        isAuthLiveData.setValue(user);
                    }
                }
            });
        }

        return isAuthLiveData;
    }

//    checkInternet

    public MutableLiveData<SignInUser> checkInternet(Context context){
        MutableLiveData<SignInUser> checkInternetLiveData = new MutableLiveData<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true)
        {
            user.isInternet = true;
        }
        else {
            user.isInternet = false;
        }
        checkInternetLiveData.setValue(user);

        return checkInternetLiveData;
    }

    //signInWithGoogle

    public MutableLiveData<String> firebaseSignInWithGoogle(AuthCredential authCredential){
        MutableLiveData<String> signInLiveData = new MutableLiveData<>();

        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                signInLiveData.setValue("Login success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signInLiveData.setValue("SignIn fail");
            }
        });

        return signInLiveData;
    }

    //Login with email

    public MutableLiveData<String> firebaseSignInWithEmail(String email, String password){
        MutableLiveData<String> emailLoginLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    emailLoginLiveData.setValue("Login success");
                    currentUser = firebaseAuth.getCurrentUser();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                emailLoginLiveData.setValue("Email or password not validate");
            }
        });

        return emailLoginLiveData;
    }

    //firebase signup
    public MutableLiveData<String> firebaseSignUpMutableLiveData(String email, String password){
        MutableLiveData<String> firebaseSignUpLiveData = new MutableLiveData<>();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseSignUpLiveData.setValue("Sign up Success");
                }else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        firebaseSignUpLiveData.setValue("User Already Registered! Please Enter new Email");
                    }else {
                        firebaseSignUpLiveData.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                }
            }
        });
        return firebaseSignUpLiveData;
    }

    //collect user info

    public MutableLiveData<TempUser> collectUserTempData(){
        MutableLiveData<TempUser> collectTempMutableLiveData = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser!=null){
            String image;

            String uId = currentUser.getUid();
            String email = currentUser.getEmail();
            String name = currentUser.getDisplayName();
            Uri imageUri = currentUser.getPhotoUrl();
            if (imageUri==null){
                image = "https://firebasestorage.googleapis.com/v0/b/diplomaian.appspot.com/o/Admin%2FAdmin%2FTemp%20user%2Fuser.png?alt=media&token=3f268d15-3cb9-44e0-a744-427e994bf2d8";
            }else {
                image = imageUri.toString();
            }

            TempUser tempUser = new TempUser(uId, email, name, image);
            collectTempMutableLiveData.setValue(tempUser);
        }
        return collectTempMutableLiveData;
    }



    //get user data
    public MutableLiveData<List<SignInUser>> getFirebaseUserData(){
        MutableLiveData<List<SignInUser>> userInfoLiveData = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final List<SignInUser> signInUsers = new ArrayList<>();

        if (currentUser!=null){
            String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

            userData = FirebaseDatabase.getInstance().getReference().child("Users List").child(currentUserId);
            userData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    signInUsers.clear();
                    if (snapshot.child("birthday").getValue()!=null) {
                        SignInUser user = snapshot.getValue(SignInUser.class);
                        signInUsers.add(user);
                        userInfoLiveData.setValue(signInUsers);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return userInfoLiveData;
    }

    //createAccount
    public MutableLiveData<String> createOrEditAccount(SignInUser user, Uri imageUri){
        MutableLiveData<String> createAccountLiveData = new MutableLiveData<>();
        String currentUser = firebaseAuth.getCurrentUser().getUid();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calDateTime = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calDateTime.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calDateTime.getTime());

        //final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users List").child(currentUser);

        if (imageUri!=null){
            StorageReference image_path = FirebaseStorage.getInstance().getReference().child("Profile_image").child(currentUser).child(user.getuId()+".jpg");
            image_path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            // uId, first_name, last_name, full_name, roll, registration, phone, department, semester, institute, blood, email, time, date, user_image, birthday, present_address, permanent_address, bio;

                            final HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("uId", user.getuId());
                            userMap.put("first_name", user.getFirst_name());
                            userMap.put("last_name", user.getLast_name());
                            userMap.put("full_name", user.getFull_name());
                            userMap.put("roll", user.getRoll());
                            userMap.put("registration", user.getRegistration());
                            userMap.put("phone", user.getPhone());
                            userMap.put("department", user.getDepartment());
                            userMap.put("semester", user.getSemester());
                            userMap.put("institute", user.getInstitute());
                            userMap.put("blood", user.getBlood());
                            userMap.put("email", user.getEmail());
                            userMap.put("time", saveCurrentTime);
                            userMap.put("date", saveCurrentDate);
                            userMap.put("user_image", uri.toString());
                            userMap.put("birthday", user.getBirthday());
                            userMap.put("gender", user.getGender());
                            userMap.put("present_address", user.getPresent_address());
                            userMap.put("permanent_address", user.getPermanent_address());
                            userMap.put("bio", user.getBio());
                            userMap.put("rank", "stu");

                            firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails")
                                    .document(user.getuId()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    createAccountLiveData.setValue("Account Update Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    createAccountLiveData.setValue("Account Update Not Success");
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    createAccountLiveData.setValue(e.toString());
                }
            });
        }
        else {
            final HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("uId", user.getuId());
            userMap.put("first_name", user.getFirst_name());
            userMap.put("last_name", user.getLast_name());
            userMap.put("full_name", user.getFull_name());
            userMap.put("roll", user.getRoll());
            userMap.put("registration", user.getRegistration());
            userMap.put("phone", user.getPhone());
            userMap.put("department", user.getDepartment());
            userMap.put("semester", user.getSemester());
            userMap.put("institute", user.getInstitute());
            userMap.put("blood", user.getBlood());
            userMap.put("email", user.getEmail());
            userMap.put("time", saveCurrentTime);
            userMap.put("date", saveCurrentDate);
            userMap.put("user_image", user.getUser_image());
            userMap.put("birthday", user.getBirthday());
            userMap.put("gender", user.getGender());
            userMap.put("present_address", user.getPresent_address());
            userMap.put("permanent_address", user.getPermanent_address());
            userMap.put("bio", user.getBio());
            userMap.put("rank", "stu");

            firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails")
                    .document(user.getuId()).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    createAccountLiveData.setValue("Account Update Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    createAccountLiveData.setValue("Account Update Not Success");
                }
            });

        }

        return createAccountLiveData;
    }


    //create title
//    private void createStudentInfo(MutableLiveData<String> createAccountLiveData, String uId, String full_name, String roll, String institute, String department, String user_image, String semester) {
//        final HashMap<String, Object> userMap = new HashMap<>();
//        userMap.put("uId", uId);
//        userMap.put("full_name", full_name);
//        userMap.put("roll", roll);
//        userMap.put("department", department);
//        userMap.put("semester", semester);
//        userMap.put("institute", institute);
//        userMap.put("user_image", user_image);
//        userMap.put("rank", "stu");
//
//        firebaseFirestore.collection("Users_details").document("Students").collection("Students_info").document(roll).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                createAccountLiveData.setValue("Account Update Success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                createAccountLiveData.setValue("Account Update Not Success");
//            }
//        });
//    }


    //get user details
    public MutableLiveData<List<SignInUser>> userDetailsMutableLiveData(String uId){
        MutableLiveData<List<SignInUser>> userDetailsMutableLiveData = new MutableLiveData<>();

        final List<SignInUser> userList = new ArrayList<>();

        DocumentReference docRef = firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails").document(uId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String bio, rank, birthday, blood, date, department, email, first_name, full_name, institute, last_name, gender, permanent_address, phone, present_address,
                            registration, roll,semester, time, uId, user_image;
                    uId = documentSnapshot.getString("uId");
                    first_name = documentSnapshot.getString("first_name");
                    last_name = documentSnapshot.getString("last_name");
                    full_name = documentSnapshot.getString("full_name");
                    roll = documentSnapshot.getString("roll");
                    registration = documentSnapshot.getString("registration");
                    phone = documentSnapshot.getString("phone");
                    department = documentSnapshot.getString("department");
                    semester = documentSnapshot.getString("semester");
                    institute = documentSnapshot.getString("institute");
                    blood = documentSnapshot.getString("blood");
                    email = documentSnapshot.getString("email");
                    time = documentSnapshot.getString("time");
                    date = documentSnapshot.getString("date");
                    user_image = documentSnapshot.getString("user_image");
                    birthday = documentSnapshot.getString("birthday");
                    gender = documentSnapshot.getString("gender");
                    present_address = documentSnapshot.getString("present_address");
                    permanent_address = documentSnapshot.getString("permanent_address");
                    bio = documentSnapshot.getString("bio");
                    rank = documentSnapshot.getString("rank");

                    SignInUser signInUser = new SignInUser(uId,first_name,last_name,full_name,roll,registration,phone,department,semester,
                            institute,blood,email,time,date,user_image,birthday, rank ,present_address,permanent_address,bio);

                    userList.add(signInUser);
                }
                userDetailsMutableLiveData.setValue(userList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return userDetailsMutableLiveData;
    }

    //get slider image

    public MutableLiveData<List<SliderClass>> sliderMutableLiveData(){
        MutableLiveData<List<SliderClass>> sliderMutableLiveData = new MutableLiveData<>();

        List<SliderClass> sliderClassList = new ArrayList<>();

        firebaseFirestore.collection("Slider").document("Slider_image").collection("image").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                sliderClassList.clear();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    String url;
                    url = documentSnapshot.getString("url");

                    SliderClass sliderClass = new SliderClass(url);
                    sliderClassList.add(sliderClass);
                }
                sliderMutableLiveData.setValue(sliderClassList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return sliderMutableLiveData;
    }

    //get user title
    public MutableLiveData<List<UserTitle>> userTitleMutableLiveData(){
        MutableLiveData<List<UserTitle>> userTitleMutableLiveData = new MutableLiveData<>();

        final List<UserTitle> userTitles = new ArrayList<>();

        CollectionReference collectionReference = firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails");

        collectionReference
                .orderBy("roll", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userTitles.clear();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    String rank, department,full_name, institute, roll,semester, uId, user_image;
                    uId = documentSnapshot.getString("uId");
                    full_name = documentSnapshot.getString("full_name");
                    roll = documentSnapshot.getString("roll");
                    department = documentSnapshot.getString("department");
                    semester = documentSnapshot.getString("semester");
                    institute = documentSnapshot.getString("institute");
                    user_image = documentSnapshot.getString("user_image");
                    rank = documentSnapshot.getString("rank");

                    UserTitle userTitle = new UserTitle(uId,full_name,roll,department,semester,institute,user_image,rank);

                    userTitles.add(userTitle);
                }
                userTitleMutableLiveData.setValue(userTitles);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//        firebaseFirestore.collection("Users_details").document("Students").collection("StudentsDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                userTitles.clear();
//                for (DocumentSnapshot documentSnapshot: task.getResult()){
//                    String rank, department,full_name, institute, roll,semester, uId, user_image;
//                    uId = documentSnapshot.getString("uId");
//                    full_name = documentSnapshot.getString("full_name");
//                    roll = documentSnapshot.getString("roll");
//                    department = documentSnapshot.getString("department");
//                    semester = documentSnapshot.getString("semester");
//                    institute = documentSnapshot.getString("institute");
//                    user_image = documentSnapshot.getString("user_image");
//                    rank = documentSnapshot.getString("rank");
//
//                    UserTitle userTitle = new UserTitle(uId,full_name,roll,department,semester,institute,user_image,rank);
//
//                    userTitles.add(userTitle);
//                }
//                userTitleMutableLiveData.setValue(userTitles);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

        return userTitleMutableLiveData;
    }
}
