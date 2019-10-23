package com.ms.projectlecturer.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@IgnoreExtraProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecturer implements Comparable<Lecturer> {

    @Override
    public int compareTo(Lecturer o) {
        if (lastName.compareTo(o.lastName) == 0) {
            return firstName.compareTo(o.firstName);
        }
        return lastName.compareTo(o.lastName);
    }

    private String lecturerID;
    private String firstName;
    private String lastName;
    private FacultyType facultyType;
    private HashMap<Period, String> schedule; // period, room_id

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lecturerID", lecturerID);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("facultyType", facultyType);
        result.put("schedule", schedule);
        return result;
    }
}
