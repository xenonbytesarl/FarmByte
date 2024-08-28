package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomCategoryInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomReferenceConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Ratio;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
final class UomDomainServiceTest {


    private UomService uomDomainService;
    private UomCategoryId uomCategoryId;

    @BeforeEach
    void setUp() {
        UomRepository uomRepository = new UomInMemoryRepositoryAdapter();
        UomCategoryRepository uomCategoryRepository = new UomCategoryInMemoryRepositoryAdapter();
        uomDomainService = new UomDomainService(uomRepository, uomCategoryRepository);

        uomCategoryId = new UomCategoryId(UUID.randomUUID());
        uomCategoryRepository.save(UomCategory.builder()
                .id(uomCategoryId)
                .name(Name.of(Text.of("Root")))
                .build());

    }

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.of(Text.of("Unit")),
                    UomType.REFERENCE,
                    null
            ),
            Arguments.of(
                    Name.of(Text.of("Carton de 10")),
                    UomType.GREATER,
                    Ratio.of(2.0)
            ),
            Arguments.of(
                    Name.of(Text.of("Centimetre")),
                    UomType.LOWER,
                    Ratio.of(0.1)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodSourceArgs")
    void should_create_uom_when_uom_type_is_reference(
            Name name,
            UomType uomType,
            Ratio ratio
    ) {
        //Given
        Uom uom = Uom.from(
                name,
                uomCategoryId,
                uomType,
                ratio
        );

        //Act
        Uom createdUom = uomDomainService.createUom(uom);

        //Then
        assertThat(createdUom).isNotNull();
        assertThat(createdUom.getId())
                .isNotNull()
                .satisfies(result -> assertThat(result.getValue()).isInstanceOf(UUID.class));
        assertThat(createdUom.getUomCategoryId())
                .isNotNull()
                .isEqualTo(uomCategoryId);
        assertThat(createdUom.getUomType()).isEqualTo(uomType);
        assertThat(createdUom.getActive()).isEqualTo(Active.with(true));
        assertThat(createdUom.getRatio()).isEqualTo(ratio == null ? Ratio.of( Ratio.REFERENCE) : ratio);
        assertThat(createdUom.getName()).isEqualTo(name);
    }

    static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.of(Text.of("Unit")),
                    UomType.GREATER,
                    null,
                    IllegalArgumentException.class,
                    "Uom.1"
            ),
            Arguments.of(
                    Name.of(Text.of("Carton de 10")),
                    UomType.GREATER,
                    Ratio.of(0.8),
                    IllegalArgumentException.class,
                    "Uom.2"
            ),
            Arguments.of(
                    Name.of(Text.of("Centimetre")),
                    UomType.LOWER,
                    Ratio.of(2.0),
                    IllegalArgumentException.class,
                    "Uom.3"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomThrowExceptionMethodSourceArgs")
    void should_throw_exception_when_create_uom_with_uom_type_and_ratio_are_not_compatible(
            Name name,
            UomType uomType,
            Ratio ratio,
            Class< ? extends RuntimeException> exceptionClass,
            String exceptionMessage
    ) {
        //Given
        Uom uom = Uom.from(
                name,
                uomCategoryId,
                uomType,
                ratio
        );

        //Act + Then
        assertThatThrownBy(() -> uomDomainService.createUom(uom))
                .isInstanceOf(exceptionClass)
                .hasMessage(exceptionMessage);

    }


    @Test
    void should_throw_exception_when_create_two_uom_with_uom_type_as_reference_for_the_same_category() {
        //Given
        Uom firstRefereceUom = Uom.from(
                Name.of(Text.of("Unite")),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of(Text.of("Piece")),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        //When + Then
        assertThatThrownBy(() -> uomDomainService.createUom(secondRefereceUom))
                .isInstanceOf(UomReferenceConflictException.class)
                .hasMessage(UOM_REFERENCE_CONFLICT_CATEGORY);
    }

    @Test
    void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() {
        //Given
        Uom firstRefereceUom = Uom.from(
                Name.of(Text.of("Carton de 10")),
                uomCategoryId,
                UomType.GREATER,
                Ratio.of(2.0));

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of(Text.of("Carton de 50")),
                uomCategoryId,
                UomType.GREATER,
                Ratio.of(50.0));
        //Act
        Uom result = uomDomainService.createUom(secondRefereceUom);

        assertThat(result).isNotNull();
        //Then
    }

    @Test
    void should_throw_exception_when_create_two_uom_with_same_name() {
        //Given
        Uom firstRefereceUom = Uom.from(
                Name.of(Text.of("Unite")),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of(Text.of("Unite")),
                uomCategoryId,
                UomType.GREATER,
                Ratio.of(1.5));

        //When + Then
        assertThatThrownBy(() -> uomDomainService.createUom(secondRefereceUom))
                .isInstanceOf(UomNameConflictException.class)
                .hasMessage(UOM_NAME_CONFLICT_EXCEPTION);
    }

    @Test
    void should_throw_error_when_uom_category_not_exist() {
        //Given
        UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
        Uom uom = Uom.from(
                Name.of(Text.of("Unite")),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        //Act + Then
        assertThatThrownBy(() -> uomDomainService.createUom(uom))
                .isInstanceOf(UomCategoryNotFoundException.class)
                .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);
    }
}
