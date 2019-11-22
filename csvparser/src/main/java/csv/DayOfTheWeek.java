package csv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DayOfTheWeek {
    Monday("Mon"),
    Tuesday("Tue"),
    Wednesday("Wed"),
    Thursday("Thur"),
    Friday("Fri"),
    Saturday("Sat"),
    Sunday("Sun");
    @Getter
    private String label;
}