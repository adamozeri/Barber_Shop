package models;

import interfaces.Haircut;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private LocalDate date;
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Customer customer;
    private Haircut type;

    public Appointment(LocalDate date, LocalTime startingTime, LocalTime endingTime, Customer customer, Haircut type){
        this.date = date;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.customer = customer;
        this.type = type;
    }


    public LocalTime getStartingTime() {
        return startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }
    public LocalDate getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Haircut getHaricut() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Appointment))
            return false;
        Appointment temp = (Appointment) other;
        return temp.getDate().equals(this.date) && temp.getStartingTime().equals(startingTime) && temp.getEndingTime().equals(endingTime);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Date: ");
        sb.append(date).append(", Start Time: ").append(startingTime).append(", Ending Time: ").append(endingTime).append(", Style: ").append(type.getName()).append("\n");
        return sb.toString();
    }
}
