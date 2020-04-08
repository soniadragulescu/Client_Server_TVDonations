package repos;

import entities.Donor;

public interface IDonorRepository {
    Donor findOne(Integer id);
    Iterable<Donor> findAll();
    Iterable<Donor> findByName(String substring);
    void save(Donor entity);
}
