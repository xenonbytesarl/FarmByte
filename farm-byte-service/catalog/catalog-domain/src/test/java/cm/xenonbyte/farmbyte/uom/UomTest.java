package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.service.IUomDomainService;
import cm.xenonbyte.farmbyte.uom.service.UomDomainService;
import cm.xenonbyte.farmbyte.uom.vo.Name;
import cm.xenonbyte.farmbyte.uom.vo.Ratio;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomTest {


    private IUomDomainService uomDomainService;

    @BeforeEach
    void setUp() {
        uomDomainService = new UomDomainService();
    }

    @Test
    void shouldCreateUom() {
        //Given
        Name name = Name.from("Unit");
        UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
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
    }
}
