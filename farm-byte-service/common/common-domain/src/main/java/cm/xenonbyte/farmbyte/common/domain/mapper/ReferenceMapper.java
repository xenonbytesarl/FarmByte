package cm.xenonbyte.farmbyte.common.domain.mapper;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainMapper;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;

/**
 * @author bamk
 * @version 1.0
 * @since 24/08/2024
 */
@DomainMapper
public class ReferenceMapper {

    public Reference map(String value) {
        if(value == null || value.isEmpty()) {
            return null;
        }
        return new Reference(Text.of(value));
    }

    public String map(Reference reference) {
        if(reference == null) {
            return null;
        }
        return reference.getText().getValue();
    }
}
