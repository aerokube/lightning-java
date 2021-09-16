package com.aerokube.lightning;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface WebDriver {

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri) {
        return new StdWebDriver(capabilities, baseUri);
    }

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        return new StdWebDriver(capabilities, baseUri, apiClientConfigurator);
    }

    Session session();

    Cookies cookies();

    Windows windows();

    Frames frames();

    // TODO: add javadoc
    Document document();

    Navigation navigation();

    Prompts prompts();

    Screenshot screenshot();

    Timeouts timeouts();

    String getSessionId();

    interface Windows {

        List<Window> list();

        Window createWindow();

        Window createTab();

        Window current();

        interface Window {

            Window close();

            Window fullscreen();

            Window maximize();

            Window minimize();

            Window setSize(int width, int height);

            Size getSize();

            Position getPosition();

            Window setPosition(int x, int y);

            Window switchTo();

            interface Size {

                int getWidth();

                int getHeight();

            }

            interface Position {

                int getX();

                int getY();

            }

        }

    }

    interface Frames {
        void switchTo(int index);

        //TODO: add switchTo(WebElement webElement); call
        void switchToParent();

        void switchToDefault();
    }

    interface Cookies {

        Cookies add(@Nonnull Cookie cookie);

        Cookies delete(@Nonnull String name);

        Cookies deleteAll();

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

            Optional<Instant> getExpires();

            com.aerokube.lightning.model.Cookie.SameSiteEnum getSameSitePolicy();
        }

        interface CookieBuilder {

            CookieBuilder path(@Nonnull String path);

            CookieBuilder domain(@Nonnull String domain);

            CookieBuilder secureOnly(boolean secureOnly);

            CookieBuilder httpOnly(boolean httpOnly);

            CookieBuilder expires(@Nonnull Instant expires);

            CookieBuilder sameSitePolicy(@Nonnull com.aerokube.lightning.model.Cookie.SameSiteEnum sameSitePolicy);

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

        Navigation back();

        Navigation forward();

        @Nonnull
        String getTitle();

        @Nonnull
        String getUrl();

        Navigation navigate(@Nonnull String url);

        Navigation refresh();

    }

    interface Prompts {

        Prompts accept();

        Prompts dismiss();

        @Nonnull
        String getText(@Nonnull String text);

        Prompts sendText(String text);

    }

    interface Screenshot {
        byte[] takeScreenshot();
        //TODO: add element screenshot call
    }

    interface Session {

        void delete();

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

        Timeouts setPageLoadTimeout(@Nonnull Duration value);

        Optional<Duration> getScriptTimeout();

        Timeouts setScriptTimeout(@Nullable Duration value);
    }

}
