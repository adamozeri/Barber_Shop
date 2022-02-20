package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import interfaces.ControlledScreen;
import interfaces.Haircut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import models.Appointment;
import models.BarberShop;
import models.PhoneException;

public class BarberController implements Initializable, ControlledScreen {

    private final BarberShop model = BarberShop.getInstance();
    private ScreensController myController;


    @FXML
    private Label dateLbl;

    @FXML
    private Label appLbl;

    @FXML
    private Button deleteBtn;

    @FXML
    private DatePicker showDatePicker;

    @FXML
    private DatePicker removeDatePicker;

    @FXML
    private Pane showAppPane;

    @FXML
    private Pane cancelAppPane;

    @FXML
    private TextArea allApp;

    @FXML
    private ComboBox<Appointment> cancelCmb;

    ///////////////////
    //  Barber Info
    //////////////////

    @FXML
    private ComboBox<Haircut> haircutCmb;

    @FXML
    private TextField address;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField cost;

    @FXML
    private CheckBox dayOff;

    @FXML
    private CheckBox setDefault;

    @FXML
    private DatePicker activityDatePicker;

    @FXML
    private ComboBox<LocalTime> dateStartTime;

    @FXML
    private ComboBox<LocalTime> dateEndTime;

    @FXML
    private ComboBox<LocalTime> defaultStartTime;

    @FXML
    private ComboBox<LocalTime> defaultEndTime;

    @FXML
    private Pane editPane;

    @FXML
    private ComboBox<String> phonePrefix;

    @FXML
    public void activityDatePickerOnAction() {
        initStartTimeCmb(dateStartTime);
        dayOff.setDisable(false);
        dayOff.setSelected(false);
        setDefault.setDisable(false);
        setDefault.setSelected(false);
        for (int i = 0; i < 31; i++) {
            if (!model.getCalendar().getAllDates().get(i).isAvailable()) {
                LocalDate temp = LocalDate.of(model.getCalendar().getAllDates().get(i).getYear(), model.getCalendar().getAllDates().get(i).getMonth(), model.getCalendar().getAllDates().get(i).getDay());
                if (temp.equals(activityDatePicker.getValue()))
                    dayOff.setSelected(true);
            }
        }
    }

    @FXML
    public void costEnable(ActionEvent event) {
        cost.setDisable(false);
    }

    public void initHaircutCmb() {
        haircutCmb.getItems().clear();
        for (int i = 0; i < model.getAllHaircuts().size(); i++) {
            haircutCmb.getItems().add(model.getAllHaircuts().get(i));
        }
    }


    public void initStartTimeCmb(ComboBox<LocalTime> startTime) {
        startTime.setDisable(false);
        startTime.getItems().clear();
        LocalTime temp = model.getStartTime();
        while (temp.getHour() < model.getEndTime().getHour()) {
            startTime.getItems().add(temp);
            temp = temp.plusMinutes(30);
        }
    }

    public void initDefaultStartTime(ComboBox<LocalTime> startTime) {
        startTime.setDisable(false);
        startTime.getItems().clear();
        LocalTime temp = LocalTime.of(6, 0);
        while (temp.getHour() < 14) {
            startTime.getItems().add(temp);
            temp = temp.plusMinutes(30);
        }
    }

    
    public void initDefaultEndTimeCmb(LocalTime start, ComboBox<LocalTime> end) {
        if (start == null)
            end.getItems().clear();
        end.setDisable(false);
        LocalTime temp = start;
        temp = temp.plusMinutes(30);
        while (temp.getHour() > 0) {
            end.getItems().add(temp);
            temp = temp.plusMinutes(30);
        }
    }

    public void initEndTimeCmb(LocalTime start, ComboBox<LocalTime> end) {
        if (start == null)
            end.getItems().clear();
        end.setDisable(false);
        LocalTime temp = start;
        while (temp.getHour() < model.getEndTime().getHour()) {
            temp = temp.plusMinutes(30);
            end.getItems().add(temp);
        }
    }

    public void disableEditButtons() {
        cost.setDisable(true);
        activityDatePicker.setValue(null);
        cost.setText("");
        phoneNumber.setText("");
        address.setText("");
        dateEndTime.setDisable(true);
        defaultEndTime.setDisable(true);
        dateStartTime.setDisable(true);
        dayOff.setDisable(true);
        dayOff.setSelected(false);
        setDefault.setDisable(true);
        defaultEndTime.getItems().clear();
        dateEndTime.getItems().clear();
        dateStartTime.getItems().clear();
        initHaircutCmb();
    }

    public void disableShowButtons() {
        allApp.setText("");
        appLbl.setVisible(false);
        showDatePicker.setValue(null);
        showAppPane.setVisible(false);
    }

    public void disableRemoveButtons() {
        cancelAppPane.setVisible(false);
        removeDatePicker.setValue(null);
    }

    @FXML
    public void dateEnd() {
        if (dateStartTime.getValue() != null)
            initEndTimeCmb(dateStartTime.getValue(), dateEndTime);
    }

    @FXML
    public void defaultEnd() {
        if (defaultStartTime.getValue() != null)
            initDefaultEndTimeCmb(defaultStartTime.getValue(), defaultEndTime);
    }

    @FXML
    public void editBtn(ActionEvent event) {
        editPane.setVisible(true);
        cost.setDisable(true);
        initHaircutCmb();
        initDatePicker(activityDatePicker, false);
        initDefaultStartTime(defaultStartTime);
        disableRemoveButtons();
        disableShowButtons();
    }

    @FXML
    public void showAllApp(ActionEvent event) {
        showAppPane.setVisible(true);
        editPane.setVisible(false);
        appLbl.setVisible(false);
        dateLbl.setVisible(false);
        disableEditButtons();
        disableRemoveButtons();
        allApp.setText("");
        initDatePicker(showDatePicker, true);
    }

    @FXML
    public void deleteBtnEnable() {
        deleteBtn.setDisable(false);
    }

    //////////////////////////
    // Delete the appointment
    //////////////////////////
    @FXML
    public void deleteApp(ActionEvent event) throws SQLException {
        if (cancelCmb.getValue() != null) {
            model.cancelAppointment(cancelCmb.getValue());
            cancelCmb.getItems().clear();
            initCancelCmb();
            deleteBtn.setDisable(true);
        }
    }


    @FXML
    public void cancelApp(ActionEvent event) {
        cancelAppPane.setVisible(true);
        deleteBtn.setDisable(true);
        cancelCmb.getItems().clear();
        disableEditButtons();
        disableShowButtons();
        editPane.setVisible(false);
        initDatePicker(removeDatePicker, true);
    }

    @FXML
    public void saveChange(ActionEvent event) throws SQLException {
        boolean isChanged = false;
        if (!phoneNumber.getText().isEmpty() && phonePrefix.getValue() != null) {
            try {
                model.setPhoneNumber(phonePrefix.getValue() + phoneNumber.getText());
                isChanged = true;
            }catch (PhoneException e){
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
        }
        if (!address.getText().isEmpty()) {
            try {
                model.setAddress(address.getText());
                isChanged = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        if (updateHaircut())
            isChanged = true;
        if (updateActivity())
            isChanged = true;
        if (defaultStartTime.getValue() != null && defaultEndTime.getValue() != null) {
            model.updateDefaultHours(defaultStartTime.getValue(), defaultEndTime.getValue());
            isChanged = true;
        }
        if (isChanged)
            JOptionPane.showMessageDialog(null, "Your Info Got Updated");
        else
            JOptionPane.showMessageDialog(null, "You must fill one or more fields to make changes");
        disableEditButtons();
        model.updateBarberDB();
    }

    public boolean updateActivity() throws SQLException {
        if (activityDatePicker.getValue() != null) {
            if (dayOff.isSelected()) {
                model.updateDayOff(activityDatePicker.getValue()); // cancel and notify the customer
                return true;
            } else if (dateStartTime.getValue() != null && dateEndTime.getValue() != null) {
                model.updateActivityHours(activityDatePicker.getValue(), dateStartTime.getValue(), dateEndTime.getValue());
                return true;
            } else if (setDefault.isSelected()) {
                model.setDateDefault(activityDatePicker.getValue());
                return true;
            }
        }
        return false;
    }

    public boolean updateHaircut() {
        if (haircutCmb.getValue() != null) {
            if (!cost.getText().isEmpty()) {
                for (int i = 0; i < model.getAllHaircuts().size(); i++) {
                    if (model.getAllHaircuts().get(i).equals(haircutCmb.getValue())) {
                        int newCost = Integer.parseInt(cost.getText());
                        try {
                            model.getAllHaircuts().get(i).updateCost(newCost);
                            BarberShop.getHaircutQuery().updateHaircuts(model.getConn(),model,haircutCmb.getValue());
                            return true;
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                    }
                }
            }
        }
        return false;
    }

    public void initPhoneCmb(){
        phonePrefix.getItems().clear();
        for (int i = 2; i < 10; i++) {
            phonePrefix.getItems().add("05" + i);
        }
    }

    @FXML
    public void dayOffSelected(ActionEvent event) {
        if (dayOff.isSelected()) {
            dateStartTime.getItems().clear();
            dateEndTime.getItems().clear();
            dateStartTime.setDisable(true);
            dateEndTime.setDisable(true);
            setDefault.setDisable(true);
            setDefault.setSelected(false);
        } else {
            setDefault.setDisable(false);
            initStartTimeCmb(dateStartTime);
            dateStartTime.setDisable(false);
        }
    }

    @FXML
    public void setDefaultSelected() {
        if (setDefault.isSelected()) {
            dayOff.setSelected(false);
            dayOff.setDisable(true);
            dateStartTime.setDisable(true);
            dateEndTime.setDisable(true);
            dateStartTime.getItems().clear();
            dateEndTime.getItems().clear();
        } else {
            dayOff.setDisable(false);
            dateStartTime.setDisable(false);
            dateEndTime.setDisable(false);
        }
    }

    public void initDatePicker(DatePicker datePicker, boolean flag) {
        datePicker.setDisable(false);
        datePicker.setValue(null);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today.plusMonths(1)) > 0 || date.compareTo(today.minusDays(0)) < 0 || date.getDayOfWeek().name().equals("SATURDAY"));
                for (int i = 0; i < 31; i++) {
                    if (!model.getCalendar().getAllDates().get(i).isAvailable() && flag) {
                        LocalDate temp = LocalDate.of(model.getCalendar().getAllDates().get(i).getYear(), model.getCalendar().getAllDates().get(i).getMonth(), model.getCalendar().getAllDates().get(i).getDay());
                        setDisable(isDisable() || date.compareTo(temp) == 0);
                    } else if (!model.getCalendar().getAllDates().get(i).isAvailable() && !flag) {
                        LocalDate temp = LocalDate.of(model.getCalendar().getAllDates().get(i).getYear(), model.getCalendar().getAllDates().get(i).getMonth(), model.getCalendar().getAllDates().get(i).getDay());
                        if (date.compareTo(temp) == 0)
                            setStyle("-fx-background-color: #ffc300;-fx-text-fill: #000000;");
                    }
                }
                if (isDisable())
                    setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: #5E7982;");
            }
        });
    }

    @FXML
    public void initCancelCmb() {
        for (int i = 0; i < model.getAllAppointments().size(); i++) {
            if (model.getAllAppointments().get(i).getDate().equals(removeDatePicker.getValue()))
                cancelCmb.getItems().add(model.getAllAppointments().get(i));
        }
    }

    @FXML
    public void logoutBtn(ActionEvent event) {
        changeScreen(event, "Login");
    }

    @FXML
    public void showAppInDate() {
        if (showDatePicker.getValue() != null) {
            appLbl.setVisible(true);
            dateLbl.setVisible(true);
            dateLbl.setText(showDatePicker.getValue().toString());
        }
        if (model.showAllAppointments(showDatePicker.getValue()).equals(""))
            allApp.setText("You don't have any appointments in this date.");
        else
            allApp.setText(model.showAllAppointments(showDatePicker.getValue()));
    }

    public BarberController() throws Exception {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        disableRemoveButtons();
        disableEditButtons();
        disableShowButtons();
        initPhoneCmb();
        editPane.setVisible(false);
    }


    public void changeScreen(ActionEvent event, String name) {
        disableEditButtons();
        disableRemoveButtons();
        disableShowButtons();
        editPane.setVisible(false);
        myController.setScreen(name);
    }


}
