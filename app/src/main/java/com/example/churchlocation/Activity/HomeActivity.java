package com.example.churchlocation.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.churchlocation.Adapter.YoutubeThumbAdapter;
import com.example.churchlocation.Database.ChurchesDB;
import com.example.churchlocation.Database.LinksDB;
import com.example.churchlocation.Model.TubeThumbs;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.google.firebase.FirebaseApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends Fragment {

    private ArrayList<TubeThumbs> thumbsArrayList = new ArrayList<>();
    private YoutubeThumbAdapter youtubeThumbAdapter;

    private List<String> linkLists;

    private ChurchesDB db;
    private LinksDB linksDB;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    private View views;
    Context ctx;

    private boolean connected = false;

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

        connected = checkConnection();

        db = connect.getChurches(inflater.getContext());
        linksDB = connect.getLinks(inflater.getContext());

        view = showHomePage(connected, container, inflater);

        SwipeRefreshLayout containerSwipe = view.findViewById(R.id.container);
        containerSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().recreate();
            }
        });

        Log.d("FirstActtt ", String.valueOf(db.totalContact()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Hot News");

        if(connected){
            RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

            recyclerView.setAdapter(youtubeThumbAdapter);
        }

//        Menu menu = navView.getMenu();
//        menu.findItem(R.id.navigation_home).setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));
    }

    private Boolean checkConnection(){
        Boolean connection;
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        connection = activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        return connection;
    }

    private View showHomePage(Boolean connect, ViewGroup container, LayoutInflater inflater){
        if(connect){
            linkLists = linksDB.getAllContacts();

            Collections.shuffle(linkLists);

            for (String s : linkLists) {
                TubeThumbs tubeThumbs = new TubeThumbs();
                tubeThumbs.setVideoId(s);

                thumbsArrayList.add(tubeThumbs);
            }
            youtubeThumbAdapter = new YoutubeThumbAdapter(getActivity(), thumbsArrayList);
            Log.d("Network", "Connected");

            return inflater.inflate(R.layout.activity_home, container, false);
        } else {
            Toast.makeText(getContext(), "Cannot connect to internet", Toast.LENGTH_LONG).show();
            return inflater.inflate(R.layout.progress_bar, container, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        connect.getChurches(views.getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        Log.d("FirstActtt ", String.valueOf(db.totalContact()));
    }

    @Override
    public void onStop() {
//        linkLists.clear();
        super.onStop();
    }
}
