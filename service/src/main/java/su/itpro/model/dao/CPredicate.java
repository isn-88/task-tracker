package su.itpro.model.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
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

  private final CriteriaBuilder criteriaBuilder;

  public static CPredicate builder(CriteriaBuilder criteriaBuilder) {
    return new CPredicate(criteriaBuilder);
  }

  public <T> CPredicate add(Collection<T> collection,
                            Function<Collection<T>, Predicate> function) {
    if (collection != null && !collection.isEmpty()) {
      predicates.add(function.apply(collection));
    }
    return this;
  }

  public Predicate buildAnd() {
    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
  }

  public Predicate buildOr() {
    return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
  }

}
