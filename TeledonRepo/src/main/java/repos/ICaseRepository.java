package repos;

import entities.Case;

public interface ICaseRepository {
    Case findOne(Integer id);
    Iterable<Case> findAll();
    void update(Double sum, Integer id);
}
