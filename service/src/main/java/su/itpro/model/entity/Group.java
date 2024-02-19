package su.itpro.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString(exclude = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "groups")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @Builder.Default
  @OneToMany(mappedBy = "group")
  private List<Account> accounts = new ArrayList<>();


  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
                               ? ((HibernateProxy) o).getHibernateLazyInitializer()
                                   .getPersistentClass()
                               : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
                                  ? ((HibernateProxy) this).getHibernateLazyInitializer()
                                      .getPersistentClass()
                                  : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Group group = (Group) o;
    return getId() != null && Objects.equals(getId(), group.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
