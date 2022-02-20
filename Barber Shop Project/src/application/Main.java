package application;


import controllers.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.BarberShop;


public class Main extends Application {

    public static String loginScreenID = "Login";
    public static String loginScreenFile = "LoginScreen.fxml";
    public static String signUpID = "Sign Up";
    public static String signUpFile = "SignUpScreen.fxml";
    public static String barberScreenID = "Barber";
    public static String barberScreenFile = "BarberScreen.fxml";
    public static String customerMainScreenID = "Customer Main";
    public static String customerScreenFile = "CustomerMainScreen.fxml";
    public static String menuCustomerID = "Menu Customer";
    public static String menuCustomerFile = "MenuCustomerScreen.fxml";


    @Override
    public void start(Stage primaryStage) throws Exception {
        BarberShop barberShop = BarberShop.getInstance();
        barberShop.loadDB();
        try {
            ScreensController mainContainer = new ScreensController();
        	mainContainer.loadScreen(Main.loginScreenID,Main.loginScreenFile);
            mainContainer.loadScreen(Main.signUpID,Main.signUpFile);
            mainContainer.loadScreen(Main.barberScreenID,Main.barberScreenFile);
            mainContainer.loadScreen(Main.customerMainScreenID,Main.customerScreenFile);
            mainContainer.loadScreen(Main.menuCustomerID,Main.menuCustomerFile);
            mainContainer.setScreen(Main.loginScreenID);
            Group root = new Group();
            root.getChildren().addAll(mainContainer);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        

    }
}
