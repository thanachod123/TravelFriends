package com.finalproject.it.travelfriend.User.BookingPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

public class ViewHolderBookingUser extends RecyclerView.ViewHolder {

ImageView imgProfileGuide;
TextView txtBookingStatus,txtGuideName,txtNameTrip,txtDescription,txtDay,txtNumTourist;

    public ViewHolderBookingUser(@NonNull View itemView) {
        super(itemView);
        imgProfileGuide = itemView.findViewById(R.id.img_profile_guide);
        txtBookingStatus = itemView.findViewById(R.id.txt_booking_status);
        txtGuideName = itemView.findViewById(R.id.txt_guide_name);
        txtNameTrip = itemView.findViewById(R.id.txt_name_trip);
        txtDescription = itemView.findViewById(R.id.txt_name_trip2);
        txtDay = itemView.findViewById(R.id.txt_day);
        txtNumTourist = itemView.findViewById(R.id.txt_num_tourist);
    }
}
