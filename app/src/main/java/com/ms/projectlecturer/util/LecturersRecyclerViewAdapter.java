package com.ms.projectlecturer.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ms.projectlecturer.R;

import java.util.List;

public class LecturersRecyclerViewAdapter extends RecyclerView.Adapter<LecturersRecyclerViewAdapter.ViewHolder> {

    private List<String> _data;
    private LayoutInflater _inflater;
    private ItemClickListener _clickListener;

    // data is passed into the constructor
    public LecturersRecyclerViewAdapter(LayoutInflater inflater, List<String> data) {
        _data = data;
        _inflater = inflater;
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
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return _data.size();
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
    String getItem(int id) {
        return _data.get(id);
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