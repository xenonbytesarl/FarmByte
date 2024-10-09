package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

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
            Path destinationFile = Paths.get(image.getName().getValue()).normalize().toAbsolutePath();
            if(!Files.exists(destinationFile.getParent())) {
                Files.createDirectories(destinationFile.getParent());
            }
            try(InputStream inputStream = image.getContent()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return Filename.of(Text.of(destinationFile.toString()));
        } catch (IOException exception) {
            throw new StorageBadException(new String[]{image.getName().getValue()});
        }
    }

    @Override
    public String fileToBase64(@Nonnull String filename) throws IOException {
        File file = new File(filename);
        if(file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            inputStream.read(fileData);
            inputStream.close();
            return Base64.getEncoder().encodeToString(fileData);
        } else {
            throw new StorageNotFoundException(new String[]{filename});
        }
    }

    @Override
    public String mimeType(@Nonnull String filename) throws IOException {
        return Paths.get(filename).toUri().toURL().openConnection().getContentType();
    }
}
