package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.entity.Uom;
import cm.xenonbyte.farmbyte.uom.ports.primary.UomDomainService;
import cm.xenonbyte.farmbyte.uom.ports.UomDomainServiceImplementation;
import cm.xenonbyte.farmbyte.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.uom.vo.*;
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


    private UomDomainService uomDomainService;
    private UomRepository uomRepository;

    @BeforeEach
    void setUp() {
        uomRepository = new InMemoryUomRepository();
        uomDomainService = new UomDomainServiceImplementation(uomRepository);
    }

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.of("Unit"),
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.REFERENCE,
                    null
            ),
            Arguments.of(
                    Name.of("Carton de 10"),
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.of(2.0)
            ),
            Arguments.of(
                    Name.of("Centimetre"),
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.LOWER,
                    Ratio.of(0.5)
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
        assertThat(createdUom.getUomId())
                .isNotNull()
                .satisfies(result -> assertThat(result.getId()).isInstanceOf(UUID.class));
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
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.GREATER,
                    null,
                    IllegalArgumentException.class,
                    "Ratio is required when unit of measure type is not reference."
            ),
            Arguments.of(
                    Name.of("Carton de 10"),
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.of(0.8),
                    IllegalArgumentException.class,
                    "Ratio should be greater than 0 when unit of measure type is not greater."
            ),
            Arguments.of(
                    Name.of("Centimetre"),
                    UomCategoryId.of(UUID.randomUUID()),
                    UomType.LOWER,
                    Ratio.of(2.0),
                    IllegalArgumentException.class,
                    "Ratio should be lower than 0 when unit of measure type is not lower."
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
        UomCategoryId uomCategoryId = UomCategoryId.of(UUID.randomUUID());
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
                .isInstanceOf(UomDomainException.class)
                .hasMessage("We can't have two units of measure with type reference in the same category");
    }
}
