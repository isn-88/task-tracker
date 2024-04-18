package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.RegistrationDto;
import su.itpro.tasktracker.model.entity.Profile;

@Component
public class ProfileRegistrationMapper implements Mapper<RegistrationDto, Profile> {

  @Override
  public Profile map(RegistrationDto registrationDto) {
    return Profile.builder()
        .lastname(registrationDto.getLastname())
        .firstname(registrationDto.getFirstname())
        .build();
  }

}
