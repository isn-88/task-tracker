package su.itpro.tasktracker.model.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
public abstract class AuditingCreateEntity<T extends Serializable>
    extends AuditingCreatedAtEntity<T> implements BaseEntity<T> {

  @CreatedBy
  private String createdBy;

}
