package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.*;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
final class UomRestAPIAdapterServiceTest {

    @InjectMocks
    private UomRestAPIAdapterService uomRestAPIAdapterService;
    @Mock
    private IUomDomainService uomDomainService;
    @Mock
    private UomRestViewMapper uomRestViewMapper;

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

        when(uomRestViewMapper.toUom(createUomViewRequest)).thenReturn(uomRequest);
        when(uomDomainService.createUom(uomRequest)).thenReturn(uomResponse);
        when(uomRestViewMapper.toCreateUomViewResponse(uomResponse)).thenReturn(createUomViewResponse);

        ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);
        ArgumentCaptor<Uom> createUomRequestArgumentCaptor = ArgumentCaptor.forClass(Uom.class);
        ArgumentCaptor<Uom> createUomResponseArgumentCaptor = ArgumentCaptor.forClass(Uom.class);

        //Act
        CreateUomViewResponse result = uomRestAPIAdapterService.createUom(createUomViewRequest);

        //Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(createUomViewResponse);

        verify(uomRestViewMapper, times(1)).toUom(createUomViewRequestArgumentCaptor.capture());
        assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

        verify(uomDomainService, times(1)).createUom(createUomRequestArgumentCaptor.capture());
        assertThat(createUomRequestArgumentCaptor.getValue()).isEqualTo(uomRequest);

        verify(uomRestViewMapper, times(1)).toCreateUomViewResponse(createUomResponseArgumentCaptor.capture());
        assertThat(createUomResponseArgumentCaptor.getValue()).isEqualTo(uomResponse);


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
                        "Ratio is required when unit of measure type is not reference."
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        0.8,
                        UomType.GREATER,
                        CreateUomViewRequest.UomTypeEnum.GREATER,
                        IllegalArgumentException.class,
                        "Ratio should be greater than 1 when unit of measure type is not greater."
                ),
                Arguments.of(
                        UUID.randomUUID(),
                        "Carton de 10",
                        2.0,
                        UomType.LOWER,
                        CreateUomViewRequest.UomTypeEnum.LOWER,
                        IllegalArgumentException.class,
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
            UomType uomType,
            CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
            Class< ? extends RuntimeException> exceptionClass,
            String exceptionMessage
    ) {
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, uomTypeEnumRequest, ratioRequest);

        Uom uomRequest = generateUom(name, uomCategoryId, uomType, ratioRequest);

        when(uomRestViewMapper.toUom(createUomViewRequest)).thenReturn(uomRequest);
        when(uomDomainService.createUom(uomRequest)).thenThrow(new IllegalArgumentException(exceptionMessage));

        assertThatThrownBy(() -> uomRestAPIAdapterService.createUom(createUomViewRequest))
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
        String exceptionMessage = "We can't have two units of measure with type reference in the same category";

        when(uomRestViewMapper.toUom(createUomViewRequest)).thenReturn(uom);
        when(uomDomainService.createUom(uom)).thenThrow(new UomException(exceptionMessage));

        //Act + Then
        assertThatThrownBy(() -> uomRestAPIAdapterService.createUom(createUomViewRequest))
                .isInstanceOf(UomException.class)
                .hasMessage(exceptionMessage);

        verify(uomRestViewMapper, times(1)).toUom(createUomViewRequest);
        verify(uomDomainService, times(1)).createUom(uom);

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
        String exceptionMessage = String.format("An unit of measure with the name '%s' already exists", name);

        when(uomRestViewMapper.toUom(createUomViewRequest)).thenReturn(uom);
        when(uomDomainService.createUom(uom)).thenThrow(new UomException(exceptionMessage));

        //Act + Then
        assertThatThrownBy(() -> uomRestAPIAdapterService.createUom(createUomViewRequest))
                .isInstanceOf(UomException.class)
                .hasMessage(exceptionMessage);

        verify(uomRestViewMapper, times(1)).toUom(createUomViewRequest);
        verify(uomDomainService, times(1)).createUom(uom);

    }



    private static Uom generateUom(String name, UUID uomCategoryId, UomType uomType, Double ratio) {
        return Uom.from(
                Name.of(name),
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
                Name.of(name),
                new UomCategoryId(uomCategoryId),
                UomType.valueOf(uomType.name()),
                Ratio.of(ratioResponse)
        );
        uom.initiate();
        return uom;
    }
}
