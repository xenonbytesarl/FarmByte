package cm.xenonbyte.farmbyte.inventory.domain.core;

import cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
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
            InventoryEmplacement result = inventoryEmplacementService.findById(inventoryEmplacementId);
            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(inventoryEmplacement);
        }

        @Test
        void should_fail_when_find_inventory_emplacement_with_non_existing_id() {
            //Given + Act + Then
            assertThatThrownBy(() -> inventoryEmplacementService.findById(new InventoryEmplacementId(UUID.randomUUID())))
                    .isInstanceOf(InventoryEmplacementNotFoundException.class)
                    .hasMessage(INVENTORY_EMPLACEMENT_ID_NOT_FOUND);
        }
    }

}
