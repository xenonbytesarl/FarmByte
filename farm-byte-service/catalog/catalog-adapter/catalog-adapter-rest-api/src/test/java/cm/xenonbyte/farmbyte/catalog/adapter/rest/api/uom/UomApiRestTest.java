package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNameDuplicateException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomReferenceDuplicateException;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
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

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
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
@ContextConfiguration(classes = {UomApiAdapterService.class})
@WebMvcTest(UomApiRest.class)

@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
final class UomApiRestTest {

    public static final String UOM_PATH_URI = "/api/v1/catalog/uoms";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UomApiAdapterService uomApiAdapterService;

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
        String message = "UomApiRest.1";
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);

        CreateUomViewResponse createUomViewResponse = generateCreateUomViewResponse(uomCategoryId, name, ratioResponse, uomTypeEnumResponse);

        when(uomApiAdapterService.createUom(createUomViewRequest)).thenReturn(createUomViewResponse);
        ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

        //When + Then
        mockMvc.perform(post(UOM_PATH_URI)
                .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, Locale.FRENCH)
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
                .andExpect(jsonPath("$.data.body.id").isNotEmpty())
                .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(message, "")));

        verify(uomApiAdapterService, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
        assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

    }


    static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        UUID.randomUUID(),
                        "Unit",
                        null,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        "Uom.1"
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        0.8,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        "Uom.2"
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        2.0,
                        CreateUomViewRequest.UomTypeEnum.LOWER,
                        "Uom.3"
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

        when(uomApiAdapterService.createUom(createUomViewRequest)).thenThrow(new IllegalArgumentException(exceptionMessage));

        ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

        //When + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, Locale.FRENCH)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(exceptionMessage, "")));

        verify(uomApiAdapterService, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
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
        String exceptionMessage = "UomReferenceDuplicateException.1";

        when(uomApiAdapterService.createUom(createUomViewRequest)).thenThrow(new UomReferenceDuplicateException());

        //Act + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, Locale.FRENCH)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(exceptionMessage,"")));

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
        String exceptionMessage = "UomNameDuplicateException.1";

        when(uomApiAdapterService.createUom(createUomViewRequest)).thenThrow(new UomNameDuplicateException(new Object[]{name}));

        //Act + Then
        mockMvc.perform(post(UOM_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, Locale.FRENCH)
                        .content(createUomViewRequestAsString(createUomViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(exceptionMessage,new String[]{name})));

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
                .andExpect(jsonPath("$.reason").isNotEmpty())
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
