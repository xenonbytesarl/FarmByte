package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomDomainException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@ActiveProfiles("test")
@EnableAutoConfiguration
@ContextConfiguration(classes = {UomRestAPIAdapterService.class})
@WebMvcTest(UomRestController.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
final class UomRestControllerTest {

    public static final String UOM_PATH_URI = "/api/v1/catalog/uoms";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UomRestAPIAdapterService uomRestAPIAdapterService;

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        UUID.randomUUID(),
                        "Unite",
                        null,
                        1.0,
                        CreateUomViewRequest.UomTypeEnum.REFERENCE,
                        CreateUomViewResponse.UomTypeEnum.REFERENCE
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 5",
                        5.0,
                        5.0,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        CreateUomViewResponse.UomTypeEnum.GREATER
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Centimetre",
                        0.1,
                        0.1,
                        CreateUomViewRequest.UomTypeEnum.LOWER,
                        CreateUomViewResponse.UomTypeEnum.LOWER
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodSourceArgs")
    void should_create_uom_when_uom_type_is_reference(
            UUID uomCategoryId,
            String name,
            Double ratioRequest,
            Double ratioResponse,
            CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
            CreateUomViewResponse.UomTypeEnum uomTypeEnumResponse
    ) throws Exception {

        //Given

        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);

        CreateUomViewResponse createUomViewResponse = generateCreateUomViewResponse(uomCategoryId, name, ratioResponse, uomTypeEnumResponse);

        when(uomRestAPIAdapterService.createUom(createUomViewRequest)).thenReturn(createUomViewResponse);
        ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

        //When + Then
        mockMvc.perform(post(UOM_PATH_URI)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.body.name").value(name))
                .andExpect(jsonPath("$.data.body.ratio").value(ratioResponse))
                .andExpect(jsonPath("$.data.body.active").value(true))
                .andExpect(jsonPath("$.data.body.uomCategoryId").value(uomCategoryId.toString()))
                .andExpect(jsonPath("$.data.body.id").isNotEmpty());

        verify(uomRestAPIAdapterService, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
        assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

    }


    static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        UUID.randomUUID(),
                        "Unit",
                        null,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        "Ratio is required when unit of measure type is not reference."
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        0.8,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        "Ratio should be greater than 1 when unit of measure type is not greater."
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        2.0,
                        CreateUomViewRequest.UomTypeEnum.LOWER,
                        "Ratio should be greater than 1 when unit of measure type is not greater."
                )
        );
    }


    @ParameterizedTest
    @MethodSource("createUomThrowExceptionMethodSourceArgs")
    void should_throw_exception_when_create_uom_with_type_and_ratio_are_not_compatible(
            UUID uomCategoryId,
            String name,
            Double ratioRequest,
            CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
            String exceptionMessage
    ) throws Exception {

        //Given
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);

        when(uomRestAPIAdapterService.createUom(createUomViewRequest)).thenThrow(new IllegalArgumentException(exceptionMessage));

        ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

        //When + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(exceptionMessage));

        verify(uomRestAPIAdapterService, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
        assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

    }

    @Test
    void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() throws Exception {
        String name = "Unite";
        UUID uomCategoryId = UUID.randomUUID();
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                uomCategoryId,
                name,
                null,
                CreateUomViewRequest.UomTypeEnum.REFERENCE
        );
        String exceptionMessage = "We can't have two units of measure with type reference in the same category";

        when(uomRestAPIAdapterService.createUom(createUomViewRequest)).thenThrow(new UomDomainException(exceptionMessage));

        //Act + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(exceptionMessage));

    }


    @Test
    void should_throw_exception_when_create_two_uom_with_same_name() throws Exception {
        String name = "Unite";
        UUID uomCategoryId = UUID.randomUUID();
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                uomCategoryId,
                name,
                null,
                CreateUomViewRequest.UomTypeEnum.REFERENCE
        );
        String exceptionMessage = String.format("An unit of measure with the name '%s' already exists", name);

        when(uomRestAPIAdapterService.createUom(createUomViewRequest)).thenThrow(new UomDomainException(exceptionMessage));

        //Act + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(exceptionMessage));

    }

    private static CreateUomViewResponse generateCreateUomViewResponse(UUID uomCategoryId, String name, Double ratio, CreateUomViewResponse.UomTypeEnum uomTypeEnumResponse) {
        return new CreateUomViewResponse()
                .id(UUID.randomUUID())
                .uomCategoryId(uomCategoryId)
                .uomType(CreateUomViewResponse.UomTypeEnum.REFERENCE)
                .name(name)
                .ratio(ratio)
                .active(true)
                .uomType(uomTypeEnumResponse);
    }

    @Test
    void should_throw_exception_when_a_least_of_one_require_attribute_is_not_present() throws Exception {
        UUID uomCategoryId = UUID.randomUUID();
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                uomCategoryId,
               null,
                null,
                null
        );

        //Act + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").isEmpty())
                .andExpect(jsonPath("$.error").isNotEmpty())
                .andExpect(jsonPath("$.error[0].message").isNotEmpty())
                .andExpect(jsonPath("$.error[0].field").isNotEmpty());
    }

    private static CreateUomViewRequest generateCreateUomViewRequest(UUID uomCategoryId, String name, Double ratioRequest, CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest) {
        return new CreateUomViewRequest()
                .uomCategoryId(uomCategoryId)
                .name(name)
                .uomType(CreateUomViewRequest.UomTypeEnum.REFERENCE)
                .ratio(ratioRequest)
                .uomType(uomTypeEnumRequest);
    }

    private String createUomViewRequestAsString(CreateUomViewRequest createUomViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createUomViewRequest);
    }
}
