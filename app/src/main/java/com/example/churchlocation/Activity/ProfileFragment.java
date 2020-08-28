package com.example.churchlocation.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.churchlocation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private CardView myProfile, addAChurch, shareApp, loadVideos, checkSettings, notifications;

    View view;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid = firebaseAuth.getUid();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "titleVerified";


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
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
        notifications = view.findViewById(R.id.notifications_card);

        myProfile.setOnClickListener(this);
        addAChurch.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        loadVideos.setOnClickListener(this);
        checkSettings.setOnClickListener(this);
        notifications.setOnClickListener(this);

//        if(preferences != null){
//            String titleVerified = preferences.getString("TITLE_VERIFIED", "");
//
//            if(titleVerified.equals("true")){
//                notifications.setVisibility(View.VISIBLE);
//            } else {
//                notifications.setVisibility(View.GONE);
//            }
//        }


        checkTitleVerificationStatus();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/"+uid+"/displayPic");
        final CircleImageView profile_image = view.findViewById(R.id.profile_image);

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult()).fit().centerCrop().into(profile_image);

                }
            }
        });

    }

    private void checkTitleVerificationStatus() {
        uid = firebaseAuth.getUid();


        // todo: Fix this, notification view not removing for disciple account
        DatabaseReference leaderStatusRef = firebaseDatabase.getReference("users/"+uid+"/title");
        leaderStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue().toString().equals("leader")){
                        DatabaseReference db = firebaseDatabase.getReference("users/"+uid+"/titleVerification");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String titleVerified = dataSnapshot.getValue(String.class);

                                if(titleVerified.equals("true")){
                                    notifications.setVisibility(View.VISIBLE);
                                } else {
                                    notifications.setVisibility(View.GONE);
                                }

                                editor = preferences.edit();
                                editor.putString("TITLE_VERIFIED", titleVerified);
                                editor.apply();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_profile_card:
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(view.getContext(), MyProfilePage.class));
                } else {
                    Toast.makeText(view.getContext(), "Sign in to access", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notifications_card:
                startActivity(new Intent(view.getContext(), NotificationsPage.class));
                break;

            case R.id.add_church_card:
                startActivity(new Intent(getActivity(), AddAChurch.class));
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

    @Override
    public void onDestroy() {
//        notifications.setVisibility(View.GONE);
        super.onDestroy();
    }
}
