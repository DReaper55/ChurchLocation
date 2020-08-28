package com.example.churchlocation.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Filterable;

import com.example.churchlocation.Adapter.UserListPageAdapter;
import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserListPage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String uid = firebaseAuth.getUid();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<UserObject> userObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_page);

//        getSupportActionBar().setElevation(10.0f);


        recyclerView = findViewById(R.id.userListPageRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

//        Log.d("UserListTAG",userDB.getUser(uid).getUsername());

        DatabaseReference db = firebaseDatabase.getReference("users/");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    UserObject userObject = dataSnapshot.getValue(UserObject.class);
//                    Log.d("UserListTag2", userObject.getUsername());

                    if (!userObject.getId().equals(uid)) {
                        userObjectList.add(userObject);
                        adapter = new UserListPageAdapter(getApplicationContext(), userObjectList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

//                    String id = userObject.getId();
//                    if(userDB.getUser(id).getUsername() == null){
//                        userDB.addUser(userObject);
//
//                        Log.d("UserListTag1", userObject.getUsername());
//
//
//                    }


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_lists_menu, menu);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Select Contact");
//        actionBar.setElevation(50.0f);

        MenuItem searchItem = menu.findItem(R.id.searchID);

        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();

        // change the keyboard search button
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                // to get the filter methods
//                filterable = (Filterable) adapter;
//                filterable.getFilter().filter(s);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
