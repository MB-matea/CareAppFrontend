package com.mateabeslic.careapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
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
import com.mateabeslic.careapp.api.model.CreateResidentRequestBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.ReturnId;
import com.mateabeslic.careapp.api.model.Therapy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateResidentActivity extends AppCompatActivity {

    private ResidentsApi client;

    Button btnAdd;
    EditText edtName, edtLastName, edtCitizenship, edtContactName, edtContactAddress, edtContactNumber,
            edtContactRelationship, edtContactEmail, edtDateOfBirth, edtPlaceOfBirth, edtIdCard, edtNote,
            edtOib, edtRoom, edtNationality, edtTherapy;
    Spinner spnIndependence, spnMobility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resident);

        btnAdd = findViewById(R.id.btn_add);

        edtName = findViewById(R.id.txt_name);
        edtLastName = findViewById(R.id.txt_last_name);
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

        edtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDateDialog(edtDateOfBirth);
                    edtDateOfBirth.clearFocus();
                    edtPlaceOfBirth.requestFocus();
                }
            }
        });


        edtPlaceOfBirth = findViewById(R.id.txt_place_of_birth);
        edtIdCard = findViewById(R.id.txt_id_card);
        edtNote = findViewById(R.id.txt_note);
        edtOib = findViewById(R.id.txt_oib);
        edtRoom = findViewById(R.id.txt_room);
        edtTherapy = findViewById(R.id.txt_therapy);

        Resident resident = new Resident();

        // INDEPENDENCE SPINNER
        spnIndependence = (Spinner) findViewById(R.id.spn_independence);
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
        spnMobility = (Spinner) findViewById(R.id.spn_mobility);
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


        if (client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath(BasePath.getBasePath());

        CreateResidentRequestBody createResidentRequestBody = new CreateResidentRequestBody();

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

                createResidentRequestBody.setResident(resident);
                createResidentRequestBody.setTherapy(therapyList);

                // (POST /residents)
                client.residentsPost(createResidentRequestBody, new Response.Listener<ReturnId>() {
                    @Override
                    public void onResponse(ReturnId response) {

                        Toast.makeText(CreateResidentActivity.this, "Kreirali ste novog korisnika doma!", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(CreateResidentActivity.this, "Unesite sve podatke!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

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
        new DatePickerDialog(CreateResidentActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}