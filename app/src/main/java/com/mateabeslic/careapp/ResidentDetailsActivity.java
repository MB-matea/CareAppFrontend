package com.mateabeslic.careapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.GetSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResidentDetailsActivity extends AppCompatActivity  {

    Spinner spinner;
    public static String idString;

    private  static ResidentsApi client;
    public Resident residentPublic = new Resident();
    public List<Therapy> therapiesPublic = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_home3_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.spinner);

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        Integer residentId = (Integer) bundle.get("residentId");
        idString = String.valueOf(residentId);


        // DATE parameter -> residentResidentIdGet
        Date date = new Date();
        String dateString = Helper.generateString(date);
        String[] dateParsed = dateString.split("-");

        int year =Integer.valueOf(dateParsed[0]);
        int month =Integer.valueOf(dateParsed[1]);
        int day =Integer.valueOf(dateParsed[2]);

        if(client == null) {
            client = new ResidentsApi();
        }

        client.setBasePath(BasePath.getBasePath());

        // (GET /residents/{residentId})
        client.residentsResidentIdGet(residentId, Helper.generateDate(year,month, day), new Response.Listener<GetSpecificResidentResponseBody>() {
            @Override
            public void onResponse(GetSpecificResidentResponseBody response) {
                Resident resident = response.getResident();
                residentPublic = resident;

                List<Therapy> therapies = response.getTherapies();
                therapiesPublic = therapies;

                // TOOLBAR title
                setTitle(resident.getName() + " " + resident.getLastName());

                // Spinner Array Adapter
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ResidentDetailsActivity.this,
                        R.array.residents_info, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                                sendDataHealthConditionFragment(resident);
                                break;
                            case 3:
                                sendDataTherapyFragment(therapies);
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


    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

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
            case R.id.action_task_resident:
                Intent intent1 = new Intent(ResidentDetailsActivity.this, ResidentTasksActivity.class);
                intent1.putExtra("residentId", residentPublic.getResidentId());
                startActivity(intent1);
                return true;
            case R.id.action_edit_resident:
                callIntentEditResident(residentPublic, therapiesPublic);
                Toast.makeText(ResidentDetailsActivity.this, "Edit Resident", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_delete_resident:
                //Toast.makeText(ResidentDetailsActivity.this, "Delete Resident", Toast.LENGTH_LONG).show();
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ResidentDetailsActivity.this);
        builder1.setMessage("Jeste li sigurni da želite izbrisati korisnika?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "IZBRIŠI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteResident();
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

    private void deleteResident() {

        // (DELETE /residents/{residentId})
        client.residentsResidentIdDelete(residentPublic.getResidentId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ResidentDetailsActivity.this, "Korisnik je izbrisan!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResidentDetailsActivity.this, "GREŠKA!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void sendDataHealthConditionFragment(Resident resident) {

        Bundle bundle = new Bundle();
        String mobility = resident.getMobility().toString();
        String independence = resident.getIndependence().toString();

        switch (mobility){
            case "MOBILE":
                bundle.putString("mobility", "pokretan");
                break;
            case "IMMOBILE":
                bundle.putString("mobility", "nepokretan");
                break;
        }

        switch (independence){
            case "INDEPENDENT":
                bundle.putString("independence", "samostalan");
                break;
            case "NECESSARY_AID":
                bundle.putString("independence", "potrebno pomagalo");
                break;
            case "COMPLETELY_DEPENDENT":
                bundle.putString("independence", "potpuno ovisan");
                break;
        }

        bundle.putString("note", resident.getNote());

        HealthConditionResidentsFragment fragment = new HealthConditionResidentsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    private void sendDataTherapyFragment(List<Therapy> therapies) {

        Bundle bundle = new Bundle();

        ArrayList<String> therapyStringList = new ArrayList<>();

        if(therapies.isEmpty()){
            List<String> emptyList = new ArrayList<>();
            therapyStringList.add("Nije dodana nijedna terapija.");
        }else{
            for(Therapy therapy : therapies){
                String therapyString = therapy.toString();
                therapyStringList.add(therapyString);
            }
        }

        bundle.putStringArrayList("therapylist",therapyStringList);

        TherapyResidentsFragment fragment = new TherapyResidentsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    private void callIntentEditResident(Resident resident, List<Therapy> therapies) {
        Intent intent1 =  new Intent(ResidentDetailsActivity.this, EditResidentActivity.class);

        // GENERAL DATA
        intent1.putExtra("id", resident.getResidentId());
        intent1.putExtra("name", resident.getName());
        intent1.putExtra("lastName", resident.getLastName());
        intent1.putExtra("room", resident.getRoom());
        intent1.putExtra("oib", resident.getOib());
        intent1.putExtra("nationality", resident.getNationality());
        intent1.putExtra("citizenship", resident.getCitizenship());
        intent1.putExtra("dateOfBirth", Helper.generateString(resident.getDateOfBirth()));
        intent1.putExtra("placeOfBirth", resident.getPlaceOfBirth());
        intent1.putExtra("idCard", resident.getIdCard());

        // CONTACT PERSON
        intent1.putExtra("contactName", resident.getContactName());
        intent1.putExtra("contactRelationship", resident.getContactRelationship());
        intent1.putExtra("contactNumber", resident.getContactNumber());
        intent1.putExtra("contactEmail", resident.getContactEmail());
        intent1.putExtra("contactAddress", resident.getContactAddress());

        // HEALTH CONDITION
        String mobility = resident.getMobility().toString();
        String independence = resident.getIndependence().toString();

        switch (mobility){
            case "MOBILE":
                intent1.putExtra("mobility", "pokretan");
                break;
            case "IMMOBILE":
                intent1.putExtra("mobility", "nepokretan");
                break;
        }

        switch (independence) {
            case "INDEPENDENT":
                intent1.putExtra("independence", "samostalan");
                break;
            case "NECESSARY_AID":
                intent1.putExtra("independence", "potrebno pomagalo");
                break;
            case "COMPLETELY_DEPENDENT":
                intent1.putExtra("independence", "potpuno ovisan");
                break;
        }
        intent1.putExtra("note", resident.getNote());

        // THERAPY
        ArrayList<String> therapyStringList = new ArrayList<>();

        if(therapies.isEmpty()){
            therapyStringList.add("Nije dodana nijedna terapija.");
        }else{
            for(Therapy therapy : therapies){
                String therapyString = therapy.toString();
                therapyStringList.add(therapyString);
            }
        }

        intent1.putExtra("note", resident.getNote());

        intent1.putStringArrayListExtra("therapy",therapyStringList);

        startActivity(intent1);
    }
}
