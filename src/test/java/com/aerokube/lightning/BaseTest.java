package com.aerokube.lightning;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Testcontainers
class BaseTest {

    @Container
    private final GenericContainer<?> browserContainer = new GenericContainer<>(DockerImageName.parse(getImage()))
            .withPrivilegedMode(true)
            .withExposedPorts(4444)
            .waitingFor(new HttpWaitStrategy().forPort(4444).forPath("/status"));
    protected WebDriver driver;

    protected String getImage() {
        return "browsers/chrome:93.0";
    }

    // Yes, we know about @SetUp and @TearDown, but it's easier to pass different capabilities like this
    protected void test(Supplier<Capabilities> caps, Consumer<WebDriver> steps) {
        WebDriver driver = null;
        try {
            Integer port = browserContainer.getMappedPort(4444);
            driver = WebDriver.create(String.format("http://localhost:%s/", port), caps.get());
            steps.accept(driver);
        } finally {
            if (driver != null) {
                driver.session().delete();
            }
        }
    }

    protected void test(Consumer<WebDriver> steps) {
        test(() -> Capabilities.create().browserName("chrome"), steps);
    }

}
