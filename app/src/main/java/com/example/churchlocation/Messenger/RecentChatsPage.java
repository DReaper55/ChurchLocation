package com.example.churchlocation.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.churchlocation.Database.SavedUserDB;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;

public class RecentChatsPage extends Fragment {
    private Button button;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SavedUserDB savedUserDB;

    private View view;

    public RecentChatsPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(inflater.getContext()).inflate(R.layout.activity_recent_chats_page,null,false);

        savedUserDB = new SavedUserDB(rootView.getContext());

        view = rootView;

        button = rootView.findViewById(R.id.etSignOut);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button.setOnClickListener(new View.OnClickListener() {
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
}
