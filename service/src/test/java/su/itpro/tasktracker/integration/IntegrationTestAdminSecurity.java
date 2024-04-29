package su.itpro.tasktracker.integration;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "admin", password = "{noop}password", authorities = {"ADMIN"})
public abstract class IntegrationTestAdminSecurity extends IntegrationTestBase {

}
