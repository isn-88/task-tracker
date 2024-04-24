package su.itpro.tasktracker.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import su.itpro.tasktracker.model.enums.Role;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"profile", "groupAccounts"})
@ToString(exclude = {"profile", "groupAccounts"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account implements BaseEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Column(nullable = false)
  @Builder.Default
  private Boolean enabled = Boolean.TRUE;

  @Builder.Default
  @OneToMany(mappedBy = "account")
  private List<GroupAccount> groupAccounts = new ArrayList<>();

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
  private Profile profile;

}
