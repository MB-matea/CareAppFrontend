package com.mateabeslic.careapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ResidentDetailsActivity extends AppCompatActivity  {

    Spinner spinner;
    public static String idString;

    GeneralDataResidentsFragment generalDataResidentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_details);

        spinner = findViewById(R.id.spinner);

        generalDataResidentsFragment = new GeneralDataResidentsFragment();

        // RESIDENT ID
        Bundle bundle = getIntent().getExtras();
        Integer residentId = (Integer) bundle.get("residentId");
        idString = String.valueOf(residentId);

        Toast.makeText(ResidentDetailsActivity.this, residentId.toString(), Toast.LENGTH_LONG).show();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.residents_info, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setFragment(generalDataResidentsFragment);
                        break;
                    case 1:
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

    public void setFragment (Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putInt("residentId", Integer.valueOf(idString));
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }


    //private GeneralDataResidentsFragment sendDataAddTimeFragment() {
    //    Bundle bundle = new Bundle();
    //    bundle.putInt("id", Integer.valueOf(idString));
    //    GeneralDataResidentsFragment fragment = new GeneralDataResidentsFragment();
    //    fragment.setArguments(bundle);

    //    return fragment;
    //}
}
