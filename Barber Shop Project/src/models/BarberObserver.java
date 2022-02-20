package models;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class BarberObserver{
    protected BarberShop barberSubject;

    public abstract void notifyAppCancellation(Appointment appointment);
    public abstract void notifyActivityTime(LocalDate date);
    public abstract ArrayList<Appointment> getAllAppointments();
    public abstract BarberShop getBarberSubject();
    public abstract String getUsername();
    public abstract String getPhoneNumber();
    public abstract void removeAppointment(Appointment appointment) throws SQLException;
    public abstract boolean equals(Object other);
    public abstract String getPassword();
}
