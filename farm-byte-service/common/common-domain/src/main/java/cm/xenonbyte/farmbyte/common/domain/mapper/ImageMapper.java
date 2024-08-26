package cm.xenonbyte.farmbyte.common.domain.mapper;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainMapper;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;

/**
 * @author bamk
 * @version 1.0
 * @since 24/08/2024
 */
@DomainMapper
public final class ImageMapper {
    public Image map(String value) {
        if(value == null || value.isEmpty()) {
            return null;
        }
        return new Image(Text.of(value));
    }

    public String map(Image image) {
        if(image == null) {
            return null;
        }
        return image.getText().getValue();
    }
}
