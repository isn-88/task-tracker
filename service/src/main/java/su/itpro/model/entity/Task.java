package su.itpro.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import su.itpro.model.enums.Priority;
import su.itpro.model.enums.Status;
import su.itpro.model.enums.Type;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long parentId;
  private UUID projectId;
  @Enumerated(value = EnumType.STRING)
  private Type type;
  private String title;
  @Enumerated(value = EnumType.STRING)
  private Status status;
  @Enumerated(value = EnumType.STRING)
  private Priority priority;
  private UUID assigned;
  private String category;
  private LocalDate createDate;
  private LocalDate endDate;
  private Short progress;
  private String description;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return Objects.equals(id, task.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
