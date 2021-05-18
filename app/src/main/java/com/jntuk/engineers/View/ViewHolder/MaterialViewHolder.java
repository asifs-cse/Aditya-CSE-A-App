package com.jntuk.engineers.View.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jntuk.engineers.R;

public class MaterialViewHolder extends RecyclerView.ViewHolder {
    public TextView material_title, material_sub, submitter_name;
    public MaterialViewHolder(@NonNull View itemView) {
        super(itemView);
        material_title = itemView.findViewById(R.id.material_titleId);
        material_sub = itemView.findViewById(R.id.material_subId);
        submitter_name = itemView.findViewById(R.id.material_submitter_nameId);
        material_title.setSelected(true);
    }
}
