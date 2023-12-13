package be.shwan.springsecurityjwt.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
public abstract class TestContainers {
    static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:9.6.12"));
        postgreSQLContainer.start();
    }

}
