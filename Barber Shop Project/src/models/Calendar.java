package models;

import sql.DateQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Calendar {
    private final int DAYS = 31;
    private ArrayList<Date> allDates;
    private static final DateQuery dateQuery = new DateQuery();

    public Calendar() {
        this.allDates = new ArrayList<>();
    }

    public void initDates(Connection conn) throws SQLException {
        LocalDate date;
        int count;
        if(allDates.size() > 0){
            count = 1;
            date = LocalDate.of(allDates.get(allDates.size()-1).getYear(),allDates.get(allDates.size()-1).getMonth(),allDates.get(allDates.size()-1).getDay());
        }else {
            date = LocalDate.now();
            count = 0;
        }
        int nextMonth = 0;
        for (int i = allDates.size(); i < DAYS; i++) {
            int month = date.getMonth().getValue();
            if((month == 2) && ( date.getDayOfMonth() + count == 29)){
                count = -date.getDayOfMonth()+1;
                allDates.add(new Date(date.getYear(), date.getMonth().getValue()+1, 1));
                nextMonth = 1;

            }
            else if((month == 4 || month == 6 || month == 9 || month == 11) && (date.getDayOfMonth() + count) == 31){
                count = -date.getDayOfMonth()+1;
                allDates.add(new Date(date.getYear(), date.getMonth().getValue()+1, 1));
                nextMonth = 1;
            }
            else if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                    && (date.getDayOfMonth() + count == 32)){
                count = -date.getDayOfMonth()+1;
                allDates.add(new Date(date.getYear(), date.getMonth().getValue()+1, 1));
                nextMonth = 1;
            }
            else{
                allDates.add(new Date(date.getYear(), date.getMonth().getValue() + nextMonth, date.getDayOfMonth() + count));
            }
            LocalDate currDate = LocalDate.of(allDates.get(i).getYear(),allDates.get(i).getMonth(),allDates.get(i).getDay());
            dateQuery.insertDates(conn,currDate,allDates.get(i).isAvailable());
            count++;
        }
    }

    ////////////////////////////////////////////////////////
    // Updating the specific date available hours and time
    ///////////////////////////////////////////////////////
    public void addAppointmentToDate(Date date, LocalTime startTime, LocalTime endTime) {
        date.getStartingHr().add(startTime);
        date.getEndingHr().add(endTime);
    }

    public void removeAppointmentFromDate(LocalDate date, LocalTime startHr) {
        Date temp = new Date(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth());
        for (Date d : allDates) {
            if (d.equals(temp)) {
                for (int i = 0; i < d.getStartingHr().size(); i++) {
                    if (startHr.equals(d.getStartingHr().get(i))) {
                        d.getStartingHr().remove(i);
                        d.getEndingHr().remove(i);
                        break;
                    }
                }
                break;
            }
        }
    }

    public ArrayList<Date> getAllDates() {
        return allDates;
    }
}
