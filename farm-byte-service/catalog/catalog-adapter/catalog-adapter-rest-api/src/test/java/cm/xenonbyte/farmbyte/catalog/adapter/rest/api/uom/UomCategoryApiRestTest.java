package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.ApiRestConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryApiRest.UOM_CATEGORY_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@ActiveProfiles("test")
@WebMvcTest(UomCategoryApiRest.class)
@ContextConfiguration(classes = {UomCategoryApiAdapterService.class})
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
public final class UomCategoryApiRestTest extends ApiRestConfigTest {

    public static final String UOM_CATEGORY_PATH_URI = "/api/v1/catalog/uom-categories";
    @Autowired
    private MockMvc mockMvc;

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Unite",
                        null,
                        UUID.randomUUID(),
                        true
                ),
                Arguments.of(
                        "Unite",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        true
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodSourceArgs")
    void should_create_root_or_child_uom_category(
            String nameValue,
            UUID parentUomCategoryUUID,
            UUID newCreateUomCategoryUUID,
            boolean activeValue
    ) throws Exception {
        CreateUomCategoryViewRequest createUomCategoryViewRequest = new CreateUomCategoryViewRequest()
                .name(nameValue)
                .parentUomCategoryId(parentUomCategoryUUID);
        CreateUomCategoryViewResponse createUomCategoryViewResponse = new CreateUomCategoryViewResponse()
                .id(newCreateUomCategoryUUID)
                .name(nameValue)
                .parentUomCategoryId(parentUomCategoryUUID)
                .active(activeValue);

        when(uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest))
                .thenReturn(createUomCategoryViewResponse);

        ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);

        mockMvc.perform(post(UOM_CATEGORY_PATH_URI)
                    .accept(APPLICATION_JSON)
                    .header(ACCEPT_LANGUAGE, EN_LOCALE)
                    .contentType(APPLICATION_JSON)
                    .content(createUomCategoryViewRequestAsString(createUomCategoryViewRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.content.name").value(createUomCategoryViewResponse.getName()))
                .andExpect(jsonPath("$.data.content.id").value(createUomCategoryViewResponse.getId().toString()))
                .andExpect(jsonPath("$.data.content.active").value(createUomCategoryViewResponse.getActive()));


        verify(uomCategoryApiAdapterService, times(1))
                .createUomCategory(createUomCategoryViewRequestArgumentCaptor.capture());

        assertThat(createUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createUomCategoryViewRequest);


    }

    static Stream<Arguments> createUomMethodThrowExceptionSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Unite",
                        UUID.randomUUID(),
                        new UomCategoryNameConflictException(new String[] {"Unite"}),
                        UOM_CATEGORY_NAME_CONFLICT_EXCEPTION,
                        "Unite",
                        409,
                        "CONFLICT",
                        status().isConflict()

                ),
                Arguments.of(
                        "Unite",
                        UUID.fromString("019156f3-0db6-794e-bfe0-f371636cd410"),
                        new UomParentCategoryNotFoundException(new String[] {"019156f3-0db6-794e-bfe0-f371636cd410"}),
                        UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION,
                        "019156f3-0db6-794e-bfe0-f371636cd410",
                        404,
                        "NOT_FOUND",
                        status().isNotFound()
                )
        );
    }

   @ParameterizedTest
    @MethodSource("createUomMethodThrowExceptionSourceArgs")
    void should_throw_exception_when_create_uom_category_fail(
           String nameValue,
           UUID parentUomCategoryUUID,
           RuntimeException exception,
           String exceptionMessage,
           String messageArgs,
           int code,
           String status,
           ResultMatcher isStatus

    ) throws Exception {
       CreateUomCategoryViewRequest createUomCategoryViewRequest = new CreateUomCategoryViewRequest()
               .name(nameValue)
               .parentUomCategoryId(parentUomCategoryUUID);

       when(uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest))
               .thenThrow(exception);

       ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
               ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);

       mockMvc.perform(post(UOM_CATEGORY_PATH_URI)
                       .accept(APPLICATION_JSON)
                       .header(ACCEPT_LANGUAGE, EN_LOCALE)
                       .contentType(APPLICATION_JSON)
                       .content(createUomCategoryViewRequestAsString(createUomCategoryViewRequest)))
               .andDo(print())
               .andExpect(isStatus)
               .andExpect(jsonPath("$").isNotEmpty())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.code").value(code))
               .andExpect(jsonPath("$.status").value(status))
               .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                       exceptionMessage, Locale.forLanguageTag(EN_LOCALE), messageArgs)));

       verify(uomCategoryApiAdapterService, times(1)).createUomCategory(createUomCategoryViewRequestArgumentCaptor.capture());
       assertThat(createUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createUomCategoryViewRequest);
   }

    @Test
    void should_throw_exception_when_create_uom_category_without_required_field() throws Exception {
        CreateUomCategoryViewRequest createUomCategoryViewRequest = new CreateUomCategoryViewRequest()
                .name(null)
                .parentUomCategoryId(null);

        mockMvc.perform(post(UOM_CATEGORY_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_LOCALE)
                        .contentType(APPLICATION_JSON)
                        .content(createUomCategoryViewRequestAsString(createUomCategoryViewRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                        VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), "")))
                .andExpect(jsonPath("$.error.[0].field").isNotEmpty())
                .andExpect(jsonPath("$.error.[0].message").isNotEmpty());
    }

    private String createUomCategoryViewRequestAsString(CreateUomCategoryViewRequest createUomCategoryViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createUomCategoryViewRequest);
    }
}
