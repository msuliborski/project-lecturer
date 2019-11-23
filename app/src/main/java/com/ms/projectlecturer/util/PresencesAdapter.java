package com.ms.projectlecturer.util;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PresencesAdapter extends RecyclerView.Adapter<PresencesAdapter.ViewHolder> implements Filterable {

    private List<Presence> allPresences;
    @Getter
    private List<Presence> filteredPresences;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private Context context;
    private Resources resources;

    // data is passed into the constructor
    public PresencesAdapter(LayoutInflater inflater, List<Presence> presences, Context context) {
        this.allPresences = presences;
        this.filteredPresences = presences;
        Collections.sort(filteredPresences);
        this.layoutInflater = inflater;
        this.context = context;
        resources = context.getResources();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.element_presence, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Presence presence = filteredPresences.get(position);
        TextView dayOfTheWeekTextView = holder.itemView.findViewById(R.id.dayOfTheWeekTextView);
        TextView timeTextView = holder.itemView.findViewById(R.id.timeTextView);
        TextView roomNumberTextView = holder.itemView.findViewById(R.id.roomNumberTextView);
        TextView buildingNameTextView = holder.itemView.findViewById(R.id.buildingNameTextView);
        int dayOfWeekId = resources.getIdentifier(presence.getDayOfTheWeek().getLabel(), "string", context.getPackageName());
        dayOfTheWeekTextView.setText(resources.getString(dayOfWeekId));
        timeTextView.setText(presence.getStartTime() + " - " + presence.getEndTime());
        roomNumberTextView.setText(presence.getRoomNumber());
        buildingNameTextView.setText(presence.getBuildingName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return filteredPresences.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredPresences = (List<Presence>) results.values;
                Collections.sort(filteredPresences);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Presence> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = new ArrayList<>();
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }


    protected List<Presence> getFilteredResults(String selectedDaysString) {
        List<Presence> results = new ArrayList<>();
        for (Presence p : allPresences) {
            if (selectedDaysString.toUpperCase().contains(p.getDayOfTheWeek().getLabel().toUpperCase())) {
                results.add(p);
            }
        }
        return results;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Presence getItem(int id) {
        return filteredPresences.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}