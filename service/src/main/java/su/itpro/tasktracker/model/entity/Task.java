package su.itpro.tasktracker.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"parent", "project", "assigned", "category"})
@ToString(of = "title")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task implements BaseEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Task parent;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Project project;

  @Enumerated(value = EnumType.STRING)
  private TaskType type;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private TaskStatus status;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private TaskPriority priority;

  @ManyToOne
  @JoinColumn(name = "assigned_id")
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

}
