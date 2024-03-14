package su.itpro.tasktracker.model.dao;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@SuppressWarnings("AbbreviationAsWordInName")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CPredicate {

  private final List<Predicate> predicates = new ArrayList<>();

  public static CPredicate builder() {
    return new CPredicate();
  }

  public <T> CPredicate add(T object, Function<T, Predicate> function) {
    if (object != null) {
      predicates.add(function.apply(object));
    }
    return this;
  }

  public <T> CPredicate add(Collection<T> collection,
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
