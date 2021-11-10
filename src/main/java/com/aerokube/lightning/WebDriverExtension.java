package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

public class WebDriverExtension implements WebDriver {

    private final WebDriver webDriver;

    public WebDriverExtension(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    @Nonnull
    public Session session() {
        return webDriver.session();
    }

    @Override
    @Nonnull
    public Actions actions() {
        return webDriver.actions();
    }

    @Override
    @Nonnull
    public Cookies cookies() {
        return webDriver.cookies();
    }

    @Override
    @Nonnull
    public Elements elements() {
        return webDriver.elements();
    }

    @Override
    @Nonnull
    public Windows windows() {
        return webDriver.windows();
    }

    @Override
    @Nonnull
    public Frames frames() {
        return webDriver.frames();
    }

    @Override
    @Nonnull
    public Document document() {
        return webDriver.document();
    }

    @Override
    @Nonnull
    public Navigation navigation() {
        return webDriver.navigation();
    }

    @Override
    @Nonnull
    public Prompts prompts() {
        return webDriver.prompts();
    }

    @Override
    @Nonnull
    public Print print() {
        return webDriver.print();
    }

    @Override
    @Nonnull
    public Screenshot screenshot() {
        return webDriver.screenshot();
    }

    @Override
    @Nonnull
    public Timeouts timeouts() {
        return webDriver.timeouts();
    }

    @Override
    @Nonnull
    public String getSessionId() {
        return webDriver.getSessionId();
    }

    @Nonnull
    @Override
    public Capabilities getCapabilities() {
        return webDriver.getCapabilities();
    }

    @Nonnull
    @Override
    public <T extends WebDriver> T extension(@Nonnull Class<T> cls) {
        return webDriver.extension(cls);
    }

    @Nonnull
    @Override
    public <T> T api(@Nonnull Class<T> cls) {
        return webDriver.api(cls);
    }

    @Override
    public <T> T execute(@Nonnull Callable<T> action) {
        return webDriver.execute(action);
    }
}
