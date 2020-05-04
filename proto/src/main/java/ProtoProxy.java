import entities.Case;
import entities.Donor;
import entities.Volunteer;
import repos.TeledonException;
import service.ITeledonObserver;
import service.ITeledonService;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements ITeledonService {
    private String host;
    private int port;

    private ITeledonObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Model.Response> qresponses;
    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Model.Response>();
    }

    private void initializeConnection() throws TeledonException {
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();
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
        sendRequest(ProtoUtils.createLoginRequest(volunteer));
        Model.Response response=readResponse();
        if(response.getType()==Model.Response.Type.OK){
            this.client=client;
            return;
        }
        if(response.getType()==Model.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            //ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new TeledonException(errorText);
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
        sendRequest(ProtoUtils.createNewDonationRequest(donorId, name, address, telephone, sum, caseId));
        Model.Response response=readResponse();
        if (response.getType()==Model.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new TeledonException(errorText);
        }

        //NewDonationResponse newDonationResponse=(NewDonationResponse)response;
        return;
    }

    @Override
    public List<Donor> searchDonorByName(String substring) throws TeledonException {
        sendRequest(ProtoUtils.createGetAllDonorsRequest(substring));
        Model.Response response=readResponse();
        if (response.getType()==Model.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new TeledonException(errorText);
        }
        List<Donor> donors=ProtoUtils.getDonors(response);
        return donors;
    }

    @Override
    public List<Case> getAllCases() throws TeledonException {
        sendRequest(ProtoUtils.createGetAllCasesRequest());
        Model.Response response=readResponse();
        if (response.getType()==Model.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new TeledonException(errorText);
        }
        List<Case> cases=ProtoUtils.getCases(response);
        return cases;
    }


    @Override
    public void logout(ITeledonObserver client) throws TeledonException {
        sendRequest(ProtoUtils.createLogoutRequest());
        Model.Response response=readResponse();
        closeConnection();
        if (response.getType()==Model.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new TeledonException(errorText);
        }
    }

    private void sendRequest(Model.Request request)throws TeledonException {
        try {
            System.out.println("Sending request... "+request);
            //output.writeObject(request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new TeledonException("Error sending object "+e);
        }

    }

    private Model.Response readResponse() {
        Model.Response response=null;
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
                    Model.Response response=Model.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);
                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Model.Response updateResponse) {
        if(updateResponse.getType()==Model.Response.Type.Update){
            //NewDonationResponse newDonationResponse=(NewDonationResponse)update;
            System.out.println("New donation done... ");
            try{
                List<Donor> donors=ProtoUtils.getDonors(updateResponse);
                List<Case> cases=ProtoUtils.getCases(updateResponse);
                client.donationDone(donors,cases);
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isUpdateResponse(Model.Response.Type type){
        if(type==Model.Response.Type.Update){
            return true;
        }
        return false;
    }

}
