package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.LinearViewHolder> {
    private Context context;
    private List<Map<String,Object>> list2;
    public commentAdapter(Context context, List<Map<String, Object>> list2) {
        this.context=context;
        this.list2=list2;
    }


    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        holder.commentAuthor.setText(list2.get(position).get("nickname").toString());
        holder.commentComment.setText(list2.get(position).get("content").toString());
        Glide.with(context)
                .load(list2.get(position).get("avatar"))
                .placeholder(R.drawable.loading)
                .into(holder.commentPicture);

    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView commentAuthor,commentComment;
        ImageView commentPicture;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthor=itemView.findViewById(R.id.commentAuthor);
            commentComment=itemView.findViewById(R.id.commentComment);
            commentPicture=itemView.findViewById(R.id.commentPicture);
        }
    }
}
