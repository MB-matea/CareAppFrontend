package com.mateabeslic.careapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.Date;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "TasksRecyclerViewAdapter";

    private ArrayList<Date> mDates;
    private ArrayList<String> mUsers;
    private ArrayList<Boolean> mCheckBoxes;
    private ArrayList<Integer> mIds;

    private Context mContext;

    public TasksRecyclerViewAdapter(Context mContext, ArrayList<Integer> mIds, ArrayList<String> mUsers, ArrayList<Boolean> mCheckBoxes, ArrayList<Date> mDates) {
        this.mUsers = mUsers;
        this.mDates = mDates;
        this.mContext = mContext;
        this.mCheckBoxes = mCheckBoxes;
        this.mIds = mIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasks_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.txtDate.setText(Helper.generateString(mDates.get(position)));
        holder.txtUser.setText(mUsers.get(position));
        if(mCheckBoxes.get(position).booleanValue()){
            holder.txtDone.setText("Da");
        } else {
            holder.txtDone.setText("Ne");
            //holder.txtUser.setTextColor(73648976);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" + mUsers.get(holder.getAdapterPosition()));
                Toast.makeText(mContext, mUsers.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(mContext, ResidentDetailsActivity.class);
                //intent.putExtra("residentId", mIds.get(holder.getAdapterPosition()));
                //mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtUser, txtDone;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txt_date);
            txtUser = itemView.findViewById(R.id.txt_user);
            txtDone = itemView.findViewById(R.id.txt_done);
            parentLayout = itemView.findViewById(R.id.tasks_layout);
        }
    }
}


