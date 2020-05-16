package com.example.todoandroidmvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.todoandroidmvvm.adapters.TodosAdapter;
import com.example.todoandroidmvvm.fragments.FirstPageFragment;
import com.example.todoandroidmvvm.interfaces.OnRemoveTodoClickedListener;
import com.example.todoandroidmvvm.viewmodels.MainActivityViewModel;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FirstPageFragment.OnFirstPageFragmentInteractionListener {

    private TodosAdapter todosAdapter;
    private FragmentManager fragmentManager;
    private EditText edt_input;
    private ImageView btn_add_todo;
    private ConstraintLayout progress_bar_container;
    private RecyclerView recyclerView;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        edt_input = (EditText)findViewById(R.id.edt_input);
        btn_add_todo = (ImageView)findViewById(R.id.btn_add);
        progress_bar_container = (ConstraintLayout)findViewById(R.id.progress_bar_container);

        fragmentManager = this.getSupportFragmentManager();

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.init();

        mainActivityViewModel.getTodosLiveData().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                if (jsonObject.length()!=0) {
                    todosAdapter.updateData(jsonObject);
                    progress_bar_container.setVisibility(View.GONE);
                } else {
                    showFirstPageFragment();
                    progress_bar_container.setVisibility(View.GONE);
                }
            }
        });


        OnRemoveTodoClickedListener onRemoveTodoClickedListener = new OnRemoveTodoClickedListener() {
            @Override
            public void onRemoveTodoClicked(String key) {
                mainActivityViewModel.removeTodo(key);
            }
        };

        btn_add_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityViewModel.addTodo(edt_input.getText().toString());
                edt_input.setText("");
                hideKeyboard(btn_add_todo);
            }
        });

        edt_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt_input.getText())) {
                        mainActivityViewModel.addTodo(edt_input.getText().toString());
                        edt_input.setText("");
                        hideKeyboard(btn_add_todo);
                    }
                }
                return false;
            }
        });

        todosAdapter = new TodosAdapter(new JSONObject(),  onRemoveTodoClickedListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(todosAdapter);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showFirstPageFragment() {
        FirstPageFragment firstPageFragment = new FirstPageFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,firstPageFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFirstTodoAdded(String todoText) {
        mainActivityViewModel.addTodo((todoText));
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();
        hideKeyboard(btn_add_todo);
    }
}