package com.mateabeslic.careapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.User;

import java.util.ArrayList;
import java.util.List;

public class ResidentsActivity extends AppCompatActivity {

    private static ResidentsApi client;

    private static final String TAG = "ResidentsActivity";
    //var
    private ArrayList<Integer> mRoomNumbers = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residents);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        Integer userId = (Integer) bundle.get("userId");
        String userName = (String) bundle.get("name");
        String userLastName = (String) bundle.get("lastName");

        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath("http://192.168.1.4:8080");

        client.residentsGet(new Response.Listener<GetAllResidentsResponseBody>() {
            @Override
            public void onResponse(GetAllResidentsResponseBody response) {
                List<GetAllResidentsResponseBodyResidents> residents = response.getResidents();

                for (GetAllResidentsResponseBodyResidents resident:residents) {
                    mRoomNumbers.add(resident.getRoom());
                    String name = resident.getName() + " " + resident.getLastName();
                    mNames.add(name);
                    mCheckBoxes.add(false);
                    mIds.add(resident.getResidentId());
                }

                initRecyclerView();

                //initData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });



    }

    private void initData() {

        Log.d(TAG, "initData: initData");
        mRoomNumbers.add(1);
        mNames.add("Ivo Ivić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(2);
        mNames.add("Marin Marić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(3);
        mNames.add("Ana Anić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(4);
        mNames.add("Toni Tonić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(5);
        mNames.add("Karla Karlić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(6);
        mNames.add("Anita Anić");
        mCheckBoxes.add(false);
        mRoomNumbers.add(1);
        mNames.add("Ivo Ivić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(2);
        mNames.add("Marin Marić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(3);
        mNames.add("Ana Anić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(4);
        mNames.add("Toni Tonić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(5);
        mNames.add("Karla Karlić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(6);
        mNames.add("Anita Anić");
        mCheckBoxes.add(false);

        Log.d(TAG, "initData: test");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initrecyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(ResidentsActivity.this, mRoomNumbers, mNames, mCheckBoxes, mIds);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResidentsActivity.this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create_resident:
                Toast.makeText(ResidentsActivity.this, "Create resident", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
