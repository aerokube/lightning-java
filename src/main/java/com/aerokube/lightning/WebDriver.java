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
    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri) {
        return new StdWebDriver(capabilities, baseUri);
    }

    @Nonnull
    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        return new StdWebDriver(capabilities, baseUri, apiClientConfigurator);
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
        Element findFirst(@Nonnull Locator locator);

        @Nonnull
        List<WebDriver.Element> findAll(@Nonnull WebDriver.Locator locator);

        @Nonnull
        Element current();

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

        void switchTo(@Nonnull Element element);

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
        String getText(@Nonnull String text);

        @Nonnull
        Prompts sendText(String text);

    }

    interface Screenshot {
        byte[] take();

        byte[] take(Element element);
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

    interface Element {

        @Nonnull
        Element click();

        @Nonnull
        Element clear();

        @Nonnull
        List<Element> findAll(@Nonnull Locator locator);

        @Nonnull
        Element findFirst(@Nonnull Locator locator);

        boolean isSelected();

        boolean isEnabled();

        @Nonnull
        Optional<String> getAttribute(@Nonnull String name);

        @Nonnull
        String getId();

        @Nonnull
        Position getPosition();

        @Nonnull
        Optional<String> getProperty(@Nonnull String name);

        @Nonnull
        String getCssProperty(@Nonnull String name);

        @Nonnull
        Size getSize();

        @Nonnull
        String getTagName(@Nonnull String name);

        @Nonnull
        String getText();

        @Nonnull
        Element sendKeys(@Nonnull String text);

        @Nonnull
        Accessibility accessibility();

        interface Accessibility {

            @Nonnull
            String getRole();

            @Nonnull
            String getLabel();

        }

    }

    interface Locator {

        @Nonnull
        String getExpression();

        @Nonnull
        LocatorStrategy getStrategy();

    }
}
