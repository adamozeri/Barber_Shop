package models;
import interfaces.Haircut;
import sql.AppointmentQuery;
import sql.CustomerQuery;
import sql.DateQuery;
import sql.HaircutQuery;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class BarberShop {
    private static CustomerQuery customerQuery = new CustomerQuery();
    private static AppointmentQuery appointmentQuery = new AppointmentQuery();
    private static HaircutQuery haircutQuery = new HaircutQuery();
    private static DateQuery dateQuery = new DateQuery();
    private Connection conn;

    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private String phoneNumber;
    private String address;
    private Calendar calendar;
    private String username;
    private String password;
    private ArrayList<BarberObserver> allCustomers;
    private ArrayList<Haircut> allHaircuts;
    private ArrayList<Appointment> allAppointments;
    private static BarberShop firstInstance = null;
    private User currentUser;

    private BarberShop() throws ClassNotFoundException, SQLException {
        this.conn = getConnection();
        this.calendar = new Calendar();
        loadBarberDB();
        this.allCustomers = new ArrayList<BarberObserver>();
        this.allHaircuts = new ArrayList<Haircut>();
        this.allAppointments = new ArrayList<Appointment>();
        currentUser = null;
    }

    public static BarberShop getInstance() throws ClassNotFoundException, SQLException {
        synchronized (BarberShop.class) {
            if (firstInstance == null)
                firstInstance = new BarberShop();
        }
        return firstInstance;
    }

    public void loadDB() throws SQLException {
        dateQuery.loadDates(conn,this);
        calendar.initDates(conn);
        haircutQuery.loadHaircuts(conn,this);
        customerQuery.loadCustomers(conn, this);
        appointmentQuery.loadAppointments(conn,this);
    }

    public void loadBarberDB() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM barberTable");
        rs.next();
        this.name = rs.getString("name");
        this.phoneNumber = rs.getString("phoneNumber");
        this.address = rs.getString("address");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.startTime = rs.getTime("startTime").toLocalTime();
        this.endTime = rs.getTime("endTime").toLocalTime();
    }

    public void updateBarberDB() throws SQLException {
        PreparedStatement pst =conn.prepareStatement("Update barberTable Set address = '" + address + "' , phoneNumber = '"
                + phoneNumber + "' , name = '" + name + "' , startTime = '" + Time.valueOf(startTime) + "' , endTime = '" + Time.valueOf(endTime) + "'");
        pst.execute();
    }



    public static HaircutQuery getHaircutQuery() {
        return haircutQuery;
    }

    public static AppointmentQuery getAppointmentQuery() {
        return appointmentQuery;
    }

    public Connection getConn() {
        return conn;
    }

    public ArrayList<Haircut> getAllHaircuts() {
        return allHaircuts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public void attach(BarberObserver newCustomer) {
        allCustomers.add(newCustomer);
    }


    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static CustomerQuery getCustomerQuery() {
        return customerQuery;
    }

    ///////////////////////////////////////
    // add an appointment and update the calendar
    //////////////////////////////////////
    public void addAppointment(Appointment newAppointment, LocalDate date, LocalTime startTime, LocalTime endTime) {
        allAppointments.add(newAppointment);
        this.calendar.addAppointmentToDate(getDate(date), startTime, endTime);
    }

    ///////////////////////////////////////
    // remove an appointment and update the calendar
    //////////////////////////////////////
    public void removeAppointment(Appointment appointment) throws SQLException {
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).equals(appointment)) {
                allAppointments.remove(i);
                appointment.getCustomer().removeAppointment(appointment);
                appointmentQuery.deleteAppointment(conn,appointment);
                this.calendar.removeAppointmentFromDate(appointment.getDate(), appointment.getStartingTime());
                return;
            }
        }
    }

    ///////////////////////////////////////
    ///getting the value of the right Date
    //////////////////////////////////////
    public Date getDate(LocalDate date) {
        Date currentDate = new Date(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth());
        for (int i = 0; i < 31; i++) {
            if (currentDate.equals(this.getCalendar().getAllDates().get(i))) {
                return this.getCalendar().getAllDates().get(i);
            }
        }
        return null;
    }

    ///////////////////////////////////////
    // Updating the day off of the barbershop and notifying all observers(customers)
    //////////////////////////////////////
    public void updateDayOff(LocalDate date) throws SQLException {
        Date d = findDate(date);
        d.setAvailable(false);
        dateQuery.updateDates(conn,date,false);
        for (BarberObserver observer : allCustomers) {
            observer.notifyActivityTime(date);
            for (int i = 0; i < observer.getAllAppointments().size(); i++) {
                Appointment temp = observer.getAllAppointments().get(i);
                if (temp.getDate().equals(date)) {
                    //observer.notifyAppCancellation(temp);
                    cancelAppointment(temp);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //  finding all the 'vacations' in the specific date
    //  and removing them from the lists, so we are left with only appointments
    ///////////////////////////////////////////////////////////////////////////
    public void setDateDefault(LocalDate date) throws SQLException {
        Date d = getDate(date);
        d.setAvailable(true);
        dateQuery.updateDates(conn,date,true);
        for (int i = 0; i < d.getStartingHr().size(); i++) {
            LocalTime currStart = d.getStartingHr().get(i);
            LocalTime currEnd = d.getEndingHr().get(i);
            boolean check = false;

            for (int j = 0; j < getAllAppointments().size(); j++) {
                Appointment temp = getAllAppointments().get(j);
                if (temp.getDate().equals(date) && temp.getStartingTime().equals(currStart) && temp.getEndingTime().equals(currEnd)) {
                    check = true;
                    break;
                }
            }
            if (!check){
                d.getStartingHr().remove(i);
                d.getEndingHr().remove(i);
            }
        }
    }

    public void updateActivityHours(LocalDate date, LocalTime startTime, LocalTime endTime) throws SQLException {
        Date d = findDate(date);
        for (BarberObserver observer : allCustomers) {
            for (int i = observer.getAllAppointments().size()-1; i >= 0; i--) {
                Appointment temp = observer.getAllAppointments().get(i);
                if (isAppointmentInRage(temp, date, startTime, endTime)) {
                    //observer.notifyAppCancellation(temp);
                    cancelAppointment(temp);
                }
            }
        }
        d.getStartingHr().add(startTime);
        d.getEndingHr().add(endTime);
    }

    public void updateDefaultHours(LocalTime startTime, LocalTime endTime) throws SQLException {
        for(int i = 0; i < allAppointments.size(); i++){
            Appointment currApp = allAppointments.get(i);
            if(!isAppointmentInRage(allAppointments.get(i),currApp.getDate(),startTime,endTime)){
                //currApp.getCustomer().notifyAppCancellation(currApp);
                cancelAppointment(currApp);
            }
        }
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public Date findDate(LocalDate date) {
        for (int i = 0; i < this.calendar.getAllDates().size(); i++) {
            LocalDate temp = LocalDate.of(this.getCalendar().getAllDates().get(i).getYear(), this.getCalendar().getAllDates().get(i).getMonth(), this.getCalendar().getAllDates().get(i).getDay());
            if (temp.equals(date))
                return this.calendar.getAllDates().get(i);
        }
        return null;
    }


    public boolean isAppointmentInRage(Appointment app, LocalDate date, LocalTime startTime, LocalTime endTime) {
        LocalTime appStart = app.getStartingTime();
        LocalTime appEnd = app.getEndingTime();
        if (date.equals(app.getDate()) && (((appStart.isAfter(startTime) || appStart.equals(startTime)) && appStart.isBefore(endTime))
            || ((appEnd.isAfter(startTime)) && (appEnd.isBefore(endTime) || appEnd.equals(endTime))))) {
            return true;
        }
        return false;
    }

    ///////////////////////////////////////
    // Notifying the observers about the cancellation and removing the appointment from the lists
    //////////////////////////////////////
    public void cancelAppointment(Appointment appointment) throws SQLException {
        appointment.getCustomer().notifyAppCancellation(appointment);
        removeAppointment(appointment);
    }

    public String showAllAppointments(LocalDate date) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).getDate().equals(date)) {
                sb.append(allAppointments.get(i).getCustomer()).append(allAppointments.get(i)).append("\n");
            }
        }
        return sb.toString();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<BarberObserver> getAllCustomers() {
        return allCustomers;
    }

    public void setAddress(String address) throws Exception {
        if(address.length() < 3 || address.length() > 50){
            throw new Exception("Address must be between 3 and 20 characters");
        }
        int count = 0;
        address.toLowerCase();
        for (int i = 0; i < address.length(); i++) {
            if(address.charAt(i) >= 'a' && address.charAt(i) <= 'z')
                count++;
        }
        if(count < 3)
            throw new Exception("Address must have 3 letters or more");
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) throws PhoneException {
        if(checkStr(phoneNumber,10)){
            throw new PhoneException();
        }
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



    public static Connection getConnection() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrlString = "jdbc:mysql://localhost:3306/barber";
            conn = DriverManager.getConnection(dbUrlString, "root", "adamesti1204");
            return conn;
        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
}

