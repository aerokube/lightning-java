package com.aerokube.lightning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class BaseTest {

    private static final String image = "browsers/chrome:93.0";

    @Container
    private final GenericContainer<?> browserContainer = new GenericContainer<>(DockerImageName.parse(image))
            .withPrivilegedMode(true)
            .withExposedPorts(4444)
            .waitingFor(new HttpWaitStrategy().forPort(4444).forPath("/status"));

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        Capabilities capabilities = Capabilities.create()
                .browserName("chrome");
        Integer port = browserContainer.getMappedPort(4444);
        driver = WebDriver.create(String.format("http://localhost:%s/", port), capabilities);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.session().delete();
            driver = null;
        }
    }

}
