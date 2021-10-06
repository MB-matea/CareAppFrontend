package com.mateabeslic.careapp;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "UsersRecyclerViewAdapter";

    private ArrayList<Integer> mIds;
    private ArrayList<String> mNames;
    private Context mContext;

    public UsersRecyclerViewAdapter(Context mContext, ArrayList<Integer> mIds, ArrayList<String> mNames) {
        this.mNames = mNames;
        this.mContext = mContext;
        this.mIds = mIds;
    }

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_users_listitem, parent, false);
        UsersRecyclerViewAdapter.ViewHolder holder = new UsersRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.txtId.setText(mIds.get(position).toString());
        holder.txtName.setText(mNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UserDetailsActivity.class);
                intent.putExtra("userId", mIds.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtId;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txt_id);
            txtName = itemView.findViewById(R.id.txt_name);
            parentLayout = itemView.findViewById(R.id.users_layout);
        }
    }
}
