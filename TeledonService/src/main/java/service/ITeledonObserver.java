package service;

import entities.Case;
import entities.Donor;
import repos.TeledonException;

import java.util.List;

public interface ITeledonObserver {
    //void volunteerLoggedIn(String username, String password) throws TeledonException;
    void donationDone(List<Donor> donors, List<Case> cases) throws TeledonException;
}
