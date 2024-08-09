package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory.UomCategoryJpa;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Ratio;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T15:58:56+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Homebrew)"
)
@Component
public class UomJpaMapperImpl implements UomJpaMapper {

    @Override
    public UomJpa fromUom(Uom uom) {
        if ( uom == null ) {
            return null;
        }

        UomJpa.UomJpaBuilder<?, ?> uomJpa = UomJpa.builder();

        uomJpa.uomCategoryJpa( uomCategoryIdToUomCategoryJpa( uom.getUomCategoryId() ) );
        uomJpa.id( uomIdValue( uom ) );
        uomJpa.name( uomNameValue( uom ) );
        uomJpa.ratio( uomRatioValue( uom ) );
        uomJpa.active( uomActiveValue( uom ) );
        uomJpa.uomTypeJpa( toUomTypeJpa( uom.getUomType() ) );

        return uomJpa.build();
    }

    @Override
    public Uom fromUomJpa(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        Uom.Builder uom = Uom.builder();

        uom.id( uomJpaToUomId( uomJpa ) );
        uom.name( uomJpaToName( uomJpa ) );
        uom.ratio( uomJpaToRatio( uomJpa ) );
        uom.active( uomJpaToActive( uomJpa ) );
        uom.uomCategoryId( uomCategoryJpaToUomCategoryId( uomJpa.getUomCategoryJpa() ) );
        uom.uomType( toUomType( uomJpa.getUomTypeJpa() ) );

        return uom.build();
    }

    protected UomCategoryJpa uomCategoryIdToUomCategoryJpa(UomCategoryId uomCategoryId) {
        if ( uomCategoryId == null ) {
            return null;
        }

        UomCategoryJpa.UomCategoryJpaBuilder<?, ?> uomCategoryJpa = UomCategoryJpa.builder();

        uomCategoryJpa.id( uomCategoryId.getValue() );

        return uomCategoryJpa.build();
    }

    private UUID uomIdValue(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        UomId id = uom.getId();
        if ( id == null ) {
            return null;
        }
        UUID value = id.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String uomNameValue(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        Name name = uom.getName();
        if ( name == null ) {
            return null;
        }
        String value = name.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private Double uomRatioValue(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        Ratio ratio = uom.getRatio();
        if ( ratio == null ) {
            return null;
        }
        Double value = ratio.value();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private Boolean uomActiveValue(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        Active active = uom.getActive();
        if ( active == null ) {
            return null;
        }
        Boolean value = active.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    protected UomId uomJpaToUomId(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        UUID value = null;

        value = uomJpa.getId();

        UomId uomId = new UomId( value );

        return uomId;
    }

    protected Name uomJpaToName(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        String value = null;

        value = uomJpa.getName();

        Name name = new Name( value );

        return name;
    }

    protected Ratio uomJpaToRatio(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        Double value = null;

        value = uomJpa.getRatio();

        Ratio ratio = new Ratio( value );

        return ratio;
    }

    protected Active uomJpaToActive(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        Boolean value = null;

        value = uomJpa.getActive();

        Active active = new Active( value );

        return active;
    }

    protected UomCategoryId uomCategoryJpaToUomCategoryId(UomCategoryJpa uomCategoryJpa) {
        if ( uomCategoryJpa == null ) {
            return null;
        }

        UUID value = null;

        value = uomCategoryJpa.getId();

        UomCategoryId uomCategoryId = new UomCategoryId( value );

        return uomCategoryId;
    }
}
