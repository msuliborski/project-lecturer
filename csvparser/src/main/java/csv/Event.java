package csv;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String id;
    private String weekDay;
    private String startTime;
    private String endTime;
    private String roomNumber;
    private String buildingName;
    private String type;
    private String name;
    private Double lat;
    private Double lng;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("dayOfTheWeek", weekDay);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("roomNumber", roomNumber);
        result.put("buildingName", buildingName);
        result.put("eventType", type);
        result.put("eventName", name);
        result.put("lat", lat);
        result.put("lng", lng);
        return result;
    }

    @Override
    public String toString() {
        return "\nEvent{" +
                "id='" + id + '\'' +
                ", weekDay='" + weekDay + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}