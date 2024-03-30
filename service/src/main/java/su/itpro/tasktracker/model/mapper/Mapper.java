package su.itpro.tasktracker.model.mapper;

public interface Mapper<F, T> {

  T map(F object);

  default T map(F fromObject, T toObject) {
    return toObject;
  }
}
