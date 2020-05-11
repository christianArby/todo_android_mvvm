package com.example.todoandroidmvvm.presenter;

import android.content.Context;

public interface IMainPresenter {
    void onAddTodo(String text);
    void onRemoveTodo(String key);
    void onRequestData(Context context);
}
