package csv;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presence {
    private DayOfTheWeek dayOfTheWeek;
    private String startTime;
    private String endTime;
    private String roomNumber;
    private String buildingName;
    private Double lat;
    private Double lng;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("dayOfTheWeek", dayOfTheWeek);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("roomNumber", roomNumber);
        result.put("buildingName", buildingName);
        result.put("lat", lat);
        result.put("lng", lng);
        return result;
    }
}