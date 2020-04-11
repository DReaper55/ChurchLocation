package com.example.churchlocation.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.example.churchlocation.Adapter.YoutubeThumbAdapter;
import com.example.churchlocation.Model.TubeThumbs;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConstantsRandom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends Fragment {

    private ArrayList<TubeThumbs> thumbsArrayList = new ArrayList<>();
    private YoutubeThumbAdapter youtubeThumbAdapter;

    RecyclerView recyclerView;

    View views;
    Context ctx;

    private boolean connected;

    public HomeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        views = container.getRootView();

        do{
            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            connected = activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
            if(connected){
                view = inflater.inflate(R.layout.activity_home, container, false);

                String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);

                List<String> randomIDArray = new ArrayList<>(Arrays.asList(videoIDArray));

                Collections.shuffle(randomIDArray);

                for (String s : randomIDArray) {
                    TubeThumbs tubeThumbs = new TubeThumbs();
                    tubeThumbs.setVideoId(s);

                    thumbsArrayList.add(tubeThumbs);
                }

                youtubeThumbAdapter = new YoutubeThumbAdapter(getActivity(), thumbsArrayList);
                Log.d("Network", "Connected");
                break;

            } else {
                Log.d("Network", "Not Connected");
                Toast.makeText(getContext(), "Cannot connect to internet", Toast.LENGTH_LONG).show();
                view = inflater.inflate(R.layout.progress_bar, container, false);
            }
        } while (!connected);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Hot News");

        if(connected){
            recyclerView = getActivity().findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(youtubeThumbAdapter);
        }

//        Menu menu = navView.getMenu();
//        menu.findItem(R.id.navigation_home).setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));

        SwipeRefreshLayout containerSwipe = views.findViewById(R.id.container);
        containerSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Refresh", "Refresh");
            }
        });

    }

}
