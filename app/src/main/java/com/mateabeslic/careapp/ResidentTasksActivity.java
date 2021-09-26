package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.client.TasksApi;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.GetAllTasksForSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResidentTasksActivity extends AppCompatActivity {

    private static TasksApi client;

    private static final String TAG = "Tasks Activity";
    //var
    private ArrayList<Date> mDates = new ArrayList<>();
    private ArrayList<String> mUsers = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();

    public Integer residentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        residentId = (Integer) bundle.get("residentId");

        getTasks();

        Toast.makeText(ResidentTasksActivity.this, "u oncreate", Toast.LENGTH_SHORT).show();

        //etResidents();
    }

    private void getTasks() {

        if(client == null) {
            client = new TasksApi();
        }

        client.setBasePath("http://192.168.1.4:8080");

        client.tasksResidentResidentIdGet(residentId, new Response.Listener<GetAllTasksForSpecificResidentResponseBody>() {
            @Override
            public void onResponse(GetAllTasksForSpecificResidentResponseBody response) {
                List<Task> tasks = response.getTasks();

                for (Task task : tasks) {
                    mIds.add(task.getTaskId());
                    String userName = task.getUserName() + " " + task.getUserLastName();
                    mUsers.add(userName);
                    mCheckBoxes.add(false);
                    mDates.add(task.getDate());
                }

                initRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initrecyclerview");
        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        TasksRecyclerViewAdapter adapter = new TasksRecyclerViewAdapter(ResidentTasksActivity.this, mIds, mUsers, mCheckBoxes, mDates);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResidentTasksActivity.this));

    }

}