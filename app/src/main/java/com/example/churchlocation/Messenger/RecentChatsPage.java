package com.example.churchlocation.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Activity.MyProfilePage;
import com.example.churchlocation.Adapter.RecentChatsAdapter;
import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.ChatMessage;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentChatsPage extends Fragment {
    private SavedUserDB savedUserDB;

    private View view;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String uid = firebaseAuth.getUid();
    private DatabaseReference titleStatus = firebaseDatabase.getReference("users/" + uid + "/titleVerification");


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "titleVerified";
    private static final String PREFS_NAME_STATUS = "userStatus";

    ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    private MenuItem verifiedButton;

    public RecentChatsPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recent Chats");

//        Todo: change this to save the verification status of the user instead
//        If the user is verified then the button to send verification
        // check OneNote for further details

//        if (preferences != null) {
//        } else {
//            editor = preferences.edit();
//            editor.putInt("LOAD_TIME", LOAD_TIME);
//            editor.apply();
//        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(inflater.getContext()).inflate(R.layout.activity_recent_chats_page, null, false);

        view = rootView;

        savedUserDB = new SavedUserDB(view.getContext());

        setHasOptionsMenu(true);

        loadRecentMessages();

        // The 1 will only be used to show its first call and create request node
        // After getting the title of the user
        confirmTitleAndStatusForVerification(1);

        return rootView;
    }

    private void loadRecentMessages() {
        // Recycler for displaying user chats
        recyclerView = view.findViewById(R.id.recentChatsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        if(chatMessageArrayList.size() == 0){
//            recyclerView.setVisibility(View.GONE);
//        } else {
//            recyclerView.setVisibility(View.VISIBLE);
//            view.findViewById(R.id.relateLayoutId).setVisibility(View.GONE);
//        }

        DatabaseReference db = firebaseDatabase.getReference("latestMessage/" + uid);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.relateLayoutId).setVisibility(View.GONE);

                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatMessageArrayList.add(chatMessage);
                    adapter = new RecentChatsAdapter(getContext(), chatMessageArrayList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
                    adapter.notifyDataSetChanged();

                    Log.d("RecentTag0", chatMessage.getText());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                recyclerView.removeAllViews();
                chatMessageArrayList.clear();
                chatMessageArrayList.add(chatMessage);
                adapter = new RecentChatsAdapter(getContext(), chatMessageArrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.recent_chats_menu, menu);

        verifiedButton = menu.findItem(R.id.verifyUserMenu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        switch (menu_id) {
            case R.id.myProfileMenu:
                startActivity(new Intent(view.getContext(), MyProfilePage.class));

                break;
            case R.id.userListMenu:
//                Toast.makeText(getContext(), "User List", Toast.LENGTH_LONG).show();

                startActivity(new Intent(view.getContext(), UserListPage.class));
                break;
            case R.id.verifyUserMenu:

                confirmTitleAndStatusForVerification(0);

                break;
            case R.id.settingsMenu:
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOutMenu:

                savedUserDB.deleteUser(uid);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, new LoginPage());

//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.fragment_container, new LoginPage());
//                transaction.remove(RecentChatsPage.this);

                firebaseAuth.signOut();

                break;

        }


        return super.onOptionsItemSelected(item);
    }


//    "Condition" is to call only one action at a time
//    and prevent the onCreate action from being called several times
//    and changing the value of "status" back to 'pending' if it was 'declined'

    private void confirmTitleAndStatusForVerification(final int condition) {

//        To confirm if user is a leader or disciple
        DatabaseReference confirmTitle = firebaseDatabase.getReference("users/" + uid + "/title");
        confirmTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue().toString().equals("leader")) {

                        Log.d("RecentTAG3", "Here");

//                        Get the country of the leader to add to the leaderVerification node
                        DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/leaderCountry");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("RecentTAG4", "Here");

                                if (dataSnapshot.exists()) {
                                    final String leaderCountry = dataSnapshot.getValue().toString();

                                    checkTitleVerification(leaderCountry, 1, condition);

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

                                    checkTitleVerification(userChurch, 0, condition);

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


    //    The status will help determine if user is a leader or disciple
    private void sendVerificationRequest(final String data, final int status, final int condition) {
        final DatabaseReference db;
        if (status == 1) {
            db = firebaseDatabase.getReference("leaderVerification/" + data + "/" + uid);
        } else {
            db = firebaseDatabase.getReference("discipleVerification/" + data + "/" + uid);
        }

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (status == 1) {
                        final DatabaseReference datab = firebaseDatabase.getReference("leaderVerification/" + data + "/" + uid + "/status");
                        datab.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue().equals("declined")) {

                                    // condition will confirm that it is coming from the button pressed
                                    if (condition == 0) {
                                        datab.setValue("pending");

                                        verifiedButton.setVisible(true);
                                        verifiedButton.setEnabled(false);
                                        verifiedButton.setCheckable(false);

                                    } else if (condition == 1) {
                                        verifiedButton.setVisible(true);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (status == 0) {
                        final DatabaseReference datab = firebaseDatabase.getReference("discipleVerification/" + data + "/" + uid + "/status");
                        datab.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue().equals("declined")) {
                                    if (condition == 0) {
                                        datab.setValue("pending");

                                        verifiedButton.setVisible(true);
                                        verifiedButton.setEnabled(false);
                                        verifiedButton.setCheckable(false);
                                        verifiedButton.setTitle("pending verification...");

                                    } else if (condition == 1) {
                                        verifiedButton.setVisible(true);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                } else {
                    DatabaseReference userRequestRef = firebaseDatabase.getReference("users/" + uid);
                    userRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserObject userObject = dataSnapshot.getValue(UserObject.class);

                            if (status == 0) {
                                db.setValue(new UserObject(userObject.getFullname(), userObject.getEmail(), userObject.getChurch(), uid, "pending", userObject.getUsername()));
                            } else if (status == 1) {
                                db.setValue(new UserObject(userObject.getFullname(), userObject.getEmail(), userObject.getChurch(), uid, data, "pending", userObject.getUsername()));

                            }

                            verifiedButton.setVisible(true);
                            verifiedButton.setEnabled(false);
                            verifiedButton.setCheckable(false);
                            verifiedButton.setTitle("pending verification...");
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

    //    To add or remove the leader from admin
    //    Then remove the user form the notifications node
    //    But first confirm if the user's title is verified or not
    private void checkTitleVerification(final String data, final int status, final int condition) {
        Log.d("RecentTAG2", "Here");


        titleStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("RecentTAG1", "Here");


                String titleVerified = dataSnapshot.getValue(String.class);

                if (titleVerified.equals("true")) {
                    verifiedButton.setVisible(false);

                    DatabaseReference db = firebaseDatabase.getReference("users/" + uid);

                    // 'status' will tell if user is a leader(1) or disciple(0) which is set after confirming title
                    if (status == 1) {
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("RecentTAG0", "Here");

                                UserObject userObject = dataSnapshot.getValue(UserObject.class);

                                firebaseDatabase.getReference("admins/" + uid).setValue(userObject);

                                // Go to request node and remove user from it
                                DatabaseReference leaderCountryRef = firebaseDatabase.getReference("leaderVerification/" + data + "/" + uid);
                                leaderCountryRef.removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (status == 0) {
                        DatabaseReference leaderCountryRef = firebaseDatabase.getReference("discipleVerification/" + data + "/" + uid);
                        leaderCountryRef.removeValue();
                    }

                } else {
                    verifiedButton.setVisible(true);
                    verifiedButton.setEnabled(false);
                    verifiedButton.setCheckable(false);
                    verifiedButton.setTitle("pending verification...");

                    sendVerificationRequest(data, status, condition);
                }

                final DatabaseReference datab = firebaseDatabase.getReference("admins/" + uid);
//                String key = db.getKey();
//                Log.d("RecentTAG", key);


                // To remove the leader from admin if not verified
                datab.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/titleVerification");

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String confirm = dataSnapshot.getValue(String.class);
                                    if (confirm.equals("false")) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        TextView startNewChatText = view.findViewById(R.id.startNewChatID);
        ImageView startNewChatImage = view.findViewById(R.id.startNewChatImage);

        startNewChatText.setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(view.getContext(), UserListPage.class));
        });

        startNewChatImage.setOnClickListener(view -> view.getContext().startActivity(new Intent(view.getContext(), UserListPage.class)));

        preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // To refresh the user's sqlite data with firebase data
        savedUserDB.deleteUser(uid);

        firebaseDatabase.getReference("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserObject userObject = dataSnapshot.getValue(UserObject.class);
                assert userObject != null;
                savedUserDB.addUser(userObject);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        if (preferences != null && verifiedButton != null) {
//            String title = preferences.getString(PREFS_NAME_STATUS, "");
//
//            Log.d("RecentTag", title);
//            switch (title) {
//                case "verified":
//                    verifiedButton.setVisible(false);
//                    break;
//                case "pending":
//                    verifiedButton.setVisible(true);
//                    verifiedButton.setEnabled(false);
//                    verifiedButton.setCheckable(false);
//                    break;
//                case "declined":
//                    verifiedButton.setVisible(true);
//                    break;
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recent Chats");

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
