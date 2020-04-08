package repos;

import entities.Volunteer;

public interface IVolunteerRepository {
    Volunteer findOne(String username, String password) throws TeledonException;
}
