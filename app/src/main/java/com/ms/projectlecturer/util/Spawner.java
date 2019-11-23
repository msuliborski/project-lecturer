package com.ms.projectlecturer.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Presence;

import java.util.HashMap;
import java.util.Map;

public class Spawner {

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public static void spawnNewLecturer(String firstName, String lastName, String title, String imageUrl, Map<String, Presence> presences) {
        String key = databaseReference.child("Lecturers").push().getKey();
        Lecturer lecturer = new Lecturer(false, key, firstName, lastName, title, imageUrl.length() == 0 ? "https://cdn.icon-icons.com/icons2/510/PNG/512/person_icon-icons.com_50075.png" : imageUrl, presences);
        Map<String, Object> lecturerValues = lecturer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Lecturers/" + key, lecturerValues);
        databaseReference.updateChildren(childUpdates);
    }

    public static void removeLecturerFromFav(String lecturerId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + userId + "/Favourites/" + lecturerId, lecturerId);
        databaseReference.child("Users").child(userId).child("Favourites").child(lecturerId).removeValue();
    }
    public static void addLecturerToFav(String lecturerId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + userId + "/Favourites/" + lecturerId, lecturerId);
        databaseReference.updateChildren(childUpdates);
    }
}
