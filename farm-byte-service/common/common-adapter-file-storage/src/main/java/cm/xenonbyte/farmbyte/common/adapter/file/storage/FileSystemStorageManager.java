package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static cm.xenonbyte.farmbyte.common.adapter.file.storage.StorageBadException.CANNOT_STORE_FILE_OUTSIDE_CURRENT_DIRECTORY;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
@DomainService
public final class FileSystemStorageManager implements StorageManager {

    @Override
    public Filename store(@Nonnull Image image, @Nonnull StorageLocation location) {

        try {
            Path rootLocation = Paths.get(location.getPath().getValue());

            if(!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            Text newName = image.computeImageName();
            Path destinationFile = rootLocation.resolve(Paths.get(newName.getValue())).normalize().toAbsolutePath();

            if(!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageBadException(CANNOT_STORE_FILE_OUTSIDE_CURRENT_DIRECTORY);
            }
            try(InputStream inputStream = image.getContent()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return Filename.of(Text.of(destinationFile.toString()));
        } catch (IOException exception) {
            throw new StorageBadException(new String[]{image.getName().getValue()});
        }
    }
}
