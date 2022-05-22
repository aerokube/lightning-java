package com.aerokube.lightning.adapter;

import com.aerokube.lightning.Capabilities;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.aerokube.lightning.WebDriver.create;

@Testcontainers
public class BaseTest {

    @Container
    protected final GenericContainer<?> browserContainer = getBrowserContainer();

    protected WebDriver driver;

    private static final String IMAGE = "browsers/chrome:101.0";

    protected GenericContainer<?> getBrowserContainer() {
        return new GenericContainer<>(DockerImageName.parse(IMAGE))
                .withPrivilegedMode(true)
                .withExposedPorts(4444)
                .withSharedMemorySize(268435456L)
                .waitingFor(new HttpWaitStrategy().forPort(4444).forPath("/status"));
    }

    protected String getUri(int port) {
        return String.format("http://localhost:%s/", port);
    }

    protected void test(Supplier<Capabilities> caps, Consumer<WebDriver> steps) {
        WebDriver driver = null;
        try {
            Integer port = browserContainer.getMappedPort(4444);
            driver = new SeleniumWebDriver(create(getUri(port), caps.get()));
            steps.accept(driver);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    protected void test(Consumer<WebDriver> steps) {
        test(() -> Capabilities.create().browserName("chrome"), steps);
    }

}
