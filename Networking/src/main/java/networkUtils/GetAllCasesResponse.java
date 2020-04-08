package networkUtils;

import entities.Case;

import java.util.List;

public class GetAllCasesResponse implements Response {
    private List<Case> cases;

    public GetAllCasesResponse(List<Case> cases) {
        this.cases = cases;
    }

    public List<Case> getCases() {
        return cases;
    }
}
