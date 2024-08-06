package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.vo.Name;
import cm.xenonbyte.farmbyte.uom.vo.Ratio;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Uom {
    private final Name name;
    private final UomCategoryId uomCategoryId;
    private final UomType uomType;
    private final Ratio ratio;

    public Uom(Name name, UomCategoryId uomCategoryId, UomType uomType, Ratio ratio) {

        this.name = name;
        this.uomCategoryId = uomCategoryId;
        this.uomType = uomType;
        this.ratio = ratio;
    }

    public static Uom from(Name name, UomCategoryId uomCategoryId, UomType uomType, Ratio ratio) {
        return new Uom(
                name,
                uomCategoryId,
                uomType,
                ratio
        );
    }
}
