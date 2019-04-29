package com.finalproject.it.travelfriend.Guide.BookingPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

public class ViewHolderBookingGuide extends RecyclerView.ViewHolder {

public ImageView imgProfileTourist;
public TextView txtBookingStatus;
    public TextView txtTouristName;
    public TextView txtNameTrip;
    public TextView txtDescription;
    public TextView txtDay;
    public TextView txtNumTourist;

    public ViewHolderBookingGuide(@NonNull View itemView) {
        super(itemView);
        imgProfileTourist = itemView.findViewById(R.id.img_profile_tourist);
        txtBookingStatus = itemView.findViewById(R.id.txt_booking_status);
        txtTouristName = itemView.findViewById(R.id.txt_tourist_name);
        txtNameTrip = itemView.findViewById(R.id.txt_name_trip);
        txtDescription = itemView.findViewById(R.id.txt_name_trip2);
        txtDay = itemView.findViewById(R.id.txt_day);
        txtNumTourist = itemView.findViewById(R.id.txt_num_tourist);
    }
}
