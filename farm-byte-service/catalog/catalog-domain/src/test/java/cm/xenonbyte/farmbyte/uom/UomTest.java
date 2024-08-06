package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.service.IUomDomainService;
import cm.xenonbyte.farmbyte.uom.service.UomDomainService;
import cm.xenonbyte.farmbyte.uom.vo.*;
import org.junit.jupiter.api.BeforeEach;
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


    private IUomDomainService uomDomainService;

    @BeforeEach
    void setUp() {
        uomDomainService = new UomDomainService();
    }

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.from("Unit"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.REFERENCE,
                    null
            ),
            Arguments.of(
                    Name.from("Carton de 10"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.from(2.0)
            ),
            Arguments.of(
                    Name.from("Centimetre"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.LOWER,
                    Ratio.from(0.5)
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
        assertThat(createdUom.getActive()).isEqualTo(Active.from(true));
        assertThat(createdUom.getRatio()).isEqualTo(ratio == null ? Ratio.from( Ratio.REFERENCE) : ratio);
        assertThat(createdUom.getName()).isEqualTo(name);
    }

    static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
        return Stream.of(
            Arguments.of(
                    Name.from("Unit"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.GREATER,
                    null,
                    IllegalArgumentException.class,
                    "Ratio is required when unit of measure type is not reference."
            ),
            Arguments.of(
                    Name.from("Carton de 10"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.GREATER,
                    Ratio.from(0.8),
                    IllegalArgumentException.class,
                    "Ratio should be greater than 0 when unit of measure type is not greater."
            ),
            Arguments.of(
                    Name.from("Centimetre"),
                    UomCategoryId.generate(UUID.randomUUID()),
                    UomType.LOWER,
                    Ratio.from(2.0),
                    IllegalArgumentException.class,
                    "Ratio should be lower than 0 when unit of measure type is not lower."
            )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomThrowExceptionMethodSourceArgs")
    void should_throw_exception_when_create_uom_with_uom_type_is_not_reference_and_ratio_is_null(
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


}
