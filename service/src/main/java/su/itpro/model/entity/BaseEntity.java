package su.itpro.model.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> {

  void setId(K id);

  K getId();

}
