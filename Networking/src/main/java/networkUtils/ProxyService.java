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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyService implements ITeledonService {
    private String host;
    private int port;

    private ITeledonObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ProxyService(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws TeledonException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(ITeledonObserver client,String username, String password) throws TeledonException {
        initializeConnection();
        Volunteer volunteer=new Volunteer(username,password);
        sendRequest(new LoginRequest(volunteer));
        Response response=readResponse();
        if(response instanceof OKResponse){
            this.client=client;
            return;
        }
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new TeledonException(err.getMessage());
        }
    }

    public void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveDonation(Integer donorId, String name, String address, String telephone, Double sum, Integer caseId) throws TeledonException {
        sendRequest(new NewDonationRequest(donorId, name, address, telephone, sum, caseId));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new TeledonException(err.getMessage());
        }

        //NewDonationResponse newDonationResponse=(NewDonationResponse)response;
        return;
    }

    @Override
    public List<Donor> searchDonorByName(String substring) throws TeledonException {
        sendRequest(new GetAllDonorsRequest(substring));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new TeledonException(err.getMessage());
        }
        GetAllDonorsResponse getAllDonorsResponse=(GetAllDonorsResponse)response;
        List<Donor> donors=getAllDonorsResponse.getDonors();
        return donors;
    }

    @Override
    public List<Case> getAllCases() throws TeledonException {
        sendRequest(new GetAllCasesRequest());
        Response response=readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse er=(ErrorResponse)response;
            throw new TeledonException(er.getMessage());
        }

        GetAllCasesResponse getAllCasesResponse=(GetAllCasesResponse)response;
        List<Case> cases=getAllCasesResponse.getCases();
        return cases;
    }


    @Override
    public void logout(ITeledonObserver client) throws TeledonException {
        sendRequest(new LogoutRequest());
        Response response=readResponse();
        closeConnection();
        if(response instanceof ErrorResponse){
            ErrorResponse er=(ErrorResponse)response;
            throw new TeledonException(er.getMessage());
        }
    }

    private void sendRequest(Request request)throws TeledonException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new TeledonException("Error sending object "+e);
        }

    }

    private Response readResponse() {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse)response);
                    }else{
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(UpdateResponse update) {
        if(update instanceof NewDonationResponse){
           NewDonationResponse newDonationResponse=(NewDonationResponse)update;
            System.out.println("New donation done... ");
            try{
                List<Donor> donors=newDonationResponse.getDonors();
                List<Case> cases=newDonationResponse.getCases();
                client.donationDone(donors,cases);
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }

    }

}
