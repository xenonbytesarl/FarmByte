package cm.xenonbyte.farmbyte.bootstrap.inventory;

/**
 * @author bamk
 * @version 1.0
 * @since 18/10/2024
 */

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.CreateInventoryEmplacementApiViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementByIdViewApiResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementsViewApiResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.SearchInventoryEmplacementsViewApiResponse;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException.INVENTORY_EMPLACEMENT_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException.INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@DirtiesContext
@ActiveProfiles("test")
@ExtendWith({DatabaseSetupExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
public class InventoryEmplacementIT {

    //TODO to move in common module
    public static final String FIND_URL_PARAM = "?page={page}&size={size}&attribute={attribute}&direction={direction}";
    public static final String SEARCH_URL_PARAM = FIND_URL_PARAM + "&keyword={keyword}";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    InventoryEmplacementServiceRestApiAdapter inventoryEmplacementServiceRestApiAdapter;

    String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1/inventory/inventory-emplacements";
    }

    @Nested
    class CreateInventoryEmplacementRestApiIT {

        public static Stream<Arguments> createInventoryEmplacementMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Root Emplacement",
                            CreateInventoryEmplacementViewRequest.TypeEnum.VIEW,
                            null
                    ),
                    Arguments.of(
                            "Child Emplacement View",
                            CreateInventoryEmplacementViewRequest.TypeEnum.INTERNAL,
                            UUID.fromString("019296f7-8b01-74bb-be63-035025f53c1f")
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createInventoryEmplacementMethodSourceArgs")
        void should_create_root_or_child_inventory_emplacement(
                String name,
                CreateInventoryEmplacementViewRequest.TypeEnum type,
                UUID parentId
        )  {
            //Given
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = getCreateInventoryEmplacementViewRequest(name, type, parentId);
            HttpEntity<CreateInventoryEmplacementViewRequest> request = new HttpEntity<>(createInventoryEmplacementViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<CreateInventoryEmplacementApiViewResponse> response = restTemplate.exchange(BASE_URL , POST, request,CreateInventoryEmplacementApiViewResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(CreateInventoryEmplacementViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_existing_name() {
            //Given
            String  name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;

            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = getCreateInventoryEmplacementViewRequest(name, type, null);
            HttpEntity<CreateInventoryEmplacementViewRequest> request = new HttpEntity<>(createInventoryEmplacementViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL , POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_NAME_CONFLICT, Locale.forLanguageTag(EN_LOCALE), name));
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() {
            //Given
            String  name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;
            UUID parentId = UUID.fromString("01929cd3-5716-7606-a364-b1a30d863762");

            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = getCreateInventoryEmplacementViewRequest(name, type, parentId);
            HttpEntity<CreateInventoryEmplacementViewRequest> request = new HttpEntity<>(createInventoryEmplacementViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL , POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), parentId.toString()));
        }

        static Stream<Arguments> createInventoryEmplacementFailMethodSource() {
            return Stream.of(
                    Arguments.of(
                            null,
                            CreateInventoryEmplacementViewRequest.TypeEnum.INTERNAL
                    ),
                    Arguments.of(
                            "Internal Inventory Emplacement",
                            null
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createInventoryEmplacementFailMethodSource")
        void should_fail_when_create_inventory_emplacement_without_mandatory_attribute(
                String name, CreateInventoryEmplacementViewRequest.TypeEnum type
        ) {
            //Given
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest =
                    getCreateInventoryEmplacementViewRequest(name, type, null);

            HttpEntity<CreateInventoryEmplacementViewRequest> request = new HttpEntity<>(createInventoryEmplacementViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.ApiErrorResponse> response = restTemplate.exchange(BASE_URL, POST, request, cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getError()).isNotEmpty();
            assertThat(response.getBody().getError().getFirst().getField()).isNotEmpty();
            assertThat(response.getBody().getError().getFirst().getMessage()).isNotEmpty();
        }

        private CreateInventoryEmplacementViewRequest getCreateInventoryEmplacementViewRequest(String name, CreateInventoryEmplacementViewRequest.TypeEnum type, UUID parentId) {
            return new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);
        }
    }

    @Nested
    class FindInventoryEmplacementByIdIT {

        @Test
        void should_success_when_find_inventory_emplacement_existing_id() {
            //Given
            String inventoryEmplacementUUID = "0192a686-3f49-7f2d-b7eb-ebe08814b82a";
            HttpEntity<Object> request = new HttpEntity<>(getHttpHeaders());
            FindInventoryEmplacementByIdViewResponse findInventoryEmplacementByIdViewResponse = new FindInventoryEmplacementByIdViewResponse()
                    .id(UUID.fromString(inventoryEmplacementUUID))
                    .name("Supplier Emplacement 1")
                    .type(FindInventoryEmplacementByIdViewResponse.TypeEnum.SUPPLIER)
                    .active(true);

            //Act
            ResponseEntity<FindInventoryEmplacementByIdViewApiResponse> response = restTemplate.exchange(
                    (BASE_URL + "/" + inventoryEmplacementUUID), GET, request, FindInventoryEmplacementByIdViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData()).isNotEmpty();
            assertThat(response.getBody().getData().get(BODY)).isEqualTo(findInventoryEmplacementByIdViewResponse);

        }
    }

    @Nested
    class FindInventoryEmplacementsIT {
        @Test
        void should_success_when_find_uoms() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();

            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");

            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<FindInventoryEmplacementsViewApiResponse> response = restTemplate.exchange(BASE_URL + FIND_URL_PARAM , GET, request, FindInventoryEmplacementsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(FindInventoryEmplacementsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getElements().size()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchInventoryEmplacementsIT {
        @Test
        void should_success_when_find_uoms() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();

            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");
            params.put("keyword", "int");

            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<SearchInventoryEmplacementsViewApiResponse> response = restTemplate.exchange(BASE_URL + SEARCH_URL_PARAM , GET, request, SearchInventoryEmplacementsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(SearchInventoryEmplacementsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getElements().size()).isGreaterThan(0);
        }
    }

    private static @Nonnull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
