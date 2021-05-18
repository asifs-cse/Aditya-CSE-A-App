package com.jntuk.engineers.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.jntuk.engineers.Model.Teachers;
import com.jntuk.engineers.Ripository.TeacherRepository;

import java.util.List;

public class TeacherViewModel extends AndroidViewModel {
    private TeacherRepository repository;
    public MutableLiveData<List<Teachers>> teachersLiveData;


    public TeacherViewModel(@NonNull Application application) {
        super(application);
        repository = new TeacherRepository();
    }
    public void getTeacherInfo(String dept){
        teachersLiveData = repository.teachersMutableLiveData(dept);
    }
}
