package com.mateabeslic.careapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {

    private TasksApi client;

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
        holder.txtDate.setText(Helper.generateString(mDates.get(position)));
        holder.txtUser.setText(mUsers.get(position));
        if(mCheckBoxes.get(position).booleanValue()){
            holder.txtDone.setText("Da");
        } else {
            holder.txtDone.setText("Ne");
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mUsers.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

                showDeleteDialog(mIds.get(holder.getAdapterPosition()));
            }
        });
    }

    private void showDeleteDialog(Integer taskId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("??elite li izbrisati zadu??enje?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "IZBRI??I",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteResident(taskId);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Odustani",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void deleteResident(Integer taskId) {

        if(client == null) {
            client = new TasksApi();
        }

        client.setBasePath(BasePath.getBasePath());

        // (DELETE /tasks/{taskId})
        client.tasksTaskIdDelete(taskId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "Zadu??enje je izbrisano!", Toast.LENGTH_LONG).show();
                ((ResidentTasksActivity)mContext).recreate();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 404) {
                    Toast.makeText(mContext, "Zadu??enje nije prona??eno!", Toast.LENGTH_LONG).show();
                }
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


