package com.example.churchlocation.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProfilePage extends AppCompatActivity {
    private SavedUserDB userDB = new SavedUserDB(this);

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid = firebaseAuth.getUid();

    private UserObject userObject = new UserObject();

    private CircleImageView displayPic;
    private RadioButton leader, disciple, selectTitle;
    private RadioGroup mRadioGroup;
    private TextInputEditText fullname, username;
    private MultiAutoCompleteTextView bio, hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayPic = findViewById(R.id.mDisplayPic);
        fullname = findViewById(R.id.mFullName);
        username = findViewById(R.id.mUserName);
        leader = findViewById(R.id.etConfirmLeader);
        disciple = findViewById(R.id.etConfirmDisciple);
        mRadioGroup = findViewById(R.id.titleLayout);
        bio = findViewById(R.id.mBio);
        hobby = findViewById(R.id.mHobbies);

        if (userDB.totalUsers() >= 1 && userDB.getUser(uid) != null) {
            UserObject userObject = userDB.getUser(uid);
            displayData(userObject);
        }

        findViewById(R.id.cancelEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.editDisplayPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        FloatingActionButton fab = findViewById(R.id.saveEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentData();
            }
        });
    }

    private void getCurrentData() {
        userObject.setFullname(fullname.getText().toString());
        userObject.setUsername(username.getText().toString());
        userObject.setHobby(hobby.getText().toString());
        userObject.setBio(bio.getText().toString());

        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        selectTitle = findViewById(selectedId);

        if(selectTitle.getText().toString().equals("Leader")){
            userObject.setTitle("leader");
        } else {
            userObject.setTitle("disciple");
        }

        saveEditToDb();
    }

    private void saveEditToDb() {
        // Todo: test if this replaces the image already there
        if (userObject.getDisplayPic() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");

            storageReference.putFile(Uri.parse(userObject.getDisplayPic()));
        }

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        assert userObject.getDisplayPic() != null;
        firebaseDatabase.getReference("users/"+uid+"/displayPic").setValue(userObject.getDisplayPic());

        firebaseDatabase.getReference("users/"+uid+"/fullname").setValue(userObject.getFullname());
        firebaseDatabase.getReference("users/"+uid+"/username").setValue(userObject.getUsername());
        firebaseDatabase.getReference("users/"+uid+"/title").setValue(userObject.getTitle());
        firebaseDatabase.getReference("users/"+uid+"/hobby").setValue(userObject.getHobby());
        firebaseDatabase.getReference("users/"+uid+"/bio").setValue(userObject.getBio()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    userDB.deleteUser(uid);

                    firebaseDatabase.getReference("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserObject userObject = dataSnapshot.getValue(UserObject.class);
                            assert userObject != null;
                            userDB.addUser(userObject);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(EditProfilePage.this, "Updated", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Log.d("EditProfTag", task.getException().toString());
                }
            }
        });
    }

    private void displayData(UserObject userObject) {
        fullname.setText(userObject.getFullname());
        username.setText(userObject.getUsername());
        hobby.setText(userObject.getHobby());
        bio.setText(userObject.getBio());

        if (userObject.getTitle().equals("leader")) {
            leader.toggle();
        } else {
            disciple.toggle();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("users/" + uid + "/displayPic");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).fit().centerCrop().into(displayPic);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Picasso.get().load(data.getData()).fit().centerCrop().into(displayPic);

            userObject.setDisplayPic(String.valueOf(data.getData()));
        }
    }
}
