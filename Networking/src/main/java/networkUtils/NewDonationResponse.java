package networkUtils;

import entities.Case;
import entities.Donation;
import entities.Donor;

import java.util.List;

public class NewDonationResponse implements UpdateResponse {
//    private Integer donorId;
//    private String name;
//    private String address;
//    private String telephone;
//    private Double sum;
//    private Integer caseId;
    private List<Donor> donors;
    private List<Case> cases;

    public List<Donor> getDonors() {
        return donors;
    }

    public List<Case> getCases() {
        return cases;
    }

    public NewDonationResponse(List<Donor> donors, List<Case> cases) {
        this.donors=donors;
        this.cases=cases;
//        this.donorId = donorId;
//        this.name = name;
//        this.address = address;
//        this.telephone = telephone;
//        this.sum = sum;
//        this.caseId = caseId;
    }

//    public Integer getDonorId() {
//        return donorId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public String getTelephone() {
//        return telephone;
//    }
//
//    public Double getSum() {
//        return sum;
//    }
//
//    public Integer getCaseId() {
//        return caseId;
//    }
}

