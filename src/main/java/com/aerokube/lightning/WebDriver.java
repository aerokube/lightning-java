package com.aerokube.lightning;

import com.aerokube.lightning.model.LocatorStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface WebDriver {

    @Nonnull
    static WebDriver create(@Nonnull String baseUri, @Nonnull Capabilities capabilities) {
        return new StdWebDriver(baseUri, capabilities);
    }

    @Nonnull
    static WebDriver create(@Nonnull String baseUri, @Nonnull Capabilities capabilities, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        return new StdWebDriver(baseUri, capabilities, apiClientConfigurator);
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

    interface Actions {

        @Nonnull
        Actions release();

        //TODO: add actions builder and perform() method

    }

    interface Elements {

        @Nonnull
        WebElement findFirst(@Nonnull Locator locator);

        @Nonnull
        List<WebElement> findAll(@Nonnull WebDriver.Locator locator);

        @Nonnull
        WebElement current();

    }

    interface Windows {

        @Nonnull
        List<Window> list();

        @Nonnull
        Window createWindow();

        @Nonnull
        Window createTab();

        @Nonnull
        Window current();

        interface Window {

            @Nonnull
            Window close();

            @Nonnull
            Window fullscreen();

            @Nonnull
            Window maximize();

            @Nonnull
            Window minimize();

            @Nonnull
            Window setSize(int width, int height);

            @Nonnull
            Size getSize();

            @Nonnull
            Position getPosition();

            @Nonnull
            Window setPosition(int x, int y);

            @Nonnull
            Window switchTo();

        }

    }

    interface Frames {
        void switchTo(int index);

        void switchTo(@Nonnull WebElement element);

        void switchToParent();

        void switchToDefault();
    }

    interface Cookies {

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

        interface Cookie {

            static CookieBuilder create(@Nonnull String name, @Nonnull String value) {
                return new com.aerokube.lightning.Cookie.CookieBuilder(name, value);
            }

            @Nonnull
            String getName();

            @Nonnull
            String getValue();

            @Nonnull
            String getPath();

            @Nonnull
            String getDomain();

            boolean isSecureOnly();

            boolean isHttpOnly();

            @Nonnull
            Optional<Instant> getExpires();

            @Nonnull
            com.aerokube.lightning.model.Cookie.SameSiteEnum getSameSitePolicy();
        }

        interface CookieBuilder {

            @Nonnull
            CookieBuilder path(@Nonnull String path);

            @Nonnull
            CookieBuilder domain(@Nonnull String domain);

            @Nonnull
            CookieBuilder secureOnly(boolean secureOnly);

            @Nonnull
            CookieBuilder httpOnly(boolean httpOnly);

            @Nonnull
            CookieBuilder expires(@Nonnull Instant expires);

            @Nonnull
            CookieBuilder sameSitePolicy(@Nonnull com.aerokube.lightning.model.Cookie.SameSiteEnum sameSitePolicy);

            @Nonnull
            Cookie build();
        }

    }

    interface Document {

        @Nonnull
        String getPageSource();

        @Nonnull
        String executeScript(@Nonnull String script, String... args);

        @Nonnull
        String executeScriptAsync(@Nonnull String script, @Nonnull String... args);

    }

    interface Navigation {

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

    interface Prompts {

        @Nonnull
        Prompts accept();

        @Nonnull
        Prompts dismiss();

        @Nonnull
        String getText();

        @Nonnull
        Prompts sendText(String text);

    }

    interface Screenshot {
        byte[] take();

        byte[] take(WebElement element);
    }

    interface Session {

        void delete();

        @Nonnull
        Status status();

        interface Status {
            boolean isReady();

            @Nonnull
            String getMessage();
        }
    }

    interface Timeouts {
        @Nonnull
        Duration getImplicitWaitTimeout();

        void setImplicitWaitTimeout(@Nonnull Duration value);

        @Nonnull
        Duration getPageLoadTimeout();

        @Nonnull
        Timeouts setPageLoadTimeout(@Nonnull Duration value);

        @Nonnull
        Optional<Duration> getScriptTimeout();

        @Nonnull
        Timeouts setScriptTimeout(@Nullable Duration value);
    }

    interface Size {

        int getWidth();

        int getHeight();

    }

    interface Position {

        int getX();

        int getY();

    }

    interface Print {

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

        byte[] pdf();
    }

    interface Locator {

        @Nonnull
        String getExpression();

        @Nonnull
        LocatorStrategy getStrategy();

    }
}
