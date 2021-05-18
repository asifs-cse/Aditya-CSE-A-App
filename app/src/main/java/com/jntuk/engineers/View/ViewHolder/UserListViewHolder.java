package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.Interface.ItemClickListener;
import com.jntuk.engineers.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView user_name, user_roll, user_institute, user_department, user_semester;
    public CardView userCard;
    public CircleImageView user_image;
    public ItemClickListener listener;
    public UserListViewHolder(@NonNull View itemView) {
        super(itemView);

        user_name = itemView.findViewById(R.id.user_nameId);
        user_roll = itemView.findViewById(R.id.user_rollId);
        user_institute = itemView.findViewById(R.id.user_instituteId);
        user_department = itemView.findViewById(R.id.user_departmentId);
        user_semester = itemView.findViewById(R.id.user_semesterId);
        user_image = itemView.findViewById(R.id.user_imageId);
        userCard = itemView.findViewById(R.id.user_cardViewId);

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
