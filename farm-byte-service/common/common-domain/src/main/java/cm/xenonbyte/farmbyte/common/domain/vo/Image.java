package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.URL_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class Image {

    public static final String DEFAULT_PRODUCT_IMAGE_URL = "products/product.png";
    private final Text text;

    public Image(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    public static Image with(@Nonnull Text url) {
        if(url == null || url.isEmpty()) {
            throw new IllegalArgumentException(URL_VALUE_IS_REQUIRED);
        }
        return new Image(url);
    }

    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(text, image.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
