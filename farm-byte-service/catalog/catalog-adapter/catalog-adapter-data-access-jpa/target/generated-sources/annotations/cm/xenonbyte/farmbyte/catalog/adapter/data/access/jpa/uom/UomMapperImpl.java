package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.entity.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.Active;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.Ratio;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomType;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-08T11:11:00+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Homebrew)"
)
@Component
public class UomMapperImpl implements UomMapper {

    @Override
    public UomJpa fromUom(Uom uom) {
        if ( uom == null ) {
            return null;
        }

        UomJpa.UomJpaBuilder<?, ?> uomJpa = UomJpa.builder();

        uomJpa.uomCategoryJpa( uomCategoryIdToUomCategoryJpa( uom.getUomCategoryId() ) );
        uomJpa.id( uomUomIdId( uom ) );
        uomJpa.name( uomNameValue( uom ) );
        uomJpa.ratio( uomRatioValue( uom ) );
        uomJpa.active( uomActiveValue( uom ) );
        uomJpa.type( toUomTypeJpa( uom.getUomType() ) );

        return uomJpa.build();
    }

    @Override
    public Uom fromUomJpa(UomJpa uomJpa) {
        if ( uomJpa == null ) {
            return null;
        }

        Name name = null;
        Ratio ratio = null;
        UomCategoryId uomCategoryId = null;
        UomType uomType = null;

        name = uomJpaToName( uomJpa );
        ratio = uomJpaToRatio( uomJpa );
        uomCategoryId = uomCategoryJpaToUomCategoryId( uomJpa.getUomCategoryJpa() );
        uomType = toUomType( uomJpa.getType() );

        Uom uom = new Uom( name, uomCategoryId, uomType, ratio );

        return uom;
    }

    protected UomCategoryJpa uomCategoryIdToUomCategoryJpa(UomCategoryId uomCategoryId) {
        if ( uomCategoryId == null ) {
            return null;
        }

        UomCategoryJpa.UomCategoryJpaBuilder<?, ?> uomCategoryJpa = UomCategoryJpa.builder();

        uomCategoryJpa.id( uomCategoryId.getId() );

        return uomCategoryJpa.build();
    }

    private UUID uomUomIdId(Uom uom) {
        if ( uom == null ) {
            return null;
        }
        UomId uomId = uom.getUomId();
        if ( uomId == null ) {
            return null;
        }
        UUID id = uomId.getId();
        if ( id == null ) {
            return null;
        }
        return id;
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

    protected UomCategoryId uomCategoryJpaToUomCategoryId(UomCategoryJpa uomCategoryJpa) {
        if ( uomCategoryJpa == null ) {
            return null;
        }

        UUID id = null;

        id = uomCategoryJpa.getId();

        UomCategoryId uomCategoryId = new UomCategoryId( id );

        return uomCategoryId;
    }
}
