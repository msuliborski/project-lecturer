package com.ms.projectlecturer.view;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ms.projectlecturer.R;

import java.util.Objects;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner languageSpinner;
    private LecturersActivity lecturersActivity;
    private SharedPreferences sharedPreferences;
    private Configuration configuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] spinnerPaths = new String[]{
                this.getResources().getString(R.string.languageEnglish),
                getResources().getString(R.string.languagePolish)};

        languageSpinner = view.findViewById(R.id.languageSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, spinnerPaths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(this);
        lecturersActivity = (LecturersActivity) getActivity();
        sharedPreferences = lecturersActivity.getPref();
        configuration = lecturersActivity.getConf();

        setLanguageOnSpinner(configuration.locale.getLanguage());
    }

    private void setLanguageOnSpinner(String lang) {
        switch (lang) {
            case "en":
                languageSpinner.setSelection(0);
                break;
            case "pl":
                languageSpinner.setSelection(1);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor;
        switch (position) {
            case 0:
                if (!configuration.locale.getLanguage().equals("en")) {
                    editor = sharedPreferences.edit();
                    editor.putString("language", "en");
                    editor.apply();
                    lecturersActivity.setLocale("en");
                }
                break;
            case 1:
                if (!configuration.locale.getLanguage().equals("pl")) {
                    editor = sharedPreferences.edit();
                    editor.putString("language", "pl");
                    editor.apply();
                    lecturersActivity.setLocale("pl");
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
