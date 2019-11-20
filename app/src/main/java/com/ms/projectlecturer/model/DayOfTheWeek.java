package com.ms.projectlecturer.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DayOfTheWeek {
    Mon("Mon"), Thu("Thu");
    @Getter
    private String label;
}