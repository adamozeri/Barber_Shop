package controllers;

import interfaces.ControlledScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.BarberObserver;
import models.BarberShop;
import models.User;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable, ControlledScreen {

    private final BarberShop model = BarberShop.getInstance();
    private ScreensController myController;

    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private Label loginError;

    public LoginController() throws Exception {

    }

    @FXML
    public void loginApplication(ActionEvent event)  {
        String username = user.getText();
        String pass = password.getText();
        if (username.equals(model.getUsername()) && pass.equals(model.getPassword())) {
            changeScreen(event, "Barber");
            return;
        }
        for (BarberObserver observer : model.getAllCustomers()) {
            if (username.equals(observer.getUsername()) && pass.equals(observer.getPassword())) {
                User currentUser = new User(username,pass);
                model.setCurrentUser(currentUser);
                changeScreen(event, "Menu Customer");
                MenuCustomerController.stCustomerName.setText(model.getCurrentUser().getUserName());
                MenuCustomerController.stPhoneNumber.setText(model.getPhoneNumber());
                MenuCustomerController.stAddress.setText(model.getAddress());
                return;
            }
        }
        loginError.setText("Wrong username or password!");
        loginError.setVisible(true);
    }
    @FXML
    public void signUpBtn(ActionEvent event) {
        changeScreen(event, "Sign Up");
    }


    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void changeScreen(ActionEvent event, String name) {
        password.setText("");
        user.setText("");
        loginError.setText("");
        myController.setScreen(name);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}