package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.util.function.Function;

public interface WebDriver {

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri) {
        return new StdWebDriver(capabilities, baseUri);
    }

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri, @Nonnull Function<ApiClient, ApiClient> apiClientConfigurator) {
        return new StdWebDriver(capabilities, baseUri, apiClientConfigurator);
    }

    Session session();

    Navigation navigation();

    Screenshot screenshot();

    String getSessionId();

    interface Navigation {
        void back();
        void forward();
        String url();
        void navigate(String url);
        void refresh();
        String title();
    }

    interface Screenshot {
        byte[] takeScreenshot();
        //TODO: add element screenshot call
    }

    interface Session {

        interface SessionStatus {
            boolean ready();
            String message();
        }

        void delete();
        SessionStatus status();
    }

}
