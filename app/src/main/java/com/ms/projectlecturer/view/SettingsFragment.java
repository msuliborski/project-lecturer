package com.ms.projectlecturer.view;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ms.projectlecturer.R;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner _spinner;
    private String[] _spinnerPaths;
    private LecturersActivity _lecturersActivity;
    private MapFragment _mapFragment;
    private SharedPreferences _pref;
    private SharedPreferences.Editor _editor;
    private Configuration _conf;



    public Spinner getSpinner() {
        return _spinner;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _spinnerPaths = new String[] {
                "English",
                "Polski"};
        _spinner = view.findViewById(R.id.spinner_language);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, _spinnerPaths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);
        _spinner.setOnItemSelectedListener(this);
        _lecturersActivity = (LecturersActivity) getActivity();
        _mapFragment = _lecturersActivity.getMapScreenFragment();
        _pref = _lecturersActivity.getPref();
        _conf = _lecturersActivity.getConf();
        setLanguageOnSpinner(_conf.locale.getLanguage());
    }



    public void setLanguageOnSpinner(String lang) {
        switch (lang) {
            case "en":
                _spinner.setSelection(0);
                break;
            case "pl":
                _spinner.setSelection(1);
                break;
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                if (!_conf.locale.getLanguage().equals("en")) {
                    _editor = _pref.edit();
                    _editor.putString("lang_code", "en");
                    _editor.commit();
                    _lecturersActivity.setLocale("en");
                }
                break;
            case 1:
                if (!_conf.locale.getLanguage().equals("pl")) {
                    _editor = _pref.edit();
                    _editor.putString("lang_code", "pl");
                    _editor.commit();
                    Log.d("tag", "onComplete: " + _pref.getString("lang_code", "en"));
                    _lecturersActivity.setLocale("pl");
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}