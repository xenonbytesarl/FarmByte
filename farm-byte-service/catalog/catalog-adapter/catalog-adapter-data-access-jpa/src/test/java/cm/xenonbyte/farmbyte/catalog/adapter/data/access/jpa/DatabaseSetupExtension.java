package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
public final class DatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        PostgresContainer.container.start();
        updateDataSourceProps(PostgresContainer.container);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        PostgresContainer.container.stop();
    }

    private void updateDataSourceProps(PostgresContainer container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }


}
