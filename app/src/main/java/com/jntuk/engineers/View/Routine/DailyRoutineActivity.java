package com.jntuk.engineers.View.Routine;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jntuk.engineers.Model.RoutineClass;
import com.jntuk.engineers.Model.RoutineTime;
import com.jntuk.engineers.R;
import com.jntuk.engineers.Ripository.RoutineRepository;
import com.jntuk.engineers.ViewModel.RoutineViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyRoutineActivity extends AppCompatActivity {

    private TextView day,time, institute, sub_1, sub_2, sub_3, sub_4, sub_5, sub_6, sub_7,
                          class_r_1, class_r_2, class_r_3, class_r_4, class_r_5, class_r_6, class_r_7,
                          class_t_1, class_t_2, class_t_3, class_t_4, class_t_5, class_t_6, class_t_7,
                          time_1, time_2,time_3,time_4,time_5,time_6,time_7;
    private String currentdate;
    private RoutineViewModel routineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_routine);

        initViewModel();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        currentdate = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        day = findViewById(R.id.routine_date);
        //time = findViewById(R.id.dr_currentTimeId);
        day.setText("Today: "+currentdate);
        //time.setText(currentTime);
        routineClass(currentdate);

        institute = findViewById(R.id.dr_routine_inst);

        time_1 = findViewById(R.id.dr_time1);
        time_2 = findViewById(R.id.dr_time2);
        time_3 = findViewById(R.id.dr_time3);
        time_4 = findViewById(R.id.dr_time4);
        time_5 = findViewById(R.id.dr_time5);
        time_6 = findViewById(R.id.dr_time6);
        time_7 = findViewById(R.id.dr_time7);

        sub_1 = findViewById(R.id.dr_s_no1);
        sub_2 = findViewById(R.id.dr_s_no2);
        sub_3 = findViewById(R.id.dr_s_no3);
        sub_4 = findViewById(R.id.dr_s_no4);
        sub_5 = findViewById(R.id.dr_s_no5);
        sub_6 = findViewById(R.id.dr_s_no6);
        sub_7 = findViewById(R.id.dr_s_no7);

        class_r_1 = findViewById(R.id.dr_r_no1);
        class_r_2 = findViewById(R.id.dr_r_no2);
        class_r_3 = findViewById(R.id.dr_r_no3);
        class_r_4 = findViewById(R.id.dr_r_no4);
        class_r_5 = findViewById(R.id.dr_r_no5);
        class_r_6 = findViewById(R.id.dr_r_no6);
        class_r_7 = findViewById(R.id.dr_r_no7);

        class_t_1 = findViewById(R.id.dr_t_no1);
        class_t_2 = findViewById(R.id.dr_t_no2);
        class_t_3 = findViewById(R.id.dr_t_no3);
        class_t_4 = findViewById(R.id.dr_t_no4);
        class_t_5 = findViewById(R.id.dr_t_no5);
        class_t_6 = findViewById(R.id.dr_t_no6);
        class_t_7 = findViewById(R.id.dr_t_no7);

        showTime();

        findViewById(R.id.routine_weeklyId).setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(), WeeklyRoutineActivity.class));
        });
        findViewById(R.id.routine_back).setOnClickListener((View view)->{
            finish();
        });
    }

    private void routineClass(String currentdate) {
        routineViewModel.getRoutineClass(currentdate);
        routineViewModel.routineClassLiveData.observe(this, new Observer<List<RoutineClass>>() {
            @Override
            public void onChanged(List<RoutineClass> routineClasses) {
                if (!routineClasses.get(0).getSub().equals(null)){
                    sub_1.setText(routineClasses.get(0).getSub());
                    sub_2.setText(routineClasses.get(1).getSub());
                    sub_3.setText(routineClasses.get(2).getSub());
                    sub_4.setText(routineClasses.get(3).getSub());
                    sub_5.setText(routineClasses.get(4).getSub());
                    sub_6.setText(routineClasses.get(5).getSub());
                    sub_7.setText(routineClasses.get(6).getSub());

                    class_r_1.setText(routineClasses.get(0).getRoom());
                    class_r_2.setText(routineClasses.get(1).getRoom());
                    class_r_3.setText(routineClasses.get(2).getRoom());
                    class_r_4.setText(routineClasses.get(3).getRoom());
                    class_r_5.setText(routineClasses.get(4).getRoom());
                    class_r_6.setText(routineClasses.get(5).getRoom());
                    class_r_7.setText(routineClasses.get(6).getRoom());

                    class_t_1.setText(routineClasses.get(0).getTeacher());
                    class_t_2.setText(routineClasses.get(1).getTeacher());
                    class_t_3.setText(routineClasses.get(2).getTeacher());
                    class_t_4.setText(routineClasses.get(3).getTeacher());
                    class_t_5.setText(routineClasses.get(4).getTeacher());
                    class_t_6.setText(routineClasses.get(5).getTeacher());
                    class_t_7.setText(routineClasses.get(6).getTeacher());
                }
            }
        });
    }

    private void showTime() {
        routineViewModel.getRoutineTime();
        routineViewModel.routineTimeLiveData.observe(this, new Observer<List<RoutineTime>>() {
            @Override
            public void onChanged(List<RoutineTime> routineTimes) {
                if (!routineTimes.get(0).getStart_time().equals(null)){
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

    private void initViewModel() {
        routineViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.
                getInstance(this.getApplication())).get(RoutineViewModel.class);
    }
}