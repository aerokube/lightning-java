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

    // TODO: add javadoc
    Document document();

    Navigation navigation();

    Prompts prompts();

    Screenshot screenshot();

    Timeouts timeouts();

    String getSessionId();

    interface Cookies {

        void add(@Nonnull Cookie cookie);

        void delete(@Nonnull String name);

        void deleteAll();

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

        void back();

        void forward();

        @Nonnull
        String getTitle();

        @Nonnull
        String getUrl();

        void navigate(@Nonnull String url);

        void refresh();

    }

    interface Prompts {

        void accept();

        void dismiss();

        @Nonnull
        String getText(@Nonnull String text);

        void sendText(String text);

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

        void setPageLoadTimeout(@Nonnull Duration value);

        Optional<Duration> getScriptTimeout();

        void setScriptTimeout(@Nullable Duration value);
    }

}
