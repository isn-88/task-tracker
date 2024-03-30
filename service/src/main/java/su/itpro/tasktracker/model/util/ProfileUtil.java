package su.itpro.tasktracker.model.util;

import lombok.experimental.UtilityClass;
import su.itpro.tasktracker.model.entity.Profile;

@UtilityClass
public class ProfileUtil {

  private static final String SHORT_NAME_FORMAT = "%s %s.";
  private static final String FULL_NAME_FORMAT = "%s %s.%s.";

  public String getUserFullName(Profile profile) {
    if (profile == null) {
      return "";
    }
    String firstname = getShortName(profile.getFirstname());
    String surname = getShortName(profile.getSurname());
    return surname.isBlank()
           ? SHORT_NAME_FORMAT.formatted(profile.getLastname(), firstname)
           : FULL_NAME_FORMAT.formatted(profile.getLastname(), firstname, surname);
  }

  private String getShortName(String name) {
    if (name == null || name.isBlank()) {
      return "";
    }
    return name.substring(0, 1).toUpperCase();
  }

}
