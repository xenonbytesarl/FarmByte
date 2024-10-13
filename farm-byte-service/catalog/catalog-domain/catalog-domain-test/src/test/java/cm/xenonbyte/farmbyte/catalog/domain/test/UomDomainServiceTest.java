package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomCategoryInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
final class UomDomainServiceTest {


    private UomService uomDomainService;
    private UomCategoryId uomCategoryId;
    private UomRepository uomRepository;
    private UomCategoryRepository uomCategoryRepository;

    @BeforeEach
    void setUp() {
        uomCategoryRepository = new UomCategoryInMemoryRepositoryAdapter();
        uomRepository = new UomInMemoryRepositoryAdapter();
        uomDomainService = new UomDomainService(uomRepository, uomCategoryRepository);

        uomCategoryId = new UomCategoryId(UUID.randomUUID());
        uomCategoryRepository.save(UomCategory.builder()
                .id(uomCategoryId)
                .name(Name.of(Text.of("Root")))
                .build());

    }

    @Nested
    class CreateUomDomainServiceTest {
        static Stream<Arguments> createUomMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            Name.of(Text.of("Unit")),
                            UomType.REFERENCE,
                            null
                    ),
                    Arguments.of(
                            Name.of(Text.of("Carton de 10")),
                            UomType.GREATER,
                            Ratio.of(2.0)
                    ),
                    Arguments.of(
                            Name.of(Text.of("Centimetre")),
                            UomType.LOWER,
                            Ratio.of(0.1)
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomMethodSourceArgs")
        void should_create_uom_when_uom_type_is_reference(
                Name name,
                UomType uomType,
                Ratio ratio
        ) {
            //Given
            Uom uom = Uom.from(
                    name,
                    uomCategoryId,
                    uomType,
                    ratio
            );

            //Act
            Uom createdUom = uomDomainService.createUom(uom);

            //Then
            assertThat(createdUom).isNotNull();
            assertThat(createdUom.getId())
                    .isNotNull()
                    .satisfies(result -> assertThat(result.getValue()).isInstanceOf(UUID.class));
            assertThat(createdUom.getUomCategoryId())
                    .isNotNull()
                    .isEqualTo(uomCategoryId);
            assertThat(createdUom.getUomType()).isEqualTo(uomType);
            assertThat(createdUom.getActive()).isEqualTo(Active.with(true));
            assertThat(createdUom.getRatio()).isEqualTo(ratio == null ? Ratio.of( Ratio.REFERENCE) : ratio);
            assertThat(createdUom.getName()).isEqualTo(name);
        }

        static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            Name.of(Text.of("Unit")),
                            UomType.GREATER,
                            null,
                            IllegalArgumentException.class,
                            "Uom.1"
                    ),
                    Arguments.of(
                            Name.of(Text.of("Carton de 10")),
                            UomType.GREATER,
                            Ratio.of(0.8),
                            IllegalArgumentException.class,
                            "Uom.2"
                    ),
                    Arguments.of(
                            Name.of(Text.of("Centimetre")),
                            UomType.LOWER,
                            Ratio.of(2.0),
                            IllegalArgumentException.class,
                            "Uom.3"
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomThrowExceptionMethodSourceArgs")
        void should_throw_exception_when_create_uom_with_uom_type_and_ratio_are_not_compatible(
                Name name,
                UomType uomType,
                Ratio ratio,
                Class< ? extends RuntimeException> exceptionClass,
                String exceptionMessage
        ) {
            //Given
            Uom uom = Uom.from(
                    name,
                    uomCategoryId,
                    uomType,
                    ratio
            );

            //Act + Then
            assertThatThrownBy(() -> uomDomainService.createUom(uom))
                    .isInstanceOf(exceptionClass)
                    .hasMessage(exceptionMessage);

        }

        @Test
        void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() {
            //Given
            Uom firstRefereceUom = Uom.from(
                    Name.of(Text.of("Carton de 10")),
                    uomCategoryId,
                    UomType.GREATER,
                    Ratio.of(2.0));

            uomDomainService.createUom(firstRefereceUom);

            Uom secondRefereceUom = Uom.from(
                    Name.of(Text.of("Carton de 50")),
                    uomCategoryId,
                    UomType.GREATER,
                    Ratio.of(50.0));
            //Act
            Uom result = uomDomainService.createUom(secondRefereceUom);

            assertThat(result).isNotNull();
            //Then
        }

        @Test
        void should_throw_exception_when_create_two_uom_with_same_name() {
            //Given
            Uom firstRefereceUom = Uom.from(
                    Name.of(Text.of("Unite")),
                    uomCategoryId,
                    UomType.REFERENCE,
                    null);

            uomDomainService.createUom(firstRefereceUom);

            Uom secondRefereceUom = Uom.from(
                    Name.of(Text.of("Unite")),
                    uomCategoryId,
                    UomType.GREATER,
                    Ratio.of(1.5));

            //When + Then
            assertThatThrownBy(() -> uomDomainService.createUom(secondRefereceUom))
                    .isInstanceOf(UomNameConflictException.class)
                    .hasMessage(UOM_NAME_CONFLICT_EXCEPTION);
        }

        @Test
        void should_throw_error_when_uom_category_not_exist() {
            //Given
            UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());
            Uom uom = Uom.from(
                    Name.of(Text.of("Unite")),
                    uomCategoryId,
                    UomType.REFERENCE,
                    null);

            //Act + Then
            assertThatThrownBy(() -> uomDomainService.createUom(uom))
                    .isInstanceOf(UomCategoryNotFoundException.class)
                    .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindUomByIdDomainServiceTest {

        String uomUUID = "0191b1d0-9874-79ee-a0c4-ef3a5aa7b9c6";
        Uom uom;

        @BeforeEach
        void setUp() {
            uom = uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.fromString(uomUUID)))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("Unite")))
                            .uomCategoryId(uomCategoryId)
                            .build()
            );
        }

        @Test
        void should_success_when_find_uom_with_existing_id() {
            //Given + Act
            Uom result = uomDomainService.findUomById(new UomId(UUID.fromString(uomUUID)));
            //Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(uom);
        }

        @Test
        void should_fail_when_find_uom_non_existing_id() {
            //Given + Act + Then
            assertThatThrownBy(() -> uomDomainService.findUomById(new UomId(UUID.randomUUID())))
                    .isInstanceOf(UomNotFoundException.class)
                    .hasMessage(UOM_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindUomsDomainServiceTest {

        @BeforeEach
        void setUp() {
           uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("piece")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("metre")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("heure")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("litre")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("gramme")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );
        }
        @Test
        void should_success_when_find_uoms() {
            //Given
            int page = 0;
            int size = 3;
            String attribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Uom> result = uomDomainService.findUoms(page, size, attribute, direction);

            //Then
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchUomsDomainServiceTest {

        @BeforeEach
        void setUp() {
            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("piece")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("metre")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("heure")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("litre")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            uomRepository.save(
                    Uom.builder()
                            .id(new UomId(UUID.randomUUID()))
                            .uomType(UomType.REFERENCE)
                            .name(Name.of(Text.of("gramme")))
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );
        }

        static Stream<Arguments> searhUomsMethodSource() {
            return Stream.of(
                    Arguments.of(0, 3, "name", Direction.ASC, "r", 3, 4, 2),
                    Arguments.of(0, 0, "name", Direction.ASC, "w", 0, 0, 0)
            );
        }
        @ParameterizedTest
        @MethodSource("searhUomsMethodSource")
        void should_success_when_search_uoms_with_existing_by_keyword(
                int page, int size, String attribute, Direction direction, String keyword, int contentSize, int totalElements, int totalPages
        ) {
            //Act
            PageInfo<Uom> result = uomDomainService.searchUoms(page, size, attribute, direction, Keyword.of(Text.of(keyword)));

            //Then
            assertThat(result.getElements().size()).isEqualTo(contentSize);
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
        }
    }

    @Nested
    class UpdateUomDomainServiceTest {

        Uom uom;
        UomId uomId;
        UomCategoryId parentUomCategoryId;
        UomId uomId1;

        @BeforeEach
        void setUp() {
            uomId = new UomId(UUID.fromString("0191d5fe-609b-7105-bb4d-a36eb618e2be"));
            parentUomCategoryId = new UomCategoryId(UUID.fromString("0191d5ff-0b64-7fdb-b93a-6f3a0458d5e5"));
            uomId1 = new UomId(UUID.fromString("0191d62d-77ff-76b6-b357-c36a17457288"));

            uomCategoryRepository.save(
                    UomCategory.builder()
                            .id(parentUomCategoryId)
                            .name(Name.of(Text.of("Unite")))
                            .active(Active.with(true))
                            .build()
            );

            uom = uomRepository.save(
                    Uom.builder()
                        .id(uomId)
                        .name(Name.of(Text.of("Carton de 10")))
                        .uomCategoryId(parentUomCategoryId)
                        .ratio(Ratio.of(1.0))
                        .uomType(UomType.REFERENCE)
                        .active(Active.with(true))
                        .build());

            uomRepository.save(
                    Uom.builder()
                        .id(uomId1)
                        .name(Name.of(Text.of("Carton de 15")))
                        .uomCategoryId(parentUomCategoryId)
                        .ratio(Ratio.of(15.0))
                        .uomType(UomType.GREATER)
                        .active(Active.with(true))
                        .build());
        }

        @Test
        void should_success_when_update_uom() {

            //Given
            Uom uomToUpdated = Uom.builder()
                .id(uomId)
                .name(Name.of(Text.of("Carton de 20")))
                .uomType(UomType.GREATER)
                .ratio(Ratio.of(20.0))
                .uomCategoryId(parentUomCategoryId)
                .active(Active.with(true))
                .build();

            //Act
            Uom result = uomDomainService.updateUom(uomId, uomToUpdated);

            //Then
            assertThat(result)
                .isNotNull()
                .isEqualTo(uomToUpdated);
        }

        @Test
        void should_fail_when_update_uom_non_existing_id() {

            //Given
            UomId nonExistingUomId = new UomId(UUID.fromString("0191d627-36b4-79ec-9570-315e9d093b7a"));
            Uom uomToUpdated = Uom.builder()
                    .id(nonExistingUomId)
                    .name(Name.of(Text.of("Carton de 15")))
                    .uomType(UomType.GREATER)
                    .ratio(Ratio.of(15.0))
                    .uomCategoryId(parentUomCategoryId)
                    .active(Active.with(true))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> uomDomainService.updateUom(nonExistingUomId, uomToUpdated))
                    .isInstanceOf(UomNotFoundException.class)
                    .hasMessage(UOM_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_fail_when_update_with_non_existing_uom_category_id() {

            //Given
            UomCategoryId nonExistingUomCategoryId = new UomCategoryId(UUID.fromString("0191d627-36b4-79ec-9570-315e9d093b7a"));
            Uom uomToUpdated = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of("Carton de 15")))
                    .uomType(UomType.GREATER)
                    .ratio(Ratio.of(15.0))
                    .uomCategoryId(nonExistingUomCategoryId)
                    .active(Active.with(true))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> uomDomainService.updateUom(uomId, uomToUpdated))
                    .isInstanceOf(UomCategoryNotFoundException.class)
                    .hasMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_fail_when_update_with_duplicate_name() {

            //Given
            Uom uomToUpdated = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of("Carton de 15")))
                    .uomType(UomType.GREATER)
                    .ratio(Ratio.of(15.0))
                    .uomCategoryId(parentUomCategoryId)
                    .active(Active.with(true))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> uomDomainService.updateUom(uomId, uomToUpdated))
                    .isInstanceOf(UomNameConflictException.class)
                    .hasMessage(UOM_NAME_CONFLICT_EXCEPTION);
        }
    }
}
