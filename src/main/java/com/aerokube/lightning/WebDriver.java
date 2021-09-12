package com.aerokube.lightning;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
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

    Navigation navigation();

    Screenshot screenshot();

    Timeouts timeouts();

    String getSessionId();

    interface Navigation {
        void back();

        void forward();

        @Nonnull
        String getUrl();

        void navigate(@Nonnull String url);

        void refresh();

        @Nonnull
        String getTitle();
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
