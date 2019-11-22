package com.ms.projectlecturer.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Objects;

public class SidebarFragment extends Fragment implements View.OnClickListener{

    private ImageView profileImageView;
    private Button profileButton;
    private Button lecturersButton;
    private Button wikampButton;
    private Button settingsButton;
    private Button creditsButton;
    private Button logOutButton;

    private FirebaseAuth _auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sidebar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImageView = view.findViewById(R.id.profileImageView);
        String imgUrl = Objects.requireNonNull(Objects.requireNonNull(_auth.getCurrentUser()).getPhotoUrl()).toString();
        Glide.with(this).load(imgUrl).apply(RequestOptions.circleCropTransform()).into(profileImageView);

        profileButton = view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(this);
        lecturersButton = view.findViewById(R.id.lecturersButton);
        lecturersButton.setOnClickListener(this);
        wikampButton = view.findViewById(R.id.wikampButton);
        wikampButton.setOnClickListener(this);
        settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        creditsButton = view.findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(this);
        logOutButton = view.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;

        if (view == profileButton) {
            fragment = ((LecturersActivity)getActivity()).getProfileFragment();
        } else if (view == lecturersButton) {
            fragment = ((LecturersActivity)getActivity()).getLecturersListFragment();
        } else if (view == wikampButton) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.wikamp)));
            startActivity(browserIntent);
        } else if (view == settingsButton) {
            fragment = ((LecturersActivity)getActivity()).getSettingsFragment();
        } else if (view == creditsButton) {
            fragment = ((LecturersActivity)getActivity()).getCreditsFragment();
        } else if (view == logOutButton) {
            _auth.signOut();
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
            getActivity().finish();
        }

        ((LecturersActivity)getActivity()).setFragment(fragment);
    }
}
