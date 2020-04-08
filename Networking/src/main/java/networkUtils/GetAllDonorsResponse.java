package networkUtils;

import entities.Donor;

import java.util.List;

public class GetAllDonorsResponse implements Response {
    private List<Donor> donors;

    public GetAllDonorsResponse(List<Donor> donors) {
        this.donors = donors;
    }

    public List<Donor> getDonors() {
        return donors;
    }
}
