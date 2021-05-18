package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.Interface.ItemClickListener;
import com.jntuk.engineers.R;

public class ServicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ItemClickListener listener;
    public TextView service_title;
    public ImageView logo;

    public ServicesViewHolder(@NonNull View itemView) {
        super(itemView);
        logo = itemView.findViewById(R.id.services_logo);
        service_title = itemView.findViewById(R.id.services_title);
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
