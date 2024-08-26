package cm.xenonbyte.farmbyte.bootstrap;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainMapper;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication(
        scanBasePackages = "cm.xenonbyte.farmbyte"
)
@ComponentScan(
        basePackages = "cm.xenonbyte.farmbyte",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {DomainService.class, DomainMapper.class})
        }
)
@EnableJpaRepositories(
        basePackages = "cm.xenonbyte.farmbyte"
)
@EntityScan(basePackages = "cm.xenonbyte.farmbyte")
public class FarmByteBootStrapApplication {

    public static void main( String[] args )
    {
        SpringApplication.run(FarmByteBootStrapApplication.class, args);
    }
}
