package sql;

import interfaces.Haircut;
import models.Appointment;
import models.BarberShop;
import models.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentQuery {

    private static final String QUERY_APPOINTMENT = "SELECT * FROM appointmentTable";

    public void loadAppointments(Connection conn, BarberShop barberShop) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_APPOINTMENT);
        while (rs.next()) {
            LocalDate date = rs.getDate("appointment_date").toLocalDate();
            LocalTime startTime = rs.getTime("starting_time").toLocalTime();
            LocalTime endTime = rs.getTime("ending_time").toLocalTime();
            Customer appCustomer = findCustomer(barberShop, rs.getString("username"));
            Appointment newAppointment = new Appointment(date, startTime, endTime, appCustomer, findHaircut(barberShop, rs.getString("Haircut_type")));
            barberShop.addAppointment(newAppointment, date, startTime, endTime);
            appCustomer.addAppointment(newAppointment);
        }
    }

    public void insertAppointment(Connection conn, Appointment app) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("insert INTO AppointmentTable Values  (?, ?, ?, ?, ?)");
        pst.setString(1,app.getCustomer().getUsername());
        pst.setDate(2,Date.valueOf(app.getDate()));
        pst.setTime(3,Time.valueOf(app.getStartingTime()));
        pst.setTime(4,Time.valueOf(app.getEndingTime()));
        pst.setString(5,app.getHaricut().getName());
        pst.execute();
    }

    public void deleteAppointment(Connection conn, Appointment app) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("delete from AppointmentTable where appointment_date = '" + Date.valueOf(app.getDate())
        + "' AND starting_time = '" + Time.valueOf(app.getStartingTime()) + "' AND ending_time = '" + Time.valueOf(app.getEndingTime()) + "'");
        pst.execute();
    }

    public Customer findCustomer(BarberShop barberShop, String username) {
        for (int i = 0; i < barberShop.getAllCustomers().size(); i++) {
            if (barberShop.getAllCustomers().get(i).getUsername().equals(username))
                return (Customer) barberShop.getAllCustomers().get(i);
        }
        return null;
    }

    public Haircut findHaircut(BarberShop barberShop, String name) {
        for (int i = 0; i < barberShop.getAllHaircuts().size(); i++) {
            if (barberShop.getAllHaircuts().get(i).getName().equals(name))
                return barberShop.getAllHaircuts().get(i);
        }
        return null;
    }
}
