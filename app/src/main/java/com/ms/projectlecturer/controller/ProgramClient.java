package com.ms.projectlecturer.controller;

import com.ms.projectlecturer.model.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProgramClient {

    private static ProgramClient _instance = null;
    private User _currentUser;
    private DatabaseReference _databaseReference = FirebaseDatabase.getInstance().getReference();


    private ProgramClient() {

    }

    public static ProgramClient getInstance() {
        if (_instance == null) {
             _instance = new ProgramClient();
        }
        return _instance;
    }



    public void logInUser(User user) {
        _currentUser = user;
    }


}
