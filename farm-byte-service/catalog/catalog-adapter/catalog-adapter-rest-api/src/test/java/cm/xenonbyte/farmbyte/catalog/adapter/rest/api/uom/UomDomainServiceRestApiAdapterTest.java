package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomReferenceConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Ratio;
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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_RATIO_IS_REQUIRED_WHEN_TYPE_IS_REFERENCE;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_RATIO_MUST_BE_GREATER_THANT_ONE_WHEN_UOM_TYPE_IS_GREATER;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_RATIO_MUST_BE_LOWER_THANT_ONE_WHEN_UOM_TYPE_IS_LOWER;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
final class UomDomainServiceRestApiAdapterTest {

    private UomServiceRestApiAdapter uomServiceRestApiAdapter;
    @Mock
    private UomService uomService;
    @Mock
    private UomViewMapper uomViewMapper;

    @BeforeEach
    void setUp() {
        uomServiceRestApiAdapter = new UomDomainServiceRestApiAdapter(uomService, uomViewMapper);
    }

    @Nested
    class CreateUomDomainServiceRestApiAdapterTest {

        static Stream<Arguments> createUomMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.randomUUID(),
                            "Unite",
                            null,
                            1.0,
                            UomType.REFERENCE,
                            CreateUomViewRequest.UomTypeEnum.REFERENCE,
                            CreateUomViewResponse.UomTypeEnum.REFERENCE
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 5",
                            5.0,
                            5.0,
                            UomType.GREATER,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            CreateUomViewResponse.UomTypeEnum.GREATER
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Centimetre",
                            0.1,
                            0.1,
                            UomType.LOWER,
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
                UomType uomType,
                CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
                CreateUomViewResponse.UomTypeEnum uomTypeEnumResponse
                ) {

            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, uomTypeEnumRequest, ratioRequest);
            Uom uomRequest = generateUom(name, uomCategoryId, uomType, ratioRequest);
            CreateUomViewResponse createUomViewResponse = generateCreateUomViewResponse(uomCategoryId, name, uomTypeEnumResponse, ratioResponse);
            Uom uomResponse = generateCreateUomResponse(name, uomCategoryId, uomType, ratioResponse);

            when(uomViewMapper.toUom(createUomViewRequest)).thenReturn(uomRequest);
            when(uomService.createUom(uomRequest)).thenReturn(uomResponse);
            when(uomViewMapper.toCreateUomViewResponse(uomResponse)).thenReturn(createUomViewResponse);

            ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);


            //Act
            CreateUomViewResponse result = uomServiceRestApiAdapter.createUom(createUomViewRequest);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(createUomViewResponse);

            verify(uomViewMapper, times(1)).toUom(createUomViewRequestArgumentCaptor.capture());
            assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

            verify(uomService, times(1)).createUom(uomArgumentCaptor.capture());
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uomRequest);

            verify(uomViewMapper, times(1)).toCreateUomViewResponse(uomArgumentCaptor.capture());
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uomResponse);


        }

        static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.randomUUID(),
                            "Unit",
                            null,
                            UomType.GREATER,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            IllegalArgumentException.class,
                            UOM_RATIO_IS_REQUIRED_WHEN_TYPE_IS_REFERENCE
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 10",
                            0.8,
                            UomType.GREATER,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            IllegalArgumentException.class,
                            UOM_RATIO_MUST_BE_GREATER_THANT_ONE_WHEN_UOM_TYPE_IS_GREATER
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 10",
                            2.0,
                            UomType.LOWER,
                            CreateUomViewRequest.UomTypeEnum.LOWER,
                            IllegalArgumentException.class,
                            UOM_RATIO_MUST_BE_LOWER_THANT_ONE_WHEN_UOM_TYPE_IS_LOWER
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomThrowExceptionMethodSourceArgs")
        void should_throw_exception_when_create_uom_with_type_and_ratio_are_not_compatible(
                UUID uomCategoryId,
                String name,
                Double ratioRequest,
                UomType uomType,
                CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
                Class< ? extends RuntimeException> exceptionClass,
                String exceptionMessage
                ) {
                CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, uomTypeEnumRequest, ratioRequest);

                Uom uomRequest = generateUom(name, uomCategoryId, uomType, ratioRequest);

                when(uomViewMapper.toUom(createUomViewRequest)).thenReturn(uomRequest);
                when(uomService.createUom(uomRequest)).thenThrow(new IllegalArgumentException(exceptionMessage));

                assertThatThrownBy(() -> uomServiceRestApiAdapter.createUom(createUomViewRequest))
                        .isInstanceOf(exceptionClass)
                        .hasMessageContaining(exceptionMessage);
        }

        @Test
        void should_throw_exception_when_create_two_uom_with_uom_type_as_reference_for_the_same_category() {
            //Given
            String name = "Unite";
            UUID uomCategoryId = UUID.randomUUID();
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    CreateUomViewRequest.UomTypeEnum.REFERENCE,
                    null
            );

            Uom uom = generateUom(name, uomCategoryId, UomType.REFERENCE, null);

            when(uomViewMapper.toUom(createUomViewRequest)).thenReturn(uom);
            when(uomService.createUom(uom)).thenThrow(new UomReferenceConflictException());

            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.createUom(createUomViewRequest))
                    .isInstanceOf(UomReferenceConflictException.class)
                    .hasMessage(UOM_REFERENCE_CONFLICT_CATEGORY);

            verify(uomViewMapper, times(1)).toUom(createUomViewRequest);
            verify(uomService, times(1)).createUom(uom);

        }

        @Test
        void should_throw_exception_when_create_two_uom_with_same_name() {
            //Given
            String name = "Unite";
            UUID uomCategoryId = UUID.randomUUID();
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    CreateUomViewRequest.UomTypeEnum.REFERENCE,
                    null
            );

            Uom uom = generateUom(name, uomCategoryId, UomType.REFERENCE, null);
            String exceptionMessage = UOM_NAME_CONFLICT_EXCEPTION;

            when(uomViewMapper.toUom(createUomViewRequest)).thenReturn(uom);
            when(uomService.createUom(uom)).thenThrow(new UomNameConflictException(new Object[]{uom.getName().getText()}));

            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.createUom(createUomViewRequest))
                    .isInstanceOf(UomNameConflictException.class)
                    .hasMessage(exceptionMessage);

            verify(uomViewMapper, times(1)).toUom(createUomViewRequest);
            verify(uomService, times(1)).createUom(uom);

        }
    }

    @Nested
    class FindUomByIdDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_uom_by_existing_uom_id() {
            //Given
            UUID uomIdUUID = UUID.fromString("0191b388-5a00-7c69-9700-92c07fa9bf17");
            String name = "Heure";
            UUID uomCategoryId = UUID.randomUUID();
            UomType uomType = UomType.REFERENCE;
            double ratio = 1D;
            Uom uom = Uom.builder()
                    .id(new UomId(uomIdUUID))
                    .name(Name.of(Text.of(name)))
                    .uomCategoryId(new UomCategoryId(uomCategoryId))
                    .uomType(uomType)
                    .ratio(Ratio.of(ratio))
                    .build();
            FindUomByIdViewResponse findUomByIdViewResponse = new FindUomByIdViewResponse()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryId)
                    .ratio(ratio)
                    .uomType(FindUomByIdViewResponse.UomTypeEnum.valueOf(uomType.name()));

            when(uomService.findUomById(new UomId(uomIdUUID))).thenReturn(uom);
            when(uomViewMapper.toFindUomByIdViewResponse(uom)).thenReturn(findUomByIdViewResponse);

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);

            //Act
            FindUomByIdViewResponse result = uomServiceRestApiAdapter.findUomById(uomIdUUID);

            //Then
            assertThat(result).isNotNull().isEqualTo(findUomByIdViewResponse);
            verify(uomService, times(1)).findUomById(uomIdArgumentCaptor.capture());
            verify(uomViewMapper, times(1)).toFindUomByIdViewResponse(uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue().getValue()).isEqualTo(uomIdUUID);
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uom);


        }

        @Test
        void should_fail_when_find_uom_by_non_existing_id() {
            //Given
            UUID uomIdUUID = UUID.fromString("0191b388-5a00-7c69-9700-92c07fa9bf17");
            UomId uomId = new UomId(uomIdUUID);

            when(uomService.findUomById(new UomId(uomIdUUID))).thenThrow(UomNotFoundException.class);
            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);

            //Act
            assertThatThrownBy(() -> uomServiceRestApiAdapter.findUomById(uomIdUUID))
                    .isInstanceOf(UomNotFoundException.class);

            verify(uomService, times(1)).findUomById(uomIdArgumentCaptor.capture());
            assertThat(uomIdArgumentCaptor.getValue().getValue()).isEqualTo(uomIdUUID);
        }
    }

    @Nested
    class FindUomsDomainServiceRestApiAdapterTest {
        @Test
        void should_success_when_find_uoms() {
            //Given
            int page = 0;
            int pageSize = 2;
            String attribute = "name";

            PageInfo<Uom> uomPageInfo = new PageInfo<Uom>().with(
                    page, pageSize,
                    List.of(
                        Uom.builder()
                                .id(new UomId(UUID.randomUUID()))
                                .uomType(UomType.REFERENCE)
                                .name(Name.of(Text.of("piece")))
                                .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                .build(),
                        Uom.builder()
                                .id(new UomId(UUID.randomUUID()))
                                .uomType(UomType.REFERENCE)
                                .name(Name.of(Text.of("metre")))
                                .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                .build(),
                        Uom.builder()
                                .id(new UomId(UUID.randomUUID()))
                                .uomType(UomType.REFERENCE)
                                .name(Name.of(Text.of("heure")))
                                .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                .build(),
                        Uom.builder()
                                .id(new UomId(UUID.randomUUID()))
                                .uomType(UomType.REFERENCE)
                                .name(Name.of(Text.of("litre")))
                                .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                .build(),
                        Uom.builder()
                                .id(new UomId(UUID.randomUUID()))
                                .uomType(UomType.REFERENCE)
                                .name(Name.of(Text.of("gramme")))
                                .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                .build()
                    )
            );
            FindUomsPageInfoViewResponse findUomPageInfoViewResponse = new FindUomsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .elements(
                            List.of(
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("piece")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("metre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("heure")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("litre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("gramme")
                                            .uomCategoryId(UUID.randomUUID())
                            )
                    );

            when(uomService.findUoms(page, pageSize, "name", Direction.ASC)).thenReturn(uomPageInfo);
            when(uomViewMapper.toFindUomsPageInfoViewResponse(uomPageInfo)).thenReturn(findUomPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);

            //Act
            FindUomsPageInfoViewResponse result = uomServiceRestApiAdapter.findUoms(page, pageSize, attribute, "ASC");

            //Then
            assertThat(result).isNotNull().isEqualTo(findUomPageInfoViewResponse);

            verify(uomService, times(1))
                    .findUoms(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(uomViewMapper, times(1)).toFindUomsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(uomPageInfo);

        }
    }

    @Nested
    class SearchUomDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_search_uom_with_existing_keyword() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String keyword = "r";

            PageInfo<Uom> uomPageInfo = new PageInfo<Uom>().with(
                    page, size,
                    List.of(
                            Uom.builder()
                                    .id(new UomId(UUID.randomUUID()))
                                    .uomType(UomType.REFERENCE)
                                    .name(Name.of(Text.of("piece")))
                                    .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            Uom.builder()
                                    .id(new UomId(UUID.randomUUID()))
                                    .uomType(UomType.REFERENCE)
                                    .name(Name.of(Text.of("metre")))
                                    .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            Uom.builder()
                                    .id(new UomId(UUID.randomUUID()))
                                    .uomType(UomType.REFERENCE)
                                    .name(Name.of(Text.of("heure")))
                                    .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            Uom.builder()
                                    .id(new UomId(UUID.randomUUID()))
                                    .uomType(UomType.REFERENCE)
                                    .name(Name.of(Text.of("litre")))
                                    .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                    .build(),
                            Uom.builder()
                                    .id(new UomId(UUID.randomUUID()))
                                    .uomType(UomType.REFERENCE)
                                    .name(Name.of(Text.of("gramme")))
                                    .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                                    .build()
                    )
            );
            SearchUomsPageInfoViewResponse searchUomPageInfoViewResponse = new SearchUomsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .elements(
                            List.of(
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("metre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("heure")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("litre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("gramme")
                                            .uomCategoryId(UUID.randomUUID())
                            )
                    );

            when(uomService.searchUoms(page, size, "name", Direction.ASC, Keyword.of(Text.of(keyword)))).thenReturn(uomPageInfo);
            when(uomViewMapper.toSearchUomsPageInfoViewResponse(uomPageInfo)).thenReturn(searchUomPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchUomsPageInfoViewResponse result = uomServiceRestApiAdapter.searchUoms(page, size, attribute, "ASC", keyword);

            //Then
            assertThat(result).isNotNull().isEqualTo(searchUomPageInfoViewResponse);

            verify(uomService, times(1))
                    .searchUoms(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());

            verify(uomViewMapper, times(1)).toSearchUomsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(uomPageInfo);

        }
    }

    @Nested
    class UpdatedUomDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_update_uom_by_id() {
            //Given
            UUID uomIdUUID = UUID.randomUUID();
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String uomType = "REFERENCE";
            String name = "Nouvelle Unite";
            boolean active = true;
            double ratio = 1.0;
            UomId uomId = new UomId(uomIdUUID);

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);

            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of(name)))
                    .uomType(UomType.valueOf(uomType))
                    .uomCategoryId(new UomCategoryId(uomCategoryIdUUID))
                    .ratio(Ratio.of(ratio))
                    .active(Active.with(active))
                    .build();

            UpdateUomViewResponse updateUomViewResponse = new UpdateUomViewResponse()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewResponse.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);
            when(uomViewMapper.toUom(updateUomViewRequest)).thenReturn(uom);
            when(uomService.updateUom(uomId, uom)).thenReturn(uom);
            when(uomViewMapper.toUpdateUomViewResponse(uom)).thenReturn(updateUomViewResponse);

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
            ArgumentCaptor<UpdateUomViewRequest> updateUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateUomViewRequest.class);

            //Act
            UpdateUomViewResponse result = uomServiceRestApiAdapter.updateUom(uomIdUUID, updateUomViewRequest);

            //Then
            assertThat(result).isNotNull().isEqualTo(updateUomViewResponse);

            verify(uomViewMapper, times(1)).toUom(updateUomViewRequestArgumentCaptor.capture());
            verify(uomService, times(1)).updateUom(uomIdArgumentCaptor.capture(), uomArgumentCaptor.capture());
            verify(uomViewMapper, times(1)).toUpdateUomViewResponse(uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue()).isEqualTo(uomId);
            assertThat(updateUomViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomViewRequest);
            assertThat(uomArgumentCaptor.getAllValues().getFirst()).isEqualTo(uom);
            assertThat(uomArgumentCaptor.getAllValues().get(1)).isEqualTo(uom);
        }

        @Test
        void should_fail_when_update_uom_with_not_existing_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID uomIdUUID = UUID.randomUUID();
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String uomType = "REFERENCE";
            String name = "Nouvelle Unite";
            boolean active = true;
            double ratio = 1.0;
            UomId uomId = new UomId(uomIdUUID);

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);

            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of(name)))
                    .uomType(UomType.valueOf(uomType))
                    .uomCategoryId(new UomCategoryId(uomCategoryIdUUID))
                    .ratio(Ratio.of(ratio))
                    .active(Active.with(active))
                    .build();

            when(uomViewMapper.toUom(updateUomViewRequest)).thenReturn(uom);
            when(uomService.updateUom(uomId, uom))
                    .thenThrow(UomNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{uomIdUUID.toString()}}));

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
            ArgumentCaptor<UpdateUomViewRequest> updateUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateUomViewRequest.class);


            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.updateUom(uomIdUUID, updateUomViewRequest))
                    .isInstanceOf(UomNotFoundException.class)
                    .hasMessage(UOM_NOT_FOUND_EXCEPTION);

            verify(uomViewMapper, times(1)).toUom(updateUomViewRequestArgumentCaptor.capture());
            verify(uomService, times(1)).updateUom(uomIdArgumentCaptor.capture(), uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue()).isEqualTo(uomId);
            assertThat(updateUomViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomViewRequest);
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uom);

        }

        @Test
        void should_fail_when_update_uom_with_not_existing_uom_category_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID uomIdUUID = UUID.randomUUID();
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String uomType = "REFERENCE";
            String name = "Nouvelle Unite";
            boolean active = true;
            double ratio = 1.0;
            UomId uomId = new UomId(uomIdUUID);

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);

            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of(name)))
                    .uomType(UomType.valueOf(uomType))
                    .uomCategoryId(new UomCategoryId(uomCategoryIdUUID))
                    .ratio(Ratio.of(ratio))
                    .active(Active.with(active))
                    .build();

            when(uomViewMapper.toUom(updateUomViewRequest)).thenReturn(uom);
            when(uomService.updateUom(uomId, uom))
                    .thenThrow(UomCategoryNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{uomCategoryIdUUID.toString()}}));

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
            ArgumentCaptor<UpdateUomViewRequest> updateUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateUomViewRequest.class);


            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.updateUom(uomIdUUID, updateUomViewRequest))
                    .isInstanceOf(UomCategoryNotFoundException.class)
                    .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);

            verify(uomViewMapper, times(1)).toUom(updateUomViewRequestArgumentCaptor.capture());
            verify(uomService, times(1)).updateUom(uomIdArgumentCaptor.capture(), uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue()).isEqualTo(uomId);
            assertThat(updateUomViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomViewRequest);
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uom);

        }

        @Test
        void should_fail_when_update_uom_with_duplicate_name() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID uomIdUUID = UUID.randomUUID();
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String uomType = "REFERENCE";
            String name = "Nouvelle Unite";
            boolean active = true;
            double ratio = 1.0;
            UomId uomId = new UomId(uomIdUUID);

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);

            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of(name)))
                    .uomType(UomType.valueOf(uomType))
                    .uomCategoryId(new UomCategoryId(uomCategoryIdUUID))
                    .ratio(Ratio.of(ratio))
                    .active(Active.with(active))
                    .build();

            when(uomViewMapper.toUom(updateUomViewRequest)).thenReturn(uom);
            when(uomService.updateUom(uomId, uom))
                    .thenThrow(UomNameConflictException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
            ArgumentCaptor<UpdateUomViewRequest> updateUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateUomViewRequest.class);


            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.updateUom(uomIdUUID, updateUomViewRequest))
                    .isInstanceOf(UomNameConflictException.class)
                    .hasMessage(UOM_NAME_CONFLICT_EXCEPTION);

            verify(uomViewMapper, times(1)).toUom(updateUomViewRequestArgumentCaptor.capture());
            verify(uomService, times(1)).updateUom(uomIdArgumentCaptor.capture(), uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue()).isEqualTo(uomId);
            assertThat(updateUomViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomViewRequest);
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uom);
        }

        @Test
        void should_fail_when_update_uom_with_duplicate_reference_and_category() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID uomIdUUID = UUID.randomUUID();
            UUID uomCategoryIdUUID = UUID.randomUUID();
            String uomType = "REFERENCE";
            String name = "Nouvelle Unite";
            boolean active = true;
            double ratio = 1.0;
            UomId uomId = new UomId(uomIdUUID);

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);

            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of(name)))
                    .uomType(UomType.valueOf(uomType))
                    .uomCategoryId(new UomCategoryId(uomCategoryIdUUID))
                    .ratio(Ratio.of(ratio))
                    .active(Active.with(active))
                    .build();

            when(uomViewMapper.toUom(updateUomViewRequest)).thenReturn(uom);
            when(uomService.updateUom(uomId, uom))
                    .thenThrow(UomReferenceConflictException.class.getConstructor().newInstance());

            ArgumentCaptor<UomId> uomIdArgumentCaptor = ArgumentCaptor.forClass(UomId.class);
            ArgumentCaptor<Uom> uomArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
            ArgumentCaptor<UpdateUomViewRequest> updateUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateUomViewRequest.class);


            //Act + Then
            assertThatThrownBy(() -> uomServiceRestApiAdapter.updateUom(uomIdUUID, updateUomViewRequest))
                    .isInstanceOf(UomReferenceConflictException.class)
                    .hasMessage(UOM_REFERENCE_CONFLICT_CATEGORY);

            verify(uomViewMapper, times(1)).toUom(updateUomViewRequestArgumentCaptor.capture());
            verify(uomService, times(1)).updateUom(uomIdArgumentCaptor.capture(), uomArgumentCaptor.capture());

            assertThat(uomIdArgumentCaptor.getValue()).isEqualTo(uomId);
            assertThat(updateUomViewRequestArgumentCaptor.getValue()).isEqualTo(updateUomViewRequest);
            assertThat(uomArgumentCaptor.getValue()).isEqualTo(uom);

        }
    }



    private static Uom generateUom(String name, UUID uomCategoryId, UomType uomType, Double ratio) {
        return Uom.from(
                Name.of(Text.of(name)),
                new UomCategoryId(uomCategoryId),
                uomType,
                ratio == null? null: Ratio.of(ratio)
        );
    }


    private static CreateUomViewResponse generateCreateUomViewResponse(
            UUID uomCategoryId,
            String name,
            CreateUomViewResponse.UomTypeEnum uomTypeEnum,
            Double ratio) {
        return new CreateUomViewResponse()
                .id(UUID.randomUUID())
                .uomCategoryId(uomCategoryId)
                .uomType(uomTypeEnum)
                .name(name)
                .ratio(ratio)
                .active(true);
    }

    private static CreateUomViewRequest generateCreateUomViewRequest(
            UUID uomCategoryId,
            String name, CreateUomViewRequest.UomTypeEnum uomTypeEnum,
            Double ratio) {
        return new CreateUomViewRequest()
                .uomCategoryId(uomCategoryId)
                .name(name)
                .uomType(uomTypeEnum)
                .ratio(ratio);
    }


    private static Uom generateCreateUomResponse(String name, UUID uomCategoryId, UomType uomType, Double ratioResponse) {
        Uom uom = Uom.from(
                Name.of(Text.of(name)),
                new UomCategoryId(uomCategoryId),
                UomType.valueOf(uomType.name()),
                Ratio.of(ratioResponse)
        );
        uom.initiate();
        return uom;
    }
}
