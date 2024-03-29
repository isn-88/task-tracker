package su.itpro.tasktracker.model.dao;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("AbbreviationAsWordInName")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

  private final List<Predicate> predicates = new ArrayList<>();

  public static QPredicate builder() {
    return new QPredicate();
  }

  public <T> QPredicate add(T object, Function<T, Predicate> function) {
    if (object != null) {
      predicates.add(function.apply(object));
    }
    return this;
  }

  public <T> QPredicate add(Collection<T> collection,
                            Function<Collection<T>, Predicate> function) {
    if (collection != null && !collection.isEmpty()) {
      predicates.add(function.apply(collection));
    }
    return this;
  }

  public Predicate buildAnd() {
    return ExpressionUtils.allOf(predicates);
  }

  public Predicate buildOr() {
    return ExpressionUtils.anyOf(predicates);
  }
}
