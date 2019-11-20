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
public class Place {

    private String placeId;
    private Double lat;
    private Double lng;
    private String placeName;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("placeId", placeId);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("placeName", placeName);
        return result;
    }

}
