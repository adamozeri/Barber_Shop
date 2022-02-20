package models;

import interfaces.Haircut;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Customer extends BarberObserver{

    private String name;
    private String phoneNumber;
    private String username;
    private String password;
    private ArrayList<Appointment> allAppointments;

    public Customer(String name, String phoneNumber, BarberShop subject, String username, String password) throws Exception {
        this.barberSubject = subject;
        setPhoneNumber(phoneNumber);
        setCustomerName(name);
        setUsername(username);
        setPassword(password);
        this.allAppointments = new ArrayList<>();
        this.barberSubject.attach(this);
    }

    public ArrayList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    @Override
    public BarberShop getBarberSubject() {
        return this.barberSubject;
    }

    @Override
    public void notifyAppCancellation(Appointment appointment) {
        System.out.println(this + " Your Appointment" + appointment + " Has been canceled");
    }

    @Override
    public void notifyActivityTime(LocalDate date) {
        System.out.println("I'm not working on " + date);
    }

    public void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    public void removeAppointment(Appointment appointment) throws SQLException {
        barberSubject.removeAppointment(appointment);
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).equals(appointment)) {
                allAppointments.remove(i);
                return;
            }
        }
    }

    public static synchronized Appointment makeAnAppointment(Haircut haircut, LocalTime startingTime, LocalDate date, Customer customer) {
        int hours = (int) haircut.getDuration();
        int minutes = (int) ((haircut.getDuration() % 1) * 60);
        LocalTime endingTime = startingTime.plusHours(hours);
        endingTime = endingTime.plusMinutes(minutes);
        Appointment newAppointment = new Appointment(date, startingTime, endingTime, customer, haircut);
        customer.addAppointment(newAppointment);
        customer.getBarberSubject().addAppointment(newAppointment, date, startingTime, endingTime);
        return newAppointment;
    }

    public void setUsername(String username) throws Exception {
        if (username.length() > 15 || username.length() < 3)
            throw new Exception("Username's length must be between 3 and 15");
        this.username = username;
    }

    public void setPassword(String password) throws Exception {
        if (password.length() > 15 || password.length() < 4)
            throw new Exception("Password's length must be between 4 and 15");
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) throws PhoneException {
        if (checkStr(phoneNumber, 10))
            throw new PhoneException();
        this.phoneNumber = phoneNumber;
    }

    public boolean checkStr(String str, int length) {
        if (str.length() != length)
            return true;
        for (int i = 0; i < length; i++) {
            if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Name: ");
        sb.append(name).append(", Phone Number: ").append(phoneNumber).append("\n");
        return sb.toString();
    }


    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Customer)) {
            return false;
        }
        Customer temp = (Customer) other;
        return this.phoneNumber.equals(temp.phoneNumber) && this.username.equals(temp.getUsername());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getCustomerName() {
        return name;
    }

    public void setCustomerName(String name) throws Exception {
        int length = name.length();
        String tempName = name.toLowerCase();
        for (int i = 0; i < length; i++) {
            if ((tempName.charAt(i) < 'a' || tempName.charAt(i) > 'z') && tempName.charAt(i) != ' ' || length <= 1) {
                throw new Exception("Name must be only letters and more than 2 letters.");
            }
        }
        this.name = name;
    }
}
