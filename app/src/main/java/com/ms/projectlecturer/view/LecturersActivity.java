package com.ms.projectlecturer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.DayOfTheWeek;
import com.ms.projectlecturer.model.Presence;
import com.ms.projectlecturer.util.Spawner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LecturersActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;
    private LecturersListFragment lecturersListFragment = new LecturersListFragment();
    private LecturerFragment lecturerFragment = new LecturerFragment();
    private MapFragment mapFragment = new MapFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private CreditsFragment creditsFragment = new CreditsFragment();
    private SidebarFragment sidebarFragment = new SidebarFragment();

    private SharedPreferences sharedPreferences;
    private Configuration configuration;
    private Resources resources;

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
    public Fragment getLecturersListFragment() {
        return lecturersListFragment;
    }
    public Fragment getLecturerFragment() {
        return lecturerFragment;
    }
    public MapFragment getMapFragment() {
        return mapFragment;
    }
    public ProfileFragment getProfileFragment () {
        return profileFragment;
    }
    public SettingsFragment getSettingsFragment () {
        return settingsFragment;
    }
    public Fragment getCreditsFragment() {
        return creditsFragment;
    }

    public SharedPreferences getPref() {
        return sharedPreferences;
    }
    public Configuration getConf() {
        return configuration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturers);

//        populateDatabase();

        sharedPreferences = getApplicationContext().getSharedPreferences("global", MODE_PRIVATE);
        resources = getResources();
        configuration = resources.getConfiguration();
        if (sharedPreferences.contains("language")) {
            String defaultLanguage = sharedPreferences.getString("language", "en");
            String currentLanguage = configuration.locale.getLanguage();
            if (!currentLanguage.equals(defaultLanguage)) {
                setLocale(defaultLanguage);
            }
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_area, sidebarFragment);
        fragmentTransaction.add(R.id.screen_area, lecturersListFragment);
        fragmentTransaction.add(R.id.screen_area, lecturerFragment);
        fragmentTransaction.add(R.id.screen_area, mapFragment);
        fragmentTransaction.add(R.id.screen_area, profileFragment);
        fragmentTransaction.add(R.id.screen_area, settingsFragment);
        fragmentTransaction.add(R.id.screen_area, creditsFragment);
        fragmentTransaction.hide(lecturerFragment);
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.hide(settingsFragment);
        fragmentTransaction.hide(creditsFragment);
        currentFragment = lecturersListFragment;
        fragmentTransaction.commit();
    }

    public void setFragment(Fragment newFragment) {
        if (newFragment != null) {
            if(newFragment != currentFragment) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(currentFragment);
                currentFragment = newFragment;
                fragmentTransaction.show(currentFragment);
                fragmentTransaction.commit();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
        Intent refresh = new Intent(this, LecturersActivity.class);
        startActivity(refresh);
        finish();
    }

    public void populateDatabase(){
        // LODEX (B9)           51.747230, 19.453853
        // CTI (B7)             51.747002, 19.455931
        // Ins. Fizyki (B14)    51.746546, 19.455403

        Map<String, Presence> ABPresences = new HashMap<>();
        ABPresences.put("230", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("30", new Presence(DayOfTheWeek.Monday, "10:15:00", "12:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("0", new Presence(DayOfTheWeek.Monday, "12:15:00", "14:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("24", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        ABPresences.put("4", new Presence(DayOfTheWeek.Friday, "12:15:00", "14:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        Spawner.spawnNewLecturer("Aaaaa", "Bbbbbb", "dr. inż.", ABPresences);

        Map<String, Presence> CDPresences = new HashMap<>();
        CDPresences.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        CDPresences.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        CDPresences.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        CDPresences.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        CDPresences.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        Spawner.spawnNewLecturer("Cccccc", "Dddddd", "dr. inż.", CDPresences);

        Map<String, Presence> EFPresences = new HashMap<>();
        EFPresences.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        EFPresences.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        EFPresences.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        EFPresences.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        EFPresences.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        Spawner.spawnNewLecturer("Eeeee", "Fffff", "dr. inż.", EFPresences);

        Map<String, Presence> GHPresences = new HashMap<>();
        GHPresences.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        GHPresences.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        GHPresences.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        GHPresences.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        GHPresences.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "CTI (B7)", 51.747002, 19.455931 ));
        Spawner.spawnNewLecturer("Gggggg", "Hhhhhh", "dr. inż.", GHPresences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        if (currentFragment == _mapScreenFragment) {
//            if (_mapScreenFragment.getCurrentFragment() == _answerCreator) {
//                setCurrentFragment(_enquireViewFragment.getPreviousFragment());
//            } else {
//                super.onBackPressed();
//            }
//        } else if (_currentFragment == _foodModuleFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            _mapScreenFragment.setLockedOnPlace(false);
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _creditsFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            _mapScreenFragment.setLockedOnPlace(false);
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _enquireCreatorFragment) {
//            setCurrentFragment(_enquireCreatorFragment.getPreviousFragment());
//        } else if (_currentFragment == _settingsFragment) {
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _enquireViewFragment) {
//            setCurrentFragment(_enquireViewFragment.getPreviousFragment());
//        } else if (_currentFragment == _placeViewFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _eventsModuleFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            _mapScreenFragment.setLockedOnPlace(false);
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _facilitiesModuleFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            _mapScreenFragment.setLockedOnPlace(false);
//            setCurrentFragment(_mapScreenFragment);
//        } else if (_currentFragment == _stayModuleFragment) {
//            _mapScreenFragment.setCurrentFragment(_mainMenuFragment);
//            _mapScreenFragment.setLockedOnPlace(false);
//            setCurrentFragment(_mapScreenFragment);
//        }
    }
}