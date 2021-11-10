package com.aerokube.lightning;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.aerokube.lightning.Image.CHROME;

@Testcontainers
public class BaseTest {

    @Container
    protected final GenericContainer<?> browserContainer = new GenericContainer<>(DockerImageName.parse(getImage().getRef()))
            .withPrivilegedMode(true)
            .withExposedPorts(4444)
            .waitingFor(new HttpWaitStrategy().forPort(4444).forPath("/status"));

    protected WebDriver driver;

    protected Image getImage() {
        return CHROME;
    }

    protected String getUri(int port) {
        return String.format("http://localhost:%s/", port);
    }

    // Yes, we know about @SetUp and @TearDown, but it's easier to pass different capabilities like this
    protected void test(Supplier<Capabilities> caps, Consumer<WebDriver> steps) {
        WebDriver driver = null;
        try {
            Integer port = browserContainer.getMappedPort(4444);
            driver = WebDriver.create(getUri(port), caps.get());
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
