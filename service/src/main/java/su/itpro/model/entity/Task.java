package su.itpro.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;
import su.itpro.model.enums.Priority;
import su.itpro.model.enums.Status;
import su.itpro.model.enums.Type;

@Getter
@Setter
@ToString(of = "title")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Task parent;

  @OneToOne
  private Project project;

  @Enumerated(value = EnumType.STRING)
  private Type type;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Priority priority;

  @OneToOne
  private Account assigned;

  @OneToOne
  private Category category;

  @CreationTimestamp
  @Column(name = "create_at", nullable = false)
  private Instant createAt;

  @Column(name = "close_at")
  private Instant closeAt;

  @Builder.Default
  @Column(nullable = false)
  private Short progress = 0;

  private String description;


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
    Task task = (Task) o;
    return getId() != null && Objects.equals(getId(), task.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
