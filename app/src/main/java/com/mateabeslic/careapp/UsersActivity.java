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
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBody;
import com.mateabeslic.careapp.api.model.GetAllUsersResponseBodyUsers;
import com.mateabeslic.careapp.api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static UsersApi client;

    private static final String TAG = "UsersActivity";
    //var
    private ArrayList<Integer> mIds = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUsers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }


    public void getUsers(){
        if(client == null) {
            client = new UsersApi();
        }

        client.setBasePath(BasePath.getBasePath());

        client.usersGet(new Response.Listener<GetAllUsersResponseBody>() {
            @Override
            public void onResponse(GetAllUsersResponseBody response) {
                List<GetAllUsersResponseBodyUsers> users = response.getUsers();

                for (GetAllUsersResponseBodyUsers user : users) {
                    mIds.add(user.getUserId());
                    String name = user.getName() + " " + user.getLastName();
                    mNames.add(name);
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
        RecyclerView recyclerView = findViewById(R.id.users_recycler_view);
        UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(UsersActivity.this, mIds, mNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));

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
                Intent intent = new Intent(UsersActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_resident:
                Intent intent2 = new Intent(UsersActivity.this, CreateUserActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}