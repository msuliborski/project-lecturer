package com.ms.projectlecturer.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Lecturer;

import java.util.ArrayList;
import java.util.List;

public class LecturersAdapter extends RecyclerView.Adapter<LecturersAdapter.ViewHolder> implements Filterable {

    private List<Lecturer> allLecturers;
    private List<Lecturer> filteredLecturers;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private Context context;

    // data is passed into the constructor
    public LecturersAdapter(LayoutInflater inflater, List<Lecturer> lecturers, Context context) {
        this.allLecturers = lecturers;
        this.filteredLecturers = lecturers;
        this.layoutInflater = inflater;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.element_lecturer, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lecturer lecturer = filteredLecturers.get(position);

        TextView lecturerTitleTextView = holder.itemView.findViewById(R.id.lecturerTitleTextView);
        TextView lecturerFirstNameTextView = holder.itemView.findViewById(R.id.lecturerFirstNameTextView);
        TextView lecturerLastNameTextView = holder.itemView.findViewById(R.id.lecturerLastNameTextView);

        lecturerTitleTextView.setText(lecturer.getTitle());
        lecturerFirstNameTextView.setText(lecturer.getFirstName());
        lecturerLastNameTextView.setText(lecturer.getLastName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return filteredLecturers.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredLecturers = (List<Lecturer>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Lecturer> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = allLecturers;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }


    protected List<Lecturer> getFilteredResults(String constraint) {
        List<Lecturer> results = new ArrayList<>();

        for (Lecturer l : allLecturers) {
            String ls = l.getFirstName() + l.getLastName();
            if (ls.toLowerCase().contains(constraint)) {
                results.add(l);
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
    Lecturer getItem(int id) {
        return filteredLecturers.get(id);
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