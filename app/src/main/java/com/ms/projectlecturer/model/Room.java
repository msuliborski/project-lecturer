package com.ms.projectlecturer.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@IgnoreExtraProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private String roomId;
    private String roomNumber;
    private String buildingId;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomId", roomId);
        result.put("roomNumber", roomNumber);
        result.put("buildingId", buildingId);
        return result;
    }

}
