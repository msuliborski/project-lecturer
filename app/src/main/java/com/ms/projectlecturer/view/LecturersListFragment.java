package com.ms.projectlecturer.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.R;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;
import com.ms.projectlecturer.util.LecturersAdapter;
import com.ms.projectlecturer.util.Spawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LecturersListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, LecturersAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private LecturersAdapter lecturersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LecturersActivity lecturersActivity;
    private LecturerFragment lecturerFragment;
    private LayoutInflater layoutInflater;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("");
    private List<Lecturer> lecturers;
    private LecturersListFragment lecturersListFragment = this;
    private SearchView lecturerSearchView;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onItemClick(View view, int position) {
        lecturerFragment.setLecturer(lecturersAdapter.getFilteredLecturers().get(position));
        lecturersActivity.setFragment(lecturerFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_lecturers_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lecturersActivity = (LecturersActivity) getActivity();
        lecturerFragment = (LecturerFragment) lecturersActivity.getLecturerFragment();
        recyclerView = view.findViewById(R.id.lecturersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        lecturerSearchView = view.findViewById(R.id.lecturerSearchView);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState(); //pause
                
                String favLecturers = "";
                for (DataSnapshot fav : dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favourites").getChildren()) {
                    favLecturers += fav.getKey() + ";";
                }

                lecturers = new ArrayList<>();
                for (DataSnapshot lecturerDataSnapshot : dataSnapshot.child("Lecturers").getChildren()) {
                    Map<String, Presence> presences = new HashMap<>();
                    for (DataSnapshot presencesDataSnapshot : lecturerDataSnapshot.child("presences").getChildren()){
                        presences.put(presencesDataSnapshot.getKey(), presencesDataSnapshot.getValue(Presence.class));
                    }
                    Lecturer lecturer = new Lecturer(
                            favLecturers.contains(lecturerDataSnapshot.child("lecturerID").getValue().toString()), lecturerDataSnapshot.getKey(), lecturerDataSnapshot.child("firstName").getValue().toString(),
                            lecturerDataSnapshot.child("lastName").getValue().toString(), lecturerDataSnapshot.child("title").getValue().toString(),
                            lecturerDataSnapshot.child("imageUrl").getValue().toString(), presences);
                    lecturers.add(lecturer);
                }

                Collections.sort(lecturers);
                lecturersAdapter = new LecturersAdapter(layoutInflater, lecturers, getContext());
                lecturerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String text) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        lecturersAdapter.getFilter().filter(text);
                        return true;
                    }
                });
                recyclerView.setAdapter(lecturersAdapter);
                lecturersAdapter.setClickListener(lecturersListFragment);
                
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState); //unpause
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) { }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
