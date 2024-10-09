package cm.xenonbyte.farmbyte.common.domain.ports.primary;

import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
public interface StorageManager {
    Filename store(@Nonnull Image image, @Nonnull StorageLocation location);
    String fileToBase64(@Nonnull String filename) throws IOException;
    String mimeType(@Nonnull String filename) throws IOException;
}
