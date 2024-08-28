package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.config.BaseEntityJpaConfig;
import cm.xenonbyte.farmbyte.common.domain.mapper.ReferenceMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */

@TestConfiguration
@Import({ReferenceMapper.class, BaseEntityJpaConfig.class})
@EntityScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa")
@ComponentScan(basePackages = {"cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa"})
@EnableJpaRepositories(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa")
public class JpaRepositoryAdapterTest {
}
