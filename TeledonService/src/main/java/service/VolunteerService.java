package service;

import entities.Volunteer;
import repos.IVolunteerRepository;
import repos.TeledonException;

public class VolunteerService {
    private IVolunteerRepository volunteerRepository;

    public VolunteerService(IVolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Volunteer loginVolunteer(String username, String password) throws TeledonException {
        return this.volunteerRepository.findOne(username,password);
    }
}
