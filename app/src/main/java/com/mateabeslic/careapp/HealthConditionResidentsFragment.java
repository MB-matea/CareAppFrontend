package com.mateabeslic.careapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class HealthConditionResidentsFragment extends Fragment {

    private static final String TAG = "Fragment";
    private View layout;

    TextView txtMobility, txtIndependence, txtNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_health_condition_residents, container, false);

        Integer residentId = this.getArguments().getInt("residentId");
        String mobility = this.getArguments().getString("mobility");
        String independence = this.getArguments().getString("independence");
        String note = this.getArguments().getString("note");

        txtIndependence = layout.findViewById(R.id.txt_independence);
        txtMobility = layout.findViewById(R.id.txt_mobility);
        txtNote = layout.findViewById(R.id.txt_note);

        txtIndependence.setText(txtIndependence.getText() + independence);
        txtMobility.setText(txtMobility.getText() + mobility);
        txtNote.setText(txtNote.getText() + note);

        return layout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}