package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.AdapterPostgresRepositoryConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(AdapterPostgresRepositoryConfigTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ProductJpaRepository.class, ProductJpaMapper.class})
class ProductAdapterPostgresRepositoryIT extends ProductRepositoryTest {

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
