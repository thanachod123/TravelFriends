package com.finalproject.it.travelfriend.User.Category;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderPackageUser extends RecyclerView.ViewHolder {
    TextView txtNamePackage;
    TextView txtNameGuide;
    TextView txtPrice;
    TextView txtProvincePackage;
    TextView txtVehicleType;
    TextView txtActivity;
    CircleImageView imgGuide;
    ImageView imgPackage;
    ImageView img_vehicle_type;
    CardView cardView;

    public ViewHolderPackageUser(View itemView){
        super(itemView);
        txtNamePackage = itemView.findViewById(R.id.txt_package_name);
        txtNameGuide = itemView.findViewById(R.id.txt_guide_name);
        txtPrice = itemView.findViewById(R.id.txt_price);
        txtProvincePackage = itemView.findViewById(R.id.txt_province);
        txtVehicleType = itemView.findViewById(R.id.txt_vehicle_type);
        txtActivity = itemView.findViewById(R.id.txt_activity);
        img_vehicle_type = itemView.findViewById(R.id.img_vehicle_type);
        imgPackage = itemView.findViewById(R.id.img_package);
        imgGuide = itemView.findViewById(R.id.profile_image_guide);
        cardView = itemView.findViewById(R.id.cardView);





    }
}
