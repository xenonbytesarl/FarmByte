package cm.xenonbyte.farmbyte.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication(
        scanBasePackages = "cm.xenonbyte.farmbyte"
)
@EnableJpaRepositories(
        basePackages = "cm.xenonbyte.farmbyte"
)
@EntityScan(basePackages = "cm.xenonbyte.farmbyte")
public class FarmByteBootStrapApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(FarmByteBootStrapApplication.class, args);
    }
}
