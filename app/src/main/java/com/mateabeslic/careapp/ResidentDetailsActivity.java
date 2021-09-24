package com.mateabeslic.careapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.GetSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;
import com.mateabeslic.careapp.api.model.TherapyPlan;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResidentDetailsActivity extends AppCompatActivity  {

    private static final String TAG = "Date";
    Spinner spinner;
    public static String idString;

    private  static ResidentsApi client;


    GeneralDataResidentsFragment generalDataResidentsFragment;
    ContactPersonResidentsFragment contactPersonResidentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.spinner);

        generalDataResidentsFragment = new GeneralDataResidentsFragment();

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        Integer residentId = (Integer) bundle.get("residentId");
        idString = String.valueOf(residentId);


        //Toast.makeText(ResidentDetailsActivity.this, residentId.toString(), Toast.LENGTH_LONG).show();


        // endpoint GET resident
        Calendar calendar = Calendar.getInstance();
        Date date2 = calendar.getTime();
        String dateStr = date2.toString();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        ZonedDateTime dateZoned = format.parse(dateStr, ZonedDateTime::from);
        java.util.Date date = java.util.Date.from( dateZoned.toInstant() );

        Log.d(TAG, "onCreateView: Date: " + date.toString());

        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath("http://192.168.1.4:8080");
        client.residentsResidentIdGet(residentId, Helper.generateDate(2021,9, 23), new Response.Listener<GetSpecificResidentResponseBody>() {
            @Override
            public void onResponse(GetSpecificResidentResponseBody response) {
                //Toast.makeText(ResidentDetailsActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Resident resident = response.getResident();
                List<Therapy> therapies = response.getTherapies();
                List<TherapyPlan> therapyPlans = response.getTherapyPlans();

                setTitle(resident.getName() + " " + resident.getLastName());


                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ResidentDetailsActivity.this,
                        R.array.residents_info, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                sendDataGeneralDataFragment(resident);
                                break;
                            case 1:
                                sendDataContactPersonFragment(resident);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    public void setFragment (Fragment fragment, Resident resident){


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
                Intent intent = new Intent(ResidentDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_resident:
                Toast.makeText(ResidentDetailsActivity.this, "Create resident", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDataGeneralDataFragment(Resident resident) {

        String dateOfBirthString = Helper.generateString(resident.getDateOfBirth());

        Bundle bundle = new Bundle();
        bundle.putInt("residentId", Integer.valueOf(idString));
        bundle.putString("name", resident.getName());
        bundle.putString("lastName", resident.getLastName());
        bundle.putInt("room", resident.getRoom());
        bundle.putString("oib", resident.getOib());
        bundle.putString("nationality", resident.getNationality());
        bundle.putString("citizenship", resident.getCitizenship());
        bundle.putString("dateOfBirth", dateOfBirthString);
        bundle.putString("placeOfBirth", resident.getPlaceOfBirth());
        bundle.putString("idCard", resident.getIdCard());

        GeneralDataResidentsFragment fragment = new GeneralDataResidentsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    private void sendDataContactPersonFragment(Resident resident) {

        Bundle bundle = new Bundle();
        bundle.putString("contactName", resident.getContactName());
        bundle.putString("contactRelationship", resident.getContactRelationship());
        bundle.putString("contactNumber", resident.getContactNumber());
        bundle.putString("contactEmail", resident.getContactEmail());
        bundle.putString("contactAddress", resident.getContactAddress());

        ContactPersonResidentsFragment fragment = new ContactPersonResidentsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
