package com.ms.projectlecturer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

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
    private LecturersListFragment lecturersListFragment = new LecturersListFragment();
    private LecturerFragment lecturerFragment = new LecturerFragment();
    private MapFragment mapFragment = new MapFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private CreditsFragment creditsFragment = new CreditsFragment();
    private SidebarFragment sidebarFragment = new SidebarFragment();
    private Fragment previousFragment = lecturersListFragment;
    private Fragment currentFragment = lecturersListFragment;

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
        fragmentTransaction.commit();
    }

    public void setFragment(Fragment newFragment) {
        if (newFragment != null) {
            if(newFragment != currentFragment) {
                previousFragment = currentFragment;
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
        // LODEX (B9)            51.747230, 19.453853
        // CTI (B19)             51.747002, 19.455931
        // Ins. Fizyki (B14)     51.746546, 19.455403

        Map<String, Presence> presenceSet1 = new HashMap<>();
        presenceSet1.put("230", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        presenceSet1.put("30", new Presence(DayOfTheWeek.Monday, "10:15:00", "11:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        presenceSet1.put("222", new Presence(DayOfTheWeek.Monday, "12:15:00", "13:45:00", "100", "CTI (B19)", 51.747002, 19.455931));
        presenceSet1.put("0", new Presence(DayOfTheWeek.Monday, "14:15:00", "15:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        presenceSet1.put("231", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        presenceSet1.put("17", new Presence(DayOfTheWeek.Tuesday, "12:15:00", "13:45:00", "100", "CTI (B19)", 51.747002, 19.455931));
        presenceSet1.put("51", new Presence(DayOfTheWeek.Tuesday, "14:15:00", "15:45:00", "100", "CTI (B19)", 51.747002, 19.455931));
        presenceSet1.put("112", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931));
        presenceSet1.put("22", new Presence(DayOfTheWeek.Wednesday, "12:15:00", "13:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));
        presenceSet1.put("3", new Presence(DayOfTheWeek.Thursday, "10:15:00", "11:45:00", "100", "LODEX (B9)", 51.747230, 19.453853));

        Map<String, Presence> presenceSet2 = new HashMap<>();
        presenceSet2.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853 ));
        presenceSet2.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853 ));
        presenceSet2.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853 ));
        presenceSet2.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853 ));
        presenceSet2.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "LODEX (B9)", 51.747230, 19.453853 ));

        Map<String, Presence> presenceSet3 = new HashMap<>();
        presenceSet3.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931 ));
        presenceSet3.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931 ));
        presenceSet3.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931 ));
        presenceSet3.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931 ));
        presenceSet3.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "CTI (B19)", 51.747002, 19.455931 ));

        Map<String, Presence> presenceSet4 = new HashMap<>();
        presenceSet4.put("0", new Presence(DayOfTheWeek.Monday, "08:15:00", "09:45:00", "100", "Ins. Fizyki (B14)", 51.746546, 19.455403 ));
        presenceSet4.put("1", new Presence(DayOfTheWeek.Tuesday, "08:15:00", "09:45:00", "100", "Ins. Fizyki (B14)", 51.746546, 19.455403 ));
        presenceSet4.put("2", new Presence(DayOfTheWeek.Wednesday, "08:15:00", "09:45:00", "100", "Ins. Fizyki (B14)", 51.746546, 19.455403 ));
        presenceSet4.put("3", new Presence(DayOfTheWeek.Thursday, "08:15:00", "09:45:00", "100", "Ins. Fizyki (B14)", 51.746546, 19.455403 ));
        presenceSet4.put("4", new Presence(DayOfTheWeek.Friday, "08:15:00", "09:45:00", "100", "Ins. Fizyki (B14)", 51.746546, 19.455403 ));
        Spawner.spawnNewLecturer("Piotr", "Napieralski", "dr. inż.", "https://ftims.edu.p.lodz.pl/pluginfile.php/1378/user/icon/adaptable/f3?rev=3554694", presenceSet1);
        Spawner.spawnNewLecturer("Piotr", "Nowak", "dr. inż.", "", presenceSet2);
        Spawner.spawnNewLecturer("Dariusz", "Piekarski", "dr.", "", presenceSet1);
        Spawner.spawnNewLecturer("Artur", "Boski", "dr. inż., hab.", "", presenceSet3);
        Spawner.spawnNewLecturer("Beata", "Adamska", "dr. inż.", "", presenceSet1);
        Spawner.spawnNewLecturer("Agata", "Grzelak", "dr. inż.", "", presenceSet4);
        Spawner.spawnNewLecturer("Adam", "Komar", "dr. inż.", "", presenceSet1);
        Spawner.spawnNewLecturer("Arkadiusz", "Błaszczyk", "prof. hab.", "", presenceSet3);
        Spawner.spawnNewLecturer("Michał", "Wilanowski", "dr. inż.", "", presenceSet1);
        Spawner.spawnNewLecturer("Wojciech", "Lech", "prof.", "", presenceSet2);
        Spawner.spawnNewLecturer("Filip", "Wojciechowski", "dr. inż.", "", presenceSet4);

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
        if (currentFragment == lecturersListFragment) {
            super.onBackPressed();
        } else if (currentFragment == profileFragment) {
            setFragment(lecturersListFragment);
        } else if (currentFragment == lecturerFragment) {
            setFragment(lecturersListFragment);
        } else if (currentFragment == mapFragment) {
            setFragment(lecturerFragment);
        } else if (currentFragment == settingsFragment) {
            setFragment(lecturersListFragment);
        } else if (currentFragment == creditsFragment) {
            setFragment(lecturersListFragment);
        }
    }
}