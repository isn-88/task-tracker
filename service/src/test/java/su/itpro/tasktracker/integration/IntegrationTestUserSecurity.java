package su.itpro.tasktracker.integration;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "user", password = "{noop}password", authorities = {"USER"})
public abstract class IntegrationTestUserSecurity extends IntegrationTestBase {

}
