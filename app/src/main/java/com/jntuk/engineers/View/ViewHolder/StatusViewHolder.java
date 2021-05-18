package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.Interface.ItemClickListener;
import com.jntuk.engineers.R;

public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView user_image;
    public TextView show_wish, user_name;
    public ItemClickListener listener;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        user_image = itemView.findViewById(R.id.user_imageId);
        user_name = itemView.findViewById(R.id.user_nameId);
        show_wish = itemView.findViewById(R.id.show_wishId);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
