package com.example.myapplication;
import android.content.Context;
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

import javax.microedition.khronos.opengles.GL;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.LinearViewHolder> {
    private Context context;
    private List<Map<String, Object>> list1;

    public newsAdapter(Context context, List<Map<String, Object>> list1) {
        this.context=context;
        this.list1=list1;
    }


    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content,parent,false));



    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter.LinearViewHolder holder, int position) {
        holder.textView.setText(list1.get(position).get("content").toString());
    }





    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.contentNews);

        }
    }


}

