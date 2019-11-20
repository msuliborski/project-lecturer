package com.ms.projectlecturer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.util.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LecturerFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener,
        View.OnClickListener {


    //private String lecturerId;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> dataset;
    private List<String> presencesIds;
    private ValueEventListener currentListener;
    private DatabaseReference lecturerReference;
    private TextView lecturerStats;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecturer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {

    }


    public void setLecturer(DatabaseReference lecturerReference, String lecturerStats) {
        this.lecturerStats.setText(lecturerStats);
        if (currentListener != null) {
            lecturerReference.removeEventListener(currentListener);
        }
        this.lecturerReference = lecturerReference;
        currentListener = lecturerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataset = new ArrayList<>();
                presencesIds = new ArrayList<>();
//               for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                     e = childSnapshot.getValue(Enquire.class);
//                    if (e.getType() == _moduleType) {
//                        _dataset.add(e.toString());
//                        _enquiresIDs.add(childSnapshot.getKey());
//                    }
//                }
//                Collections.reverse(_enquiresIDs);
//                Collections.reverse(_dataset);
//                _adapter = new RecyclerViewAdapter(_inflater, _dataset);
//                _recyclerView.setAdapter(_adapter);
//                _adapter.setClickListener(_moduleItemClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

    }
}
