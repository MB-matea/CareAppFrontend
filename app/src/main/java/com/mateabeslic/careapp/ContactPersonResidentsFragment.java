package com.mateabeslic.careapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ContactPersonResidentsFragment extends Fragment {

    private View layout;

    TextView txtContactName, txtContactRelationship, txtContactNumber, txtContactEmail, txtContactAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_contact_person_residents, container, false);

        String contactName = this.getArguments().getString("contactName");
        String contactRelationship = this.getArguments().getString("contactRelationship");
        String contactNumber = this.getArguments().getString("contactNumber");
        String contactEmail = this.getArguments().getString("contactEmail");
        String contactAddress = this.getArguments().getString("contactAddress");

        txtContactName = layout.findViewById(R.id.txt_contact_name);
        txtContactRelationship = layout.findViewById(R.id.txt_contact_relationship);
        txtContactNumber = layout.findViewById(R.id.txt_contact_number);
        txtContactEmail = layout.findViewById(R.id.txt_contact_email);
        txtContactAddress = layout.findViewById(R.id.txt_contact_address);

        txtContactName.setText(txtContactName.getText() + contactName);
        txtContactRelationship.setText(txtContactRelationship.getText() + contactRelationship);
        txtContactNumber.setText(txtContactNumber.getText() + contactNumber);
        txtContactEmail.setText(txtContactEmail.getText() + contactEmail);
        txtContactAddress.setText(txtContactAddress.getText() + contactAddress);

        return layout;
    }
}