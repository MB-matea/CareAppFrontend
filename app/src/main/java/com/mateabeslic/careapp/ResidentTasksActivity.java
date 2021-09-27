package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.GetAllTasksForSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBody;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBodyUsers;
import com.mateabeslic.careapp.api.model.ReturnId;
import com.mateabeslic.careapp.api.model.Task;
import com.mateabeslic.careapp.api.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResidentTasksActivity extends AppCompatActivity {

    private TasksApi client;
    private UsersApi clientUsers;

    private static final String TAG = "Tasks Activity";
    //var
    private ArrayList<Date> mDates = new ArrayList<>();
    private ArrayList<String> mUsers = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();

    public Integer userId;
    public String date;
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

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    private void getTasks() {

        if(client == null) {
            client = new TasksApi();
        }

        client.setBasePath(BasePath.basePath);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ResidentTasksActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_resident:
                Toast.makeText(ResidentTasksActivity.this, "Create task", Toast.LENGTH_SHORT).show();
                //Intent intent2 = new Intent(ResidentTasksActivity.this, CreateResidentActivity.class);
                //startActivity(intent2);
                ShowCustomDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowCustomDialog() {
        ArrayList<Integer> dUserIds = new ArrayList<>();
        ArrayList<String> dUserNames = new ArrayList<>();
        ArrayList<CharSequence> dUsers = new ArrayList<>();

        CreateNewTaskRequestBody createNewTaskRequestBody = new CreateNewTaskRequestBody();

        if(clientUsers == null) {
            clientUsers = new UsersApi();
        }

        clientUsers.setBasePath(BasePath.basePath);

        clientUsers.usersGet(new Response.Listener<GetAllUsersResponseBody>() {
            @Override
            public void onResponse(GetAllUsersResponseBody response) {
                List<GetAllUsersResponseBodyUsers> users = response.getUsers();

                for (GetAllUsersResponseBodyUsers user : users) {
                    dUserIds.add(user.getUserId());
                    String name = user.getName() + " " + user.getLastName();
                    dUsers.add(name);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ResidentTasksActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.login_dialog_layout, null);
        //builder.setCancelable(false);
        builder.setTitle("Novo zaduženje");
        //final View dialogView = LayoutInflater.from(ResidentTasksActivity.this).inflate(R.layout.login_dialog_layout, null);
        //builder.setView(dialogView);
        //final AlertDialog alertDialog = builder.show();

        // DATE
        EditText edtDate = (EditText) dialogView.findViewById(R.id.edt_date);
        edtDate.setInputType(InputType.TYPE_NULL);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtDate);
            }
        });

        // SPINNER
        final Spinner spnUser = (Spinner) dialogView.findViewById(R.id.spn_user);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(ResidentTasksActivity.this,
                android.R.layout.simple_spinner_item, dUsers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUser.setAdapter(adapter);

        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                date = edtDate.getText().toString();
                Integer position = spnUser.getSelectedItemPosition() + 1;
                userId = dUserIds.get(position);
                addTask();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void addTask() {

        CreateNewTaskRequestBody createNewTaskRequestBody = new CreateNewTaskRequestBody();
        createNewTaskRequestBody.setUserId(userId);
        createNewTaskRequestBody.setResidentId(residentId);

        String[] dateParsed = date.split("-");

        int year =Integer.valueOf(dateParsed[0]);
        int month =Integer.valueOf(dateParsed[1]);
        int day =Integer.valueOf(dateParsed[2]);

        if(client == null) {
            client = new TasksApi();
        }

        client.setBasePath(BasePath.basePath);
        createNewTaskRequestBody.setDate(Helper.generateDate(year, month, day));

        client.tasksPost(createNewTaskRequestBody, new Response.Listener<ReturnId>() {
            @Override
            public void onResponse(ReturnId response) {
                Toast.makeText(ResidentTasksActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void showDateDialog(EditText date) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(ResidentTasksActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}