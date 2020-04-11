package com.example.churchlocation.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.churchlocation.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    CardView myProfile, addAChurch, shareApp, loadVideos, checkSettings;

    View view;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = container.getRootView();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myProfile = view.findViewById(R.id.my_profile_card);
        addAChurch = view.findViewById(R.id.add_church_card);
        shareApp = view.findViewById(R.id.share_app_card);
        loadVideos = view.findViewById(R.id.video_tube_card);
        checkSettings = view.findViewById(R.id.settings_card);

        myProfile.setOnClickListener(this);
        addAChurch.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        loadVideos.setOnClickListener(this);
        checkSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_profile_card:
                Toast.makeText(view.getContext(), "My Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.add_church_card:
                Toast.makeText(view.getContext(), "Church Location", Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_app_card:
                Toast.makeText(view.getContext(), "Share App", Toast.LENGTH_SHORT).show();

                break;

            case R.id.video_tube_card:
                Toast.makeText(view.getContext(), "Load Hotnews Videos", Toast.LENGTH_SHORT).show();

                break;

            case R.id.settings_card:
                Toast.makeText(view.getContext(), "Settings", Toast.LENGTH_SHORT).show();

                break;

        }
    }
}
