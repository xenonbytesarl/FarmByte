package cm.xenonbyte.farmbyte.adapter.data.access.jpa.uom;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */

public enum UomTypeJpa {
    GREATER, LOWER, REFERENCE
}
