package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.LogInApi;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.LoginRequestBody;
import com.mateabeslic.careapp.api.model.LoginResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.ReturnId;
import com.mateabeslic.careapp.api.model.User;

import java.time.LocalDate;
import java.util.Date;

public class CreateResidentActivity extends AppCompatActivity {

    private ResidentsApi client;

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resident);

        btnAdd = findViewById(R.id.btn_add);

        if (client == null) {
            client = new ResidentsApi();
        }


        client.setBasePath("http://192.168.1.4:8080");
        Resident resident = new Resident();
        resident.setName("Novi");
        resident.setLastName("Rezident");
        resident.setCitizenship("hrvatsko");
        resident.setContactAddress("adresaa");
        resident.setContactName("imee");
        resident.setContactNumber("brooj");
        resident.setContactRelationship("odnos");
        resident.setContactEmail("emaiil");
        resident.setDateOfBirth(Helper.generateDate(1990, 05, 11));
        resident.setPlaceOfBirth("mjestorodenja");
        resident.setIdCard("8493593895");
        resident.setIndependence(Resident.IndependenceEnum.INDEPENDENT);
        resident.setMobility(Resident.MobilityEnum.IMMOBILE);
        resident.setNote("/");
        resident.setOib("000999");
        resident.setRoom(30);
        resident.setNationality("Hrvat");

        //resident.setUserName(username);
        //resident.setPassword(password);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.residentsPost(resident, new Response.Listener<ReturnId>() {
                    @Override
                    public void onResponse(ReturnId response) {

                        Toast.makeText(CreateResidentActivity.this, response.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(CreateResidentActivity.this, "Greška", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(CreateResidentActivity.this, "Greška2", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

}