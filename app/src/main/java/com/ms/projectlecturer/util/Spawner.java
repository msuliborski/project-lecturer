package com.ms.projectlecturer.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.projectlecturer.model.Lecturer;
import com.ms.projectlecturer.model.Place;
import com.ms.projectlecturer.model.Presence;
import com.ms.projectlecturer.model.Room;

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
//    public static String spawnNewRoom(String roomNumber, String buildingId) {
//        String key = _databaseReference.child("Rooms").push().getKey();
//        Room room = new Room(key, roomNumber, buildingId);
//        Map<String, Object> lValues = room.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/Rooms/" + key, lValues);
//        _databaseReference.updateChildren(childUpdates);
//        return key;
//    }
//    public static String spawnNewPlace(Double lat, Double lng, String placeName) {
//        String key = _databaseReference.child("Places").push().getKey();
//        Place place = new Place(key, lat, lng, placeName);
//        Map<String, Object> lValues = place.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/Places/" + key, lValues);
//        _databaseReference.updateChildren(childUpdates);
//        return key;
//    }


}
