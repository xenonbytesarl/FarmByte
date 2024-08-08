package cm.xenonbyte.farmbyte.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.uom.JpaUomRepository;
import cm.xenonbyte.farmbyte.adapter.data.access.jpa.uom.PostgreSqlUomRepository;
import cm.xenonbyte.farmbyte.adapter.data.access.jpa.uom.UomMapper;
import cm.xenonbyte.farmbyte.repository.UomRepositoryTest;
import cm.xenonbyte.farmbyte.uom.entity.Uom;
import cm.xenonbyte.farmbyte.uom.vo.Name;
import cm.xenonbyte.farmbyte.uom.vo.Ratio;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 07/08/2024
 */
@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PostgreSqlUomRepositoryIT extends UomRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine")
            .withDatabaseName("catalogdb")
            .withUsername("catalog")
            .withPassword("PSofy1ULf0M69J7nV5SNTJmCNflrkxKb");

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
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

    @Autowired
    private JpaUomRepository jpaUomRepository;

    @Autowired
    private UomMapper uomMapper;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {

        uomCategoryId = UomCategoryId.of(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
        uomType =  UomType.REFERENCE;
        name = Name.of("Unite");

        super.uomRepository = new PostgreSqlUomRepository(jpaUomRepository, uomMapper);

        //We save some uom in storage for some test case
        Uom uom1 = createSomeUom(
                name,
                uomCategoryId,
                uomType,
                null);
        Uom uom2 = createSomeUom(Name.of("Carton de 5"),
                uomCategoryId,
                uomType,
                Ratio.of(5.0));
        //uomRepository.save(uom1);
        //uomRepository.save(uom2);
    }


}
