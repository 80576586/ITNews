package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.LinearViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;

    public Adapter(Context context, List<Map<String, Object>> list) {
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        holder.tv1.setText(list.get(position).get("title").toString());
        holder.tv2.setText(list.get(position).get("username").toString());
        Glide.with(context)
                .load(list.get(position).get("news_pics_set"))
                .placeholder(R.drawable.loading)
                .into(holder.iv);
        String id = list.get(position).get("id").toString();
        String avatar=list.get(position).get("avatar").toString();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, newsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("avatar",avatar);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        LinearLayout linearLayout;
        ImageView iv;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            iv=itemView.findViewById(R.id.iv);
            linearLayout=itemView.findViewById(R.id.ln);
        }
    }
}
