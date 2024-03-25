package org.example.betteredt;

import java.util.List;

public interface IEdtController {


    public void switchToDailyFilter();
    public void switchToWeeklyFilter();
    public void switchToMonthlyFilter();
    public void updateEventList(List<EventCalendrier> newEvents);
    public List<EventCalendrier> getCurrentEvents();

}
