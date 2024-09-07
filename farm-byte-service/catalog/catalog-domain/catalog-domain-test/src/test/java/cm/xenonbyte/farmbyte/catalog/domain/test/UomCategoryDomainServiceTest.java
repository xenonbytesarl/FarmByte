package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomCategoryInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
final class UomCategoryDomainServiceTest {

    private UomCategoryService uomCategoryService;
    private UomCategory parentUomCategory;
    UomCategoryRepository uomCategoryRepository;

    @BeforeEach
    void setUp() {
        uomCategoryRepository = new UomCategoryInMemoryRepositoryAdapter();
        uomCategoryService = new UomCategoryDomainService(uomCategoryRepository);
    }

    @Nested
    class CreateUomCategoryDomainTest {

        @BeforeEach
        void setUp() {
            parentUomCategory = UomCategory.of(new Name(Text.of("Temps")));
            parentUomCategory.initiate();
            uomCategoryRepository.save(parentUomCategory);
        }

        @Test
        void should_create_uom_category_as_root_category() {
            //Given
            Name uomCategoryName = Name.of(Text.of("Unite"));
            UomCategory uomCategory = UomCategory.of(uomCategoryName);

            //Act
            UomCategory createUomCategory = uomCategoryService.createUomCategory(uomCategory);

            //Then
            assertThat(createUomCategory).isNotNull();
            assertThat(createUomCategory.getName()).isEqualTo(uomCategoryName);
            assertThat(createUomCategory.getActive().getValue()).isTrue();
            assertThat(createUomCategory.getId().getValue())
                    .isInstanceOf(UUID.class)
                    .isNotNull();
        }

        @Test
        void should_create_child_uom_category_when_child_exist() {
            //Given
            Name uomCategoryName = Name.of(Text.of("Unite"));
            UomCategory uomCategory = UomCategory.of(uomCategoryName, parentUomCategory.getId());

            //Act
            UomCategory childUomCategory = uomCategoryService.createUomCategory(uomCategory);

            //Then
            assertThat(childUomCategory).isNotNull();
            assertThat(childUomCategory.getName()).isEqualTo(uomCategoryName);
            assertThat(childUomCategory.getActive().getValue()).isTrue();
            assertThat(childUomCategory.getId().getValue())
                    .isInstanceOf(UUID.class)
                    .isNotNull();
            assertThat(childUomCategory.getParentUomCategoryId()).isEqualTo(parentUomCategory.getId());
        }

        @Test
        void should_throw_exception_when_uom_category_name_exists() {
            //Given
            Name uomCategoryName = Name.of(Text.of("Temps"));
            UomCategory uomCategory = UomCategory.of(uomCategoryName);

            //Act + Then
            assertThatThrownBy(() -> uomCategoryService.createUomCategory(uomCategory))
                    .isInstanceOf(UomCategoryNameConflictException.class)
                    .hasMessage(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION);
        }

        @Test
        void should_throw_exception_when_create_child_category_with_non_existing_parent() {
            //Given
            Name uomCategoryName = Name.of(Text.of("Unite"));
            UomCategory uomCategory = UomCategory.of(uomCategoryName, new UomCategoryId(UUID.randomUUID()));

            assertThatThrownBy(() -> uomCategoryService.createUomCategory(uomCategory))
                    .isInstanceOf(UomParentCategoryNotFoundException.class)
                    .hasMessage(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindUomCategoryByIdDomainTest {

        UomCategoryId uomCategoryId;
        UomCategory uomCategory;


        @BeforeEach
        void setUp() {
            uomCategoryId = new UomCategoryId(UUID.fromString("0191a324-e2fd-7d93-876e-ac18a3fd69ce"));

            uomCategory = UomCategory.builder()
                    .id(uomCategoryId)
                    .name(Name.of(Text.of("Temps")))
                    .build();

            uomCategoryRepository.save(uomCategory);
        }

        @Test
        void should_success_when_find_uom_by_category_with_existing_id_should_success() {
            //Act
            UomCategory result = uomCategoryService.findUomCategoryById(uomCategoryId);
            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(uomCategory);
        }

        @Test
        void should_fail_when_find_uom_category_with_non_existing_uom_category_id() {
            //Given + Act + Then
            assertThatThrownBy(() -> uomCategoryService.findUomCategoryById(new UomCategoryId(UUID.randomUUID())))
                    .isInstanceOf(UomCategoryNotFoundException.class)
                    .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindUomCategoriesDomainTest {

        @BeforeEach
        void setUp() {
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-c68e-7801-8ee1-b4440de65267")))
                    .name(Name.of(Text.of("Temps")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-5241-706b-9aa3-4a596d3c68d8")))
                    .name(Name.of(Text.of("Distance")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-70c9-76f8-8d47-a9cadaa64db4")))
                    .name(Name.of(Text.of("Unite")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-89da-73f6-bebc-b6852270e376")))
                    .name(Name.of(Text.of("Volume")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-a17c-7a3c-bf52-9f1ccf029208")))
                    .name(Name.of(Text.of("Poids")))
                    .build());
        }

        static Stream<Arguments> findUomCategoriesMethodSource() {
            return Stream.of(
                    Arguments.of(2, 2, "name", Direction.ASC, 3, 5l, 2,1),
                    Arguments.of(2, 2, "name", Direction.DSC, 3, 5l, 2,1)
            );
        }

        @ParameterizedTest
        @MethodSource("findUomCategoriesMethodSource")
        void should_success_when_find_all_uom_categories(
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            //Then
            PageInfo<UomCategory> result = uomCategoryService.findUomCategories(page, size, sortAttribute, direction);

            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getContent().size()).isEqualTo(contentSize);
        }

    }

    @Nested
    class FindUomCategoryByKeyword {

        @BeforeEach
        void setUp() {
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-c68e-7801-8ee1-b4440de65267")))
                    .name(Name.of(Text.of("Temps")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-5241-706b-9aa3-4a596d3c68d8")))
                    .name(Name.of(Text.of("Distance")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-70c9-76f8-8d47-a9cadaa64db4")))
                    .name(Name.of(Text.of("Unite")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-89da-73f6-bebc-b6852270e376")))
                    .name(Name.of(Text.of("Volume")))
                    .build());
            uomCategoryRepository.save(UomCategory.builder()
                    .id(new UomCategoryId(UUID.fromString("0191a3b7-a17c-7a3c-bf52-9f1ccf029208")))
                    .name(Name.of(Text.of("Poids")))
                    .build());
        }

        static Stream<Arguments> findUomCategoryByKeywordMethodSource() {
            return Stream.of(
                    Arguments.of("n",0, 2, "name", Direction.ASC, 1, 2l, 2,2),
                    Arguments.of("e",1, 2, "name", Direction.ASC, 2, 4l, 2,2),
                    Arguments.of("w",0, 0, "name", Direction.ASC, 0, 0l, 0,0)

            );
        }

        @ParameterizedTest
        @MethodSource("findUomCategoryByKeywordMethodSource")
        void should_success_when_find_uom_category_with_keyword(
                String keyword,
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            PageInfo<UomCategory> result = uomCategoryService.searchUomCategories(page, size, sortAttribute, direction, Keyword.of(Text.of(keyword)));
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getContent()).hasSize(contentSize);
        }
    }

    @Nested
    class UpdateUomCategoryDomainTest {

        UomCategoryId uomCategoryId;
        UomCategory uomCategory;

        @BeforeEach
        void setUp() {
            uomCategoryId = new UomCategoryId(UUID.fromString("0191c90f-3b30-7528-80dc-40ee9d10cc00"));
            uomCategory = uomCategoryRepository.save(
                    UomCategory.builder()
                            .id(uomCategoryId)
                            .name(Name.of(Text.of("Temps")))
                            .active(Active.with(true))
                            .build()
            );
        }

        @Test
        void should_success_when_update_uom_category() {
            //Given
            UomCategory uomCategoryToUpdate = UomCategory.builder()
                    .id(uomCategoryId)
                    .name(Name.of(Text.of("Temps")))
                    .active(Active.with(true))
                    .build();

            //Act
            UomCategory result = uomCategoryService.updateUomCategory(uomCategoryId, uomCategoryToUpdate);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(uomCategoryToUpdate);
        }

        @Test
        void should_fail_when_we_update_uom_category_with_duplicate_name() {
            //Given
            UomCategory uomCategoryToUpdate = UomCategory.builder()
                    .id(new UomCategoryId(UUID.randomUUID()))
                    .name(Name.of(Text.of("Temps")))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> uomCategoryService.updateUomCategory(uomCategoryId, uomCategoryToUpdate))
                    .isInstanceOf(UomCategoryNameConflictException.class)
                    .hasMessage(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION);
        }

        @Test
        void should_fail_when_update_uom_category_with_non_existing_uom_category_id() {
            //Given
            UomCategoryId unExistingUomCategoryId = new UomCategoryId(UUID.randomUUID());

            UomCategory uomCategoryToUpdate = UomCategory.builder()
                    .id(unExistingUomCategoryId)
                    .name(Name.of(Text.of("Temps")))
                    .active(Active.with(true))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> uomCategoryService.updateUomCategory(unExistingUomCategoryId, uomCategoryToUpdate))
                    .isInstanceOf(UomCategoryNotFoundException.class)
                    .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_fail_when_update_uom_category_with_non_existing_parent_uom_category() {
            //Given
            UomCategoryId unExistingParentUomCategoryId = new UomCategoryId(UUID.randomUUID());

            UomCategory uomCategoryToUpdate = UomCategory.builder()
                    .id(uomCategoryId)
                    .name(Name.of(Text.of("Temps")))
                    .parentUomCategoryId(unExistingParentUomCategoryId)
                    .active(Active.with(true))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> uomCategoryService.updateUomCategory(uomCategoryId, uomCategoryToUpdate))
                    .isInstanceOf(UomParentCategoryNotFoundException.class)
                    .hasMessage(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION);
        }
    }


}
