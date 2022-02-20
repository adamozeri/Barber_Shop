package controllers;

import interfaces.ControlledScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.BarberObserver;
import models.BarberShop;
import models.Customer;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable, ControlledScreen {

    private final BarberShop model = BarberShop.getInstance();
    private ScreensController myController;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpPassword;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Label signupError;
    @FXML
    private ComboBox<String> phonePrefix;

    public void initPhoneCmb(){
        phonePrefix.getItems().clear();
        for (int i = 2; i < 10; i++) {
            phonePrefix.getItems().add("05" + i);
        }
    }

    @FXML
    public void registerBtn(ActionEvent event) {
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || signUpUsername.getText().isEmpty()
                || signUpPassword.getText().isEmpty() || phoneNumber.getText().isEmpty() || phonePrefix.getValue() == null) {
            signupError.setText("You must to fill all fields");
            signupError.setVisible(true);
        }
        //TODO: exceptions
        else {
            String phone = phonePrefix.getValue() + phoneNumber.getText();
            if (signUpUsername.getText().equals(model.getUsername()) || phone.equals(model.getPhoneNumber())) {
                signupError.setText("Username/ID/Phone Number already exists");
                return;
            }
            String name = firstName.getText() + " " + lastName.getText();
            for (BarberObserver c : model.getAllCustomers()) {
                if (c.getPhoneNumber().equals(phone) || c.getUsername().equals(signUpUsername.getText())) {
                    signupError.setText("Username/ID/Phone Number already exists");
                    return;
                }
            }
            try {
                Customer newCustomer = new Customer(name, phone, model, signUpUsername.getText(), signUpPassword.getText()); //creating the new customer
                BarberShop.getCustomerQuery().insertCustomers(model.getConn(),model,newCustomer);
                JOptionPane.showMessageDialog(null,"Thank you for signing up!");
                changeScreen(event, "Login");
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
        }
    }

    @FXML
    public void cancelBtn(ActionEvent event){
        changeScreen(event,"Login");
    }

    public SignUpController() throws Exception {
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        initPhoneCmb();
    }

    @Override
    public void changeScreen(ActionEvent event, String name) {
        signUpUsername.setText("");
        signUpPassword.setText("");
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        myController.setScreen(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
