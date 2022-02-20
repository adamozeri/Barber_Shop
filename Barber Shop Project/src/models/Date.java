package models;
import java.time.LocalTime;
import java.util.ArrayList;


public class Date {
    private int year;
    private int month;
    private int day;
    private boolean available;
    private ArrayList<LocalTime> startingHr;
    private ArrayList<LocalTime> endingHr;

    public Date(int year, int month, int day){
        setDay(day);
        setMonth(month);
        setYear(year);
        this.available = true;
        this.startingHr = new ArrayList<>();
        this.endingHr = new ArrayList<>();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setDay(int day) {
        if(month == 2 && day > 28){
            setMonth(month + 1);
            this.day = day - 28;
        }
        else if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30){
            setMonth(month + 1);
            this.day = 1;
        }
        else if(day > 31){
            setMonth(month + 1);
            this.day = 1;
        }
        else
            this.day = day;
    }

    public void setMonth(int month) {
        if(month > 12){
            setYear(year + 1);
            this.month = 1;
        }
        else
            this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<LocalTime> getEndingHr() {
        return endingHr;
    }

    public ArrayList<LocalTime> getStartingHr() {
        return startingHr;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Date))
            return false;
        Date temp = (Date) other;
        return this.year == temp.getYear() && this.month == temp.getMonth() && this.day == temp.getDay();
    }

}
