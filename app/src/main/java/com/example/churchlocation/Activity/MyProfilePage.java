package com.example.churchlocation.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfilePage extends AppCompatActivity {
    private SavedUserDB userDB = new SavedUserDB(this);

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid = firebaseAuth.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.editProfile);
        fab.setOnClickListener(view -> startActivity(new Intent(MyProfilePage.this, EditProfilePage.class)));

        if(userDB.totalUsers() >= 1){
            UserObject userObject = userDB.getUser(uid);

            if(userObject != null){
                displayData(userObject);
            }

            ArrayList<UserObject> userObjectList;
            userDB = new SavedUserDB(this);
            userObjectList = userDB.getAllUsers();
            for(int i=0; i<userObjectList.size(); i++){
                Log.d("UserListTag2", userObjectList.get(i).getUsername() );
            }
        }

        findViewById(R.id.mCloseUserInfo).setOnClickListener(view -> finish());
    }

    private void displayData(UserObject userObject) {
        TextView fullname, username, email, membership,
                currentCountry, currentState, gender,
                DOBirth, DOBaptism, hobbies, bio;
        final ImageView displayPic;

        fullname = findViewById(R.id.mFullName);
        username = findViewById(R.id.mUserName);
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


        fullname.setText(userObject.getFullname());
        username.setText(userObject.getUsername());
        email.setText(userObject.getEmail());
        membership.setText(userObject.getChurch());
        currentCountry.setText(userObject.getLeaderCountry());
        currentState.setText(userObject.getState());
        gender.setText(userObject.getGender());
        DOBaptism.setText(userObject.getDateOfBaptism());
        DOBirth.setText(userObject.getDateOfBirth());
        hobbies.setText(userObject.getHobby());
        bio.setText(userObject.getBio());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).fit().centerCrop().into(displayPic);

            }
        });
    }

}
