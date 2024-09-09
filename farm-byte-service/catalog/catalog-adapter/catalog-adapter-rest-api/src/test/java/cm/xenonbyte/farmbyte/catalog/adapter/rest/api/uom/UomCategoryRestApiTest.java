package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.RestApiBeanConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryRestApi.UOM_CATEGORIES_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryRestApi.UOM_CATEGORY_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryRestApi.UOM_CATEGORY_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryRestApi.UOM_CATEGORY_UPDATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@ActiveProfiles("test")
@WebMvcTest(UomCategoryRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {UomCategoryDomainServiceRestApiAdapter.class})
public final class UomCategoryRestApiTest extends RestApiBeanConfigTest {

    public static final String UOM_CATEGORY_PATH_URI = "/api/v1/catalog/uom-categories";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateUomCategoryRestApiTest {
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

            when(uomCategoryDomainServiceRestApiAdapter.createUomCategory(createUomCategoryViewRequest))
                    .thenReturn(createUomCategoryViewResponse);

            ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);

            mockMvc.perform(post(UOM_CATEGORY_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(createUomCategoryViewRequest)))
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


            verify(uomCategoryDomainServiceRestApiAdapter, times(1))
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

            when(uomCategoryDomainServiceRestApiAdapter.createUomCategory(createUomCategoryViewRequest))
                    .thenThrow(exception);

            ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);

            mockMvc.perform(post(UOM_CATEGORY_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(createUomCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(isStatus)
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(code))
                    .andExpect(jsonPath("$.status").value(status))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                            exceptionMessage, Locale.forLanguageTag(EN_LOCALE), messageArgs)));

            verify(uomCategoryDomainServiceRestApiAdapter, times(1)).createUomCategory(createUomCategoryViewRequestArgumentCaptor.capture());
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
                            .content(uomCategoryViewRequestToString(createUomCategoryViewRequest)))
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

    }

    @Nested
    class FindUomCategoryByIdRestApiTest {
        @Test
        void should_success_when_find_uom_category_with_existing_id() throws Exception {
            //Given
            UUID uomcategoryUUID = UUID.fromString("0191ac31-e0e9-76c2-bb68-bc155b49c3cf");
            FindUomCategoryByIdViewResponse findUomCategoryByIdViewResponse = new FindUomCategoryByIdViewResponse().id(uomcategoryUUID).name("Unite");

            when(uomCategoryDomainServiceRestApiAdapter.findUomCategoryById(uomcategoryUUID)).thenReturn(findUomCategoryByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(UOM_CATEGORY_PATH_URI + "/%s", uomcategoryUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_CATEGORY_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findUomCategoryByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findUomCategoryByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.active").value(findUomCategoryByIdViewResponse.getActive()));

            //Then
            verify(uomCategoryDomainServiceRestApiAdapter, times(1)).findUomCategoryById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomcategoryUUID);
        }

        @Test
        void should_fail_when_find_uom_category_with_non_existing_id() throws Exception {
            //Given
            UUID uomcategoryUUID = UUID.fromString("0191ac31-e0e9-76c2-bb68-bc155b49c3cf");

            when(uomCategoryDomainServiceRestApiAdapter.findUomCategoryById(uomcategoryUUID)).thenThrow(UomCategoryNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[] {new String[]{uomcategoryUUID.toString()}}));
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(UOM_CATEGORY_PATH_URI + "/%s", uomcategoryUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomcategoryUUID.toString())));

            //Then
            verify(uomCategoryDomainServiceRestApiAdapter, times(1)).findUomCategoryById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomcategoryUUID);
        }
    }

    @Nested
    class FindUomCategoriesRestApiTest {
        @Test
        void should_success_when_find_uom_categories() throws Exception {
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";

            FindUomCategoriesPageInfoViewResponse findUomCategoriesPageInfoViewResponse = new FindUomCategoriesPageInfoViewResponse()
                    .pageSize(size)
                    .last(false)
                    .first(true)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(List.of(
                            new FindUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Unite")
                                    .active(true),
                            new FindUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Poids")
                                    .active(true),
                            new FindUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Temps")
                                    .active(true),
                            new FindUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Volume")
                                    .active(true),
                            new FindUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Distance")
                                    .active(true)
                    ));
            when(uomCategoryDomainServiceRestApiAdapter.findUomCategories(page, size, attribute, direction)).thenReturn(findUomCategoriesPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            //Act
            mockMvc.perform(get(UOM_CATEGORY_PATH_URI )
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .header("page", page)
                            .header("size", size)
                            .header("attribute", attribute)
                            .header("direction", direction)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(uomCategoryDomainServiceRestApiAdapter, times(1)).findUomCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);

        }
    }

    @Nested
    class SearchUomCategoriesRestApiTest {
        @Test
        void should_success_when_search_uom_categories_with_keyword() throws Exception {
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "e";

            SearchUomCategoriesPageInfoViewResponse searchUomCategoriesPageInfoViewResponse = new SearchUomCategoriesPageInfoViewResponse()
                    .pageSize(size)
                    .last(false)
                    .first(true)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(List.of(
                            new SearchUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Unite")
                                    .active(true),
                            new SearchUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Temps")
                                    .active(true),
                            new SearchUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Volume")
                                    .active(true),
                            new SearchUomCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Distance")
                                    .active(true)
                    ));
            when(uomCategoryDomainServiceRestApiAdapter.searchUomCategories(page, size, attribute, direction, keyword)).thenReturn(searchUomCategoriesPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act
            mockMvc.perform(get(UOM_CATEGORY_PATH_URI + "/search" )
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .header("page", page)
                            .header("size", size)
                            .header("attribute", attribute)
                            .header("direction", direction)
                            .header("keyword", keyword)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(uomCategoryDomainServiceRestApiAdapter, times(1)).searchUomCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                    stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
            assertThat(stringArgumentCaptor.getAllValues().get(2)).isEqualTo(keyword);

        }
    }

    @Nested
    class UpdateUomCategoryRestApiTest {

        @Test
        void should_success_when_update_uom_category() throws Exception {
            //Given
            UUID uomCategoryIdUUID = UUID.randomUUID();
            UpdateUomCategoryViewRequest updateUomCategoryViewRequest = new UpdateUomCategoryViewRequest()
                    .id(uomCategoryIdUUID)
                    .name("Nouvelle Unite")
                    .active(true);

            UpdateUomCategoryViewResponse updateUomCategoryViewResponse = new UpdateUomCategoryViewResponse()
                    .id(uomCategoryIdUUID)
                    .name("Nouvelle Unite")
                    .active(true);

            when(uomCategoryDomainServiceRestApiAdapter.updateUomCategory(uomCategoryIdUUID, updateUomCategoryViewRequest))
                    .thenReturn(updateUomCategoryViewResponse);

            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UpdateUomCategoryViewRequest> updateUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateUomCategoryViewRequest.class);

            //Act + Then
            mockMvc.perform(put(UOM_CATEGORY_PATH_URI + "/" + uomCategoryIdUUID.toString())
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(updateUomCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_CATEGORY_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.name").value(updateUomCategoryViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.id").value(updateUomCategoryViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(updateUomCategoryViewResponse.getActive()));


            verify(uomCategoryDomainServiceRestApiAdapter, times(1))
                    .updateUomCategory(uuidArgumentCaptor.capture(), updateUomCategoryViewRequestArgumentCaptor.capture());

            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomCategoryIdUUID);
            assertThat(updateUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomCategoryViewRequest);
        }

        @Test
        void should_fail_when_update_uom_category_id_with_not_existing_id() throws Exception {
            //Given
            UUID uomCategoryIdUUID = UUID.randomUUID();
            UpdateUomCategoryViewRequest updateUomCategoryViewRequest = new UpdateUomCategoryViewRequest()
                    .id(uomCategoryIdUUID)
                    .name("Nouvelle Unite")
                    .active(true);

            when(uomCategoryDomainServiceRestApiAdapter.updateUomCategory(uomCategoryIdUUID, updateUomCategoryViewRequest))
                    .thenThrow(UomCategoryNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[] {new String[] {uomCategoryIdUUID.toString()}}));

            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UpdateUomCategoryViewRequest> updateUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateUomCategoryViewRequest.class);

            //Act + Then
            mockMvc.perform(put(UOM_CATEGORY_PATH_URI + "/" + uomCategoryIdUUID.toString())
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(updateUomCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomCategoryIdUUID.toString())));


            verify(uomCategoryDomainServiceRestApiAdapter, times(1))
                    .updateUomCategory(uuidArgumentCaptor.capture(), updateUomCategoryViewRequestArgumentCaptor.capture());

            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomCategoryIdUUID);
            assertThat(updateUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomCategoryViewRequest);
        }

        @Test
        void should_fail_when_update_uom_category_id_with_not_existing_parent_id() throws Exception {
            //Given
            UUID uomCategoryIdUUID = UUID.randomUUID();
            UpdateUomCategoryViewRequest updateUomCategoryViewRequest = new UpdateUomCategoryViewRequest()
                    .id(uomCategoryIdUUID)
                    .name("Nouvelle Unite")
                    .parentUomCategoryId(UUID.randomUUID())
                    .active(true);

            when(uomCategoryDomainServiceRestApiAdapter.updateUomCategory(uomCategoryIdUUID, updateUomCategoryViewRequest))
                    .thenThrow(UomParentCategoryNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[] {new String[] {uomCategoryIdUUID.toString()}}));

            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UpdateUomCategoryViewRequest> updateUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateUomCategoryViewRequest.class);

            //Act + Then
            mockMvc.perform(put(UOM_CATEGORY_PATH_URI + "/" + uomCategoryIdUUID.toString())
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(updateUomCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomCategoryIdUUID.toString())));


            verify(uomCategoryDomainServiceRestApiAdapter, times(1))
                    .updateUomCategory(uuidArgumentCaptor.capture(), updateUomCategoryViewRequestArgumentCaptor.capture());

            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomCategoryIdUUID);
            assertThat(updateUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomCategoryViewRequest);
        }

        @Test
        void should_fail_when_update_uom_category_id_with_duplicate_name() throws Exception {
            //Given
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String name = "Nouvelle Unite";
            UpdateUomCategoryViewRequest updateUomCategoryViewRequest = new UpdateUomCategoryViewRequest()
                    .id(uomCategoryIdUUID)
                    .name(name)
                    .parentUomCategoryId(UUID.randomUUID())
                    .active(true);

            when(uomCategoryDomainServiceRestApiAdapter.updateUomCategory(uomCategoryIdUUID, updateUomCategoryViewRequest))
                    .thenThrow(UomCategoryNameConflictException.class.getConstructor(Object[].class)
                            .newInstance(new Object[] {new String[] {name}}));

            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UpdateUomCategoryViewRequest> updateUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateUomCategoryViewRequest.class);

            //Act + Then
            mockMvc.perform(put(UOM_CATEGORY_PATH_URI + "/" + uomCategoryIdUUID.toString())
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(uomCategoryViewRequestToString(updateUomCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name)));


            verify(uomCategoryDomainServiceRestApiAdapter, times(1))
                    .updateUomCategory(uuidArgumentCaptor.capture(), updateUomCategoryViewRequestArgumentCaptor.capture());

            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomCategoryIdUUID);
            assertThat(updateUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomCategoryViewRequest);
        }
    }

    private String uomCategoryViewRequestToString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
