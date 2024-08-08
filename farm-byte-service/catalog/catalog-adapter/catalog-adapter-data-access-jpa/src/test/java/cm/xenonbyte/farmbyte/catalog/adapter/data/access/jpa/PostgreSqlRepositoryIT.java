package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomRepositoryTest;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author bamk
 * @version 1.0
 * @since 07/08/2024
 */
@ActiveProfiles("test")
@DataJpaTest
@Testcontainers
@EnableAutoConfiguration
@ComponentScan(basePackages = {"cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa"})
@EntityScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa")
public abstract class PostgreSqlRepositoryIT extends UomRepositoryTest {
    @Container
    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    protected static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.test.database.replace=none",
                    String.format("spring.datasource.url=%s", postgreSQLContainer.getJdbcUrl()),
                    String.format("spring.datasource.username=%s", postgreSQLContainer.getUsername()),
                    String.format("spring.datasource.password=%s", postgreSQLContainer.getPassword())
            );

        }
    }

    @BeforeAll
    protected static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    protected static void afterAll() {
        postgreSQLContainer.stop();
    }
}
