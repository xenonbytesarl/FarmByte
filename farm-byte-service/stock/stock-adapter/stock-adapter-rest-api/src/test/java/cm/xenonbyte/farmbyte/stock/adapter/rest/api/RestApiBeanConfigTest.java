package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
public class RestApiBeanConfigTest {
    @MockBean
    protected StockLocationDomainServiceRestApiAdapter stockLocationDomainRestApiAdapter;
}
