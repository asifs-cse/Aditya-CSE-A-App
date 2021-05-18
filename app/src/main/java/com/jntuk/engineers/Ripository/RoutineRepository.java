package com.jntuk.engineers.Ripository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jntuk.engineers.Model.RoutineClass;
import com.jntuk.engineers.Model.RoutineTime;
import com.jntuk.engineers.Model.Semester;

import java.util.ArrayList;
import java.util.List;

public class RoutineRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public MutableLiveData<List<RoutineTime>> routineTimeMutableLiveData(){
        MutableLiveData<List<RoutineTime>> routineTimeLiveData = new MutableLiveData<>();

        final List<RoutineTime> routineTimes = new ArrayList<>();
        firebaseFirestore.collection("Class Routine").document("Time").collection("Class Time").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        routineTimes.clear();
                        for (DocumentSnapshot documentSnapshots: task.getResult()){
                            String s_time, e_time;
                            s_time = documentSnapshots.getString("start");
                            e_time = documentSnapshots.getString("end");

                            RoutineTime routineTime = new RoutineTime(s_time,e_time);
                            routineTimes.add(routineTime);
                        }
                        routineTimeLiveData.setValue(routineTimes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return routineTimeLiveData;
    }

    public MutableLiveData<List<RoutineClass>> routineClassMutableLiveData(String day){
        MutableLiveData<List<RoutineClass>> routineClassLiveData = new MutableLiveData<>();

        final List<RoutineClass> routineClassList = new ArrayList<>();
        firebaseFirestore.collection("Class Routine").document("Class").collection(day).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                routineClassList.clear();
                for (DocumentSnapshot snapshots: task.getResult()){
                    String sub, teacher, room;
                    sub = snapshots.getString("sub");
                    teacher = snapshots.getString("teacher");
                    room = snapshots.getString("room");

                    RoutineClass routineClass = new RoutineClass(sub, teacher, room);
                    routineClassList.add(routineClass);
                }
                routineClassLiveData.setValue(routineClassList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return routineClassLiveData;
    }

    public MutableLiveData<List<Semester>> semesterClassMutableLiveData(){
        MutableLiveData<List<Semester>> semesterClassLiveData = new MutableLiveData<>();

        final List<Semester> semestersClassList = new ArrayList<>();
        firebaseFirestore.collection("Class Routine").document("Semester").collection("c_semester").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                semestersClassList.clear();
                for (DocumentSnapshot snapshots: task.getResult()){
                    String semester;
                    semester = snapshots.getString("semester");

                    Semester semesterClass = new Semester(semester);
                    semestersClassList.add(semesterClass);
                }
                semesterClassLiveData.setValue(semestersClassList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return semesterClassLiveData;
    }
}
