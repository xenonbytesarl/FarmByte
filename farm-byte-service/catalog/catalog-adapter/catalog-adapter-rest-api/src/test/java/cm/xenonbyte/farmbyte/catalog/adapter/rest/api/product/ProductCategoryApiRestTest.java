package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.ApiRestBeanConfig;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ParentProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomIdMapper;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.common.domain.mapper.ImageMapper;
import cm.xenonbyte.farmbyte.common.domain.mapper.MoneyMapper;
import cm.xenonbyte.farmbyte.common.domain.mapper.ReferenceMapper;
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

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryApiRest.PRODUCT_CATEGORY_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
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
@WebMvcTest(ProductCategoryApiRest.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {ProductCategoryApiAdapterService.class, ReferenceMapper.class, ImageMapper.class, UomIdMapper.class, MoneyMapper.class})
public final class ProductCategoryApiRestTest extends ApiRestBeanConfig {

    public static final String PRODUCT_CATEGORY_PATH_URI = "/api/v1/catalog/product-categories";
    @Autowired
    private MockMvc mockMvc;


    static Stream<Arguments> createProductMethodSourceArgs() {
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
    @MethodSource("createProductMethodSourceArgs")
    void should_create_root_or_child_product_category(
            String nameValue,
            UUID parentProductCategoryUUID,
            UUID newCreateProductCategoryUUID,
            boolean activeValue
    ) throws Exception {
        CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                .name(nameValue)
                .parentProductCategoryId(parentProductCategoryUUID);
        CreateProductCategoryViewResponse createProductCategoryViewResponse = new CreateProductCategoryViewResponse()
                .id(newCreateProductCategoryUUID)
                .name(nameValue)
                .parentProductCategoryId(parentProductCategoryUUID)
                .active(activeValue);

        when(productCategoryApiAdapterService.createProductCategory(createProductCategoryViewRequest))
                .thenReturn(createProductCategoryViewResponse);

        ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);

        mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                    .accept(APPLICATION_JSON)
                    .header(ACCEPT_LANGUAGE, EN_LOCALE)
                    .contentType(APPLICATION_JSON)
                    .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.content.name").value(createProductCategoryViewResponse.getName()))
                .andExpect(jsonPath("$.data.content.id").value(createProductCategoryViewResponse.getId().toString()))
                .andExpect(jsonPath("$.data.content.active").value(createProductCategoryViewResponse.getActive()));


        verify(productCategoryApiAdapterService, times(1))
                .createProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());

        assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);


    }

    static Stream<Arguments> createProductMethodThrowExceptionSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Unite",
                        UUID.randomUUID(),
                        new ProductCategoryNameConflictException(new String[] {"Unite"}),
                        PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION,
                        "Unite",
                        409,
                        "CONFLICT",
                        status().isConflict()

                ),
                Arguments.of(
                        "Unite",
                        UUID.fromString("019156f3-0db6-794e-bfe0-f371636cd410"),
                        new ParentProductCategoryNotFoundException(new String[] {"019156f3-0db6-794e-bfe0-f371636cd410"}),
                        PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION,
                        "019156f3-0db6-794e-bfe0-f371636cd410",
                        404,
                        "NOT_FOUND",
                        status().isNotFound()
                )
        );
    }

   @ParameterizedTest
    @MethodSource("createProductMethodThrowExceptionSourceArgs")
    void should_throw_exception_when_create_product_category_fail(
           String nameValue,
           UUID parentProductCategoryUUID,
           RuntimeException exception,
           String exceptionMessage,
           String messageArgs,
           int code,
           String status,
           ResultMatcher isStatus

    ) throws Exception {
       CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
               .name(nameValue)
               .parentProductCategoryId(parentProductCategoryUUID);

       when(productCategoryApiAdapterService.createProductCategory(createProductCategoryViewRequest))
               .thenThrow(exception);

       ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
               ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);

       mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                       .accept(APPLICATION_JSON)
                       .header(ACCEPT_LANGUAGE, EN_LOCALE)
                       .contentType(APPLICATION_JSON)
                       .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
               .andDo(print())
               .andExpect(isStatus)
               .andExpect(jsonPath("$").isNotEmpty())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.code").value(code))
               .andExpect(jsonPath("$.status").value(status))
               .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                       exceptionMessage, Locale.forLanguageTag(EN_LOCALE), messageArgs)));

       verify(productCategoryApiAdapterService, times(1)).createProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());
       assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);
   }

    @Test
    void should_throw_exception_when_create_product_category_without_required_field() throws Exception {
        CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                .name(null)
                .parentProductCategoryId(null);

        mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                        .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_LOCALE)
                        .contentType(APPLICATION_JSON)
                        .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
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

    private String createProductCategoryViewRequestAsString(CreateProductCategoryViewRequest createProductCategoryViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createProductCategoryViewRequest);
    }
}
