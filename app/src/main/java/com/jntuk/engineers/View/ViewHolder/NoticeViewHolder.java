package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.R;


public class NoticeViewHolder extends RecyclerView.ViewHolder {

    public TextView notice_title, date;
    public NoticeViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.notice_dateId);
        notice_title = itemView.findViewById(R.id.notice_titleId);
        notice_title.setSelected(true);
    }
}
