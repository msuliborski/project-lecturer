package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    public enum Mode {
        skip, lecturer, event
    }

    List<Lecturer> lecturers = new ArrayList<>();


    void doThisShit(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/timetable-good.csv"));
            String timetableRow;
            Event latestEvent = new Event();
            Mode mode = Mode.skip;
            while ((timetableRow = bufferedReader.readLine()) != null) {
                if (timetableRow.split(";").length == 0) continue; //row is completely empty
                else {
                    String firstCell = timetableRow.split(";")[0];
                    if (firstCell.equals("CT_STAFF")) {
                        mode = Mode.lecturer; continue;
                    } else if (firstCell.equals("CT_EVENT")) {
                        mode = Mode.event; continue;
                    } else if (firstCell.contains("CT_")) {
                        mode = Mode.skip; continue;
                    }
                }
                if (mode == Mode.lecturer) {
                    String id = "", firstName = "", lastName = "", title = "";
                    String[] lecturerRow = timetableRow.split(";");
                    id = lecturerRow[1];
                    if (id.equals("_staff_id")) continue;
                    firstName = getFirstNameFromFuckedUpName(lecturerRow[2]);
                    lastName = getLastNameFromFuckedUpName(lecturerRow[2]);
                    title = getFixedFuckedUpTitle(getTitleFromFuckedUpName(lecturerRow[2]).equals("") ? lecturerRow[3] : getTitleFromFuckedUpName(lecturerRow[2]));

                    System.out.println(id + " -- " + firstName + " -- " + lastName + " -- " + title);
                    lecturers.add(new Lecturer(id, firstName, lastName, title, new HashMap<>()));

                } else if (mode == Mode.event) {
                        if (!timetableRow.split(";")[0].equals("")) { //mamy start nowego eventu

                            String eventId = "", dayOfTheWeek = "", startTime = "", endTime = "", eventType = "";

                            eventId = timetableRow.split(";")[0];
                            if (eventId.equals("_event_id")) continue;
                            dayOfTheWeek = timetableRow.split(";")[1];
                            startTime = timetableRow.split(";")[2];
                            endTime = timetableRow.split(";")[3];
                            eventType = timetableRow.split(";")[7];

                            latestEvent = new Event(eventId, dayOfTheWeek, startTime, endTime, "", "", eventType, "", 0d, 0d);


                        } else if (timetableRow.split(";")[18].equals("Module")) {
                            latestEvent.setName(timetableRow.split(";")[19]);
                        } else if (timetableRow.split(";")[18].equals("Staff")) {
                            String s = timetableRow.split(";")[19];
                            Lecturer l = getLecturerById(s);
                            if (l != null){
                                l.addEvent(latestEvent, latestEvent.getId());
                            }

                        } else if (timetableRow.split(";")[18].equals("Room")) {
                            latestEvent.setRoomNumber(timetableRow.split(";")[19]);
                        }
                    }
                }
            bufferedReader.close();

            for (Lecturer l : lecturers) {
                System.out.println(l.toString());
                System.out.println("\n");
                System.out.println("-------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Lecturer getLecturerById(String lecturerId) {
        for (Lecturer lecturer : lecturers) {
            if (lecturer.getLecturerId().equals(lecturerId))
                return lecturer;
        }
        return null;
    }

    private String getTitleFromFuckedUpName(String fuckedUpName) {
        if (fuckedUpName.split(" ").length > 1 && fuckedUpName.split(",").length > 1)
            return fuckedUpName.split(",")[1];
        else
            return "";
    }

    private String getFirstNameFromFuckedUpName(String fuckedUpName){
        if (fuckedUpName.split(" ").length > 1){
            String[] s1 = fuckedUpName.split(",");
            if (s1.length > 1) {
                if (s1[0].split(" ").length > 1) {
                    return s1[0].split(" ")[1];
                } else {
                    return s1[0];
                }
            } else {
                return fuckedUpName.split(" ")[1];
            }
        } else {
            if (fuckedUpName.split(",").length <= 1) {
                return "";
            } else {
                return fuckedUpName.split(",")[1];}
        }
    }

    private String getLastNameFromFuckedUpName(String fuckedUpName){
        if (fuckedUpName.split(" ").length > 1){
            String[] s1 = fuckedUpName.split(",");
            if (s1.length > 1) {
                if (s1[0].split(" ").length > 1) {
                    String[] s2 = s1[0].split(" ");
                    return s2[0];
                } else
                    return s1[0];
            } else {
                return fuckedUpName.split(" ")[0];
            }
        } else {
            String[] s1 = fuckedUpName.split(",");
            return s1[0];
        }
    }

    private String getFixedFuckedUpTitle(String fuckedUpTitle){
        String fuckedUpTitleUpper = fuckedUpTitle.toUpperCase().trim();
        switch (fuckedUpTitleUpper){
            case "PROF. DR H":
                return "Prof. Dr hab.";
            case "PROF.":
            case "PROF":
                return "Prof.";
            case "DR HAB.":
                return "Dr hab.";
            case "DR INŻ.":
                return "Dr inż.";
            case "DR":
            case "DOC":
            case "DOC.":
                return "Dr";
            case "MGR":
                return "Mgr";
            case "MGR INŻ.":
            case "MGR INŻ":
                return "Mgr inż.";
            case "INŻ":
            case "INŻ.":
                return "Inż.";
            case "":
                return "";
            default:
                return fuckedUpTitle;
        }
    }
}
