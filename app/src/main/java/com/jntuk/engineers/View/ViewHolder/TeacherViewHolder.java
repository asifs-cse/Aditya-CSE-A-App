package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.Interface.ItemClickListener;
import com.jntuk.engineers.R;


public class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView teacher_name, teacher_sub, teacher_rank, teacher_degree;
    public ImageView teacher_image,teacher_email, teacher_wap;
    public ItemClickListener listener;

    public TeacherViewHolder(@NonNull View itemView) {
        super(itemView);

        teacher_name = itemView.findViewById(R.id.teacher_nameId);
        teacher_sub = itemView.findViewById(R.id.teacher_subId);
        teacher_rank = itemView.findViewById(R.id.teacher_rankId);
        teacher_degree = itemView.findViewById(R.id.teacher_degreeId);

        teacher_image = itemView.findViewById(R.id.teacher_imageId);
        teacher_email = itemView.findViewById(R.id.teacher_gmailId);
        teacher_wap = itemView.findViewById(R.id.teacher_wpId);

    }

    public void onClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
