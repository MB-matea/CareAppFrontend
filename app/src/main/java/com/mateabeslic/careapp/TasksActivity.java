package com.mateabeslic.careapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.LogInApi;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.invoker.ApiException;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.LoginRequestBody;
import com.mateabeslic.careapp.api.model.LoginResponseBody;
import com.mateabeslic.careapp.api.model.Resident;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class TasksActivity extends AppCompatActivity {

    private static ResidentsApi client;

    private static final String TAG = "TasksActivity";
    //var
    private ArrayList<Integer> mRoomNumbers = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath("http://192.168.1.4:8080");

        client.residentsGet(new Response.Listener<GetAllResidentsResponseBody>() {
            @Override
            public void onResponse(GetAllResidentsResponseBody response) {
                List<GetAllResidentsResponseBodyResidents> residents = response.getResidents();
                Toast.makeText(TasksActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: U erroru smo");
                error.printStackTrace();
            }
        });


        initData();
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
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mRoomNumbers, mNames, mCheckBoxes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
