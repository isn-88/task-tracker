package su.itpro.util.datasource;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Category;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Profile;
import su.itpro.model.entity.Project;
import su.itpro.model.entity.Task;

@UtilityClass
public class HibernateUtil {

  public SessionFactory getSessionFactory() {
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
