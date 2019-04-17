package com.finalproject.it.travelfriend.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finalproject.it.travelfriend.R;
import com.finalproject.it.travelfriend.User.Category.Adventure;
import com.finalproject.it.travelfriend.User.Category.Art;
import com.finalproject.it.travelfriend.User.Category.Culture;
import com.finalproject.it.travelfriend.User.Category.History;
import com.finalproject.it.travelfriend.User.Category.Nature;

public class HomeUserFragment extends Fragment {
    CardView cardViewAdventure,cardViewCulture,cardViewArt,cardViewNature,cardViewHistory;

    FloatingActionButton floatingSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        cardViewAdventure = view.findViewById(R.id.cardviewAdventure);
        cardViewCulture = view.findViewById(R.id.cardviewCulture);
        cardViewArt = view.findViewById(R.id.cardviewArt);
        cardViewNature = view.findViewById(R.id.cardviewNature);
        cardViewHistory = view.findViewById(R.id.cardviewHistory);
        floatingSearch = view.findViewById(R.id.SearchButton);
        floatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SearchActivity.class));

            }

        });

        cardViewAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Adventure.class));
            }
        });
        cardViewCulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Culture.class));
            }
        });
        cardViewArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Art.class));
            }
        });
        cardViewNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Nature.class));
            }
        });
        cardViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),History.class));
            }
        });

        return view;
    }
}
