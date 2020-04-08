package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repos.TeledonException;
import service.*;

import java.io.IOException;

public class LoginController{
    //private VolunteerService volunteerService;
    //private DonationService donationService;
    private ITeledonService server;
    private DonationController donationController;
    private Parent parent;

    public LoginController() {
        //this.server = server;
    }
    public void setParent (Parent parent){
        this.parent=parent;
    }

    public void setController(DonationController donationController){
        this.donationController=donationController;
    }
    @FXML
    TextField textFieldUsername;

    @FXML
    TextField textFieldPassword;

    @FXML
    Button buttonLogin;


    public void init(){
        buttonLogin.setOnAction(x->{
            try{
                String username=textFieldUsername.getText();
                String password=textFieldPassword.getText();
                server.login(this.donationController,username,password);
                loginVolunteer(username);
            }catch (Exception e){
                e.printStackTrace();
                showErrorMessage(e.getMessage());
            }
        });
    }

    private static void showErrorMessage(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }

    public void loginVolunteer(String username) throws IOException, TeledonException {
        Stage primaryStage=new Stage();

        //FXMLLoader loader=new FXMLLoader();
       // loader.setLocation(getClass().getResource("/views/DonationView.fxml"));
        //AnchorPane root=loader.load();

       // DonationController donationController=loader.getController();
      //  donationController.setService(server);

        primaryStage.setScene(new Scene(parent,700,500));
        primaryStage.setTitle("voluntar "+username);
        primaryStage.show();
        donationController.init();

    }

    public void setService(ITeledonService server){
//        this.volunteerService=volunteerService;
//        this.donationService=donationService;
        this.server=server;
    }

}
