package entities;

import javafx.util.Pair;

public class Donation extends Entity<Pair<Integer,Integer>> {
    private Integer donorId;
    private Double sum;
    private Integer caseId;

    public Integer getDonorId() {
        return donorId;
    }

    public void setDonorId(Integer donorId) {
        this.donorId = donorId;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Donation(Integer donorId, Double sum, Integer caseId) {
        this.setId(new Pair(donorId,caseId));
        this.donorId = donorId;
        this.sum = sum;
        this.caseId = caseId;
    }
}
