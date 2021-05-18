package com.jntuk.engineers.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jntuk.engineers.Model.RoutineClass;
import com.jntuk.engineers.Model.RoutineTime;
import com.jntuk.engineers.Model.Semester;
import com.jntuk.engineers.Ripository.RoutineRepository;

import java.util.List;

public class RoutineViewModel extends AndroidViewModel {

    private RoutineRepository repository;
    public LiveData<List<RoutineTime>> routineTimeLiveData;
    public LiveData<List<RoutineClass>> routineClassLiveData;
    public LiveData<List<Semester>> semesterClassLiveData;

    public RoutineViewModel(@NonNull Application application) {
        super(application);
        repository = new RoutineRepository();
    }
    public void getRoutineTime(){
        routineTimeLiveData = repository.routineTimeMutableLiveData();
    }

    public void getRoutineClass(String day){
        routineClassLiveData = repository.routineClassMutableLiveData(day);
    }
    public void getSemester(){
        semesterClassLiveData = repository.semesterClassMutableLiveData();
    }
}
