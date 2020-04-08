package networkUtils;

import entities.Case;
import entities.Donor;
import entities.Volunteer;
import service.ITeledonObserver;
import service.ITeledonService;
import repos.TeledonException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable, ITeledonObserver {
    private ITeledonService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(ITeledonService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if(request instanceof LoginRequest){
            System.out.println("Login request...");
            LoginRequest loginRequest=(LoginRequest)request;
            Volunteer volunteer=loginRequest.getVolunteer();
            try{
                server.login(this,volunteer.getUsername(),volunteer.getPassword());

                    return new OKResponse();

            } catch (TeledonException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(request instanceof GetAllCasesRequest){
            System.out.println("Get all cases request... ");
            GetAllCasesRequest getAllCasesRequest=(GetAllCasesRequest)request;
            try{
                List<Case> cases=server.getAllCases();
                return new GetAllCasesResponse(cases);
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }

        if(request instanceof GetAllDonorsRequest){
            System.out.println("Get all donors request... ");
            GetAllDonorsRequest getAllDonorsRequest=(GetAllDonorsRequest)request;
            String substring=getAllDonorsRequest.getSubstring();
            try{
                List<Donor> donors=server.searchDonorByName(substring);
                return  new GetAllDonorsResponse(donors);

            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }

        if(request instanceof NewDonationRequest){
            System.out.println("New donation request... ");
            NewDonationRequest newDonationRequest=(NewDonationRequest)request;
            Integer donorId=newDonationRequest.getDonorId();
            String name=newDonationRequest.getName();
            String address=newDonationRequest.getAddress();
            String telephone=newDonationRequest.getTelephone();
            Double sum=newDonationRequest.getSum();
            Integer caseId=newDonationRequest.getCaseId();
            try{
                server.saveDonation(donorId,name,address,telephone,sum,caseId);
                return new OKResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(request instanceof LogoutRequest){
            System.out.println("New logout request... ");
            try{
                connected=false;
                return new OKResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

   // @Override
//    public void volunteerLoggedIn(String username, String password) throws TeledonException {
//        System.out.println("Volunteer logged in "+username);
//        //this.server.login(this, username,password);
//        try{
//            sendResponse(new OKResponse());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void donationDone(List<Donor> donors,List<Case> cases) throws TeledonException {
        //this.server.saveDonation(donorId,name,address,telephone,sum,caseId);
       // List<Donor> donors=server.searchDonorByName("");
        //List<Case> cases=server.getAllCases();
        try{
            sendResponse(new NewDonationResponse(donors, cases));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
