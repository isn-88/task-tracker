package su.itpro.tasktracker.repository;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import su.itpro.tasktracker.model.entity.BaseEntity;

@RequiredArgsConstructor
public abstract class BaseRepository<K extends Serializable, E extends BaseEntity<K>>
    implements CrudRepository<K, E> {

  protected final EntityManager entityManager;

  private final Class<E> clazz;

  @Override
  public E save(E entity) {
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<E> findById(K id) {
    return Optional.ofNullable(entityManager.find(clazz, id));
  }

  @Override
  public void update(E entity) {
    entityManager.merge(entity);
    entityManager.flush();
  }

  @Override
  public void delete(E entity) {
    entityManager.remove(entity);
    entityManager.flush();
  }
}
