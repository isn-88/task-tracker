package su.itpro;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import su.itpro.model.entity.Person;
import su.itpro.model.enums.Gender;

public class Main {

  public static void main(String[] args) {

    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();

    try (SessionFactory sessionFactory = new MetadataSources(registry)
        .addAnnotatedClass(Person.class)
        .buildMetadata()
        .buildSessionFactory()) {

      sessionFactory.inTransaction(session -> session.persist(
          new Person(null, "Иванов", "Иван", "Иванович", Gender.MALE)
      ));
    } catch (Exception ex) {
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }
}
