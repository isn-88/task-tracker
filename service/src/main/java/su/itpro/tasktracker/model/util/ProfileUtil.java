package su.itpro.tasktracker.model.util;

import lombok.experimental.UtilityClass;
import su.itpro.tasktracker.model.entity.Profile;

@UtilityClass
public class ProfileUtil {

  private static final String SHORT_INITIAL_FORMAT = "%s %s.";
  private static final String FILL_INITIAL_FORMAT = "%s %s.%s.";
  private static final String SHORT_NAME_FORMAT = "%s %s";
  private static final String FULL_NAME_FORMAT = "%s %s %s";


  public String getFullName(Profile profile) {
    if (profile == null) {
      return "";
    }
    return (profile.getSurname() == null || profile.getSurname().isBlank())
           ? SHORT_NAME_FORMAT.formatted(profile.getLastname(), profile.getFirstname())
           : FULL_NAME_FORMAT.formatted(
               profile.getLastname(), profile.getFirstname(), profile.getSurname()
           );
  }

  public String getShortName(Profile profile) {
    if (profile == null) {
      return "";
    }
    return SHORT_NAME_FORMAT.formatted(profile.getLastname(), profile.getFirstname());
  }

  public String getFullInitialName(Profile profile) {
    if (profile == null) {
      return "";
    }
    String firstnameInitial = getInitial(profile.getFirstname());
    String surnameInitial = getInitial(profile.getSurname());
    return surnameInitial.isBlank()
           ? SHORT_NAME_FORMAT.formatted(profile.getLastname(), firstnameInitial)
           : FULL_NAME_FORMAT.formatted(profile.getLastname(), firstnameInitial, surnameInitial);
  }

  public String getShortInitialName(Profile profile) {
    if (profile == null) {
      return "";
    }
    return SHORT_NAME_FORMAT.formatted(profile.getLastname(), getInitial(profile.getFirstname()));

  }

  private String getInitial(String name) {
    if (name == null || name.isBlank()) {
      return "";
    }
    return name.strip().substring(0, 1).toUpperCase();
  }

}
