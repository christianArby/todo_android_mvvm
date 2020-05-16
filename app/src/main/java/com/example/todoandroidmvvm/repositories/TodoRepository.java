package com.example.todoandroidmvvm.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TodoRepository {

    // Singleton patter
    private static TodoRepository instance;
    private MutableLiveData<JSONObject> todosLiveData = new MutableLiveData<>();

    public static TodoRepository getInstance() {
        if (instance == null){
            instance = new TodoRepository();
        }
        return instance;
    }

    public void getTodosFromAPI(Context ctx){

        RequestQueue queue;
        queue = Volley.newRequestQueue(ctx);
        String url ="https://us-central1-foxmike-test.cloudfunctions.net/todoDb/getTodo";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            todosLiveData.setValue(new JSONObject(response));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            todosLiveData.setValue(new JSONObject());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void removeTodo(String key, Context ctx) {
        JSONObject todos= todosLiveData.getValue();
        todos.remove(key);
        todosLiveData.postValue(todos);

        RequestQueue queue;

        queue = Volley.newRequestQueue(ctx);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ts", key);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://us-central1-foxmike-test.cloudfunctions.net/todoDb/removeTodo", new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
            }
        });
        queue.add(jsonObjReq);

    }

    public void addTodo(String text, Context ctx) {
        long timestamp = new Date().getTime();
        JSONObject todo = new JSONObject();
        JSONObject todos= todosLiveData.getValue();
        try {
            todo.put("text", text);
            todo.put("completed", false);
            todo.put("ts", timestamp);
            todos.put(Long.toString(timestamp), todo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        todosLiveData.postValue(todos);

        RequestQueue queue;
        queue = Volley.newRequestQueue(ctx);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ts", timestamp);
        params.put("text", text);
        params.put("completed", false);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://us-central1-foxmike-test.cloudfunctions.net/todoDb/addTodo", new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPost", response.toString());
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        queue.add(jsonObjReq);
    }

    public MutableLiveData<JSONObject> getTodosLiveData(){
        return todosLiveData;
    }
}
