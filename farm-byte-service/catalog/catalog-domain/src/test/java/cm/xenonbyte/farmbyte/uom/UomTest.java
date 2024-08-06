package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.service.IUomDomainService;
import cm.xenonbyte.farmbyte.uom.service.UomDomainService;
import cm.xenonbyte.farmbyte.uom.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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

    @Test
    void should_create_uom_when_uom_type_is_reference() {
        //Given
        Name name = Name.from("Unit");
        UomCategoryId uomCategoryId = UomCategoryId.generate(UUID.randomUUID());
        Ratio ratio =  null;
        Uom uom = Uom.from(
                name,
                uomCategoryId,
                UomType.REFERENCE,
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
        assertThat(createdUom.getActive()).isEqualTo(Active.from(true));
        assertThat(createdUom.getRatio()).isEqualTo(Ratio.from(Ratio.REFERENCE));
        assertThat(createdUom.getName()).isEqualTo(name);
    }

    @Test
    void should_throw_exception_when_create_uom_with_uom_type_is_not_reference_and_ratio_is_null() {
        //Given
        Name name = Name.from("Unit");
        UomCategoryId uomCategoryId = UomCategoryId.generate(UUID.randomUUID());
        Ratio ratio =  null;
        Uom uom = Uom.from(
                name,
                uomCategoryId,
                UomType.GREATER,
                ratio
        );

        //Act + Then
        assertThatThrownBy(() -> uomDomainService.createUom(uom))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ratio is required when unit of measure type is not reference.");

    }


}
