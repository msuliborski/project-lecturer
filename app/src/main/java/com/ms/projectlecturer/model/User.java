package com.ms.projectlecturer.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IgnoreExtraProperties
public class User {

    private HashMap<String, String> Favourites;

    public static User generateNewUser() {
        User user = new User();
        HashMap<String, String> Favourites = new HashMap<>();
        user.setFavourites(Favourites);
        return user;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Favoorites", Favourites);
        return result;
    }
}
