package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

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
