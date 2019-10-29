package com.ms.projectlecturer.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.ms.projectlecturer.R;

public class SidebarFragment extends Fragment implements View.OnClickListener{

    private ImageView _profileImage;
    private Button _food;
    private Button _credits;
    private Button _profile;
    private Button _home;
    private Button _signout;
    private Button _wikamp;
    private Button _settings;


    private AlphaAnimation _buttonClick = new AlphaAnimation(1f, 0.8f);

    private LecturersActivity _lecturersActivity;

    private FirebaseAuth _auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_sidebar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _lecturersActivity = (LecturersActivity) getActivity();


        _buttonClick.setDuration(300);

        _profileImage = view.findViewById(R.id.profile);

        String imgUrl = _auth.getCurrentUser().getPhotoUrl().toString();
        Glide.with(this).load(imgUrl).apply(RequestOptions.circleCropTransform()).into(_profileImage);

        _food = view.findViewById(R.id.foodButton);
        _food.setOnClickListener(this);
        _home = view.findViewById(R.id.home);
        _home.setOnClickListener(this);
        _signout = view.findViewById(R.id.sign_out);
        _signout.setOnClickListener(this);
        _wikamp = view.findViewById(R.id.wikamp);
        _wikamp.setOnClickListener(this);
        _settings = view.findViewById(R.id.sett);
        _settings.setOnClickListener(this);
        _credits = view.findViewById(R.id.creditsButton);
        _credits.setOnClickListener(this);
        _profile = view.findViewById(R.id.profileButton);
        _profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;

        if (view == _food) {
            view.startAnimation(_buttonClick);
            fragment = _lecturersActivity.getMapScreenFragment();
            Log.d("tag", "onComplete: kurwa3");
        } else if (view == _profile) {
            view.startAnimation(_buttonClick);
        } else if (view == _credits) {
            view.startAnimation(_buttonClick);
            fragment = _lecturersActivity.getCreditsScreenFragment();
        } else if (view == _settings) {
            view.startAnimation(_buttonClick);
            fragment = _lecturersActivity.getSettinsFragment();
        } else if (view == _home) {
            view.startAnimation(_buttonClick);
            fragment = _lecturersActivity.getLecturersFragment();
        } else if (view == _signout) {
            _auth.signOut();
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
            view.startAnimation(_buttonClick);
            _lecturersActivity.finish();
        } else if (view == _wikamp) {
            view.startAnimation(_buttonClick);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.wiki)));
            startActivity(browserIntent);
        }
        _lecturersActivity.setCurrentFragment(fragment);

    }
}
