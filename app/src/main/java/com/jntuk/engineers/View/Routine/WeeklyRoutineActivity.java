package com.jntuk.engineers.View.Routine;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jntuk.engineers.Model.RoutineClass;
import com.jntuk.engineers.Model.RoutineTime;
import com.jntuk.engineers.Model.Semester;
import com.jntuk.engineers.R;
import com.jntuk.engineers.ViewModel.RoutineViewModel;

import java.util.List;

public class WeeklyRoutineActivity extends AppCompatActivity {

    private TextView institute, tech, semester, time_1, time_2, time_3, time_4, time_5, time_6,time_7, sat_1, sat_2, sat_3, sat_4, sat_5, sat_6, sat_7,
        fri_1, fri_2, fri_3, fri_4, fri_5, fri_6, fri_7,
        mon_1, mon_2, mon_3, mon_4, mon_5, mon_6, mon_7,
        tue_1, tue_2, tue_3, tue_4, tue_5, tue_6, tue_7,
        wed_1, wed_2, wed_3, wed_4, wed_5, wed_6, wed_7,
        thu_1, thu_2, thu_3, thu_4, thu_5, thu_6, thu_7;
    private RoutineViewModel routineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_routine);

        initViewModel();

        institute = findViewById(R.id.r_instituteId);
        tech = findViewById(R.id.r_techId);
        semester = findViewById(R.id.r_semesterId);

        time_1 = findViewById(R.id.r_time1);
        time_2 = findViewById(R.id.r_time2);
        time_3 = findViewById(R.id.r_time3);
        time_4 = findViewById(R.id.r_time4);
        time_5 = findViewById(R.id.r_time5);
        time_6 = findViewById(R.id.r_time6);
        time_7 = findViewById(R.id.r_time7);

        mon_1 = findViewById(R.id.mon_1Id);
        mon_2 = findViewById(R.id.mon_2Id);
        mon_3 = findViewById(R.id.mon_3Id);
        mon_4 = findViewById(R.id.mon_4Id);
        mon_5 = findViewById(R.id.mon_5Id);
        mon_6 = findViewById(R.id.mon_6Id);
        mon_7 = findViewById(R.id.mon_7Id);

        tue_1 = findViewById(R.id.tues_1Id);
        tue_2 = findViewById(R.id.tues_2Id);
        tue_3 = findViewById(R.id.tues_3Id);
        tue_4 = findViewById(R.id.tues_4Id);
        tue_5 = findViewById(R.id.tues_5Id);
        tue_6 = findViewById(R.id.tues_6Id);
        tue_7 = findViewById(R.id.tues_7Id);

        wed_1 = findViewById(R.id.wed_1Id);
        wed_2 = findViewById(R.id.wed_2Id);
        wed_3 = findViewById(R.id.wed_3Id);
        wed_4 = findViewById(R.id.wed_4Id);
        wed_5 = findViewById(R.id.wed_5Id);
        wed_6 = findViewById(R.id.wed_6Id);
        wed_7 = findViewById(R.id.wed_7Id);

        thu_1 = findViewById(R.id.thur_1Id);
        thu_2 = findViewById(R.id.thur_2Id);
        thu_3 = findViewById(R.id.thur_3Id);
        thu_4 = findViewById(R.id.thur_4Id);
        thu_5 = findViewById(R.id.thur_5Id);
        thu_6 = findViewById(R.id.thur_6Id);
        thu_7 = findViewById(R.id.thur_7Id);

        fri_1 = findViewById(R.id.fri_1Id);
        fri_2 = findViewById(R.id.fri_2Id);
        fri_3 = findViewById(R.id.fri_3Id);
        fri_4 = findViewById(R.id.fri_4Id);
        fri_5 = findViewById(R.id.fri_5Id);
        fri_6 = findViewById(R.id.fri_6Id);
        fri_7 = findViewById(R.id.fri_7Id);

        sat_1 = findViewById(R.id.sata_1Id);
        sat_2 = findViewById(R.id.sata_2Id);
        sat_3 = findViewById(R.id.sata_3Id);
        sat_4 = findViewById(R.id.sata_4Id);
        sat_5 = findViewById(R.id.sata_5Id);
        sat_6 = findViewById(R.id.sata_6Id);
        sat_7 = findViewById(R.id.sata_7Id);

        showTime();
        showSemester();
        monClass("Monday");
        tuesClass("Tuesday");
        wedClass("Wednesday");
        thusClass("Thursday");
        friClass("Friday");
        sataClass("Saturday");

    }

    private void showSemester() {
        routineViewModel.getSemester();
        routineViewModel.semesterClassLiveData.observe(this, new Observer<List<Semester>>() {
            @Override
            public void onChanged(List<Semester> semesters) {
                if (!semesters.get(0).getSemester().isEmpty()){
                    semester.setText(semesters.get(0).getSemester());
                }
            }
        });
    }

    private void sataClass(String saturday) {
        routineViewModel.getRoutineClass(saturday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().isEmpty()){
                    sat_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    sat_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    sat_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    sat_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    sat_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    sat_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    sat_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }
            }
        });
    }

    private void friClass(String friday) {
        routineViewModel.getRoutineClass(friday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if(!routineClasses.get(0).getSub().isEmpty()){
                    fri_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    fri_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    fri_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    fri_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    fri_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    fri_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    fri_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }
            }
        });
    }

    private void thusClass(String thursday) {
        routineViewModel.getRoutineClass(thursday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().isEmpty()){
                    thu_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    thu_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    thu_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    thu_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    thu_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    thu_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    thu_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }
            }
        });
    }

    private void wedClass(String wednesday) {
        routineViewModel.getRoutineClass(wednesday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().isEmpty()){
                    wed_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    wed_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    wed_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    wed_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    wed_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    wed_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    wed_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }

            }
        });
    }

    private void tuesClass(String tuesday) {
        routineViewModel.getRoutineClass(tuesday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().isEmpty()){
                    tue_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    tue_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    tue_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    tue_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    tue_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    tue_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    tue_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }

            }
        });
    }

    private void monClass(String monday) {
        routineViewModel.getRoutineClass(monday);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().isEmpty()){
                    mon_1.setText(routineClasses.get(0).getSub()+"\n"+routineClasses.get(0).getTeacher()+"\n"+routineClasses.get(0).getRoom());
                    mon_2.setText(routineClasses.get(1).getSub()+"\n"+routineClasses.get(1).getTeacher()+"\n"+routineClasses.get(1).getRoom());
                    mon_3.setText(routineClasses.get(2).getSub()+"\n"+routineClasses.get(2).getTeacher()+"\n"+routineClasses.get(2).getRoom());
                    mon_4.setText(routineClasses.get(3).getSub()+"\n"+routineClasses.get(3).getTeacher()+"\n"+routineClasses.get(3).getRoom());
                    mon_5.setText(routineClasses.get(4).getSub()+"\n"+routineClasses.get(4).getTeacher()+"\n"+routineClasses.get(4).getRoom());
                    mon_6.setText(routineClasses.get(5).getSub()+"\n"+routineClasses.get(5).getTeacher()+"\n"+routineClasses.get(5).getRoom());
                    mon_7.setText(routineClasses.get(6).getSub()+"\n"+routineClasses.get(6).getTeacher()+"\n"+routineClasses.get(6).getRoom());
                }
            }
        });
    }

    private void initViewModel() {
        routineViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(RoutineViewModel.class);
    }

    private void showTime() {
        routineViewModel.getRoutineTime();
        routineViewModel.routineTimeLiveData.observe(this, new Observer<List<RoutineTime>>() {
            @Override
            public void onChanged(List<RoutineTime> routineTimes) {
                if (routineTimes.get(0).getStart_time().isEmpty()){
                    time_1.setText(routineTimes.get(0).getStart_time()+"\n"+routineTimes.get(0).getEnd_time());
                    time_2.setText(routineTimes.get(1).getStart_time()+"\n"+routineTimes.get(1).getEnd_time());
                    time_3.setText(routineTimes.get(2).getStart_time()+"\n"+routineTimes.get(2).getEnd_time());
                    time_4.setText(routineTimes.get(3).getStart_time()+"\n"+routineTimes.get(3).getEnd_time());
                    time_5.setText(routineTimes.get(4).getStart_time()+"\n"+routineTimes.get(4).getEnd_time());
                    time_6.setText(routineTimes.get(5).getStart_time()+"\n"+routineTimes.get(5).getEnd_time());
                    time_7.setText(routineTimes.get(6).getStart_time()+"\n"+routineTimes.get(6).getEnd_time());
                }

            }
        });
    }

}