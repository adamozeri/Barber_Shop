package sql;

import models.BarberShop;
import models.Date;

import java.sql.*;
import java.time.LocalDate;

public class DateQuery {

    private static final String QUERY_DATE = "SELECT * FROM dateTable";
    private static final String QUERY_START_HR = "SELECT * FROM startHrTable";
    private static final String QUERY_END_HR = "SELECT * FROM endHrTable";


    public void loadDates(Connection conn, BarberShop barberShop) throws SQLException {
        deleteDates(conn);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_DATE);
        while (rs.next()) {
            LocalDate date = rs.getDate("date").toLocalDate();
            Date d = new Date(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth());
            d.setAvailable(rs.getBoolean("available"));
            barberShop.getCalendar().getAllDates().add(d);
        }
        ResultSet rs2 = stmt.executeQuery(QUERY_START_HR);
        while (rs2.next()) {
            Date d = barberShop.getDate(rs2.getDate("date").toLocalDate());
            d.getStartingHr().add(rs2.getTime("startHr").toLocalTime());
        }
        ResultSet rs3 = stmt.executeQuery(QUERY_END_HR);
        while (rs3.next()) {
            Date d = barberShop.getDate(rs3.getDate("date").toLocalDate());
            d.getEndingHr().add(rs3.getTime("endHr").toLocalTime());
        }
    }


    public void updateDates(Connection conn, LocalDate date, boolean available) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("Update dateTable Set available = " + available + " Where date = '" + java.sql.Date.valueOf(date) + "'");
        pst.execute();
    }

    public void deleteDates(Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("delete from dateTable where date < '" + java.sql.Date.valueOf(LocalDate.now()) + "'");
        pst.execute();
    }

    public void insertDates(Connection conn, LocalDate date, boolean available) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("insert INTO dateTable Values  (?, ?)");
        pst.setDate(1, java.sql.Date.valueOf(date));
        pst.setBoolean(2, available);
        pst.execute();
    }
}
