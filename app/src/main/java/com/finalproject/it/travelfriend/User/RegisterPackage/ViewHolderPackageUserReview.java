package com.finalproject.it.travelfriend.User.RegisterPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderPackageUserReview extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView txtName,txtComment;
    RatingBar ratingBar;

    public ViewHolderPackageUserReview(@NonNull View itemView) {
        super(itemView);

        circleImageView = itemView.findViewById(R.id.circleImageView);
        txtName = itemView.findViewById(R.id.txt_name);
        txtComment = itemView.findViewById(R.id.txt_comment);
        ratingBar = itemView.findViewById(R.id.rating_bar);

    }
}
