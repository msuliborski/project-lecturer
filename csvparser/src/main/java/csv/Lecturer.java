package csv;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String lecturerId;//_unique_name
    private String firstName;
    private String lastName;
    private String title;
    private Map<String, Event> events;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lecturerID", lecturerId);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("title", title);
        result.put("events", events);
        return result;
    }

    public void addEvent(Event event, String key){
        events.put(key, event);
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "lecturerId='" + lecturerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", events=" + events.toString() +
                '}';
    }
}