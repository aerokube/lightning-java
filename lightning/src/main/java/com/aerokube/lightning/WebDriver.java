package com.aerokube.lightning;

import com.aerokube.lightning.model.LocatorStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public interface WebDriver extends AutoCloseable {

    @Nonnull
    static WebDriver create(@Nonnull String baseUri, @Nonnull Capabilities capabilities) {
        return new StdWebDriver(baseUri, capabilities);
    }

    @Nonnull
    static WebDriver create(@Nonnull String baseUri, @Nonnull Capabilities capabilities, @Nonnull HttpClient.Builder httpClientBuilder) {
        return new StdWebDriver(baseUri, capabilities, httpClientBuilder);
    }

    default void close(){
        session().delete();
    }

    @Nonnull
    Session session();

    @Nonnull
    Actions actions();

    @Nonnull
    Cookies cookies();

    @Nonnull
    Elements elements();

    @Nonnull
    Windows windows();

    @Nonnull
    Frames frames();

    // TODO: add javadoc
    @Nonnull
    Document document();

    @Nonnull
    Navigation navigation();

    @Nonnull
    Prompts prompts();

    @Nonnull
    Print print();

    @Nonnull
    Screenshot screenshot();

    @Nonnull
    Timeouts timeouts();

    @Nonnull
    String getSessionId();

    @Nonnull
    Capabilities getCapabilities();

    @Nonnull
    <T extends WebDriver> T extension(@Nonnull Class<T> cls);

    @Nonnull
    <T> T api(@Nonnull Class<T> cls);

    <T> T execute(@Nonnull Callable<T> action);

    interface Actions extends WebDriver {

        @Nonnull
        Actions release();

        //TODO: add actions builder and perform() method

    }

    interface Elements extends WebDriver {

        @Nonnull
        WebElement findFirst(@Nonnull Locator locator);

        @Nonnull
        List<WebElement> findAll(@Nonnull WebDriver.Locator locator);

        @Nonnull
        WebElement current();

    }

    interface Windows extends WebDriver {

        @Nonnull
        List<Window> list();

        @Nonnull
        Window createWindow();

        @Nonnull
        Window createTab();

        @Nonnull
        Window current();

    }

    interface Frames extends WebDriver {
        void switchTo(int index);

        void switchTo(@Nonnull WebElement element);

        void switchToParent();

        void switchToDefault();
    }

    interface Cookies extends WebDriver {

        @Nonnull
        Cookies add(@Nonnull Cookie cookie);

        @Nonnull
        Cookies delete(@Nonnull String name);

        @Nonnull
        Cookies deleteAll();

        @Nonnull
        Cookie get(@Nonnull String name);

        @Nonnull
        List<Cookie> getAll();

    }

    interface Document extends WebDriver {

        @Nonnull
        String getPageSource();

        //TODO: add support for passing WebElement to executeScript()

        @Nonnull
        Object executeScript(@Nonnull String script, Object... args);

        @Nonnull
        Object executeAsyncScript(@Nonnull String script, @Nonnull Object... args);

        @Nonnull
        String uploadFile(@Nonnull Path file);
    }

    interface Navigation extends WebDriver {

        @Nonnull
        Navigation back();

        @Nonnull
        Navigation forward();

        @Nonnull
        String getTitle();

        @Nonnull
        String getUrl();

        @Nonnull
        Navigation navigate(@Nonnull String url);

        @Nonnull
        Navigation refresh();

    }

    interface Prompts extends WebDriver {

        @Nonnull
        Prompts accept();

        @Nonnull
        Prompts dismiss();

        @Nonnull
        String getText();

        @Nonnull
        Prompts sendText(@Nonnull String text);

    }

    interface Screenshot extends WebDriver {
        byte[] take();

        byte[] take(@Nonnull WebElement element);
    }

    interface Session extends WebDriver {

        void delete();

        @Nonnull
        Status status();

        interface Status {
            boolean isReady();

            @Nonnull
            String getMessage();
        }
    }

    interface Timeouts extends WebDriver {
        @Nonnull
        Duration getImplicitWaitTimeout();

        Timeouts setImplicitWaitTimeout(@Nonnull Duration value);

        @Nonnull
        Duration getPageLoadTimeout();

        @Nonnull
        Timeouts setPageLoadTimeout(@Nonnull Duration value);

        @Nonnull
        Optional<Duration> getScriptTimeout();

        @Nonnull
        Timeouts setScriptTimeout(@Nullable Duration value);
    }

    interface Print extends WebDriver {

        @Nonnull
        Print addPages(@Nonnull int... pages);

        @Nonnull
        Print addPages(@Nonnull String... pages);

        @Nonnull
        Print scale(float scale);

        @Nonnull
        Print originalSize();

        @Nonnull
        Print width(float value);

        @Nonnull
        Print height(float value);

        @Nonnull
        Print marginTop(float value);

        @Nonnull
        Print marginBottom(float value);

        @Nonnull
        Print marginLeft(float value);

        @Nonnull
        Print marginRight(float value);

        @Nonnull
        Print landscape();

        byte[] pdf();
    }

    interface Locator {

        @Nonnull
        String getExpression();

        @Nonnull
        LocatorStrategy getStrategy();

    }

}
