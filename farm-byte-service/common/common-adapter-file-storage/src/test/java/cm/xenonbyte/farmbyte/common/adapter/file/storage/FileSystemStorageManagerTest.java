package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
public final class FileSystemStorageManagerTest {

    private StorageManager storageManager;

    @BeforeEach
    void setUp() {
        storageManager = new FileSystemStorageManager();
    }

    @Test
    void should_save_file_success() {
        //Given
        byte[] imageContent = "<<image data>>".getBytes(StandardCharsets.UTF_8);
        Image image = Image.with(Text.of("test.txt"), new ByteArrayInputStream(imageContent));
        StorageLocation location = StorageLocation.of(Text.of("products"));

        //Act
        Filename filename = storageManager.store(image, StorageLocation.computeStoragePtah(location.getPath().getValue()));

        //Then
        File storedfile = new File(filename.getText().getValue());
        assertThat(storedfile.exists()).isTrue();
        assertThat(storedfile.length()).isEqualTo(imageContent.length);
    }

    @Test
    void should_save_file_failure() {

    }
}
