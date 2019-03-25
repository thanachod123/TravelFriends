package com.finalproject.it.travelfriend.Guide;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;

public class AdapterProfileGuide extends BaseAdapter {
    Context mContext;
    String[] profileMenu;
    int[] resId;

    public AdapterProfileGuide(Context mContext, String[] profileMenu, int[] resId) {
        this.mContext = mContext;
        this.profileMenu = profileMenu;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return profileMenu.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.listview_profile_guide, viewGroup,false);
        Typeface myCustomFont = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/FC Lamoon Bold ver 1.00.ttf");
        TextView textView = view.findViewById(R.id.txt_profileMenu);
        textView.setTypeface(myCustomFont);
        textView.setText(profileMenu[i]);

        ImageView imageView = view.findViewById(R.id.img_profileMenu);
        imageView.setBackgroundResource(resId[i]);
        return  view;
    }
}
