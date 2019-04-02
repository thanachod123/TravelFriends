package com.finalproject.it.travelfriend.Guide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

import org.w3c.dom.Text;

public class ViewHolderPackageGuide extends RecyclerView.ViewHolder {
    TextView txtNamePackage;
    TextView txtNameGuide;
    TextView txtDescPackage;
    TextView txtStatusPackage;
    TextView txtProvincePackage;
    ImageView imgPackage;

    public ViewHolderPackageGuide(View itemView){
        super(itemView);
        txtNamePackage = itemView.findViewById(R.id.txt_namePackage);
        txtDescPackage = itemView.findViewById(R.id.txt_descPackage);
        txtStatusPackage = itemView.findViewById(R.id.txt_statusPackage);
        txtProvincePackage = itemView.findViewById(R.id.txt_provincePackage);
        txtNameGuide = itemView.findViewById(R.id.txt_guide_name);
        imgPackage = itemView.findViewById(R.id.img_package);

    }
}
