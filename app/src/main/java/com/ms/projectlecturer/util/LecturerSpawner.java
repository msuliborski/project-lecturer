package com.ms.projectlecturer.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.model.FacultyType;
import com.ms.projectlecturer.model.Lecturer;
import java.util.HashMap;
import java.util.Map;

public class LecturerSpawner {

    private static DatabaseReference _databaseReference = FirebaseDatabase.getInstance().getReference();

    public static void spawnNewLecturer(String firstName, String lastName, FacultyType facultyType) {

        String key = _databaseReference.child("Enquires").push().getKey();
        Lecturer l = new Lecturer(key, firstName, lastName, facultyType);
        Map<String, Object> lValues = l.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Lecturers/" + key, lValues);
        _databaseReference.updateChildren(childUpdates);
    }
}
