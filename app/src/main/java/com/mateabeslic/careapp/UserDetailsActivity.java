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
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.client.UsersApi;
import com.mateabeslic.careapp.api.model.GetSpecificUserResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.User;

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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        userId = (Integer) bundle.get("userId");

        if(client == null) {
            client = new UsersApi();
        }

        client.setBasePath(BasePath.basePath);

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
                //callIntentEditResident(residentPublic, therapiesPublic);
                Toast.makeText(UserDetailsActivity.this, "Edit Resident", Toast.LENGTH_LONG).show();
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
        builder1.setMessage("Jeste li sigurni da želite izbrisati djelatika?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "IZBRIŠI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteUser();
                        dialog.cancel();
                        finish();
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
                Toast.makeText(UserDetailsActivity.this, "Korisnik je izbrisan!", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserDetailsActivity.this, "GREŠKA!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}