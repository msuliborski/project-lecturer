package com.ms.projectlecturer.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DayOfTheWeek {
    Mon("Mon"),
    Tue("Tue"),
    Wed("Wed"),
    Thu("Thu"),
    Fri("Fri"),
    Sat("Sat"),
    Sun("Sun");
    @Getter
    private String label;
}