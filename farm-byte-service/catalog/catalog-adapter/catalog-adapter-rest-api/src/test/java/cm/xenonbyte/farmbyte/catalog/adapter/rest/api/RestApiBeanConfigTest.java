package cm.xenonbyte.farmbyte.catalog.adapter.rest.api;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductDomainServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryDomainServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomDomainServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryDomainServiceRestApiAdapter;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */

public class RestApiBeanConfigTest {
    @MockBean
    protected UomCategoryDomainServiceRestApiAdapter uomCategoryDomainServiceRestApiAdapter;

    @MockBean
    protected UomDomainServiceRestApiAdapter uomDomainServiceRestApiAdapter;

    @MockBean
    protected ProductCategoryDomainServiceRestApiAdapter productCategoryDomainServiceRestApiAdapter;

    @MockBean
    protected ProductDomainServiceRestApiAdapter productDomainServiceRestApiAdapter;
}
