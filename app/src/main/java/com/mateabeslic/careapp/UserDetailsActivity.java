package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.GetSpecificUserResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;
import com.mateabeslic.careapp.api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    TextView txtName, txtLastName, txtEmail, txtAddress, txtNumber, txtUsername,
            txtPassword, txtIsAdmin;

    public Integer userId;

    private static UsersApi client;
    public User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        userId = (Integer) bundle.get("userId");

        if(client == null) {
            client = new UsersApi();
        }

        client.setBasePath(BasePath.getBasePath());

        // (GET /users/{userId})
        client.usersUserIdGet(userId, new Response.Listener<GetSpecificUserResponseBody>() {
            @Override
            public void onResponse(GetSpecificUserResponseBody response) {
                user = response.getUser();
                showUserDetails();
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


    private void showUserDetails() {
        // TOOLBAR TITLE
        setTitle(user.getName() + " " + user.getLastName());

        txtName = findViewById(R.id.txt_name);
        txtLastName = findViewById(R.id.txt_last_name);
        txtEmail = findViewById(R.id.txt_email);
        txtAddress = findViewById(R.id.txt_address);
        txtNumber = findViewById(R.id.txt_number);

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);

        txtIsAdmin = findViewById(R.id.txt_is_admin);

        // SET TEXT
        txtName.setText(txtName.getText() + user.getName());
        txtLastName.setText(txtLastName.getText() + user.getLastName());
        txtEmail.setText(txtEmail.getText() + user.getEmail());
        txtAddress.setText(txtAddress.getText() + user.getAddress());
        txtNumber.setText(txtNumber.getText() + user.getNumber());

        txtUsername.setText(txtUsername.getText() + user.getUserName());
        txtPassword.setText(txtPassword.getText() + user.getPassword());

        if(user.getIsAdmin()){
            txtIsAdmin.setText(txtIsAdmin.getText() + "Da");
        } else {
            txtIsAdmin.setText(txtIsAdmin.getText() + "Ne");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_details, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(UserDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_edit_user:
                callIntentEditUser(user);
                return true;
            case R.id.action_delete_user:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserDetailsActivity.this);
        builder1.setMessage("Jeste li sigurni da želite izbrisati djelatnika?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "IZBRIŠI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteUser();
                        dialog.cancel();
                        UserDetailsActivity.this.recreate();
                    }
                });

        builder1.setNegativeButton(
                "Odustani",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void deleteUser() {
        client.usersUserIdDelete(userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UserDetailsActivity.this, "Djelatnik je izbrisan!", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserDetailsActivity.this, "GREŠKA!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callIntentEditUser(User user) {
        Intent intent1 =  new Intent(UserDetailsActivity.this, EditUserActivity.class);

        // GENERAL DATA
        intent1.putExtra("id", user.getUserId());
        intent1.putExtra("name", user.getName());
        intent1.putExtra("lastName", user.getLastName());
        intent1.putExtra("email", user.getEmail());
        intent1.putExtra("address", user.getAddress());
        intent1.putExtra("number", user.getNumber());

        // LOGIN DATA
        intent1.putExtra("username", user.getUserName());
        intent1.putExtra("password", user.getPassword());

        if (user.getIsAdmin()){
            intent1.putExtra("isAdmin", "Da");
        } else {
            intent1.putExtra("isAdmin", "Ne");
        }

        startActivity(intent1);
    }
}