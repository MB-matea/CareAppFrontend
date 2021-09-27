package com.mateabeslic.careapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class TherapyResidentsFragment extends Fragment {
    private View layout;

    ListView therapyListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_therapy_residents, container, false);

        ArrayList<String> therapyList = this.getArguments().getStringArrayList("therapylist");

        therapyListView = layout.findViewById(R.id.therapy_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(layout.getContext(), android.R.layout.simple_list_item_1, therapyList);
        therapyListView.setAdapter(adapter);

        return layout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}