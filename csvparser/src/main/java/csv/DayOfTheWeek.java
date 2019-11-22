package csv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DayOfTheWeek {
    Monday("Monday"),
    Tuesday("Tuesday"),
    Wednesday("Wednesday"),
    Thursday("Thursday"),
    Friday("Friday"),
    Saturday("Saturday"),
    Sunday("Sunday");
    @Getter
    private String label;
}