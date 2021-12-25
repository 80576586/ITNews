package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class pictureAdapter extends RecyclerView.Adapter<pictureAdapter.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;

    public pictureAdapter(Context context, List<Map<String, Object>> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_pictures,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pictureAdapter.ViewHolder holder, int position) {
        String pics=list.get(position).get("pics").toString();
        Glide.with(context)
                .load(list.get(position).get("pics"))
                .into(holder.picture);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView picture;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            picture=itemView.findViewById(R.id.picture);
        }
    }
}
