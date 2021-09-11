package com.aerokube.lightning;

import com.aerokube.lightning.api.*;
import com.aerokube.lightning.model.*;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class StdWebDriver implements WebDriver {

    private final ApiClient apiClient;

    private final ActionsApi actionsApi;
    private final ContextsApi contextsApi;
    private final CookiesApi cookiesApi;
    private final DocumentApi documentApi;
    private final ElementsApi elementsApi;
    private final NavigationApi navigationApi;
    private final PromptsApi promptsApi;
    private final ScreenshotsApi screenshotsApi;
    private final SessionsApi sessionsApi;
    private final TimeoutsApi timeoutsApi;

    private final String sessionId;

    private final WebDriver.Navigation navigation;
    private final WebDriver.Session session;
    private final WebDriver.Screenshot screenshot;

    public StdWebDriver(@Nonnull Capabilities capabilities, @Nullable String baseUri) {
        this(capabilities, baseUri, null);
    }

    public StdWebDriver(@Nonnull Capabilities capabilities, @Nullable String baseUri, @Nullable Supplier<ApiClient> apiClientSupplier) {
        this.apiClient = initApiClient(baseUri, apiClientSupplier);
        this.actionsApi = new ActionsApi(apiClient);
        this.contextsApi = new ContextsApi(apiClient);
        this.cookiesApi = new CookiesApi(apiClient);
        this.documentApi = new DocumentApi(apiClient);
        this.elementsApi = new ElementsApi(apiClient);
        this.navigationApi = new NavigationApi(apiClient);
        this.promptsApi = new PromptsApi(apiClient);
        this.screenshotsApi = new ScreenshotsApi(apiClient);
        this.sessionsApi = new SessionsApi(apiClient);
        this.timeoutsApi = new TimeoutsApi(apiClient);

        this.sessionId = execute(() -> {
            NewSessionRequestCapabilities newSessionRequestCapabilities = new NewSessionRequestCapabilities()
                    .alwaysMatch(capabilities.raw());
            NewSessionRequest newSessionRequest = new NewSessionRequest()
                    .capabilities(newSessionRequestCapabilities);
            NewSessionResponse session = sessionsApi.createSession(newSessionRequest);
            return session.getValue().getSessionId();
        });


        this.navigation = new Navigation();
        this.session = new Session();
        this.screenshot = new Screenshot();
    }

    private ApiClient initApiClient(@Nullable String baseUri, @Nullable Supplier<ApiClient> apiClientSupplier) {
        ApiClient apiClient = apiClientSupplier != null ?
                apiClientSupplier.get() : new ApiClient();
        apiClient.setHttpClientBuilder(HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1));
        if (baseUri != null) {
            if (baseUri.endsWith("/")) {
                baseUri = baseUri.substring(0, baseUri.length() - 1);
            }
            apiClient.updateBaseUri(baseUri);
        }
        return apiClient;
    }

    private <T> T execute(Callable<T> action) {
        try {
            return action.call();
        } catch (ApiException e) {
            throw errorResponseToException(e);
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
    }

    private WebDriverException errorResponseToException(ApiException e) {
        String body = e.getResponseBody();
        if (body == null) {
            return new WebDriverException(e);
        }
        try {
            ErrorResponse errorResponse = apiClient.getObjectMapper().readValue(body, new TypeReference<>() {
            });
            ErrorResponseValue value = errorResponse.getValue();
            Map<String, Object> data = value.getData();
            StringBuilder errorMessage = new StringBuilder(value.getMessage());
            if (value.getError() != null) {
                errorMessage.append(String.format("\nerror=%s", value.getError()));
            }
            if (value.getStacktrace() != null) {
                errorMessage.append(String.format("\n%s", value.getStacktrace()));
            }
            if (data != null && data.size() > 0) {
                data.forEach(
                        (k, v) -> errorMessage.append(String.format("\n%s=%s", k, v))
                );
            }
            return new WebDriverException(errorMessage.toString());
        } catch (IOException caught) {
            return new WebDriverException(caught);
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public WebDriver.Navigation navigation() {
        return navigation;
    }

    public WebDriver.Screenshot screenshot() {
        return screenshot;
    }

    public WebDriver.Session session() {
        return session;
    }

    class Session implements WebDriver.Session {

        public void delete() {
            execute(() -> {
                sessionsApi.deleteSession(sessionId);
                return null;
            });
        }

    }

    class Navigation implements WebDriver.Navigation {

        public void navigate(String url) {
            execute(() -> {
                UrlRequest urlRequest = new UrlRequest();
                urlRequest.setUrl(url);
                navigationApi.navigateTo(sessionId, urlRequest);
                return null;
            });
        }

    }

    class Screenshot implements WebDriver.Screenshot {

        public byte[] takeScreenshot() {
            return execute(() -> {
                String encodedBytes = screenshotsApi.takeScreenshot(sessionId).getValue();
                return Base64.getDecoder().decode(encodedBytes);
            });
        }

    }

}
