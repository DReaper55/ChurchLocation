package com.example.churchlocation.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecentChatsPage extends Fragment {
    private Button signOutButton, etSendVerification;
    private SavedUserDB savedUserDB;

    private View view;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String uid = firebaseAuth.getUid();
    private DatabaseReference titleStatus = firebaseDatabase.getReference("users/" + uid + "/titleVerification");


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "titleVerified";
    private static int LOAD_TIME = 1;

    public RecentChatsPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Todo: change this to save the verification status of the user instead
//        If the user is verified then the button to send verification
        preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (preferences != null) {
            LOAD_TIME = preferences.getInt("LOAD_TIME", LOAD_TIME);
        } else {
            editor = preferences.edit();
            editor.putInt("LOAD_TIME", LOAD_TIME);
            editor.apply();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(inflater.getContext()).inflate(R.layout.activity_recent_chats_page, null, false);

        view = rootView;

        signOutButton = rootView.findViewById(R.id.etSignOut);
        etSendVerification = rootView.findViewById(R.id.etSendVerification);

        etSendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmTitleAndStatusForVerification(0);
            }
        });

        confirmTitleAndStatusForVerification(1);

        Log.d("RecentTAG", String.valueOf(LOAD_TIME));

        checkTitleVerification();

        return rootView;
    }

//    "Condition" is to call only one action at a time
//    and prevent the onCreate action from being called several times

    private void confirmTitleAndStatusForVerification(final int condition) {

//        To confirm if user is a leader or disciple
        DatabaseReference confirmTitle = firebaseDatabase.getReference("users/" + uid + "/title");
        confirmTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue().toString().equals("leader")) {

//                        Get the country of the leader to add to the leaderVerification node
                        DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/leaderCountry");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    final String leaderCountry = dataSnapshot.getValue().toString();

                                    titleStatus.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                if (dataSnapshot.getValue().equals(false)) {
                                                    final DatabaseReference db = firebaseDatabase.getReference("leaderVerification/" + leaderCountry + "/" + uid + "/status");

                                                    sendVerificationRequest(db, condition);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else if (dataSnapshot.getValue().equals("disciple")) {

//                        Get the church of the user to use in creating the disciple request node
                        DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/church");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    final String userChurch = dataSnapshot.getValue().toString();

                                    titleStatus.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                if (dataSnapshot.getValue().equals(false)) {
                                                    DatabaseReference db = firebaseDatabase.getReference("discipleVerification/" + userChurch + "/" + uid + "/status");

                                                    sendVerificationRequest(db, condition);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
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


    //    The condition will help me determine if its a first time request or not
    private void sendVerificationRequest(final DatabaseReference db, final int condition) {

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (condition == 0) {
                        if (dataSnapshot.getValue().equals("declined")) {
                            db.setValue("pending");
                        }
                    }
                } else {
                    db.setValue("pending");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //    To add or remove the leader from admin
    private void checkTitleVerification() {
        titleStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean titleVerified = dataSnapshot.getValue(Boolean.class);

                if (titleVerified) {
                    DatabaseReference db = firebaseDatabase.getReference("users/" + uid);

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserObject userObject = dataSnapshot.getValue(UserObject.class);

                            firebaseDatabase.getReference("admins/" + uid).setValue(userObject);

                            Log.d("RecentTAG", userObject.getFullname());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                final DatabaseReference datab = firebaseDatabase.getReference("admins/" + uid);
//                String key = db.getKey();
//                Log.d("RecentTAG", key);


                datab.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/titleVerification");

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    boolean confirm = dataSnapshot.getValue(Boolean.class);
                                    if (!confirm) {
                                        datab.removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                if(isAdmin())
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedUserDB.deleteUsers();

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, new LoginPage());

//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.fragment_container, new LoginPage());
//                transaction.remove(RecentChatsPage.this);

                firebaseAuth.signOut();
            }
        });
    }

    @Override
    public void onDestroy() {
//        Todo: fix this
//        LOAD_TIME = LOAD_TIME + 1;
//        editor.putInt("LOAD_TIME", LOAD_TIME);
//        editor.apply();

        super.onDestroy();
    }
}
