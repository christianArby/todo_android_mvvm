package com.example.todoandroidmvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoandroidmvvm.repositories.TodoRepository;

import org.json.JSONObject;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> todosLiveData;
    private TodoRepository todoRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if (todosLiveData == null){
            todoRepository = TodoRepository.getInstance();
            todosLiveData = todoRepository.getTodosLiveData();
            todoRepository.getTodosFromAPI(getApplication().getApplicationContext());
        }
    }

    public void addTodo(String key) {
        todoRepository.addTodo(key, getApplication().getApplicationContext());
    }

    public void removeTodo(String key){
        todoRepository.removeTodo(key, getApplication().getApplicationContext());
    }

    public LiveData<JSONObject> getTodosLiveData(){
        return  todosLiveData;
    }

}
