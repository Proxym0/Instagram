package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    void save(T value);

    void remove(long id);

    Optional<T> findById(long id);

    List<T> findAll();


}
