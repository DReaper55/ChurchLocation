package com.example.churchlocation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchlocation.Activity.Hymn;
import com.example.churchlocation.Model.ListItem;
import com.example.churchlocation.R;

import java.util.ArrayList;
import java.util.List;


public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<ListItem> listItems;
    private List<ListItem> listItemList;

    public myAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
        listItemList = new ArrayList<>(listItems);
    }

    @Override
    public myAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.ViewHolder viewHolder, int i) {

        ListItem item = listItems.get(i);

        viewHolder.hymnNumber.setText(item.getHymnNumber());
        viewHolder.hymnTitle.setText(item.getHymnTitle());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listItemList);
            } else{
                String lowerFilter = constraint.toString().toLowerCase().trim();

                for(ListItem item : listItemList){
                    if(item.getHymnTitle().toLowerCase().contains(lowerFilter)){
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
            listItems.clear();
            listItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hymnNumber;
        private TextView hymnTitle;

         ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            hymnNumber = view.findViewById(R.id.hymnNumber);
            hymnTitle = view.findViewById(R.id.hymnTitle);

        }

        @Override
        public void onClick(View v) {

             int position = getAdapterPosition();

             ListItem item = listItems.get(position);

            Intent intent = new Intent(context, Hymn.class);
            intent.putExtra("number", String.valueOf(item.getHymnNumber()));
            intent.putExtra("title", item.getHymnTitle());
            context.startActivity(intent);

        }
    }
}
