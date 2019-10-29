package com.ms.projectlecturer.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FacultyType {
    MECH("W Mech."), WEEIA("WEEIA"), CHEM("W Chem."), FTIMS("W FTIMS");
    @Getter
    private String label;
}