package com.ms.projectlecturer.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Presence;

import java.util.List;

public class PresencesRecyclerViewAdapter extends RecyclerView.Adapter<PresencesRecyclerViewAdapter.ViewHolder> {

    private List<Presence> _presences;
    private LayoutInflater _inflater;
    private ItemClickListener _clickListener;
    private Context _context;

    // data is passed into the constructor
    public PresencesRecyclerViewAdapter(LayoutInflater inflater, List<Presence> presences, Context context) {
        _presences = presences;
        _inflater = inflater;
        _context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.element_presence, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView string = holder.itemView.findViewById(R.id.string);
        string.setText(_presences.get(position).toString(_context));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return _presences.size();
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
    Presence getItem(int id) {
        return _presences.get(id);
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