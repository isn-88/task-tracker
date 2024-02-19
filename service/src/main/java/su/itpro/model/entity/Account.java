package su.itpro.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import su.itpro.model.enums.Role;

@Getter
@Setter
@ToString(exclude = {"group", "profile"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @ManyToOne()
  private Group group;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
  private Profile profile;


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
    Account that = (Account) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
