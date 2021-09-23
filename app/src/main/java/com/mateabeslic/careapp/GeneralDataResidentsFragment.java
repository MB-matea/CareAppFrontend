package com.mateabeslic.careapp;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.ResidentsApi;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBody;
import com.mateabeslic.careapp.api.model.GetAllResidentsResponseBodyResidents;
import com.mateabeslic.careapp.api.model.GetSpecificResidentResponseBody;
import com.mateabeslic.careapp.api.model.Resident;
import com.mateabeslic.careapp.api.model.Therapy;
import com.mateabeslic.careapp.api.model.TherapyPlan;

import org.apache.http.impl.cookie.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GeneralDataResidentsFragment extends Fragment {

    private static final String TAG = "Fragment";
    private  static ResidentsApi client;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_general_data_residents, container, false);

        Integer residentId = this.getArguments().getInt("residentId");


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
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Resident resident = response.getResident();
                List<Therapy> therapies = response.getTherapies();
                List<TherapyPlan> therapyPlans = response.getTherapyPlans();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        // Inflate the layout for this fragment
        return layout;
    }

}

