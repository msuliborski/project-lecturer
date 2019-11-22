package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public enum Kind{
        skip("skip"),
        lecturer("lecturer"),
        event("event");
        Kind(String s) {
        }
    }
    public static void main(String args[]){

        List<Lecturer> lecturers = new ArrayList<>();

        try {
            BufferedReader csvReader1 = new BufferedReader(new FileReader("src/main/resources/timetable-good.csv"));
            String row;
            List<String> eventRows = new ArrayList<>();
            Kind kind = Kind.skip;
            newLine: while ((row = csvReader1.readLine()) != null) {
                if (row.split(";").length == 0) continue newLine;
                if (row.split(";")[0].equals("CT_STAFF")) {
                    kind = Kind.lecturer; continue newLine;
                } else if (row.split(";")[0].equals("CT_MODULE")) {
                    kind = Kind.skip; continue newLine;
                } else if (row.split(";")[0].equals("CT_ROOM")) {
                    kind = Kind.skip; continue newLine;
                } else if (row.split(";")[0].equals("CT_EVENT_CAT")) {
                    kind = Kind.skip;  continue newLine;
                } else if (row.split(";")[0].equals("CT_FACULTY")) {
                    kind = Kind.skip; continue newLine;
                } else if (row.split(";")[0].equals("CT_SPAN")) {
                    kind = Kind.skip; continue newLine;
                } else if (row.split(";")[0].equals("CT_EVENT")) {
                    kind = Kind.event; continue newLine;
                }

                switch (kind){
                    case lecturer:{
                        String id = "", firstName = "", lastName = "", title = "";
                        String[] lecturerRow = row.split(";");
                        id = lecturerRow[0];
                        if (id.equals("_staff_id")) continue newLine;
                        firstName = getFirstNameFromFuckedUpName(lecturerRow[2]);
                        lastName = getLastNameFromFuckedUpName(lecturerRow[2]);
                        title = getFixedFuckedUpTitle(getTitleFromFuckedUpName(lecturerRow[2]).equals("") ? lecturerRow[3] : getTitleFromFuckedUpName(lecturerRow[2]));

                        //System.out.println(id + " -- " + firstName + " -- " + lastName + " -- " + title);
                        lecturers.add(new Lecturer(id, firstName, lastName, title, new HashMap<>()));
                        continue newLine;

                    } case event: {
                        if (!row.split(";")[0].equals("")){ //mamy start nowego eventu
                            if(!eventRows.isEmpty()){
                                String eventId = "", dayOfTheWeek = "", startTime = "", endTime = "", eventKind = "", module = "", place = "";

                                eventId = row.split(";")[0];
                                if (eventId.equals("_event_id")) continue newLine;
                                dayOfTheWeek = row.split(";")[1];
                                startTime = row.split(";")[2];
                                endTime = row.split(";")[3];
                                eventKind = row.split(";")[7];

                                List<String> places = new ArrayList<>();
                                List<String> lecturersNames = new ArrayList<>();

                                //System.out.println(eventRows.toString());
                                //System.out.println("---------------------------");

                                for (String eventRow : eventRows) {
//                                    System.out.println(eventRow.split(";").toString());
                                    switch (eventRow.split(";")[18]) {
                                        case "Module":
                                            module = eventRow.split(";")[19]; break;
                                        case "Room":
                                            places.add(eventRow.split(";")[19]); break;
                                        case "Staff":
                                            lecturersNames.add(eventRow.split(";")[19]); break;
                                    }
                                }

                                System.out.println(eventId + " -- " + dayOfTheWeek + " -- " + startTime + " -- " + endTime + " -- " + eventKind + " -- " + module + " -- " + place + " -- " + places.toString() + " -- " + lecturersNames.toString());

                                //handle data
                                /*
                                extract this:

                                    Room (concat if many)

                                put to precence object


                                idz przez rzędy i znajdz ilosc staffu

                                daj kazdemu tego presence


                                 */

                                eventRows.clear();
                            }
                        } else {
                            eventRows.add(row);
                            continue newLine;
                        }
                    } case skip: {
                        continue newLine;
                    } default: {

                    }
                }

            }
            csvReader1.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTitleFromFuckedUpName(String fuckedUpName) {
        if (fuckedUpName.split(" ").length > 1 && fuckedUpName.split(",").length > 1)
            return fuckedUpName.split(",")[1];
        else
            return "";
    }

    private static String getFirstNameFromFuckedUpName(String fuckedUpName){
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

    private static String getLastNameFromFuckedUpName(String fuckedUpName){
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

    private static String getFixedFuckedUpTitle(String fuckedUpTitle){
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
