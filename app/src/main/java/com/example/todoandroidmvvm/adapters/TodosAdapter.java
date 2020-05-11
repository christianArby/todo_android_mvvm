package com.example.todoandroidmvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroidmvvm.R;
import com.example.todoandroidmvvm.models.TodoModel;
import com.example.todoandroidmvvm.interfaces.OnRemoveTodoClickedListener;
import com.example.todoandroidmvvm.view_holders.ListViewHolder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TodosAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private JSONObject data;
    private ArrayList<Long> timestamps;
    private OnRemoveTodoClickedListener onRemoveTodoClickedListener;

    public void updateData(JSONObject data, ArrayList<Long> timestamps) {
        this.data = data;
        this.timestamps = timestamps;
        this.notifyDataSetChanged();
    }

    public TodosAdapter(JSONObject data, ArrayList<Long> timestamps, OnRemoveTodoClickedListener onRemoveTodoClickedListener) {
        this.data = data;
        this.timestamps = timestamps;
        this.onRemoveTodoClickedListener = onRemoveTodoClickedListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row_single_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Gson gson = new Gson();

        try {
            TodoModel todoModel = gson.fromJson(data.get(timestamps.get(position).toString()).toString(), TodoModel.class);
            holder.setTodoText(todoModel.getText());
            holder.setOnRemoveTodoClickListener(todoModel.getTs().toString(), onRemoveTodoClickedListener);

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return timestamps.size();
    }
}