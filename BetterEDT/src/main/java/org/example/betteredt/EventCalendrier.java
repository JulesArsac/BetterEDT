package org.example.betteredt;

import java.util.Calendar;
import java.util.Date;

public class EventCalendrier {
    private final String summary;
    private final String startHeure;
    private final String endHeure;
    private final String location;

    private final int mois;

    private final int jourSemaine; //1 lundi, 2 mardi, 3 mercredi...
    private final int year;

    public EventCalendrier(String summary, Date startDate, Date endDate, String location) {
        //recup donner de base
        this.summary = summary;
        this.location = location;

        //création calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        //recuperation des jour mois et année
        jourSemaine = calendar.get(Calendar.DAY_OF_WEEK)-1;
        mois=calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        //recuperation de l'heure début d'un cours
        if (calendar.get(Calendar.MINUTE)==0){
            startHeure=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H00";
        } else {
            startHeure=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar.get(Calendar.MINUTE));
        }

        //creation calendar pour la fin du cours
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        //recup fin de cours
        if (calendar.get(Calendar.MINUTE)==0){
            endHeure=String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H00";
        } else {
            endHeure=String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar2.get(Calendar.MINUTE));
        }

        //displayAllInfo();

    }

    public void displayAllInfo(){
        System.out.println(summary);
        System.out.println(location);
        System.out.println(jourSemaine+"/"+mois+"/"+year);
        System.out.println(startHeure+"-"+endHeure);
        System.out.println("===========================");
    }

    public String getSummary() {
        return summary;
    }

    public String getStartHeure() {
        return startHeure;
    }

    public String getEndHeure() {
        return endHeure;
    }

    public String getLocation() {
        return location;
    }

    public int getMois() {
        return mois;
    }

    public int getJourSemaine() {
        return jourSemaine;
    }

    public int getYear() {
        return year;
    }
}
