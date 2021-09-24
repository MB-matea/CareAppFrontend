package com.mateabeslic.careapp;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    private View layout;

    TextView txtName, txtLastName, txtRoom, txtOib, txtDateOfBirth, txtPlaceOfBirth,
            txtNationality, txtCitizenship, txtIdCard;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_general_data_residents, container, false);

        Integer residentId = this.getArguments().getInt("residentId");
        String name = this.getArguments().getString("name");
        String lastName = this.getArguments().getString("lastName");
        String oib = this.getArguments().getString("oib");
        Integer room = this.getArguments().getInt("room");
        String dateOfBirth = this.getArguments().getString("dateOfBirth");
        String placeOfBirth = this.getArguments().getString("placeOfBirth");
        String nationality = this.getArguments().getString("nationality");
        String citizenship = this.getArguments().getString("citizenship");
        String idCard = this.getArguments().getString("idCard");

        txtName = layout.findViewById(R.id.txt_name);
        txtLastName = layout.findViewById(R.id.txt_last_name);
        txtOib = layout.findViewById(R.id.txt_oib);
        txtRoom = layout.findViewById(R.id.txt_room);
        txtDateOfBirth = layout.findViewById(R.id.txt_date_of_birth);
        txtPlaceOfBirth = layout.findViewById(R.id.txt_place_of_birth);
        txtNationality = layout.findViewById(R.id.txt_nationality);
        txtCitizenship = layout.findViewById(R.id.txt_citizenship);
        txtIdCard = layout.findViewById(R.id.txt_id_card);

        txtName.setText(txtName.getText() + name);
        txtLastName.setText(txtLastName.getText() + lastName);
        txtOib.setText(txtOib.getText() + oib);
        txtRoom.setText(txtRoom.getText() + room.toString());
        txtDateOfBirth.setText(txtDateOfBirth.getText() + dateOfBirth);
        txtPlaceOfBirth.setText(txtPlaceOfBirth.getText() + placeOfBirth);
        txtNationality.setText(txtNationality.getText() + nationality);
        txtCitizenship.setText(txtCitizenship.getText() + citizenship);
        txtIdCard.setText(txtIdCard.getText() + idCard);

        return layout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

