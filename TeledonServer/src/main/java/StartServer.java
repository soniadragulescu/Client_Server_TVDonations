import networkUtils.AbstractServer;
import networkUtils.ConcurrentServer;
import networkUtils.ServerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.DonationService;
import service.ServerService;
import service.VolunteerService;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;
    public static void main(String[] args){
        Properties serverProperties=new Properties();
        try {
            serverProperties.load(StartServer.class.getResourceAsStream("/teledonserver.properties"));
            System.out.println("Server properties set.");
            serverProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find teledonserver.properties "+e);
            return;
        }


        ApplicationContext context = new ClassPathXmlApplicationContext("MPPdonations.xml");

        VolunteerService volunteerService=context.getBean(VolunteerService.class);
        DonationService donationService=context.getBean(DonationService.class);

        ServerService serverService=new ServerService(volunteerService,donationService);



        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProperties.getProperty("teledonserver.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ConcurrentServer(serverPort, serverService);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
