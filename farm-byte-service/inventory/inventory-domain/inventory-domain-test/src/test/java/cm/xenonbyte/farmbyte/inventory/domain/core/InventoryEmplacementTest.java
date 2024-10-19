package cm.xenonbyte.farmbyte.inventory.domain.core;

import cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.data.access.inmemory.InMemoryInventoryEmplacementRepository;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementDomainService;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNotFoundException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementService;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException.NOT_NULL_VALUE;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException.INVENTORY_EMPLACEMENT_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNotFoundException.INVENTORY_EMPLACEMENT_ID_NOT_FOUND;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException.INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InventoryEmplacementTest {

    private InventoryEmplacementService inventoryEmplacementService;
    private InventoryEmplacementId parentId;
    InventoryEmplacementRepository inventoryEmplacementRepository;
    Name name;

    @BeforeEach
    void setUp() {
        inventoryEmplacementRepository = new InMemoryInventoryEmplacementRepository();
        inventoryEmplacementService = new InventoryEmplacementDomainService(inventoryEmplacementRepository);
        parentId = new InventoryEmplacementId(UUID.fromString("01928612-317e-7736-8ef4-3f259caa732a"));
        name = Name.of(Text.of("RootInventoryEmplacement"));

        inventoryEmplacementRepository.save(
          InventoryEmplacement.builder()
                  .id(parentId)
                  .name(name)
                  .type(InventoryEmplacementType.INTERNAL)
                  .active(Active.with(true))
                  .build()
        );
    }

    @Nested
    class CreateInventoryEmplacementDomainTest {


        @Test
        void should_create_inventory_emplacement_without_parent() {
            //Given
            InventoryEmplacement inventoryEmplacement = new InventoryEmplacement(
                    Name.of(Text.of("InventoryEmplacementName.1")),
                    InventoryEmplacementType.INTERNAL
            );

            //Act
            InventoryEmplacement result = inventoryEmplacementService.createInventoryEmplacement(inventoryEmplacement);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo(inventoryEmplacement.getName());
            assertThat(result.getType()).isEqualTo(inventoryEmplacement.getType());
            assertThat(result.getActive().getValue()).isTrue();
        }

        @Test
        void should_create_inventory_emplacement_with_parent() {
            //Given
            InventoryEmplacement inventoryEmplacement = InventoryEmplacement.builder()
                    .name(Name.of(Text.of("InventoryEmplacementName.2")))
                    .type(InventoryEmplacementType.INTERNAL)
                    .parentId(parentId)
                    .build();

            //Act
            InventoryEmplacement result = inventoryEmplacementService.createInventoryEmplacement(inventoryEmplacement);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo(inventoryEmplacement.getName());
            assertThat(result.getType()).isEqualTo(inventoryEmplacement.getType());
            assertThat(result.getActive().getValue()).isTrue();
            assertThat(result.getParentId()).isEqualTo(inventoryEmplacement.getParentId());
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_duplicate_name() {
            //Given
            InventoryEmplacement inventoryEmplacement = InventoryEmplacement.builder()
                    .name(name)
                    .type(InventoryEmplacementType.INTERNAL)
                    .parentId(parentId)
                    .build();

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementService.createInventoryEmplacement(inventoryEmplacement))
                    .isInstanceOf(InventoryEmplacementNameConflictException.class)
                    .hasMessage(INVENTORY_EMPLACEMENT_NAME_CONFLICT);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() {
            //Given
            InventoryEmplacement inventoryEmplacement = InventoryEmplacement.builder()
                    .name(Name.of(Text.of("InventoryEmplacementName.3")))
                    .type(InventoryEmplacementType.INTERNAL)
                    .parentId(new InventoryEmplacementId(UUID.fromString("01928641-e6ee-7ef5-a883-ccb70de6e427")))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementService.createInventoryEmplacement(inventoryEmplacement))
                    .isInstanceOf(InventoryEmplacementParentIdNotFoundException.class)
                    .hasMessage(INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND);
        }

        static Stream<Arguments> createInventoryEmplacementInvalidAttribute() {
            return Stream.of(
                    Arguments.of(null, InventoryEmplacementType.INTERNAL, InvalidFieldBadException.class, NOT_NULL_VALUE),
                    Arguments.of(Name.of(Text.of("InventoryEmplacementName.4")), null, InvalidFieldBadException.class, NOT_NULL_VALUE)

            );
        }

        @ParameterizedTest
        @MethodSource("createInventoryEmplacementInvalidAttribute")
        void should_fail_when_create_inventory_emplacement_with_a_least_of_one_invalid_attribute(
                Name name, InventoryEmplacementType type, Class<? extends RuntimeException> excepectedClass, String expectedMessage
        ) {
            //Given
            InventoryEmplacement inventoryEmplacement = InventoryEmplacement.builder()
                    .name(name)
                    .type(type)
                    .build();

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementService.createInventoryEmplacement(inventoryEmplacement))
                    .isInstanceOf(excepectedClass)
                    .hasMessage(expectedMessage);
        }
    }

    @Nested
    class FindInventoryEmplacementByIdDomainTest {
        InventoryEmplacement inventoryEmplacement;
        InventoryEmplacementId inventoryEmplacementId;

        @BeforeEach
        void setUp() {
            inventoryEmplacementId = new InventoryEmplacementId(UUID.fromString("0192a1bd-5bec-7764-b49e-cc99bebb26e7"));
            inventoryEmplacement = InventoryEmplacement.builder()
                    .id(inventoryEmplacementId)
                    .name(Name.of(Text.of("InventoryEmplacementName.1")))
                    .type(InventoryEmplacementType.INTERNAL)
                    .build();

            inventoryEmplacementRepository.save(inventoryEmplacement);
        }

        @Test
        void should_success_when_find_inventory_emplacement_with_existing_id() {
            //Given + Act
            InventoryEmplacement result = inventoryEmplacementService.findInventoryEmplacementById(inventoryEmplacementId);
            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(inventoryEmplacement);
        }

        @Test
        void should_fail_when_find_inventory_emplacement_with_non_existing_id() {
            //Given + Act + Then
            assertThatThrownBy(() -> inventoryEmplacementService.findInventoryEmplacementById(new InventoryEmplacementId(UUID.randomUUID())))
                    .isInstanceOf(InventoryEmplacementNotFoundException.class)
                    .hasMessage(INVENTORY_EMPLACEMENT_ID_NOT_FOUND);
        }
    }

    @Nested
    class FindInventoryEmplacementsDomainTest {
        @BeforeEach
        void setUp() {

            inventoryEmplacementRepository.save(
                InventoryEmplacement.builder()
                    .id(new InventoryEmplacementId(UUID.fromString("0192a1cf-1850-7a51-ad51-f76c625aeec0")))
                    .name(Name.of(Text.of("InventoryEmplacementName.1")))
                    .type(InventoryEmplacementType.INTERNAL)
                    .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1cf-fbcb-7241-af27-62d110c3cedb")))
                            .name(Name.of(Text.of("InventoryEmplacementName.2")))
                            .type(InventoryEmplacementType.INTERNAL)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-32e8-7c06-9924-36bd0e3149fd")))
                            .name(Name.of(Text.of("InventoryEmplacementName.3")))
                            .type(InventoryEmplacementType.CUSTOMER)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-a26a-78c1-8e11-e3d8578bfeaf")))
                            .name(Name.of(Text.of("InventoryEmplacementName.4")))
                            .type(InventoryEmplacementType.SUPPLIER)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-f7b1-75cd-8361-99a9e5a1c21f")))
                            .name(Name.of(Text.of("InventoryEmplacementName.5")))
                            .type(InventoryEmplacementType.TRANSIT)
                            .build()
            );
        }

        static Stream<Arguments> findInventoryEmplacementsMethodSource() {
            return Stream.of(
                    Arguments.of(2, 2, "name", Direction.ASC, 3, 6l, 2,2),
                    Arguments.of(1, 2, "name", Direction.DSC, 3, 6l, 2,2)
            );
        }

        @ParameterizedTest
        @MethodSource("findInventoryEmplacementsMethodSource")
        void should_success_when_find_all_inventory_emplacements(
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
            PageInfo<InventoryEmplacement> result = inventoryEmplacementService.findInventoryEmplacements(page, size, sortAttribute, direction);

            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getElements()).hasSize(contentSize);
        }
    }

    @Nested
    class SearchInventoryEmplacementsDomainTest {
        @BeforeEach
        void setUp() {

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1cf-1850-7a51-ad51-f76c625aeec0")))
                            .name(Name.of(Text.of("InventoryEmplacementNameInternal.1")))
                            .type(InventoryEmplacementType.INTERNAL)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1cf-fbcb-7241-af27-62d110c3cedb")))
                            .name(Name.of(Text.of("InventoryEmplacementNameInternal.2")))
                            .type(InventoryEmplacementType.INTERNAL)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-32e8-7c06-9924-36bd0e3149fd")))
                            .name(Name.of(Text.of("InventoryEmplacementNameCustomer.3")))
                            .type(InventoryEmplacementType.CUSTOMER)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-a26a-78c1-8e11-e3d8578bfeaf")))
                            .name(Name.of(Text.of("InventoryEmplacementNameSupplier.4")))
                            .type(InventoryEmplacementType.SUPPLIER)
                            .build()
            );

            inventoryEmplacementRepository.save(
                    InventoryEmplacement.builder()
                            .id(new InventoryEmplacementId(UUID.fromString("0192a1d0-f7b1-75cd-8361-99a9e5a1c21f")))
                            .name(Name.of(Text.of("InventoryEmplacementNameTransit.5")))
                            .type(InventoryEmplacementType.TRANSIT)
                            .build()
            );
        }

        static Stream<Arguments> findInventoryEmplacementsMethodSource() {
            return Stream.of(
                    Arguments.of("Internal", 0, 2, "name", Direction.ASC, 1, 2l, 2,2),
                    Arguments.of("Customer", 0, 2, "name", Direction.DSC, 1, 1l, 2,1)
            );
        }

        @ParameterizedTest
        @MethodSource("findInventoryEmplacementsMethodSource")
        void should_success_when_find_all_inventory_emplacements(
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
            //Then
            PageInfo<InventoryEmplacement> result = inventoryEmplacementService.searchInventoryEmplacements(page, size, sortAttribute, direction, Keyword.of(Text.of(keyword)));

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getElements()).hasSize(contentSize);
        }
    }

}
