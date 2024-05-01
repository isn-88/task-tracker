package su.itpro.tasktracker.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Builder
@Entity
@Table(name = "project_groups")
public class ProjectGroup extends AuditingCreateEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Project project;

  @ManyToOne
  private Group group;


  public void setProject(Project project) {
    this.project = project;
    this.project.getProjectGroup().add(this);
  }

  public void setGroup(Group group) {
    this.group = group;
    this.group.getProjectGroup().add(this);
  }
}
