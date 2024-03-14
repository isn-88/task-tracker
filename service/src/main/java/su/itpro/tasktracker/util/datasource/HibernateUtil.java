package su.itpro.tasktracker.util.datasource;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Category;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;

@UtilityClass
public class HibernateUtil {

  public SessionFactory buildSessionFactory() {
    Configuration configuration = buildConfiguration();
    return configuration.buildSessionFactory();
  }

  public Configuration buildConfiguration() {
    return new Configuration()
        .addAnnotatedClass(Account.class)
        .addAnnotatedClass(Category.class)
        .addAnnotatedClass(Group.class)
        .addAnnotatedClass(Profile.class)
        .addAnnotatedClass(Project.class)
        .addAnnotatedClass(Task.class);
  }

}
