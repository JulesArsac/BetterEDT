package org.example.betteredt;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.FileInputStream;
import java.io.IOException;

public class Parseur {
    public static void TestParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\flo20\\Documents\\TP Master Info\\BetterEDT\\BetterEDT\\src\\main\\stocks\\ILSEN.ics");
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fileInputStream);


            for (Object o : calendar.getComponents()) {
                Component component = (Component) o;
                if (component.getName().equals("VEVENT")) {
                    VEvent event = (VEvent) component;
                    System.out.println("Event Summary: " + event.getSummary().getValue());
                    System.out.println("Event Start: " + event.getStartDate().getDate());
                    System.out.println("Event End: " + event.getEndDate().getDate());
                    // parcer mieux ici
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}