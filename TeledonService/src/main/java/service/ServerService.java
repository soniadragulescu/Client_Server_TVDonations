package service;

import entities.Case;
import entities.Donor;
import repos.TeledonException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerService implements ITeledonService {
    private VolunteerService volunteerService;
    private DonationService donationService;
    private List<ITeledonObserver> observers;

    public ServerService(VolunteerService volunteerService, DonationService donationService) {
        this.volunteerService = volunteerService;
        this.donationService = donationService;
        observers=new ArrayList<>();
    }

    @Override
    public synchronized void login(ITeledonObserver client, String username, String password) throws TeledonException {

        volunteerService.loginVolunteer(username,password);
        observers.add(client);
    }

    @Override
    public synchronized void saveDonation(Integer donorId, String name, String address, String telephone, Double sum, Integer caseId) throws TeledonException {
        donationService.saveDonation(donorId, name, address, telephone, sum, caseId);
        notifyVolunteers();
    }

    @Override
    public List<Donor> searchDonorByName( String substring) throws TeledonException {
        return donationService.searchDonorByName(substring);
    }

    @Override
    public List<Case> getAllCases() {
        return donationService.getAllCases();
    }

    @Override
    public synchronized void logout(ITeledonObserver client) {
        observers.remove(client);
    }

    private final int defaultThreadsNo=5;
    private void notifyVolunteers() throws TeledonException {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(ITeledonObserver observer:observers){
            executor.execute(()->{
                try{
                    System.out.println("notifying ...");
                    List<Donor> donors=donationService.searchDonorByName("");
                    List<Case> cases=donationService.getAllCases();
                    observer.donationDone(donors, cases);
                }catch (TeledonException e){
                    System.out.println("error notifying ...");
                }
            });

        }
        executor.shutdown();
    }
}
