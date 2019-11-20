package com.ms.projectlecturer.model;

import com.google.firebase.database.Exclude;
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

    private String lecturerId;
    private String firstName;
    private String lastName;
    private String title;
    @Exclude
    private Map<String, Presence> presences;


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lecturerID", lecturerId);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("title", title);
        result.put("presences", presences);
        return result;
    }

    //replace
    @Override
    public String toString() {
        return  new StringBuilder().append("Name: ").append(firstName).append("\n")
                .append("Surname: ").append(lastName).append("\n")
                .toString();
    }
}
