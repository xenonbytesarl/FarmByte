package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
public final class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:16.3-alpine";
    private static final String DATABASE_NAME = "test_db";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    public static PostgresContainer container = new PostgresContainer()
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    public PostgresContainer() {
        super(IMAGE_VERSION);
    }
}
