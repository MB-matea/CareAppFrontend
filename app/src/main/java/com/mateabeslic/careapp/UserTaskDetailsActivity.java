package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.client.TasksApi;
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.GetSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.GetSpecificUserResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;
import com.mateabeslic.careapp.api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserTaskDetailsActivity extends AppCompatActivity {

    TextView txtName, txtLastName, txtRoom, txtMobility, txtIndependence, txtNote, txtTherapy;
    Button btnDone;

    public Integer residentId;
    public Integer taskId;
    public Boolean taskIsDone;
    public Integer userId;
    public String userName, userLastName;

    private static ResidentsApi client;
    private static TasksApi clientTasks;
    public Resident resident = new Resident();
    public List<Therapy> therapies = new ArrayList<>();
    public Therapy therapy = new Therapy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        residentId = (Integer) bundle.get("residentId");
        taskId = (Integer) bundle.get("taskId");
        taskIsDone = (Boolean) bundle.get("taskIsDone");
        userId = (Integer) bundle.get("userId");
        userName = (String) bundle.get("name");
        userLastName = (String) bundle.get("lastName");

        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath(BasePath.getBasePath());

        client.residentsResidentIdGet(residentId, Helper.generateDate(2021, 1, 11), new Response.Listener<GetSpecificResidentResponseBody>() {
            @Override
            public void onResponse(GetSpecificResidentResponseBody response) {
                resident = response.getResident();
                therapies = response.getTherapies();
                therapy = therapies.get(0);

                showResidentDetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }


    private void showResidentDetails() {
        setTitle(resident.getName() + " " + resident.getLastName() + " - " + resident.getRoom());
        txtName = findViewById(R.id.txt_name);
        txtLastName = findViewById(R.id.txt_last_name);
        txtRoom = findViewById(R.id.txt_room);
        txtMobility = findViewById(R.id.txt_mobility);
        txtIndependence = findViewById(R.id.txt_independence);
        txtNote = findViewById(R.id.txt_note);
        txtTherapy = findViewById(R.id.txt_therapy);

        // SET TEXT
        txtName.setText(txtName.getText() + resident.getName());
        txtLastName.setText(txtLastName.getText() + resident.getLastName());
        txtRoom.setText(txtRoom.getText() + resident.getRoom().toString());

        String mobility = resident.getMobility().toString();
        String independence = resident.getIndependence().toString();

        switch (mobility){
            case "MOBILE":
                txtMobility.setText(txtMobility.getText() + "pokretan");
                break;
            case "IMMOBILE":
                txtMobility.setText(txtMobility.getText() + "nepokretan");
                break;
        }

        switch (independence){
            case "INDEPENDENT":
                txtIndependence.setText(txtIndependence.getText() + "samostalan");
                break;
            case "NECESSARY_AID":
                txtIndependence.setText(txtIndependence.getText() + "potrebno pomagalo");
                break;
            case "COMPLETELY_DEPENDENT":
                txtIndependence.setText(txtIndependence.getText() + "potpuno ovisan");
                break;
        }

        txtNote.setText(txtNote.getText() + resident.getNote());
        txtTherapy.setText(txtTherapy.getText() + therapy.getName());


        // BUTTON DONE
        btnDone = findViewById(R.id.btn_done);

        if(taskIsDone){
            btnDone.setEnabled(false);
            btnDone.setBackgroundColor(Color.GRAY);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDone();
            }
        });

    }

    private void taskDone() {
        if(clientTasks == null) {
            clientTasks = new TasksApi();
        }

        clientTasks.setBasePath(BasePath.getBasePath());

        clientTasks.tasksTaskIdPut(taskId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UserTaskDetailsActivity.this, "Obavljeno!", Toast.LENGTH_SHORT).show();
                btnDone.setEnabled(false);
                btnDone.setBackgroundColor(Color.GRAY);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(UserTaskDetailsActivity.this, UserTasksActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("name", userName);
                intent.putExtra("lastName", userLastName);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}