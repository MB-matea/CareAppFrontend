package com.mateabeslic.careapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";
    //var
    private ArrayList<Integer> mRoomNumbers = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        
        initData();
    }

    private void initData() {
        Log.d(TAG, "initData: initData");
        mRoomNumbers.add(1);
        mNames.add("Ivo Ivić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(2);
        mNames.add("Marin Marić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(3);
        mNames.add("Ana Anić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(4);
        mNames.add("Toni Tonić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(5);
        mNames.add("Karla Karlić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(6);
        mNames.add("Anita Anić");
        mCheckBoxes.add(false);
        mRoomNumbers.add(1);
        mNames.add("Ivo Ivić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(2);
        mNames.add("Marin Marić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(3);
        mNames.add("Ana Anić");
        mCheckBoxes.add(false);

        mRoomNumbers.add(4);
        mNames.add("Toni Tonić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(5);
        mNames.add("Karla Karlić");
        mCheckBoxes.add(true);

        mRoomNumbers.add(6);
        mNames.add("Anita Anić");
        mCheckBoxes.add(false);

        Log.d(TAG, "initData: test");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initrecyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mRoomNumbers, mNames, mCheckBoxes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
