package com.mateabeslic.careapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mateabeslic.careapp.api.client.TasksApi;

import java.util.ArrayList;
import java.util.Date;

public class UserTasksRecyclerViewAdapter extends RecyclerView.Adapter<UserTasksRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "UserTasksRecyclerViewAdapter";

    private TasksApi client;

    private ArrayList<Integer> mRooms;
    private ArrayList<String> mResidents;
    private ArrayList<Boolean> mCheckBoxes;
    private ArrayList<Integer> mIds;
    private ArrayList<Integer> mResidentIds;

    private Integer residentId;
    private Integer userId;
    private String name, lastName;

    private Context mContext;

    public UserTasksRecyclerViewAdapter(Context mContext, Integer userId, String name, String lastName, ArrayList<Integer> mIds, ArrayList<String> mResidents, ArrayList<Integer> mResidentIds, ArrayList<Boolean> mCheckBoxes, ArrayList<Integer> mRooms) {
        this.mResidents = mResidents;
        this.mResidentIds = mResidentIds;
        this.mRooms = mRooms;
        this.mContext = mContext;
        this.mCheckBoxes = mCheckBoxes;
        this.mIds = mIds;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
    }

    @NonNull
    @Override
    public UserTasksRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layou_usertasks_listitem, parent, false);
        UserTasksRecyclerViewAdapter.ViewHolder holder = new UserTasksRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserTasksRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtRoom.setText(mRooms.get(position).toString());
        holder.txtResident.setText(mResidents.get(position));
        if(mCheckBoxes.get(position).booleanValue()){
            holder.txtDone.setText("Da");
        } else {
            holder.txtDone.setText("Ne");
            //holder.txtUser.setTextColor(Color.RED);
            //holder.txtDate.setTextColor(0xFF00B3D6);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                residentId = mIds.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, UserTaskDetailsActivity.class);
                intent.putExtra("residentId", mResidentIds.get(holder.getAdapterPosition()));
                intent.putExtra("taskId", mIds.get(holder.getAdapterPosition()));
                intent.putExtra("taskIsDone", mCheckBoxes.get(holder.getAdapterPosition()));
                intent.putExtra("userId", userId);
                intent.putExtra("name", name);
                intent.putExtra("lastName", lastName);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtRoom, txtResident, txtDone;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRoom = itemView.findViewById(R.id.txt_room);
            txtResident = itemView.findViewById(R.id.txt_resident);
            txtDone = itemView.findViewById(R.id.txt_done);
            parentLayout = itemView.findViewById(R.id.usertasks_layout);
        }
    }
}
