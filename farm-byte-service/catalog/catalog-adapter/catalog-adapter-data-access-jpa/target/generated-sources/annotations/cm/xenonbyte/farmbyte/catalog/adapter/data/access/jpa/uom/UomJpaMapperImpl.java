package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory.UomCategoryJpa;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Active;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Ratio;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T11:46:23+0200",
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
        uomJpa.id( uomUomIdIdentifier( uom ) );
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

        UomId uomId = null;
        Name name = null;
        Ratio ratio = null;
        Active active = null;
        UomCategoryId uomCategoryId = null;
        UomType uomType = null;

        uomId = uomJpaToUomId( uomJpa );
        name = uomJpaToName( uomJpa );
        ratio = uomJpaToRatio( uomJpa );
        active = uomJpaToActive( uomJpa );
        uomCategoryId = uomCategoryJpaToUomCategoryId( uomJpa.getUomCategoryJpa() );
        uomType = toUomType( uomJpa.getUomTypeJpa() );

        Uom uom = new Uom( uomId, name, uomCategoryId, uomType, ratio, active );

        return uom;
    }

    protected UomCategoryJpa uomCategoryIdToUomCategoryJpa(UomCategoryId uomCategoryId) {
        if ( uomCategoryId == null ) {
            return null;
        }

        UomCategoryJpa.UomCategoryJpaBuilder<?, ?> uomCategoryJpa = UomCategoryJpa.builder();

        uomCategoryJpa.id( uomCategoryId.getIdentifier() );

        return uomCategoryJpa.build();
    }

    private UUID uomUomIdIdentifier(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        UomId uomId = uom.getUomId();
        if ( uomId == null ) {
            return null;
        }
        UUID identifier = uomId.getIdentifier();
        if ( identifier == null ) {
            return null;
        }
        return identifier;
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
        Double value = ratio.getValue();
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

        UUID identifier = null;

        identifier = uomJpa.getId();

        UomId uomId = new UomId( identifier );

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

        UUID identifier = null;

        identifier = uomCategoryJpa.getId();

        UomCategoryId uomCategoryId = new UomCategoryId( identifier );

        return uomCategoryId;
    }
}
