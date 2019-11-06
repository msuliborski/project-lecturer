package com.ms.projectlecturer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import lombok.Getter;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ms.projectlecturer.R;
import com.ms.projectlecturer.controller.ProgramClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class LecturersActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private static Context _context;
    private ImageView _profileImage;
    private Button _food;
    private Button _credits;
    private Button _profile;
    private Button _home;
    private Button _signout;
    private Button _wikamp;
    private Button _settings;
    private AlphaAnimation _buttonClick = new AlphaAnimation(1f, 0.8f);

    private ActionBarDrawerToggle _toggle;
    private DrawerLayout _drawerLayout;

    private ProgramClient _programClient = ProgramClient.getInstance();
    private FirebaseAuth _auth = FirebaseAuth.getInstance();
    @Getter
    private LecturersListFragment _lecturersListFragment = new LecturersListFragment();
    private MapFragment _mapFragmentFragment = new MapFragment();
    private SidebarFragment _sidebarFragment = new SidebarFragment();
    private CreditsFragment _creditsFragmentFragment = new CreditsFragment();
    private LecturerFragment _lecturerFragment = new LecturerFragment();
    private SettingsFragment _settingsFragmentFragment = new SettingsFragment();
    private Fragment _currentFragment;
    private FragmentManager _fragmentManager = getSupportFragmentManager();
    private FragmentTransaction _fragmentTransaction;

    private SharedPreferences _pref;
    private Resources _resources;
    private Configuration _conf;

    public SharedPreferences getPref() {
        return _pref;
    }

    public Configuration getConf() {
        return _conf;
    }

    public SettingsFragment getSettingsFragment () {
        return _settingsFragmentFragment;
    }

    public MapFragment getMapScreenFragment () {
        return _mapFragmentFragment;
    }

    public Fragment getCurrentFragment() {
        return _currentFragment;
    }

    public Fragment getSettinsFragment() {
        return _settingsFragmentFragment;
    }

    public Fragment getLecturersFragment() {
        return _lecturersListFragment;
    }


    public Fragment getLecturerFragment() {
        return _lecturerFragment;
    }


    public Fragment getCreditsScreenFragment() {
        return _creditsFragmentFragment;
    }

    public void setCurrentFragment(Fragment fragment) {
        if (fragment != null) {
            if(fragment != _currentFragment) {
                _fragmentTransaction = _fragmentManager.beginTransaction();
                _fragmentTransaction.hide(_currentFragment);
                _currentFragment = fragment;
                _fragmentTransaction.show(_currentFragment);
                _fragmentTransaction.commit();
            }
            _drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public static Context getContext() {
        return _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;

        View sidebarView = getLayoutInflater().inflate(R.layout.fragment_sidebar, null);
//        TextView text = (TextView) inflatedView.findViewById(R.id.text_view);
//        text.setText("Hello!");




        _pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        _resources = getResources();
        _conf = _resources.getConfiguration();
        if (_pref.contains("lang_code")) {
            String lang = _pref.getString("lang_code", "en");
            String currentLang = _conf.locale.getLanguage();
            if (!currentLang.equals(lang)) {
                setLocale(lang);
            }
        }
        setContentView(R.layout.activity_lecturers);
        _buttonClick.setDuration(300);
        _drawerLayout = findViewById(R.id.drawer_layout);
        _toggle = new ActionBarDrawerToggle(this, _drawerLayout, 0, 0);
        _drawerLayout.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        _profileImage = sidebarView.findViewById(R.id.profile);

        String imgUrl = _auth.getCurrentUser().getPhotoUrl().toString();
        Glide.with(this).load(imgUrl).apply(RequestOptions.circleCropTransform()).into(_profileImage);

        _food = sidebarView.findViewById(R.id.foodButton);
        _food.setOnClickListener(this);
        _home = sidebarView.findViewById(R.id.home);
        _home.setOnClickListener(this);
        _signout = sidebarView.findViewById(R.id.sign_out);
        _signout.setOnClickListener(this);
        _wikamp = sidebarView.findViewById(R.id.wikamp);
        _wikamp.setOnClickListener(this);
        _wikamp.setText("kurwa");
        _settings = sidebarView.findViewById(R.id.sett);
        _settings.setOnClickListener(this);
        _credits = sidebarView.findViewById(R.id.creditsButton);
        _credits.setOnClickListener(this);
        _profile = sidebarView.findViewById(R.id.profileButton);
        _profile.setOnClickListener(this);
        _fragmentTransaction = _fragmentManager.beginTransaction();
        _fragmentTransaction.add(R.id.nav_area, _sidebarFragment);
        _fragmentTransaction.add(R.id.screen_area, _creditsFragmentFragment);
        _fragmentTransaction.hide(_creditsFragmentFragment);
        _fragmentTransaction.add(R.id.screen_area, _lecturerFragment);
        _fragmentTransaction.hide(_lecturerFragment);
        _fragmentTransaction.add(R.id.screen_area, _settingsFragmentFragment);
        _fragmentTransaction.hide(_settingsFragmentFragment);
        _fragmentTransaction.add(R.id.screen_area, _mapFragmentFragment);
        _fragmentTransaction.hide(_mapFragmentFragment);
        _fragmentTransaction.add(R.id.screen_area, _lecturersListFragment);
        _currentFragment = _lecturersListFragment;
        _fragmentTransaction.commit();

//        LecturerSpawner.spawnNewLecturer("Augusto", "Pinochet", FacultyType.FTIMS);
//        LecturerSpawner.spawnNewLecturer("Murray", "Rothbard", FacultyType.WEEIA);
//        LecturerSpawner.spawnNewLecturer("Milton", "Friedman", FacultyType.CHEM);
//        LecturerSpawner.spawnNewLecturer("Janusz", "Korwin-Mikke", FacultyType.CHEM);
//        LecturerSpawner.spawnNewLecturer("Ronald", "Reagan", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Margaret", "Thatcher", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Ozjasz", "Goldberg", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);
//        LecturerSpawner.spawnNewLecturer("Test", "Test", FacultyType.MECH);

    }



    @Override
    public void onClick(View view) {


        Fragment fragment = null;

        if (view == _food) {
            view.startAnimation(_buttonClick);
            fragment = _mapFragmentFragment;
            Log.d("tag", "onComplete: kurwa3");
        } else if (view == _profile) {
            view.startAnimation(_buttonClick);
        } else if (view == _credits) {
            view.startAnimation(_buttonClick);
            fragment = _creditsFragmentFragment;
        } else if (view == _settings) {
            view.startAnimation(_buttonClick);
            fragment = _settingsFragmentFragment;
        } else if (view == _home) {
            view.startAnimation(_buttonClick);
            fragment = _lecturersListFragment;
        } else if (view == _signout) {
            _auth.signOut();
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
            view.startAnimation(_buttonClick);
            this.finish();
        } else if (view == _wikamp) {
            view.startAnimation(_buttonClick);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.wikamp)));
            startActivity(browserIntent);
        }
        setCurrentFragment(fragment);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (_currentFragment == _mapFragmentFragment) {
            super.onBackPressed();
        }
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        DisplayMetrics dm = _resources.getDisplayMetrics();
        Configuration conf = _resources.getConfiguration();
        conf.locale = locale;
        _resources.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LecturersActivity.class);
        startActivity(refresh);
        finish();
    }


}