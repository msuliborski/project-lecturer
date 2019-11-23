package com.ms.projectlecturer.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ms.projectlecturer.R;
import com.ms.projectlecturer.model.User;
import com.ms.projectlecturer.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton googleBtn;
    private LoginButton facebookBtn;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;
    private Intent mainScreen;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private AlertDialog error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.signInError));
        builder.setMessage(getResources().getString(R.string.signInErrorMsg));
        builder.setCancelable(true);
        builder.setNeutralButton(android.R.string.ok,
                (dialog, id) -> dialog.cancel());

        error = builder.create();

        mainScreen = new Intent(this, LecturersActivity.class);



        setContentView(R.layout.activity_log_in);
        googleBtn = findViewById(R.id.google);
        facebookBtn = findViewById(R.id.facebook);
        progressDialog = new ProgressDialog(this);

        googleBtn.setOnClickListener(this);
        facebookBtn.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("480379190-n37hgr44oa67lroc4ir934kb90heei4d.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        facebookBtn.setReadPermissions("email", "public_profile");

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

        if (auth.getCurrentUser() != null) {
            startActivity(mainScreen);
        }
    }


    private void logInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        progressDialog.setMessage(getResources().getString(R.string.logging));
        progressDialog.show();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        String userId = auth.getCurrentUser().getUid();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        checkCreateUserEntityAndLogIn(usersRef, userId);

                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();

                    }


                });
    }


    private void logInFacebook() {
        progressDialog.setMessage(getResources().getString(R.string.logging));
        progressDialog.show();
    }

    @Override
    public void onClick(View view) {

        if (view == facebookBtn) {
            logInFacebook();
        } else if (view == googleBtn) {
            logInGoogle();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        googleSignInClient.signOut();
        if (isFbLoggedIn()) {
            facebookLogout();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        String userId = auth.getCurrentUser().getUid();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        checkCreateUserEntityAndLogIn(usersRef, userId);
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        error.show();
                        if (isFbLoggedIn()) {
                            facebookLogout();
                        }
                    }

                });
    }

    public boolean isFbLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void facebookLogout() {
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
    }


    private void checkCreateUserEntityAndLogIn(final DatabaseReference usersRef, final String userId) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(userId)) {
                    usersRef.child(userId).setValue(User.generateNewUser());
                }
                progressDialog.dismiss();
                startActivity(mainScreen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }



}
