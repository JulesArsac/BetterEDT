package org.example.betteredt;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parseur {
    public static void TestParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/ILSEN.ics");
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fileInputStream);

            List<EventCalendrier> mainList = new ArrayList<>();

            for (Object o : calendar.getComponents()) {
                Component component = (Component) o;
                if (component.getName().equals("VEVENT")) {
                    VEvent event = (VEvent) component;
                    System.out.println(component);

                    if(event.getLocation() == null || event.getLocation().getValue() == null){
                        mainList.add(new EventCalendrier(event.getSummary().getValue(),event.getStartDate().getDate(),event.getEndDate().getDate(),"Salle non spécifier"));
                    } else {
                        mainList.add(new EventCalendrier(event.getSummary().getValue(),event.getStartDate().getDate(),event.getEndDate().getDate(),event.getLocation().getValue()));
                    }

                    //System.out.println(mainList.get(0).getEndDate());
                    /*
                    System.out.println("Event Summary: " + event.getSummary().getValue());
                    System.out.println("Event Start: " + event.getStartDate().getDate());
                    System.out.println("Event End: " + event.getEndDate().getDate());
                    //System.out.println("Salles: "+ event.getLocation().getValue());
                    System.out.println("==============================");
                    */

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}