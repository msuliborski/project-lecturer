package com.ms.projectlecturer.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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
import com.ms.projectlecturer.util.PresencesAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class LecturerFragment extends Fragment implements PresencesAdapter.ItemClickListener, View.OnClickListener {

    private TextView lecturerTitleTextView;
    private TextView lecturerFirstNameTextView;
    private TextView lecturerLastNameTextView;
    private Resources resources;
    private RecyclerView recyclerView;
    private PresencesAdapter presencesAdapter;
    private Lecturer lecturer;
    private List<Presence> presences;
    private ValueEventListener currentListener;
    private DatabaseReference lecturerReference;
    private LecturerFragment lecturerFragment;
    private LayoutInflater layoutInflater;
    private MapFragment mapFragment;
    private LecturersActivity lecturersActivity;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialDayPicker dayPicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_lecturer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lecturerFragment = this;
        lecturersActivity = (LecturersActivity) getActivity();
        mapFragment = lecturersActivity.getMapFragment();
        recyclerView = view.findViewById(R.id.presencesRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        resources = getContext().getResources();
        lecturerTitleTextView = view.findViewById(R.id.lecturerTitleTextView);
        lecturerFirstNameTextView = view.findViewById(R.id.lecturerFirstNameTextView);
        lecturerLastNameTextView = view.findViewById(R.id.lecturerLastNameTextView);
        dayPicker = view.findViewById(R.id.dayPicker);
        Locale locale = lecturersActivity.getConf().locale;
        dayPicker.setLocale(locale);
        dayPicker.setDaySelectionChangedListener(selectedDays -> {
            String selectedDaysString = "";
            for (MaterialDayPicker.Weekday day : selectedDays){
                selectedDaysString += day.toString();
            }
            presencesAdapter.getFilter().filter(selectedDaysString);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Presence presence = presencesAdapter.getFilteredPresences().get(position);
        LatLng latLng = new LatLng(presence.getLat(), presence.getLng());
        int dayOfWeekId = resources.getIdentifier(presence.getDayOfTheWeek().getLabel(), "string", getContext().getPackageName());
        mapFragment.addMarkerAt(latLng, presence.getBuildingName(),
                resources.getString(dayOfWeekId) + " " + presence.getStartTime() + "-" +
                        presence.getEndTime());
        mapFragment.moveCamera(latLng, 15f);
        lecturersActivity.setFragment(mapFragment);
    }

    @Override
    public void onClick(View view) { }

    public void setLecturer(Lecturer lecturer) {
        if (currentListener != null) {
            lecturerReference.removeEventListener(currentListener);
        }
        lecturerReference = FirebaseDatabase.getInstance().getReference().child("Lecturers").child(lecturer.getLecturerId());
        lecturerTitleTextView.setText(lecturer.getTitle());
        lecturerFirstNameTextView.setText(lecturer.getFirstName());
        lecturerLastNameTextView.setText(lecturer.getLastName());
        currentListener = lecturerReference.child("presences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               presences = new ArrayList<>();
               for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                     Presence presence = childSnapshot.getValue(Presence.class);
                     presences.add(presence);
               }
                Collections.sort(presences);
                presencesAdapter = new PresencesAdapter(layoutInflater, presences, getContext());
                recyclerView.setAdapter(presencesAdapter);
                dayPicker.selectDay(MaterialDayPicker.Weekday.valueOf(new GregorianCalendar().getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.ENGLISH).toUpperCase()));
                presencesAdapter.setClickListener(lecturerFragment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

    }
}
