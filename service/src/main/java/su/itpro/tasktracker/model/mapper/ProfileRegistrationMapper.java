package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.model.entity.Profile;

@Component
public class ProfileRegistrationMapper implements Mapper<RegisterDto, Profile> {

  @Override
  public Profile map(RegisterDto registerDto) {
    return Profile.builder()
        .lastname(registerDto.getLastname())
        .firstname(registerDto.getFirstname())
        .build();
  }

}
