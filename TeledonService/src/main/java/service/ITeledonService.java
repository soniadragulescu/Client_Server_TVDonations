package service;

import entities.Case;
import entities.Donor;
import repos.TeledonException;

import java.util.List;

public interface ITeledonService {
    public void login(ITeledonObserver client,String username, String password) throws TeledonException;
    public void saveDonation(Integer donorId,String name, String address, String telephone, Double sum,Integer caseId) throws TeledonException;
    public List<Donor> searchDonorByName(String substring) throws TeledonException;
    public List<Case> getAllCases() throws TeledonException;
    public void logout(ITeledonObserver client) throws TeledonException;

}
