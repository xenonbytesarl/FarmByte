package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.JpaRepositoryAdapterTest;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
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
 * @since 16/08/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ProductCategoryJpaRepository.class, ProductCategoryJpaMapper.class})
public class ProductCategoryJpaRepositoryAdapterIT extends ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryJpaRepository productCategoryJpaRepository;

    @Autowired
    private ProductCategoryJpaMapper productCategoryJpaMapper;

    @BeforeEach
    void setUp() {

        productCategoryRepository = new ProductCategoryJpaRepositoryAdapter(productCategoryJpaRepository, productCategoryJpaMapper);

        name = Name.of(Text.of("Raw Of Material"));

        parentProductCategoryId = new ProductCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));

        oldProductCategory = ProductCategory.builder()
                .id(new ProductCategoryId(UUID.fromString("0191e077-b2a1-795e-8584-40e26a5fa850")))
                .name(Name.of(Text.of("Fertilizer")))
                .active(Active.with(true))
                .build();

    }
}
