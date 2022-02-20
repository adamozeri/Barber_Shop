package controllers;

import interfaces.ControlledScreen;
import interfaces.Haircut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.*;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CustomerController implements Initializable, ControlledScreen {

    private final BarberShop model = BarberShop.getInstance();
    private ScreensController myController;


    @FXML
    private Label customerName;

    public static Label stCustomerName;

    @FXML
    private ComboBox<Haircut> cmbHaircut;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<LocalTime> cmbAppTime;

    public CustomerController() throws Exception {
    }

    @FXML
    public void bookAppointment(ActionEvent event) throws SQLException {
        if (datePicker.getValue() == null || cmbHaircut.getValue() == null || cmbAppTime.getValue() == null) {
            JOptionPane.showMessageDialog(null, "You must fill all fields");
        }
        else{
            Haircut newHaircut = cmbHaircut.getValue();
            LocalTime startTime = cmbAppTime.getValue();
            Customer currentCustomer = null;
            for (BarberObserver c : model.getAllCustomers()) {
                if (c.getUsername().equals(model.getCurrentUser().getUserName()))
                    currentCustomer = (Customer) c;
            }
            Appointment app = Customer.makeAnAppointment(newHaircut, startTime, datePicker.getValue(), currentCustomer);
            if(app != null)
                BarberShop.getAppointmentQuery().insertAppointment(model.getConn(),app);
            JOptionPane.showMessageDialog(null, "Thanks for making an appointment!");
            datePicker.setDisable(true);
            changeScreen(event, "Menu Customer");
        }
    }

    @FXML
    public void backPageBtn(ActionEvent event) {
        changeScreen(event, "Menu Customer");
        datePicker.setDisable(true);
    }

    @FXML
    public void initTimeCmb() {
        cmbAppTime.getItems().clear();
        if (datePicker.getValue() == null)
            return;
        Date currentDate = getDate();
        Haircut temp = cmbHaircut.getValue();
        float startTime = (float) (model.getStartTime().getHour() + (model.getStartTime().getMinute() / 60));
        float endTime = (float) (model.getEndTime().getHour() + (model.getEndTime().getMinute() / 60));
        LocalTime current = model.getStartTime();
        int duration = (int) (temp.getDuration() * 60);
        while (startTime <= (endTime - temp.getDuration())) {
            if (checkBookedTime(current, currentDate, (float) (duration) / 60))
                cmbAppTime.getItems().add(current);
            current = current.plusMinutes(duration);
            startTime += temp.getDuration();
        }
    }

    ////////////////////////////////////////////////
    ///checking if the time is available for booking
    ////////////////////////////////////////////////
    public boolean checkBookedTime(LocalTime currentTime, Date currentDate, float duration) {
        float time = currentTime.getHour() + (float) (currentTime.getMinute()) / 60;
        for (int i = 0; i < currentDate.getStartingHr().size(); i++) {
            float startTime = currentDate.getStartingHr().get(i).getHour() + (float) (currentDate.getStartingHr().get(i).getMinute()) / 60;
            float endTime = currentDate.getEndingHr().get(i).getHour() + (float) (currentDate.getEndingHr().get(i).getMinute()) / 60;
            if (time >= startTime && time < endTime || time <= startTime && time > startTime - duration)
                return false;
        }
        return true;
    }

    ////////////////////////////////////
    // getting the current date from Date Picker
    ////////////////////////////////////
    public Date getDate() {
        LocalDate d = datePicker.getValue();
        Date currentDate = new Date(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth());
        for (int i = 0; i < 31; i++) { // finding the pressed date
            if (currentDate.equals(model.getCalendar().getAllDates().get(i))) {
                return model.getCalendar().getAllDates().get(i);
            }
        }
        return null;
    }

    public void initHaircutCmb() {
        cmbHaircut.getItems().clear();
        for (int i = 0; i < model.getAllHaircuts().size(); i++) {
            cmbHaircut.getItems().add(model.getAllHaircuts().get(i));
        }
    }

    public void initDatePicker() {
        datePicker.setDisable(false);
        datePicker.setValue(null);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today.plusMonths(1)) > 0 || date.compareTo(today.minusDays(0)) < 0 || date.getDayOfWeek().name().equals("SATURDAY"));
                for (int i = 0; i < 31; i++) {
                    if (!model.getCalendar().getAllDates().get(i).isAvailable()) {
                        LocalDate temp = LocalDate.of(model.getCalendar().getAllDates().get(i).getYear(), model.getCalendar().getAllDates().get(i).getMonth(), model.getCalendar().getAllDates().get(i).getDay());
                        setDisable(isDisable() || date.compareTo(temp) == 0);
                    }
                }
                if (isDisable())
                    setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: #5E7982;");
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        datePicker.setDisable(true);
        initHaircutCmb();
    }

    @Override
    public void changeScreen(ActionEvent event, String name) {
        cmbAppTime.getItems().clear();
        cmbHaircut.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        datePicker.setDisable(true);
        myController.setScreen(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stCustomerName = customerName;
    }
}
