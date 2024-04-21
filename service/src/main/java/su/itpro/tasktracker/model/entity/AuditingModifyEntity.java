package su.itpro.tasktracker.model.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
public abstract class AuditingModifyEntity<T extends Serializable>
    extends AuditingCreateEntity<T> {

  @LastModifiedDate
  private Instant modifiedAt;

  @LastModifiedBy
  private String modifiedBy;
}
