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

    private String country;


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

        country = searchChurchModels.get(position).getCountry();
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
    }

    @Override
    public int getItemCount() {
        return searchChurchModels.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchChurchModel> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(searchChurchModelListFilter);
            } else{
                String lowerFilter = constraint.toString().toLowerCase().trim();

                for(SearchChurchModel item : searchChurchModelListFilter){
                    if(item.getChurchName().toLowerCase().contains(lowerFilter)
                            || item.getCountry().toLowerCase().contains(lowerFilter)
                            || item.getState().toLowerCase().contains(lowerFilter)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchChurchModels.clear();
            searchChurchModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
