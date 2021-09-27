package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.mateabeslic.careapp.api.model.ReturnId;
import com.mateabeslic.careapp.api.model.User;

public class CreateUserActivity extends AppCompatActivity {

    private UsersApi client;

    Button btnAdd;
    EditText edtName, edtLastName, edtUsername, edtPassword, edtAddress, edtNumber, edtEmail;
    Spinner spnIsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        btnAdd = findViewById(R.id.btn_add);

        edtName = findViewById(R.id.txt_name);
        edtLastName = findViewById(R.id.txt_last_name);
        edtUsername = findViewById(R.id.txt_username);
        edtPassword = findViewById(R.id.txt_password);
        edtAddress = findViewById(R.id.txt_address);
        edtNumber = findViewById(R.id.txt_number);
        edtEmail = findViewById(R.id.txt_email);

        User user = new User();

        // IS_ADMIN SPINNER
        spnIsAdmin = (Spinner) findViewById(R.id.spn_is_admin);
        ArrayAdapter<CharSequence> adapterIndependence = ArrayAdapter.createFromResource(this,
                R.array.admin_status, android.R.layout.simple_spinner_item);
        adapterIndependence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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


        if (client == null) {
            client = new UsersApi();
        }


        client.setBasePath(BasePath.getBasePath());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(edtName.getText().toString());
                user.setLastName(edtLastName.getText().toString());
                user.setAddress(edtAddress.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setNumber(edtNumber.getText().toString());
                user.setUserName(edtUsername.getText().toString());
                user.setPassword(edtPassword.getText().toString());

                // (POST /users)
                client.usersPost(user, new Response.Listener<ReturnId>() {
                    @Override
                    public void onResponse(ReturnId response) {
                        Toast.makeText(CreateUserActivity.this, "Kreirali ste novog djelatnika!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(CreateUserActivity.this, "Unesite sve podatke!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}