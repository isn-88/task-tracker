package su.itpro.tasktracker.config;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static org.springframework.security.config.Customizer.withDefaults;

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
            .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
            .requestMatchers(
                "/", "/login", "/logout", "/registration", "/error",
                "/css/**", "/js/**", "/webfonts/**", "/image/**", "/favicon.ico"
            ).permitAll()
            .requestMatchers("/admin/**").hasAuthority("ADMIN")
            .anyRequest().authenticated())
        .httpBasic(withDefaults())
        .formLogin((login) -> login
            .loginPage("/login")
            .defaultSuccessUrl("/my")
            .failureUrl("/login?error=true"))
        .logout((logout) -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
