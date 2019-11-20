package com.ms.projectlecturer.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Lecturer;

import java.util.List;

public class LecturersRecyclerViewAdapter extends RecyclerView.Adapter<LecturersRecyclerViewAdapter.ViewHolder> {

    private List<Lecturer> _lecturers;
    private LayoutInflater _inflater;
    private ItemClickListener _clickListener;
    private Context _context;

    // data is passed into the constructor
    public LecturersRecyclerViewAdapter(LayoutInflater inflater, List<Lecturer> lecturers, Context context) {
        _lecturers = lecturers;
        _inflater = inflater;
        _context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.element_lecturer, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TextView string = holder.itemView.findViewById(R.id.string);
        //string.setText(_presences.get(position).toString(_context));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return _lecturers.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (_clickListener != null) _clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Lecturer getItem(int id) {
        return _lecturers.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        _clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}