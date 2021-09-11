package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface WebDriver {

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri) {
        return new StdWebDriver(capabilities, baseUri);
    }

    static WebDriver create(@Nonnull Capabilities capabilities, @Nonnull String baseUri, @Nonnull Supplier<ApiClient> apiClientSupplier) {
        return new StdWebDriver(capabilities, baseUri, apiClientSupplier);
    }

    Session session();

    Navigation navigation();

    Screenshot screenshot();

    String getSessionId();

    interface Navigation {
        void navigate(String url);
    }

    interface Screenshot {
        byte[] takeScreenshot();
    }

    interface Session {
        void delete();
    }

}
