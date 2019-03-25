package com.finalproject.it.travelfriend.Guide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

public class ViewHolderPackageGuide extends RecyclerView.ViewHolder {
    TextView txtNamePackage;
    TextView txtDescPackage;
    TextView txtStatusPackage;
    TextView txtProvincePackage;
    ImageView imgPackage;
    CardView cardView;

    public ViewHolderPackageGuide(View itemView){
        super(itemView);
        txtNamePackage = itemView.findViewById(R.id.txt_namePackage);
        txtDescPackage = itemView.findViewById(R.id.txt_descPackage);
        txtStatusPackage = itemView.findViewById(R.id.txt_statusPackage);
        txtProvincePackage = itemView.findViewById(R.id.txt_provincePackage);
        imgPackage = itemView.findViewById(R.id.img_package);
        cardView = itemView.findViewById(R.id.cardView);
    }
}
