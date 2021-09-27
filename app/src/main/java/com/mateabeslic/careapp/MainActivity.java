package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mateabeslic.careapp.api.client.LogInApi;
import com.mateabeslic.careapp.api.invoker.JsonUtil;
import com.mateabeslic.careapp.api.model.LoginRequestBody;
import com.mateabeslic.careapp.api.model.LoginResponseBody;
import com.mateabeslic.careapp.api.model.User;

import java.lang.reflect.Type;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static LogInApi client;

    EditText edtUsername, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JSON DESERIALIZER
        JsonUtil.gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String[] chunks = json.getAsJsonPrimitive().getAsString().split("-");
                return Helper.generateDate(Integer.parseInt(chunks[0]), Integer.parseInt(chunks[1]), Integer.parseInt(chunks[2]));
            }
        });

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

                client.setBasePath(BasePath.basePath);

                LoginRequestBody requestBody = new LoginRequestBody();
                requestBody.setUserName(username);
                requestBody.setPassword(password);


                // (POST /login)
                client.loginPost(requestBody, new Response.Listener<LoginResponseBody>() {
                    @Override
                    public void onResponse(LoginResponseBody response) {
                        User user = new User();
                        user.setName(response.getName());
                        user.setLastName(response.getLastName());
                        user.setUserId(response.getUserId());
                        user.setIsAdmin(response.getIsAdmin());

                        Toast.makeText(MainActivity.this, "Uspješno ste se prijavili!", Toast.LENGTH_LONG).show();

                        if(user.getIsAdmin().equals(true)){
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("userId", user.getUserId());
                            intent.putExtra("name", user.getName());
                            intent.putExtra("lastName", user.getLastName());
                            MainActivity.this.startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, UserTasksActivity.class);
                            intent.putExtra("userId", user.getUserId());
                            intent.putExtra("name", user.getName());
                            intent.putExtra("lastName", user.getLastName());
                            MainActivity.this.startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(error.networkResponse.statusCode == 400) {
                            Toast.makeText(MainActivity.this, "Pogrešno korisničko ime ili lozinka!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    // Disable back button
    @Override
    public void onBackPressed() {

    }
}