package com.ms.projectlecturer.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Presence;

import java.util.List;

public class PresencesRecyclerViewAdapter extends RecyclerView.Adapter<PresencesRecyclerViewAdapter.ViewHolder> {

    private List<Presence> _presences;
    private LayoutInflater _inflater;
    private ItemClickListener _clickListener;

    // data is passed into the constructor
    public PresencesRecyclerViewAdapter(LayoutInflater inflater, List<Presence> presences) {
        _presences = presences;
        _inflater = inflater;
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
        Presence presence = _presences.get(position);
        TextView dayOfTheWeekTextView = holder.itemView.findViewById(R.id.dayOfTheWeekTextView);
        TextView timeTextView = holder.itemView.findViewById(R.id.timeTextView);
        TextView roomNumberTextView = holder.itemView.findViewById(R.id.roomNumberTextView);
        TextView buildingNameTextView = holder.itemView.findViewById(R.id.buildingNameTextView);

        dayOfTheWeekTextView.setText(presence.getDayOfTheWeek().toString());
        timeTextView.setText(presence.getStartTime() + " - " + presence.getEndTime());
        roomNumberTextView.setText(presence.getRoomNumber());
        buildingNameTextView.setText(presence.getBuildingName());



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