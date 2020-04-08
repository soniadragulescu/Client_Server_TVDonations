package networkUtils;

import entities.Donation;

public class NewDonationRequest implements Request {
    private Integer donorId;
    private String name;
    private String address;
    private String telephone;
    private Double sum;
    private Integer caseId;

    public NewDonationRequest(Integer donorId, String name, String address, String telephone, Double sum, Integer caseId) {
        this.donorId = donorId;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.sum = sum;
        this.caseId = caseId;
    }

    public Integer getDonorId() {
        return donorId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public Double getSum() {
        return sum;
    }

    public Integer getCaseId() {
        return caseId;
    }
}
