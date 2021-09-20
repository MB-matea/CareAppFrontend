package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.LogInApi;
import com.mateabeslic.careapp.api.model.LoginRequestBody;
import com.mateabeslic.careapp.api.model.LoginResponseBody;

public class MainActivity extends AppCompatActivity {

    private static LogInApi client;

    EditText edtUsername, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(client == null) {
            client = new LogInApi();
        }

        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                client.setBasePath("http://192.168.1.4:8080");
                LoginRequestBody requestBody = new LoginRequestBody();
                requestBody.setUserName(username);
                requestBody.setPassword(password);

                client.loginPost(requestBody, new Response.Listener<LoginResponseBody>() {
                    @Override
                    public void onResponse(LoginResponseBody response) {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(error.networkResponse.statusCode == 400) {
                            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}