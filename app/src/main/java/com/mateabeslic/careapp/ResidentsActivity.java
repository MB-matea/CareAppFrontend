package com.mateabeslic.careapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;

import java.util.ArrayList;
import java.util.List;

public class ResidentsActivity extends AppCompatActivity {

    private static ResidentsApi client;

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

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getResidents();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }


    public void getResidents(){
        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath(BasePath.getBasePath());

        // (GET /residents)
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
    }

    private void initRecyclerView(){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ResidentsActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_resident:
                Intent intent2 = new Intent(ResidentsActivity.this, CreateResidentActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
