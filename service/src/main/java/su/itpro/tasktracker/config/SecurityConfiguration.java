package su.itpro.tasktracker.config;

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
            .requestMatchers("/", "/login", "/logout", "/registration", "/error", "/api/**",
                             "/css/**", "/js/**", "/webfonts/**", "/image/**", "/favicon.ico")
            .permitAll()
            .anyRequest().authenticated())
        .formLogin((login) -> login
            .loginPage("/login")
            .defaultSuccessUrl("/my/page")
            .failureUrl("/login?error=true"))
        .logout((logout) -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login"))
        .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
