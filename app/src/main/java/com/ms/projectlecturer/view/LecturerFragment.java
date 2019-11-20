package com.ms.projectlecturer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;
import com.ms.projectlecturer.util.LecturersRecyclerViewAdapter;
import com.ms.projectlecturer.util.PresencesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class LecturerFragment extends Fragment implements PresencesRecyclerViewAdapter.ItemClickListener,
        View.OnClickListener {


    //private String lecturerId;
    private RecyclerView recyclerView;
    private PresencesRecyclerViewAdapter adapter;
    private List<Presence> presences;
    private ValueEventListener currentListener;
    private DatabaseReference lecturerReference;
    private TextView lecturerStats;
    private LecturerFragment lecturerFragment;
    private LayoutInflater inflater;
    private MapFragment mapFragment;
    private LecturersActivity lecturersActivity;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_lecturer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lecturerFragment = this;
        lecturersActivity = (LecturersActivity) getActivity();
        mapFragment = lecturersActivity.getMapScreenFragment();
        recyclerView = view.findViewById(R.id.presencesRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(View view, int position) {
        Presence presence = presences.get(position);
        LatLng latLng = new LatLng(presence.getLat(), presence.getLng());
        mapFragment.addMarkerAt(latLng, presence.getBuildingName(),
                presence.getDayOfTheWeek() + " " + presence.getStartTime() + "-" +
                        presence.getEndTime());
        mapFragment.setLockedOnPlace(true);
        mapFragment.moveCamera(latLng, 15f);
        lecturersActivity.setCurrentFragment(mapFragment);
    }

    @Override
    public void onClick(View view) {

    }


    public void setLecturer(Lecturer lecturer) {
//        this.lecturerStats.setText(lecturer.toString(getContext()));
        if (currentListener != null) {
            lecturerReference.removeEventListener(currentListener);
        }
        lecturerReference = FirebaseDatabase.getInstance().getReference().child("Lecturers").child(lecturer.getLecturerId());
        currentListener = lecturerReference.child("presences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               presences = new ArrayList<>();
               for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                     Presence presence = childSnapshot.getValue(Presence.class);
                     presences.add(presence);
               }
                adapter = new PresencesRecyclerViewAdapter(inflater, presences, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setClickListener(lecturerFragment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

    }
}
