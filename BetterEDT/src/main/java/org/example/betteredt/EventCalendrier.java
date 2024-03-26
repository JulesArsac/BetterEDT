package org.example.betteredt;

import net.fortuna.ical4j.model.DateTime;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EventCalendrier {
    private final String summary;
    private final String startHeure;
    private final String endHeure;
    private final String location;

    private String dateCompacter;

    private final int mois;
    private final int jour;
    private int jourSemaine;
    private final int year;

    private final String UCE;
    private final String professeur;
    private String elevesConcerner = null;
    private final String typeDeCours;
    private final String additionalInfo;
    private String color = null;

    private boolean isDisplayed = false;


    public EventCalendrier(String summary, String startHeure, String endHeure, String location, int mois, int jour, int year, String UCE, String professeur, String color, String typeDeCours, String additionalInfo) {
        this.summary = summary;
        this.startHeure = startHeure;
        this.endHeure = endHeure;
        this.location = location;
        this.mois = mois;
        this.jour = jour;
        this.year = year;
        this.UCE = UCE;
        this.professeur = professeur;
        this.color = color;
        this.typeDeCours = typeDeCours;
        this.additionalInfo = additionalInfo;
        setJourSemaine();
        setDateCompacter();
    }

    public EventCalendrier(String summary, Date startDate, Date endDate, String location) {

        this.summary = summary;
        this.location = location;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);


        jourSemaine = calendar.get(Calendar.DAY_OF_WEEK)-1;

        jour=calendar.get(Calendar.DAY_OF_MONTH);
        mois=calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        if (calendar.get(Calendar.MINUTE)==0){
            if (calendar.get(Calendar.HOUR_OF_DAY)<10){
                startHeure="0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H00";
            } else {
                startHeure=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H00";
            }
        } else {
            if (calendar.get(Calendar.HOUR_OF_DAY)<10){
                startHeure="0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar.get(Calendar.MINUTE));
            } else {
                startHeure=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar.get(Calendar.MINUTE));
            }
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        if (calendar2.get(Calendar.MINUTE)==0){
            if (calendar2.get(Calendar.HOUR_OF_DAY)<10){
                endHeure="0"+String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H00";
            } else {
                endHeure=String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H00";
            }
        } else {
            if (calendar2.get(Calendar.HOUR_OF_DAY)<10){
                endHeure="0"+String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar2.get(Calendar.MINUTE));
            } else {
                endHeure=String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY))+"H"+String.valueOf(calendar2.get(Calendar.MINUTE));
            }
        }

        String[] fragment = summary.split(" - ");
        UCE=fragment[0];
        if (fragment.length>1){
            professeur=fragment[1];
            if (fragment.length==2){
                elevesConcerner="Ouvert a tous";
            } else{
                elevesConcerner=fragment[2];
            }

            if (fragment.length>3){
                typeDeCours=fragment[3];
            } else {
                typeDeCours="NULL";
            }
            if (fragment.length==5){
                additionalInfo=fragment[4];
            } else{
                additionalInfo="NULL";
            }
        } else{
            professeur="NULL";
            elevesConcerner="NULL";
            typeDeCours="Férier";
            additionalInfo="NULL";

        }

        setDateCompacter();

    }

    public void setDateCompacter() {
        String day;
        String month;
        if (jour < 10){
            day="0"+jour;
        } else {
            day=String.valueOf(jour);
        }
        if (mois < 10){
            month="0"+mois;
        } else {
            month=String.valueOf(mois);
        }
        dateCompacter=day+"/"+month+"/"+year+"-"+startHeure;
        //displayAllInfo();

    }

    public void setJourSemaine() {
        LocalDate date = LocalDate.of(year, mois, jour);
        jourSemaine = date.getDayOfWeek().getValue()-1;
    }

    public void displayAllInfo(){
        System.out.println(summary);
        System.out.println(location);
        System.out.println(jour+"/"+mois+"/"+year);
        System.out.println(startHeure+"-"+endHeure);

        System.out.println(UCE);
        System.out.println(professeur);
        System.out.println(elevesConcerner);
        System.out.println(typeDeCours);
        System.out.println(additionalInfo);
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

    public int getJour() {
        return jour;
    }

    public String getUCE() {
        return UCE;
    }

    public String getProfesseur() {
        return professeur;
    }

    public String getElevesConcerner() {
        return elevesConcerner;
    }

    public String getTypeDeCours() {
        return typeDeCours;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy-HH'H'mm");
        return LocalDateTime.parse(dateCompacter, formatter);
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(boolean displayed) {
        isDisplayed = displayed;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCalendrier)) return false;
        EventCalendrier that = (EventCalendrier) o;
        return getMois() == that.getMois() &&
                getJour() == that.getJour() &&
                getJourSemaine() == that.getJourSemaine() &&
                getYear() == that.getYear() &&
                Objects.equals(getSummary(), that.getSummary()) &&
                Objects.equals(getStartHeure(), that.getStartHeure()) &&
                Objects.equals(getEndHeure(), that.getEndHeure()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getLocalDateTime(), that.getLocalDateTime()) &&
                Objects.equals(getUCE(), that.getUCE()) &&
                Objects.equals(getProfesseur(), that.getProfesseur()) &&
                Objects.equals(getElevesConcerner(), that.getElevesConcerner()) &&
                Objects.equals(getTypeDeCours(), that.getTypeDeCours()) &&
                Objects.equals(getAdditionalInfo(), that.getAdditionalInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSummary(), getStartHeure(), getEndHeure(), getLocation(), getLocalDateTime(), getMois(), getJour(), getJourSemaine(), getYear(), getUCE(), getProfesseur(), getElevesConcerner(), getTypeDeCours(), getAdditionalInfo(), getLocalDateTime());
    }

}
