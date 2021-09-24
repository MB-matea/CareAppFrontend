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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TherapyResidentsFragment extends Fragment {
    private static final String TAG = "fragment";
    private View layout;

    ListView therapyListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_therapy_residents, container, false);

        Integer residentId = this.getArguments().getInt("residentId");
        ArrayList<String> therapyList = this.getArguments().getStringArrayList("therapylist");
        Log.d(TAG, "onCreateView: " + therapyList.toString());

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