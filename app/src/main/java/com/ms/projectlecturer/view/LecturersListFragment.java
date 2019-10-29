package com.ms.projectlecturer.view;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ms.projectlecturer.R;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.util.RecyclerViewAdapter;

import java.util.List;


public class LecturersListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView _recyclerView;
    private RecyclerViewAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;
    private MapFragment _mapFragment;
    private LecturersActivity _lecturersActivity;
    private Spinner _spinner;
    private String[] _spinnerPaths;
    private LayoutInflater _inflater;
    private Button _allButton;
    private Button _topButton;
    private Button _addEnquireButton;
    private AlphaAnimation _buttonClick = new AlphaAnimation(1f, 0.8f);
    private List<String> _dataset;
    private ValueEventListener _currentListener;
    private Resources _resources;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               final int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private class ModuleItemClickListener implements  RecyclerViewAdapter.ItemClickListener {
        @Override
        public void onItemClick(View v,int pos) {

        }
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
        _spinnerPaths = new String[]{
                getResources().getString(R.string.All),
                getResources().getString(R.string.this_day),
                getResources().getString(R.string.this_week),
                getResources().getString(R.string.this_month),
                getResources().getString(R.string.top_spinner)};
        _lecturersActivity = (LecturersActivity) getActivity();
        _resources = getResources();
        _mapFragment = _lecturersActivity.getMapScreenFragment();
        _recyclerView = view.findViewById(R.id.food_recycler_view);
        _spinner = view.findViewById(R.id.spinner);
        _topButton = view.findViewById(R.id.top);
        _allButton = view.findViewById(R.id.all);
        _addEnquireButton = view.findViewById(R.id.add_enquire);
        _recyclerView.setHasFixedSize(true);
        _layoutManager = new LinearLayoutManager(getActivity());
        _recyclerView.setLayoutManager(_layoutManager);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, _spinnerPaths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);
        _spinner.setOnItemSelectedListener(this);
        _topButton.setOnClickListener(this);
        _allButton.setOnClickListener(this);
        _addEnquireButton.setOnClickListener(this);
        _buttonClick.setDuration(300);
    }

    @Override
    public void onClick(View view) {
        Log.wtf("chuj", "chuj");
        if (view == _topButton) {
            view.startAnimation(_buttonClick);
            _spinner.setSelection(4);
        } else if (view == _allButton) {
            view.startAnimation(_buttonClick);
            _spinner.setSelection(0);
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