package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditResidentActivity extends AppCompatActivity {

    private ResidentsApi client;

    Button btnAdd;
    EditText edtName, edtLastName, edtCitizenship, edtContactName, edtContactAddress, edtContactNumber,
            edtContactRelationship, edtContactEmail, edtDateOfBirth, edtPlaceOfBirth, edtIdCard, edtNote,
            edtOib, edtRoom, edtNationality, edtTherapy;
    Spinner spnIndependence, spnMobility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resident);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdd = findViewById(R.id.btn_add);

        edtName = findViewById(R.id.txt_name);
        edtLastName = findViewById(R.id.txt_last_name);
        edtNationality = findViewById(R.id.txt_nationality);
        edtCitizenship = findViewById(R.id.txt_citizenship);
        edtContactName = findViewById(R.id.txt_contact_name);
        edtContactAddress = findViewById(R.id.txt_contact_address);
        edtContactNumber = findViewById(R.id.txt_contact_number);
        edtContactEmail = findViewById(R.id.txt_contact_email);
        edtContactRelationship = findViewById(R.id.txt_contact_relationship);

        // DATE OF BIRTH
        edtDateOfBirth = findViewById(R.id.txt_date_of_birth);
        edtDateOfBirth.setInputType(InputType.TYPE_NULL);

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtDateOfBirth);
            }
        });

        edtPlaceOfBirth = findViewById(R.id.txt_place_of_birth);
        edtIdCard = findViewById(R.id.txt_id_card);
        edtNote = findViewById(R.id.txt_note);
        edtOib = findViewById(R.id.txt_oib);
        edtRoom = findViewById(R.id.txt_room);
        edtTherapy = findViewById(R.id.txt_therapy);

        spnIndependence = (Spinner) findViewById(R.id.spn_independence);
        spnMobility = (Spinner) findViewById(R.id.spn_mobility);

        Resident resident = new Resident();

        // INDEPENDENCE SPINNER
        ArrayAdapter<CharSequence> adapterIndependence = ArrayAdapter.createFromResource(this,
                R.array.independence_status, android.R.layout.simple_spinner_item);
        adapterIndependence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIndependence.setAdapter(adapterIndependence);

        spnIndependence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        resident.setIndependence(Resident.IndependenceEnum.INDEPENDENT);
                        break;
                    case 1:
                        resident.setIndependence(Resident.IndependenceEnum.NECESSARY_AID);
                        break;
                    case 2:
                        resident.setIndependence(Resident.IndependenceEnum.COMPLETELY_DEPENDENT);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // MOBILITY SPINNER
        ArrayAdapter<CharSequence> adapterMobility = ArrayAdapter.createFromResource(this,
                R.array.mobility_status, android.R.layout.simple_spinner_item);
        adapterMobility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMobility.setAdapter(adapterMobility);

        spnMobility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        resident.setMobility(Resident.MobilityEnum.MOBILE);
                        break;
                    case 1:
                        resident.setMobility(Resident.MobilityEnum.IMMOBILE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // GET INTENT EXTRAS
        Bundle bundle = getIntent().getExtras();
        Integer residentId = Integer.valueOf(bundle.get("id").toString());

        // TOOLBAR TITLE
        setTitle(bundle.get("name").toString() + " " + bundle.get("lastName").toString());

        edtName.setText(bundle.get("name").toString());
        edtLastName.setText(bundle.get("lastName").toString());
        edtNationality.setText(bundle.get("nationality").toString());
        edtCitizenship.setText(bundle.get("citizenship").toString());
        edtContactName.setText(bundle.get("contactName").toString());
        edtContactAddress.setText(bundle.get("contactAddress").toString());
        edtContactNumber.setText(bundle.get("contactNumber").toString());
        edtContactEmail.setText(bundle.get("contactEmail").toString());
        edtContactRelationship.setText(bundle.get("contactRelationship").toString());

        String mobility = bundle.get("mobility").toString();
        switch (mobility){
            case "MOBILE":
                spnMobility.setVerticalScrollbarPosition(0);
                break;
            case "IMOBILE":
                spnMobility.setVerticalScrollbarPosition(1);
                break;
        }

        String independence = bundle.get("independence").toString();
        switch (independence){
            case "INDEPENDENT":
                spnMobility.setVerticalScrollbarPosition(0);
                break;
            case "NECESSARY_AID":
                spnMobility.setVerticalScrollbarPosition(1);
                break;
            case "COMPLETELY_DEPENDENT":
                spnMobility.setVerticalScrollbarPosition(2);
                break;
        }

        // DATE OF BIRTH
        edtDateOfBirth.setText(bundle.get("dateOfBirth").toString());
        edtPlaceOfBirth.setText(bundle.get("placeOfBirth").toString());
        edtIdCard.setText(bundle.get("idCard").toString());
        edtNote.setText(bundle.get("note").toString());
        edtOib.setText(bundle.get("oib").toString());
        edtRoom.setText(bundle.get("room").toString());
        edtTherapy.setText(bundle.get("therapy").toString());


        if (client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath(BasePath.getBasePath());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resident.setName(edtName.getText().toString());
                resident.setLastName(edtLastName.getText().toString());
                resident.setCitizenship(edtCitizenship.getText().toString());
                resident.setContactAddress(edtContactAddress.getText().toString());
                resident.setContactName(edtContactName.getText().toString());
                resident.setContactNumber(edtContactNumber.getText().toString());
                resident.setContactRelationship(edtContactRelationship.getText().toString());
                resident.setContactEmail(edtContactEmail.getText().toString());

                String dateOfBirthString = edtDateOfBirth.getText().toString();
                String[] dateParsed = dateOfBirthString.split("-");

                int year =Integer.valueOf(dateParsed[0]);
                int month =Integer.valueOf(dateParsed[1]);
                int day =Integer.valueOf(dateParsed[2]);
                resident.setDateOfBirth(Helper.generateDate(year, month, day));

                resident.setPlaceOfBirth(edtPlaceOfBirth.getText().toString());
                resident.setIdCard(edtIdCard.getText().toString());
                resident.setIndependence(resident.getIndependence());
                resident.setMobility(resident.getMobility());
                resident.setNote(edtNote.getText().toString());
                resident.setOib(edtOib.getText().toString());
                resident.setRoom(Integer.valueOf(edtRoom.getText().toString()));
                resident.setNationality(edtNationality.getText().toString());

                Therapy therapy = new Therapy();
                therapy.setName(edtTherapy.getText().toString());
                therapy.setQuantity(1);

                List<Therapy> therapyList = new ArrayList<>();
                therapyList.add(therapy);


                // (PUT /residents/{residentId})
                client.residentsResidentIdPut(residentId, resident, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditResidentActivity.this, "Promjene su spremljene!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditResidentActivity.this, ResidentsActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(EditResidentActivity.this, "Unesite sve podatke!", Toast.LENGTH_LONG).show();
                        }
                        else if(error.networkResponse.statusCode == 404){
                            Toast.makeText(EditResidentActivity.this, "Korisnik ne postoji!", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(EditResidentActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDateDialog(EditText date) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(EditResidentActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}