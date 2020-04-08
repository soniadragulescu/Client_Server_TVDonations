package service;

import entities.Case;
import entities.Donation;
import entities.Donor;
import repos.ICaseRepository;
import repos.IDonationRepository;
import repos.IDonorRepository;
import repos.TeledonException;

import java.util.ArrayList;
import java.util.List;

public class DonationService {
    private IDonorRepository donorRepository;
    private ICaseRepository caseRepository;
    private IDonationRepository donationRepository;

    public DonationService(IDonorRepository donorRepository, ICaseRepository caseRepository, IDonationRepository donationRepository) {
        this.donorRepository = donorRepository;
        this.caseRepository = caseRepository;
        this.donationRepository = donationRepository;
    }

    public void saveDonation(Integer donorId,String name, String address, String telephone, Double sum,Integer caseId) throws TeledonException {
        Case caz=this.caseRepository.findOne(caseId);
        if(caz==null){
            throw new TeledonException("No case!");
        }
        Donor donor=this.donorRepository.findOne(donorId);
        if(donor==null){
            this.donorRepository.save(new Donor(donorId,name, address, telephone));
        }
        Double totalSum=caz.getTotalSum();
        totalSum+=sum;
        this.caseRepository.update(totalSum,caseId);
        this.donationRepository.save(new Donation(donorId,sum,caseId));

    }

    public List<Donor> searchDonorByName(String substring){
        List<Donor> donors=new ArrayList<>();
        if(substring.equals("")){
            this.donorRepository.findAll().forEach(donors::add);
            return donors;
        }
        else{
            this.donorRepository.findByName(substring).forEach(donors::add);
            return donors;
        }
    }

    public List<Case> getAllCases(){
        List<Case> cases=new ArrayList<>();
        this.caseRepository.findAll().forEach(cases::add);
        return cases;
    }
}
