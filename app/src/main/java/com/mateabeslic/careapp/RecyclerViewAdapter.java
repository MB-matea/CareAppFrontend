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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Integer> mRoomNumbers;
    private ArrayList<String> mNames;
    private ArrayList<Boolean> mCheckBoxes;
    private ArrayList<Integer> mIds;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<Integer> mRoomNumbers, ArrayList<String> mNames, ArrayList<Boolean> mCheckBoxes, ArrayList<Integer> mIds) {
        this.mRoomNumbers = mRoomNumbers;
        this.mNames = mNames;
        this.mContext = mContext;
        this.mCheckBoxes = mCheckBoxes;
        this.mIds = mIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtRoom.setText(mRoomNumbers.get(position).toString());
        holder.txtName.setText(mNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResidentDetailsActivity.class);
                intent.putExtra("residentId", mIds.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtRoom, txtName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRoom = itemView.findViewById(R.id.txt_room);
            txtName = itemView.findViewById(R.id.txt_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}


