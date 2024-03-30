package su.itpro.tasktracker.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category implements BaseEntity<Short> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Short id;

  @Column(nullable = false, unique = true)
  private String name;

}
