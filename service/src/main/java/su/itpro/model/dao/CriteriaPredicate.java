package su.itpro.model.dao;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaPredicate {

  private final List<Predicate> predicates = new ArrayList<>();

  public static CriteriaPredicate builder() {
    return new CriteriaPredicate();
  }

  public <T> CriteriaPredicate add(T object, Function<T, Predicate> function) {
    if (object != null) {
      predicates.add(function.apply(object));
    }
    return this;
  }

  public <T> CriteriaPredicate add(Collection<T> collection,
                                   Function<Collection<T>, Predicate> function) {
    if (collection != null && !collection.isEmpty()) {
      predicates.add(function.apply(collection));
    }
    return this;
  }

  public Predicate[] build() {
    return predicates.toArray(Predicate[]::new);
  }

}
