package su.itpro.tasktracker.integration;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "user", password = "{noop}pass", authorities = {"USER"})
public abstract class IntegrationTestSecurity extends IntegrationTestBase {

}
