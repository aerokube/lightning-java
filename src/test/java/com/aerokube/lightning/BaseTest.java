package com.aerokube.lightning;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.aerokube.lightning.Image.CHROME;
import static com.aerokube.lightning.Image.FIREFOX;

@Testcontainers
public class BaseTest {

    @Container
    protected final GenericContainer<?> browserContainer = getBrowserContainer();

    protected WebDriver driver;

    protected GenericContainer<?> getBrowserContainer() {
        return new GenericContainer<>(DockerImageName.parse(getImage().getRef()))
                .withPrivilegedMode(true)
                .withExposedPorts(4444)
                .withSharedMemorySize(268435456L)
                .waitingFor(new HttpWaitStrategy().forPort(4444).forPath("/status"));
    }

    protected Image getImage() {
        return CHROME;
    }

    protected String getUri(int port) {
        return FIREFOX.equals(getImage()) ?
                String.format("http://localhost:%s/wd/hub", port) :
                String.format("http://localhost:%s/", port);
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
