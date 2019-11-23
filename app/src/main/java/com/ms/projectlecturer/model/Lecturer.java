package com.ms.projectlecturer.model;
import com.google.firebase.database.IgnoreExtraProperties;
import com.ms.projectlecturer.R;
import android.content.Context;
import android.content.res.Resources;

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

        if (isFavored != o.isFavored){
            if (isFavored) return -1;
            else return 1;
        } else {
            if (lastName.compareTo(o.lastName) == 0)
                return firstName.compareTo(o.firstName);
            return lastName.compareTo(o.lastName);
        }
    }

    private Boolean isFavored;
    private String lecturerId;
    private String firstName;
    private String lastName;
    private String title;
    private Map<String, Presence> presences;
    private String imageUrl;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lecturerID", lecturerId);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("title", title);
        result.put("presences", presences);
        result.put("imageUrl", imageUrl);
        return result;
    }

    public String toString(Context context) {
        Resources resources = context.getResources();
        return  new StringBuilder().append(resources.getString(R.string.name)).append(firstName).append("\n")
                .append(resources.getString(R.string.surname)).append(lastName).append("\n")
                .append(resources.getString(R.string.title)).append(title).append("\n")
                .toString();
    }
}
