package com.example.churchlocation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchlocation.Activity.MapsActivity;
import com.example.churchlocation.Activity.SearchedMapActivity;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SearchChurchesAdapter extends RecyclerView.Adapter<SearchChurchesAdapter.ViewModel> implements Filterable {
    private Context context;
    private List<SearchChurchModel> searchChurchModels;
    private List<SearchChurchModel> searchChurchModelListFilter;


    public SearchChurchesAdapter(Context context, List<SearchChurchModel> searchChurchModels) {
        this.context = context;
        this.searchChurchModels = searchChurchModels;
        this.searchChurchModelListFilter = new ArrayList<>(searchChurchModels);
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_locations, null);

        context = view.getContext();
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewModel holder, final int position) {
        holder.mSearchedLocationTextView.setText(searchChurchModels.get(position).getChurchName());

        String country = searchChurchModels.get(position).getCountry();
        if(country.equals("Nigeria")){
            holder.searchesId.setBackgroundResource(R.drawable.song_book);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String churchName = searchChurchModels.get(position).getChurchName();

                Intent intent = new Intent(context.getApplicationContext(), SearchedMapActivity.class);
                intent.putExtra("shower", churchName);
                context.startActivity(intent);

            }
        });

        Log.d("Fragment", searchChurchModels.get(position).getChurchLocation().toString());

    }

    @Override
    public int getItemCount() {
        return searchChurchModels.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView mSearchedLocationTextView;
        ImageView searchesId;

        public ViewModel(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.mSearchedLocation);
            mSearchedLocationTextView = itemView.findViewById(R.id.mSearchedLocationTextView);
            searchesId = itemView.findViewById(R.id.churchID);
        }
    }
}
