package networkUtils;

public class GetAllDonorsRequest implements Request {
    private String substring;
    public GetAllDonorsRequest(String substring) {
        this.substring=substring;
    }

    public String getSubstring() {
        return substring;
    }
}
