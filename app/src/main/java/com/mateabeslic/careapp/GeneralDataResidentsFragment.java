package com.mateabeslic.careapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralDataResidentsFragment extends Fragment {

    private View layout;

    TextView txtName, txtLastName, txtRoom, txtOib, txtDateOfBirth, txtPlaceOfBirth,
            txtNationality, txtCitizenship, txtIdCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_general_data_residents, container, false);

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

