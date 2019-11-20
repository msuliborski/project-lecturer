package com.ms.projectlecturer.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.model.Lecturer;
import java.util.HashMap;
import java.util.Map;

public class LecturerSpawner {

    private static DatabaseReference _databaseReference = FirebaseDatabase.getInstance().getReference();

    public static void spawnNewLecturer(String firstName, String lastName, String title, Map<String, Object> presences) {

        String key = _databaseReference.child("Lecturers").push().getKey();
        Lecturer lecturer = new Lecturer(key, firstName, lastName, title, presences);
        Map<String, Object> lValues = lecturer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Lecturers/" + key, lValues);
        _databaseReference.updateChildren(childUpdates);
    }
}
