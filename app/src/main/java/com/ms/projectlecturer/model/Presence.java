package com.ms.projectlecturer.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@IgnoreExtraProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presence {
    private DayOfTheWeek dayOfTheWeek;
    private Date startTime;
    private Date endTime;
    private String roomId;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("dayOfTheWeek", dayOfTheWeek);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("roomId", roomId);
        return result;
    }
}
