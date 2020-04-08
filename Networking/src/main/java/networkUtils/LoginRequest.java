package networkUtils;

import entities.Volunteer;

public class LoginRequest implements Request {
    private Volunteer volunteer;

    public LoginRequest(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }
}
