package com.ms.projectlecturer.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;

import java.util.HashMap;
import java.util.Map;

public class Spawner {

    private static DatabaseReference _databaseReference = FirebaseDatabase.getInstance().getReference();

    public static String spawnNewLecturer(String firstName, String lastName, String title, Map<String, Presence> presences) {
        String key = _databaseReference.child("Lecturers").push().getKey();
        Lecturer lecturer = new Lecturer(key, firstName, lastName, title, presences);
        Map<String, Object> lecturerValues = lecturer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Lecturers/" + key, lecturerValues);
        _databaseReference.updateChildren(childUpdates);
        return key;
    }
}
