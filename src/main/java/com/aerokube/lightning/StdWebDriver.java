package com.aerokube.lightning;

import com.aerokube.lightning.api.*;
import com.aerokube.lightning.model.*;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class StdWebDriver implements WebDriver {

    private final ApiClient apiClient = new ApiClient();

    private final ActionsApi actionsApi = new ActionsApi(apiClient);
    private final ContextsApi contextsApi = new ContextsApi(apiClient);
    private final CookiesApi cookiesApi = new CookiesApi(apiClient);
    private final DocumentApi documentApi = new DocumentApi(apiClient);
    private final ElementsApi elementsApi = new ElementsApi(apiClient);
    private final NavigationApi navigationApi = new NavigationApi(apiClient);
    private final PromptsApi promptsApi = new PromptsApi(apiClient);
    private final ScreenshotsApi screenshotsApi = new ScreenshotsApi(apiClient);
    private final SessionsApi sessionsApi = new SessionsApi(apiClient);
    private final TimeoutsApi timeoutsApi = new TimeoutsApi(apiClient);

    private final String sessionId;

    private final WebDriver.Navigation navigation;
    private final WebDriver.Session session;
    private final WebDriver.Screenshot screenshot;
    private final WebDriver.Timeouts timeouts;

    public StdWebDriver(@Nonnull Capabilities capabilities, @Nullable String baseUri) {
        this(
                capabilities,
                baseUri,
                apiClient -> apiClient.setHttpClientBuilder(
                        HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                )
        );
    }

    public StdWebDriver(@Nonnull Capabilities capabilities, @Nullable String baseUri, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        initApiClient(baseUri, apiClientConfigurator);

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
        this.timeouts = new Timeouts();
    }

    private void initApiClient(@Nullable String baseUri, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        apiClientConfigurator.accept(apiClient);
        if (baseUri != null) {
            if (baseUri.endsWith("/")) {
                baseUri = baseUri.substring(0, baseUri.length() - 1);
            }
            apiClient.updateBaseUri(baseUri);
        }
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

    public WebDriver.Timeouts timeouts() {
        return timeouts;
    }

    class Session implements WebDriver.Session {

        @Override
        public void delete() {
            execute(() -> {
                sessionsApi.deleteSession(sessionId);
                return null;
            });
        }

        @Override
        public Status status() {
            return execute(() -> {
                StatusResponseValue response = sessionsApi.getStatus().getValue();
                return new Status() {
                    @Override
                    public boolean isReady() {
                        return response.getReady();
                    }

                    @Override
                    @Nonnull
                    public String getMessage() {
                        return response.getMessage();
                    }
                };
            });
        }

    }

    class Navigation implements WebDriver.Navigation {

        @Override
        public void back() {
            execute(() -> {
                navigationApi.navigateBack(sessionId);
                return null;
            });
        }

        @Override
        public void forward() {
            execute(() -> {
                navigationApi.navigateForward(sessionId);
                return null;
            });
        }

        @Override
        @Nonnull
        public String getUrl() {
            return execute(() -> navigationApi.getCurrentUrl(sessionId).getValue());
        }

        @Override
        public void navigate(@Nonnull String url) {
            execute(() -> {
                UrlRequest urlRequest = new UrlRequest();
                urlRequest.setUrl(url);
                navigationApi.navigateTo(sessionId, urlRequest);
                return null;
            });
        }

        @Override
        public void refresh() {
            execute(() -> {
                navigationApi.refreshPage(sessionId);
                return null;
            });
        }

        @Override
        @Nonnull
        public String getTitle() {
            return execute(() -> navigationApi.getPageTitle(sessionId).getValue());
        }

    }

    class Screenshot implements WebDriver.Screenshot {

        @Override
        public byte[] takeScreenshot() {
            return execute(() -> {
                String encodedBytes = screenshotsApi.takeScreenshot(sessionId).getValue();
                return Base64.getDecoder().decode(encodedBytes);
            });
        }

    }

    class Timeouts implements WebDriver.Timeouts {

        com.aerokube.lightning.model.Timeouts getTimeouts() {
            return execute(() -> timeoutsApi.getTimeouts(sessionId).getValue());
        }

        void setTimeouts(com.aerokube.lightning.model.Timeouts timeouts) {
            execute(() -> {
                timeoutsApi.setTimeouts(sessionId, timeouts);
                return null;
            });
        }

        @Nonnull
        @Override
        public Duration getImplicitWaitTimeout() {
            return Duration.ofMillis(getTimeouts().getImplicit());
        }

        @Override
        public void setImplicitWaitTimeout(@Nonnull Duration value) {
            setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setImplicit(value.toMillis());
                }
            });
        }

        @Nonnull
        @Override
        public Duration getPageLoadTimeout() {
            return Duration.ofMillis(getTimeouts().getPageLoad());
        }

        @Override
        public void setPageLoadTimeout(@Nonnull Duration value) {
            setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setPageLoad(value.toMillis());
                }
            });
        }

        @Override
        public Optional<Duration> getScriptTimeout() {
            Long value = getTimeouts().getScript();
            return value != null ? Optional.of(Duration.ofMillis(value)) : Optional.empty();
        }

        @Override
        public void setScriptTimeout(@Nullable Duration value) {
            setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setScript(value != null ? value.toMillis() : null);
                }
            });
        }
    }

}
