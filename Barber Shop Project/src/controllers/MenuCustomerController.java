package controllers;

import interfaces.ControlledScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import models.Appointment;
import models.BarberObserver;
import models.BarberShop;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuCustomerController implements Initializable, ControlledScreen {

    private final BarberShop model = BarberShop.getInstance();
    private ScreensController myController;

    public static Label stCustomerName;
    public static Label stAddress;
    public static Label stPhoneNumber;

    @FXML
    private Label customerName;

    @FXML
    private Label address;

    @FXML
    private Label phoneNumber;

    @FXML
    private Button bookAppointment;

    @FXML
    private Button showAppointments;

    @FXML
    private Button logout;

    @FXML
    private Button cancel;

    @FXML
    private TextArea allAppointments;

    @FXML
    private ComboBox<Appointment> cancelAppCmb;

    @FXML
    private Button confirmCancel;

    @FXML
    private Pane cancelPane;

    public MenuCustomerController() throws Exception {
    }


    @FXML
    public void showAppointmentsBtn(ActionEvent event) {
        cancelPane.setVisible(false);
        allAppointments.setVisible(true);
        allAppointments.clear();
        for (BarberObserver c : model.getAllCustomers()) {
            if (c.getUsername().equals(model.getCurrentUser().getUserName())) {
                if (c.getAllAppointments().size() == 0) {
                    allAppointments.setText("You don't have any appointments.");
                    return;
                }
                for (int i = 0; i < c.getAllAppointments().size(); i++) {
                    allAppointments.appendText(c.getAllAppointments().get(i).toString());
                    allAppointments.appendText("\n");
                }
                return;
            }
        }
    }

    public void initCancelCmb() {
        for (BarberObserver c : model.getAllCustomers()) {
            if (c.getUsername().equals(model.getCurrentUser().getUserName())) {
                if (c.getAllAppointments().size() == 0) {
                    JOptionPane.showMessageDialog(null, "You don't have any appointments.");
                }
                for (int i = 0; i < c.getAllAppointments().size(); i++) {
                    cancelAppCmb.getItems().add(c.getAllAppointments().get(i));
                }
            }
        }
    }


    @FXML
    public void cancelBtn(ActionEvent event) {
        cancelAppCmb.getItems().clear();
        allAppointments.setVisible(false);
        cancelPane.setVisible(true);
        confirmCancel.setDisable(true);
        initCancelCmb();
    }

    @FXML
    public void confirmCancelBtn(ActionEvent event) throws SQLException {
        for (BarberObserver c : model.getAllCustomers()) {
            if (c.getUsername().equals(model.getCurrentUser().getUserName())) {
                model.removeAppointment(cancelAppCmb.getValue());
                JOptionPane.showMessageDialog(null,"Appointment Cancelled Successfully");
                cancelPane.setVisible(false);
            }
        }
    }

    @FXML
    public void cancelBtnEnable(ActionEvent event) {
        confirmCancel.setDisable(false);
    }


    @FXML
    public void bookAppBtn(ActionEvent event) {
        changeScreen(event, "Customer Main");
        allAppointments.setVisible(false);
        CustomerController.stCustomerName.setText(model.getCurrentUser().getUserName());
    }

    @FXML
    public void logoutBtn(ActionEvent event) {
        changeScreen(event, "Login");
        allAppointments.setVisible(false);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        allAppointments.setVisible(false);
        allAppointments.setEditable(false);
        cancelPane.setVisible(false);
        confirmCancel.setDisable(true);
    }

    @Override
    public void changeScreen(ActionEvent event, String name) {
        cancelPane.setVisible(false);
        allAppointments.setVisible(false);
        myController.setScreen(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stCustomerName = customerName;
        stAddress = address;
        stPhoneNumber = phoneNumber;
    }
}
