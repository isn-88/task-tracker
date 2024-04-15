package su.itpro.tasktracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf((csrf) -> csrf
            .ignoringRequestMatchers("/api/**"))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/login", "/logout", "/registration").permitAll()
            .anyRequest().authenticated())
        .formLogin((login) -> login
            .permitAll()
            .loginPage("/login")
            .defaultSuccessUrl("/tasks"))
        .logout((logout) -> logout
            .permitAll()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login"));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
