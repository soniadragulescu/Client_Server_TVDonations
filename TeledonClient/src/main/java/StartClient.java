import controllers.DonationController;
import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ITeledonService;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {

    private static int defaultPort=88888;
    private static String defaultServer="localhost";

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProperties=new Properties();
        try {
            clientProperties.load(StartClient.class.getResourceAsStream("/teledonclient.properties"));
            System.out.println("Client properties set.");
            clientProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find teledonclient.properties " + e);
            return;
        }

        String serverIP=clientProperties.getProperty("teledon.server.host", defaultServer);
        int serverPort=defaultPort;
        try{
            serverPort=Integer.parseInt(clientProperties.getProperty("teledon.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        ITeledonService server=new ProtoProxy(serverIP,serverPort);

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(StartClient.class.getResource("/views/LoginView.fxml"));
        AnchorPane root=loader.load();

        LoginController loginController=loader.getController();
        loginController.setService(server);

        FXMLLoader donationLoader=new FXMLLoader();
        donationLoader.setLocation(StartClient.class.getResource("/views/DonationView.fxml"));
        AnchorPane donationRoot=donationLoader.load();

        DonationController donationController=donationLoader.getController();
        donationController.setService(server);
        loginController.setController(donationController);
        loginController.setParent(donationRoot);

        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setTitle("LOGIN");

        primaryStage.show();
        loginController.init();
    }
}