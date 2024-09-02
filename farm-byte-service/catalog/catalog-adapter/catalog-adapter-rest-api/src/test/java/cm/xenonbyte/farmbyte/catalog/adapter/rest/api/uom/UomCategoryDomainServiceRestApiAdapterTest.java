package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
final class UomCategoryDomainServiceRestApiAdapterTest {

    private UomCategoryServiceRestApiAdapter uomCategoryApiAdapterService;
    @Mock
    private UomCategoryViewMapper uomCategoryViewMapper;
    @Mock
    private UomCategoryService uomCategoryService;

    @BeforeEach
    void setUp() {
        uomCategoryApiAdapterService = new UomCategoryDomainServiceRestApiAdapter(
                uomCategoryService,
                uomCategoryViewMapper
        );
    }

    @Nested
    class CreateUomCategoryDomainServiceRestApiAdapterTest {
        static Stream<Arguments> createUomCategoryMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Unite",
                            null,
                            UUID.randomUUID()
                    ),
                    Arguments.of(
                            "Temps",
                            UUID.randomUUID(),
                            UUID.randomUUID()
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomCategoryMethodSourceArgs")
        void should_create_root_uom_category_or_child_category(
                String nameAsString,
                UUID parentUomCategoryUUID,
                UUID newUomCategoryUUID
        ) {
            //Given
            CreateUomCategoryViewRequest createUomCategoryViewRequest = new CreateUomCategoryViewRequest()
                    .name(nameAsString)
                    .parentUomCategoryId(parentUomCategoryUUID);
            UomCategory uomCategory = UomCategory.builder()
                    .name(Name.of(Text.of(nameAsString)))
                    .build();
            CreateUomCategoryViewResponse createUomCategoryViewResponse = new CreateUomCategoryViewResponse()
                    .id(newUomCategoryUUID)
                    .active(true)
                    .parentUomCategoryId(parentUomCategoryUUID)
                    .name(nameAsString);

            when(uomCategoryViewMapper.toUomCategory(createUomCategoryViewRequest)).thenReturn(uomCategory);
            when(uomCategoryService.createUomCategory(uomCategory)).thenReturn(uomCategory);
            when(uomCategoryViewMapper.toCreateUomCategoryViewResponse(uomCategory)).thenReturn(createUomCategoryViewResponse);

            ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);
            ArgumentCaptor<UomCategory> uomCategoryArgumentCaptor = ArgumentCaptor.forClass(UomCategory.class);

            //Act
            CreateUomCategoryViewResponse result = uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(createUomCategoryViewResponse);

            verify(uomCategoryViewMapper, times(1))
                    .toUomCategory(createUomCategoryViewRequestArgumentCaptor.capture());
            assertThat(createUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createUomCategoryViewRequest);

            verify(uomCategoryService, times(1))
                    .createUomCategory(uomCategoryArgumentCaptor.capture());
            assertThat(uomCategoryArgumentCaptor.getValue()).isEqualTo(uomCategory);

            verify(uomCategoryViewMapper, times(1))
                    .toCreateUomCategoryViewResponse(uomCategoryArgumentCaptor.capture());
            assertThat(uomCategoryArgumentCaptor.getValue()).isEqualTo(uomCategory);
        }

        static Stream<Arguments> createUomCategoryThrowExceptionMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Unite",
                            null,
                            new UomCategoryNameConflictException(new String[]{UOM_CATEGORY_NAME_CONFLICT_EXCEPTION}),
                            UOM_CATEGORY_NAME_CONFLICT_EXCEPTION
                    ),
                    Arguments.of(
                            "Temps",
                            UUID.randomUUID(),
                            new UomParentCategoryNotFoundException(new String[]{UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION}),
                            UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomCategoryThrowExceptionMethodSourceArgs")
        void should_throw_exception_when_create_uom_category(
                String nameAsString,
                UUID parentUomCategoryUUID,
                RuntimeException exceptionClass,
                String exceptionMessage
        ) {

            //Given
            CreateUomCategoryViewRequest createUomCategoryViewRequest = new CreateUomCategoryViewRequest()
                    .name(nameAsString)
                    .parentUomCategoryId(parentUomCategoryUUID);
            UomCategory uomCategory = UomCategory.builder()
                    .name(Name.of(Text.of(nameAsString)))
                    .build();

            when(uomCategoryViewMapper.toUomCategory(createUomCategoryViewRequest)).thenReturn(uomCategory);
            when(uomCategoryService.createUomCategory(uomCategory)).thenThrow(exceptionClass);

            ArgumentCaptor<CreateUomCategoryViewRequest> createUomCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateUomCategoryViewRequest.class);
            ArgumentCaptor<UomCategory> uomCategoryArgumentCaptor = ArgumentCaptor.forClass(UomCategory.class);

            //Act
            assertThatThrownBy(() -> uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest))
                    .isInstanceOf(exceptionClass.getClass())
                    .hasMessage(exceptionMessage);

            verify(uomCategoryViewMapper, times(1))
                    .toUomCategory(createUomCategoryViewRequestArgumentCaptor.capture());
            assertThat(createUomCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createUomCategoryViewRequest);

            verify(uomCategoryService, times(1))
                    .createUomCategory(uomCategoryArgumentCaptor.capture());
            assertThat(uomCategoryArgumentCaptor.getValue()).isEqualTo(uomCategory);

        }

    }

    @Nested
    class FindUomCategoryByIdDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_uom_category_with_existing_id() {
            //Given
            UUID uomCategoryIdUUID = UUID.fromString("0191ab3c-1a4b-7202-935e-4eb5d7710981");
            UomCategoryId uomCategoryId = new UomCategoryId(uomCategoryIdUUID);
            String name = "Unite";
            UomCategory uomCategory = UomCategory.builder()
                    .name(Name.of(Text.of(name)))
                    .id(new UomCategoryId(uomCategoryIdUUID))
                    .build();

            FindUomCategoryByIdViewResponse findUomCategoryByIdViewResponse = new FindUomCategoryByIdViewResponse()
                    .id(uomCategoryIdUUID)
                    .name(name);

            when(uomCategoryService.findUomCategoryById(uomCategoryId)).thenReturn(uomCategory);
            when(uomCategoryViewMapper.toFindUomCategoryViewResponse(uomCategory)).thenReturn(findUomCategoryByIdViewResponse);

            ArgumentCaptor<UomCategoryId> uomCategoryIdArgumentCaptor = ArgumentCaptor.forClass(UomCategoryId.class);
            ArgumentCaptor<UomCategory> uomCategoryArgumentCaptor = ArgumentCaptor.forClass(UomCategory.class);

            //Act
            FindUomCategoryByIdViewResponse result = uomCategoryApiAdapterService.findUomCategoryById(uomCategoryIdUUID);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findUomCategoryByIdViewResponse);

            verify(uomCategoryService, times(1)).findUomCategoryById(uomCategoryIdArgumentCaptor.capture());
            verify(uomCategoryViewMapper, times(1)).toFindUomCategoryViewResponse(uomCategoryArgumentCaptor.capture());

            assertThat(uomCategoryIdArgumentCaptor.getValue()).isEqualTo(uomCategoryId);
            assertThat(uomCategoryArgumentCaptor.getValue()).isEqualTo(uomCategory);


        }

        @Test
        void should_fail_when_find_uom_category_with_non_existing_id() {
            //Given
            UUID uomCategoryIdUUID = UUID.randomUUID();
            UomCategoryId uomCategoryId = new UomCategoryId(uomCategoryIdUUID);

            when(uomCategoryService.findUomCategoryById(uomCategoryId)).thenThrow(UomCategoryNotFoundException.class);
            ArgumentCaptor<UomCategoryId> uomCategoryIdArgumentCaptor = ArgumentCaptor.forClass(UomCategoryId.class);

            //Act + Then
            assertThatThrownBy(() -> uomCategoryApiAdapterService.findUomCategoryById(uomCategoryIdUUID))
                    .isInstanceOf(UomCategoryNotFoundException.class);

            verify(uomCategoryService, times(1)).findUomCategoryById(uomCategoryIdArgumentCaptor.capture());

            assertThat(uomCategoryIdArgumentCaptor.getValue()).isEqualTo(uomCategoryId);
        }
    }

    @Nested
    class FindUomCategoriesDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_uom_category_with_existing_id() {
            //Given

            int page = 1;
            int pageSize = 2;
            String attribute = "name";

            PageInfo<UomCategory> uomCategoriesPageInfo =  new PageInfo<UomCategory>().with(
                    page, pageSize,
                    List.of(
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Unite")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Volume")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Distance")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Poids")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Temps")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build()
                    )
            );

            FindUomCategoriesPageInfoViewResponse findCategoriesPageInfoViewResponse = new FindUomCategoriesPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new FindUomCategoriesViewResponse()
                                            .name("Unite")
                                            .id(UUID.randomUUID()),
                                    new FindUomCategoriesViewResponse()
                                            .name("Volume")
                                            .id(UUID.randomUUID()),
                                    new FindUomCategoriesViewResponse()
                                            .name("Distance")
                                            .id(UUID.randomUUID()),
                                    new FindUomCategoriesViewResponse()
                                            .name("Poids")
                                            .id(UUID.randomUUID()),
                                    new FindUomCategoriesViewResponse()
                                            .name("Temps")
                                            .id(UUID.randomUUID())
                            )
                    );


            when(uomCategoryService.findUomCategories(page, pageSize, "name", Direction.ASC)).thenReturn(uomCategoriesPageInfo);
            when(uomCategoryViewMapper.toFindUomCategoriesPageInfoViewResponse(uomCategoriesPageInfo)).thenReturn(findCategoriesPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);

            //Act
            FindUomCategoriesPageInfoViewResponse result = uomCategoryApiAdapterService.findUomCategories(page, pageSize, attribute, "ASC");

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findCategoriesPageInfoViewResponse);

            verify(uomCategoryService, times(1))
                    .findUomCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(uomCategoryViewMapper, times(1)).toFindUomCategoriesPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(uomCategoriesPageInfo);


        }

    }

    @Nested
    class SearchUomCategoriesDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_search_uom_category_with_existing_keyword() {
            //Given

            int page = 1;
            int pageSize = 2;
            String keyword = "e";
            String attribute = "name";

            PageInfo<UomCategory> uomCategoriesPageInfo =  new PageInfo<UomCategory>().with(
                    1, 2,
                    List.of(
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Unite")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Volume")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Distance")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            UomCategory.builder()
                                    .name(Name.of(Text.of("Temps")))
                                    .id(new UomCategoryId(UUID.randomUUID()))
                                    .build()
                    )
            );

            SearchUomCategoriesPageInfoViewResponse searchCategoriesPageInfoViewResponse = new SearchUomCategoriesPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new SearchUomCategoriesViewResponse()
                                            .name("Unite")
                                            .id(UUID.randomUUID()),
                                    new SearchUomCategoriesViewResponse()
                                            .name("Volume")
                                            .id(UUID.randomUUID()),
                                    new SearchUomCategoriesViewResponse()
                                            .name("Distance")
                                            .id(UUID.randomUUID()),
                                    new SearchUomCategoriesViewResponse()
                                            .name("Poids")
                                            .id(UUID.randomUUID()),
                                    new SearchUomCategoriesViewResponse()
                                            .name("Temps")
                                            .id(UUID.randomUUID())
                            )
                    );


            when(uomCategoryService.searchUomCategories(page, pageSize, "name", Direction.ASC, Keyword.of(Text.of(keyword)))).thenReturn(uomCategoriesPageInfo);
            when(uomCategoryViewMapper.toSearchUomCategoriesPageInfoViewResponse(uomCategoriesPageInfo)).thenReturn(searchCategoriesPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchUomCategoriesPageInfoViewResponse result = uomCategoryApiAdapterService.searchUomCategories(page, pageSize, attribute, "ASC", keyword);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(searchCategoriesPageInfoViewResponse);
            verify(uomCategoryService, times(1))
                    .searchUomCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());
            verify(uomCategoryViewMapper, times(1)).toSearchUomCategoriesPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(keywordArgumentCaptor.getValue().getText().getValue()).isEqualTo(keyword);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(uomCategoriesPageInfo);


        }

    }

}
