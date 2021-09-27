package com.mateabeslic.careapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.User;

public class EditUserActivity extends AppCompatActivity {

    private static UsersApi client;

    Button btnAdd;
    EditText edtName, edtLastName, edtEmail, edtAddress, edtNumber, edtUsername, edtPassword;
    Spinner spnIsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdd = findViewById(R.id.btn_add);

        edtName = findViewById(R.id.txt_name);
        edtLastName = findViewById(R.id.txt_last_name);
        edtEmail = findViewById(R.id.txt_email);
        edtAddress = findViewById(R.id.txt_address);
        edtNumber = findViewById(R.id.txt_number);
        edtUsername = findViewById(R.id.txt_username);
        edtPassword = findViewById(R.id.txt_password);

        User user = new User();

        // IS_ADMIN SPINNER
        ArrayAdapter<CharSequence> adapterIndependence = ArrayAdapter.createFromResource(this,
                R.array.admin_status, android.R.layout.simple_spinner_item);
        adapterIndependence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnIsAdmin = (Spinner) findViewById(R.id.spn_is_admin);
        spnIsAdmin.setAdapter(adapterIndependence);

        spnIsAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        user.setIsAdmin(true);
                        break;
                    case 1:
                        user.setIsAdmin(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // GET INTENT EXTRAS
        Bundle bundle = getIntent().getExtras();
        Integer userId = Integer.valueOf(bundle.get("id").toString());

        // TOOLBAR TITLE
        setTitle(bundle.get("name").toString() + " " + bundle.get("lastName").toString());

        edtName.setText(bundle.get("name").toString());
        edtLastName.setText(bundle.get("lastName").toString());
        edtEmail.setText(bundle.get("email").toString());
        edtAddress.setText(bundle.get("address").toString());
        edtNumber.setText(bundle.get("number").toString());
        edtUsername.setText(bundle.get("username").toString());
        edtPassword.setText(bundle.get("password").toString());

        String isAdmin = bundle.get("isAdmin").toString();
        Log.d(TAG, "onCreate: ISADMIN"+ isAdmin);
        switch (isAdmin){
            case "Da":
                spnIsAdmin.setSelection(0);
                break;
            case "Ne":
                spnIsAdmin.setSelection(1);
                break;
        }


        if (client == null) {
            client = new UsersApi();
        }

        client.setBasePath(BasePath.getBasePath());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(edtName.getText().toString());
                user.setLastName(edtLastName.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setAddress(edtAddress.getText().toString());
                user.setNumber(edtNumber.getText().toString());
                user.setUserName(edtUsername.getText().toString());
                user.setPassword(edtPassword.getText().toString());

                client.usersUserIdPut(userId, user, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditUserActivity.this, "Promjene su spremljene!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(EditUserActivity.this, "Unesite sve podatke!", Toast.LENGTH_LONG).show();
                        }
                        else if (error.networkResponse.statusCode == 404) {
                            Toast.makeText(EditUserActivity.this, "Djelatnik ne postoji!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(EditUserActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}