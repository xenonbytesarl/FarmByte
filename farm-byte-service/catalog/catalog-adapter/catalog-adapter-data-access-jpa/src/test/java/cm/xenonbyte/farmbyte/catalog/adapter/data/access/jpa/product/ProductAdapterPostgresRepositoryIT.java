package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.config.BaseEntityJpaConfig;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.mapper.ReferenceMapper;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@Import({BaseEntityJpaConfig.class, ReferenceMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa")
@ComponentScan(basePackages = {"cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa"})
@ContextConfiguration(classes = {ProductJpaRepository.class, ProductJpaMapper.class})
@EnableJpaRepositories(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa")
class ProductAdapterPostgresRepositoryIT extends ProductRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
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
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductJpaMapper productJpaMapper;

    @BeforeEach
    void setUp() {
        super.productRepository = new ProductAdapterPostgresRepository(productJpaRepository, productJpaMapper);
        super.categoryId = new ProductCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
        super.image = Image.with(Text.of("product.png"));
        super.name = Name.of(Text.of("Product.2"));
    }
}
