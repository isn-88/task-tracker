package su.itpro.repository;

import java.io.Serializable;
import java.util.Optional;
import su.itpro.model.entity.BaseEntity;

public interface CrudRepository<K extends Serializable, E extends BaseEntity<K>> {

  E save(E entity);

  Optional<E> findById(K id);

  void update(E entity);

  void delete(E entity);

}
