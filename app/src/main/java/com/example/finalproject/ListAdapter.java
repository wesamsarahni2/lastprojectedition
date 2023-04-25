package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    ArrayList<String> list;
    Context context;

    public ListAdapter(ArrayList<String> list , Context context) {
        this.list = list;
        this.context = context;
    }
    public void setFilteredList(ArrayList<String> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position));

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("productname",holder.tvTitle.getText().toString());
                ProductFragment pf = new ProductFragment();
                pf.setArguments(bundle);
                FragmentTransaction ft =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, pf);
                ft.commit();
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        public ViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textView2);


        }
    }
}
