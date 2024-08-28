package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.EMPTY;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.IMAGE_CONTENT_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.IMAGE_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.PLUS;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.POINT;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.SLASH;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.UNDERSCORE;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class Image {

    public static final String DEFAULT_PRODUCT_IMAGE_URL = "/product.png";

    private final Text name;
    private final InputStream content;

    public Image(@Nonnull Text name, @Nonnull InputStream content) {
        this.name = Objects.requireNonNull(name);
        this.content = Objects.requireNonNull(content);
    }

    public static Image with(@Nonnull Text name, InputStream content) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException(IMAGE_NAME_IS_REQUIRED);
        }

        if (content == null) {
            throw new IllegalArgumentException(IMAGE_CONTENT_IS_REQUIRED);
        }
        return new Image(name, content);
    }

    public Text getName() {
        return name;
    }

    public InputStream getContent() {
        return content;
    }

    public Image computeImageName(String rootTypePath) {
        Text computedFileName = StorageLocation.computeStoragePtah(rootTypePath).getPath()
                                        .concat(SLASH)
                                        .concat(
                                            ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSZ"))
                                                .replace(PLUS, EMPTY)
                                                .replace(POINT, EMPTY))
                                        .concat(UNDERSCORE)
                                        .concat(name.getValue());
        return Image.with(computedFileName, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(name, image.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
