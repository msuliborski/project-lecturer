package com.ms.projectlecturer.view;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.R;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;
import com.ms.projectlecturer.util.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LecturersListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        RecyclerViewAdapter.ItemClickListener {

    private RecyclerView _recyclerView;
    private RecyclerViewAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;
    private MapFragment _mapFragment;
    private LecturersActivity _lecturersActivity;
    private LecturerFragment _lecturerFragment;
    private LayoutInflater _inflater;
    private Button _allButton;
    private Button _topButton;
    private Button _addEnquireButton;
    private AlphaAnimation _buttonClick = new AlphaAnimation(1f, 0.8f);
    private List<String> _dataset;
    private ValueEventListener _currentListener;
    private Resources _resources;
    private DatabaseReference _lecturersReference = FirebaseDatabase.getInstance().getReference("Lecturers");
    private List<Lecturer> _lecturers;
    private LecturersListFragment _lecturersListFragment = this;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               final int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onItemClick(View view, int position) {
        _lecturerFragment.setLecturer(_lecturers.get(position));
        _lecturersActivity.setCurrentFragment(_lecturerFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _inflater = inflater;
        return inflater.inflate(R.layout.fragment_lecturers_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _lecturersActivity = (LecturersActivity) getActivity();
        _lecturerFragment = (LecturerFragment) _lecturersActivity.getLecturerFragment();
        _resources = getResources();
        _mapFragment = _lecturersActivity.getMapScreenFragment();
        _recyclerView = view.findViewById(R.id.lecturersRecyclerView);
        _recyclerView.setHasFixedSize(true);
        _layoutManager = new LinearLayoutManager(getActivity());
        _recyclerView.setLayoutManager(_layoutManager);

        _buttonClick.setDuration(300);

        _lecturersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _dataset = new ArrayList<>();
                _lecturers = new ArrayList<>();
                for (DataSnapshot lecturerDataSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Presence> presences = new HashMap<>();
                    for (DataSnapshot presencesDataSnapshot : lecturerDataSnapshot.child("presences").getChildren()){
                        presences.put(presencesDataSnapshot.getKey(), presencesDataSnapshot.getValue(Presence.class));
                    }
                    Lecturer lecturer = new Lecturer(lecturerDataSnapshot.getKey(), lecturerDataSnapshot.child("firstName").getValue().toString(),
                            lecturerDataSnapshot.child("lastName").getValue().toString(), lecturerDataSnapshot.child("title").getValue().toString(), presences);
                    _lecturers.add(lecturer);
                }
                Collections.sort(_lecturers);
                Collections.reverse(_lecturers);
                for (Lecturer l : _lecturers) {
                    _dataset.add(l.toString(getContext()));
                }
                _adapter = new RecyclerViewAdapter(_inflater, _dataset);
                _recyclerView.setAdapter(_adapter);
                _adapter.setClickListener(_lecturersListFragment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == _topButton) {
            view.startAnimation(_buttonClick);
        } else if (view == _allButton) {
            view.startAnimation(_buttonClick);
        } else if (view == _addEnquireButton) {
            _lecturersActivity.setCurrentFragment(_mapFragment);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }
}