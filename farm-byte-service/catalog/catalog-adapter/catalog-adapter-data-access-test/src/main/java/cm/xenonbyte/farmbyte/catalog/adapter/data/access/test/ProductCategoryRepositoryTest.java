package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public abstract class ProductCategoryRepositoryTest {

    protected ProductCategoryRepository productCategoryRepository;
    protected Name name;
    protected ProductCategoryId parentProductCategoryId;
    protected ProductCategory oldProductCategory;

    @Nested
    class CreateProductCategoryRepositoryTest {
        @Test
        protected void should_return_false_when_product_category_with_given_name_not_exist() {
            //Act
            Boolean result = productCategoryRepository.existsByName(Name.of(Text.of("Anything")));

            //Then
            assertThat(result).isFalse();

        }

        @Test
        protected void should_return_true_when_product_category_with_given_name_exist() {
            //Act
            Boolean result = productCategoryRepository.existsByName(name);

            //Then
            assertThat(result).isTrue();

        }

        @Test
        protected void should_return_false_when_given_parent_product_category_id_does_not_exist() {
            //Act
            Boolean result = productCategoryRepository.existsById(new ProductCategoryId(UUID.randomUUID()));

            //Then
            assertThat(result).isFalse();

        }

        @Test
        protected void should_return_true_when_given_parent_product_category_id__exist() {
            //Act
            Boolean result = productCategoryRepository.existsById(parentProductCategoryId);

            //Then
            assertThat(result).isTrue();

        }

        @Test
        protected void should_create_new_uom_category() {
            //Given
            Name volumeCategory = Name.of(Text.of("Raw Material"));
            ProductCategory productCategory = ProductCategory.of(volumeCategory);
            productCategory.initiate();

            //Act
            ProductCategory createdProductCategory = productCategoryRepository.save(productCategory);
            Boolean result = productCategoryRepository.existsByName(volumeCategory);

            //Then
            assertThat(createdProductCategory.getId()).isNotNull();
            assertThat(result).isTrue();

        }
    }

    @Nested
    class FindProductByIdProductCategoryRepositoryTest {
        @Test
        void should_success_when_find_product_category_by_existing_id() {
            //Given + Act
            Optional<ProductCategory> result = productCategoryRepository.findById(parentProductCategoryId);

            //Then
            assertThat(result.isPresent())
                    .isTrue();
        }

        @Test
        void should_fail_when_find_product_category_by_non_existing_id() {
            //Given
            ProductCategoryId productCategoryId = new ProductCategoryId(UUID.randomUUID());

            //Act
            Optional<ProductCategory> result = productCategoryRepository.findById(productCategoryId);

            //Then
            assertThat(result.isEmpty())
                    .isTrue();
        }
    }

    @Nested
    class FindProductCategoriesRepositoryTest {
        @Test
        void should_success_when_find_uom_categories() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<ProductCategory> result = productCategoryRepository.findAll(page, size, sortAttribute, direction);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchProductCategoryRepositoryTest {
        @Test
        void should_success_when_find_product_category_with_keyword() {
            //Given
            Integer page = 0;
            Integer size = 1;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("m"));

            //Then
            PageInfo<ProductCategory> result = productCategoryRepository.search(page, size, sortAttribute, direction, keyword);

            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

        @Test
        void should_return_empty_page_when_find_product_category_with_keyword() {
            //Given
            Integer page = 1;
            Integer size = 1;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("x"));

            //Then
            PageInfo<ProductCategory> result = productCategoryRepository.search(page, size, sortAttribute, direction, keyword);

            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getContent().size()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }

    @Nested
    class UpdateProductCategoryRepositoryTest {

        @Test
        void should_success_when_update_product_category() {
            //Given
            ProductCategory productCategoryToUpdate = ProductCategory.builder()
                    .id(oldProductCategory.getId())
                    .name(Name.of(Text.of("New Raw Material")))
                    .active(Active.with(true))
                    .build();

            //Act
            ProductCategory result = productCategoryRepository.update(oldProductCategory, productCategoryToUpdate);

            //Then
            assertThat(result).isNotNull().isEqualTo(productCategoryToUpdate);
        }

        @Test
        void should_return_true_when_find_product_category_with_existing_name() {
            //Given + Act
            Optional<ProductCategory> result = productCategoryRepository.findByName(name);

            //Then
            assertThat(result.isPresent()).isTrue();

        }

        @Test
        void should_return_false_when_find_product_category_with_non_existing_name() {
            //Given + Act
            Optional<ProductCategory> result = productCategoryRepository.findByName(Name.of(Text.of("New Raw Material")));

            //Then
            assertThat(result.isPresent()).isFalse();
        }
    }



}
