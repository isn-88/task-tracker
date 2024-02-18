package su.itpro;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Category;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Person;
import su.itpro.model.entity.Project;
import su.itpro.model.entity.Task;
import su.itpro.model.enums.Gender;

public class HibernateRunner {

  public static void main(String[] args) {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .build();

    try (SessionFactory sessionFactory = new MetadataSources(registry)
        .addAnnotatedClass(Account.class)
        .addAnnotatedClass(Category.class)
        .addAnnotatedClass(Group.class)
        .addAnnotatedClass(Person.class)
        .addAnnotatedClass(Project.class)
        .addAnnotatedClass(Task.class)
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
