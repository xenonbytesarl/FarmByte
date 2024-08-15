package cm.xenonbyte.farmbyte.catalog.adapter.rest.api;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomApiAdapterService;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uomcategory.UomCategoryApiAdapterService;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public class ApiRestConfigTest {
    @MockBean
    protected UomCategoryApiAdapterService uomCategoryApiAdapterService;

    @MockBean
    protected UomApiAdapterService uomApiAdapterService;
}
