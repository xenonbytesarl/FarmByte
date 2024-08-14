package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.InMemoryUomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Ratio;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
final class UomTest {


    private IUomService uomDomainService;

    @BeforeEach
    void setUp() {
        UomRepository uomRepository = new InMemoryUomRepository();
        uomDomainService = new UomService(uomRepository);
    }

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.of("Unit"),
                    new UomCategoryId(UUID.randomUUID()),
                    UomType.REFERENCE,
                    null
            ),
            Arguments.of(
                    Name.of("Carton de 10"),
                    new UomCategoryId(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.of(2.0)
            ),
            Arguments.of(
                    Name.of("Centimetre"),
                    new UomCategoryId(UUID.randomUUID()),
                    UomType.LOWER,
                    Ratio.of(0.1)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodSourceArgs")
    void should_create_uom_when_uom_type_is_reference(
            Name name,
            UomCategoryId uomCategoryId,
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
                    Name.of("Unit"),
                    new UomCategoryId(UUID.randomUUID()),
                    UomType.GREATER,
                    null,
                    IllegalArgumentException.class,
                    "Uom.1"
            ),
            Arguments.of(
                    Name.of("Carton de 10"),
                    new UomCategoryId(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.of(0.8),
                    IllegalArgumentException.class,
                    "Uom.2"
            ),
            Arguments.of(
                    Name.of("Centimetre"),
                    new UomCategoryId(UUID.randomUUID()),
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
            UomCategoryId uomCategoryId,
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
        UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
        Uom firstRefereceUom = Uom.from(
                Name.of("Unite"),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of("Piece"),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        //When + Then
        assertThatThrownBy(() -> uomDomainService.createUom(secondRefereceUom))
                .isInstanceOf(UomException.class)
                .hasMessage("UomReferenceDuplicateException.1");
    }

    @Test
    void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() {
        //Given
        UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
        Uom firstRefereceUom = Uom.from(
                Name.of("Carton de 10"),
                uomCategoryId,
                UomType.GREATER,
                Ratio.of(2.0));

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of("Carton de 50"),
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
        UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
        Uom firstRefereceUom = Uom.from(
                Name.of("Unite"),
                uomCategoryId,
                UomType.REFERENCE,
                null);

        uomDomainService.createUom(firstRefereceUom);

        Uom secondRefereceUom = Uom.from(
                Name.of("Unite"),
                uomCategoryId,
                UomType.GREATER,
                Ratio.of(1.5));

        //When + Then
        assertThatThrownBy(() -> uomDomainService.createUom(secondRefereceUom))
                .isInstanceOf(UomException.class)
                .hasMessage("UomNameDuplicateException.1");
    }
}
