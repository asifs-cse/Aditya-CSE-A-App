package com.jntuk.engineers.Ripository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.Teachers;

import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private SignInUser user = new SignInUser();
    private DatabaseReference userData;
    private String cUser;
    FirebaseUser currentUser;

    public MutableLiveData<List<Teachers>> teachersMutableLiveData(String dept){
        MutableLiveData<List<Teachers>> teachersMutableLiveData = new MutableLiveData<>();

        final List<Teachers> teachersList = new ArrayList<>();

        firebaseFirestore.collection("Users_details").document("Teachers").collection(dept).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                teachersList.clear();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    String name, image, phone, email, rank, sub, degree;

                    name = documentSnapshot.getString("name");
                    image = documentSnapshot.getString("image");
                    phone = documentSnapshot.getString("phone");
                    email = documentSnapshot.getString("email");
                    rank = documentSnapshot.getString("rank");
                    sub = documentSnapshot.getString("sub");
                    degree = documentSnapshot.getString("degree");

                    Teachers teachers = new Teachers(name, image, phone, email, rank, sub, degree);
                    teachersList.add(teachers);
                }
                teachersMutableLiveData.setValue(teachersList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return teachersMutableLiveData;
    }
}
