package com.mateabeslic.careapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.client.TasksApi;
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.CreateNewTaskRequestBody;
import com.mateabeslic.careapp.api.model.GetAllTasksForSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.GetAllTasksForSpecificUserResponseBody;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBody;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBodyUsers;
import com.mateabeslic.careapp.api.model.ReturnId;
import com.mateabeslic.careapp.api.model.Task;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserTasksActivity extends AppCompatActivity {

    private TasksApi client;
    private ResidentsApi clientResidents;

    private static final String TAG = "Tasks Activity";

    //var
    private ArrayList<String> mResidents = new ArrayList<>();
    private ArrayList<Integer> mRooms = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();
    private ArrayList<Integer> mResidentIds = new ArrayList<>();

    public Integer userId;
    public String name, lastName;
    public Integer residentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        userId = (Integer) bundle.get("userId");
        name = (String) bundle.get("name");
        lastName = (String) bundle.get("lastName");

        setTitle(name.toUpperCase(Locale.ROOT) + " " + lastName.toUpperCase() + " " + "- zaduženja");

        getTasks();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    private void getTasks() {

        Date date = new Date();
        String dateString = Helper.generateString(date);
        //String dateString = date.toString();
        String[] dateParsed = dateString.split("-");

        int year =Integer.valueOf(dateParsed[0]);
        int month =Integer.valueOf(dateParsed[1]);
        int day =Integer.valueOf(dateParsed[2]);

        if(client == null) {
            client = new TasksApi();
        }

        Log.d(TAG, "getTasks: Date:" + dateString);

        client.setBasePath(BasePath.getBasePath());

        client.tasksUserIdGet(userId, Helper.generateDate(year, month, day), new Response.Listener<GetAllTasksForSpecificUserResponseBody>() {
            @Override
            public void onResponse(GetAllTasksForSpecificUserResponseBody response) {
                List<Task> tasks = response.getTasks();

                for (Task task : tasks) {
                    mIds.add(task.getTaskId());
                    String residentName = task.getResidentName() + " " + task.getResidentLastName();
                    mResidents.add(residentName);
                    mResidentIds.add(task.getResidentId());
                    mRooms.add(task.getRoom());

                    if(task.getIsDone()){
                        mCheckBoxes.add(true);
                    } else {
                        mCheckBoxes.add(false);
                    }
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
        RecyclerView recyclerView = findViewById(R.id.usertasks_recycler_view);
        UserTasksRecyclerViewAdapter adapter = new UserTasksRecyclerViewAdapter(UserTasksActivity.this, userId, name, lastName, mIds, mResidents, mResidentIds, mCheckBoxes, mRooms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserTasksActivity.this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_tasks, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_sign_out:
                Intent intent = new Intent(UserTasksActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}