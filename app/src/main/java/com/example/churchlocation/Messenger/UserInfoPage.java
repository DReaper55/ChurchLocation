package com.example.churchlocation.Messenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.churchlocation.Model.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserInfoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
//        UserObject userObject = intent.getParcelableExtra("USER");

        String uid = intent.getStringExtra("USER");

        if (uid != null) {
//            Log.d("UserInfoTag", userObject.getUsername());
//            Log.d("UserInfoTag", userObject.getId());
//            Log.d("UserInfoTag", userObject.getChurch());
//            Log.d("UserInfoTag", userObject.getFullname());
//            Log.d("UserInfoTag", userObject.getGender());
//            Log.d("UserInfoTag", userObject.getEmail());
//            Log.d("UserInfoTag", userObject.getTitle());
//            Log.d("UserInfoTag", userObject.getHobby());
//            Log.d("UserInfoTag", userObject.getBio());

            displayData(uid);
        }

        findViewById(R.id.mCloseUserInfo).setOnClickListener(view -> finish());
    }

    private void displayData(String uid) {
        final TextView fullname, email, membership,
                currentCountry, currentState, gender,
                DOBirth, DOBaptism, hobbies, bio;
        final ImageView displayPic;

        fullname = findViewById(R.id.mFullName);
        email = findViewById(R.id.mEmailText);
        membership = findViewById(R.id.mChurchDenomination);
        currentCountry = findViewById(R.id.mCurrentCountry);
        currentState = findViewById(R.id.mCurrentState);
        gender = findViewById(R.id.mGender);
        DOBaptism = findViewById(R.id.mDOBaptism);
        DOBirth = findViewById(R.id.mDOBirth);
        hobbies = findViewById(R.id.mHobbies);
        bio = findViewById(R.id.mBio);
        displayPic = findViewById(R.id.mDisplayPic);

        FirebaseDatabase.getInstance().getReference("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserObject userObject = dataSnapshot.getValue(UserObject.class);

                if(userObject != null){
                    getSupportActionBar().setTitle(userObject.getUsername());
                    getSupportActionBar().setElevation(10.0f);

                    fullname.setText(userObject.getFullname());
//        username.setText(userObject.getUsername());
                    email.setText(userObject.getEmail());
                    membership.setText(userObject.getChurch());
                    currentCountry.setText(userObject.getLeaderCountry());
                    currentState.setText(userObject.getState());
                    gender.setText(userObject.getGender());
                    DOBaptism.setText(userObject.getDateOfBaptism());
                    DOBirth.setText(userObject.getDateOfBirth());
                    hobbies.setText(userObject.getHobby());
                    bio.setText(userObject.getBio());

                    String uid = userObject.getId();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Picasso.get().load(task.getResult()).fit().centerCrop().into(displayPic);

                        }
                    });

                    FloatingActionButton fab = findViewById(R.id.fab);
                    fab.setOnClickListener((view) -> {
                        Intent intent = new Intent(view.getContext(), ChatScreen.class);
                        intent.putExtra("USER", userObject);
                        view.getContext().startActivity(intent);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
