package su.itpro.tasktracker.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import su.itpro.tasktracker.model.enums.Gender;

@Getter
@Setter
@EqualsAndHashCode(exclude = "account")
@ToString(exclude = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile implements BaseEntity<Long> {

  @Id
  @Column(name = "account_id")
  private Long id;

  @OneToOne
  @PrimaryKeyJoinColumn
  private Account account;

  @Column(nullable = false)
  private String lastname;

  @Column(nullable = false)
  private String firstname;

  private String surname;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  public void setAccount(Account account) {
    account.setProfile(this);
    this.account = account;
    this.id = account.getId();
  }

}
