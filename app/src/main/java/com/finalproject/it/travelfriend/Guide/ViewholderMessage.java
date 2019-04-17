package com.finalproject.it.travelfriend.Guide;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.finalproject.it.travelfriend.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewholderMessage extends RecyclerView.ViewHolder {

    TextView message;
    public TextView packagename;
    public TextView nametour;
    public CircleImageView imgUser;
    public TextView textDate;

    public ViewholderMessage(@NonNull View itemView) {
        super(itemView);

        message = itemView.findViewById(R.id.message);
        packagename = itemView.findViewById(R.id.nametrip);
        nametour = itemView.findViewById(R.id.nametour);
        imgUser = itemView.findViewById(R.id.Imageuser);
        textDate = itemView.findViewById(R.id.textDate);
    }
}
