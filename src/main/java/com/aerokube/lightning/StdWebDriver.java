package com.aerokube.lightning;

import com.aerokube.lightning.api.*;
import com.aerokube.lightning.model.*;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private final WebDriver.Document document;
    private final WebDriver.Navigation navigation;
    private final WebDriver.Prompts prompts;
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

        this.document = new Document();
        this.navigation = new Navigation();
        this.prompts = new Prompts();
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

    public WebDriver.Document document() {
        return document;
    }

    public WebDriver.Navigation navigation() {
        return navigation;
    }

    public WebDriver.Prompts prompts() {
        return prompts;
    }

    public WebDriver.Screenshot screenshot() {
        return screenshot;
    }

    public WebDriver.Session session() {
        return session;
    }

    @Override
    public Cookies cookies() {
        return new Cookies();
    }

    public WebDriver.Timeouts timeouts() {
        return timeouts;
    }

    class Cookies implements WebDriver.Cookies {

        @Override
        public void add(@Nonnull Cookie cookie) {

        }

        @Override
        public void delete(@Nonnull String name) {
            execute(() -> cookiesApi.deleteCookie(sessionId, name));
        }

        @Override
        public void deleteAll() {
            execute(() -> cookiesApi.deleteAllCookies(sessionId));
        }

        @Override
        public Cookie get(@Nonnull String name) {
            return null;
        }

        @Nonnull
        @Override
        public List<Cookie> getAll() {
            return execute(() -> cookiesApi.getAllCookies(sessionId).getValue())
                    .stream()
                    .map(com.aerokube.lightning.Cookie::new)
                    .collect(Collectors.toList());
        }

    }

    class Document implements WebDriver.Document {

        @Nonnull
        @Override
        public String getPageSource() {
            return execute(() -> documentApi.getPageSource(sessionId).getValue());
        }

        @Nonnull
        @Override
        public String executeScript(@Nonnull String script, String... args) {
            return execute(() -> {
                ScriptRequest scriptRequest = new ScriptRequest()
                        .script(script)
                        .args(Arrays.asList(args));
                return documentApi.executeScript(sessionId, scriptRequest).getValue();
            });
        }

        @Nonnull
        @Override
        public String executeScriptAsync(@Nonnull String script, @Nonnull String... args) {
            return execute(() -> {
                ScriptRequest scriptRequest = new ScriptRequest()
                        .script(script)
                        .args(Arrays.asList(args));
                return documentApi.executeScriptAsync(sessionId, scriptRequest).getValue();
            });
        }
    }

    class Prompts implements WebDriver.Prompts {

        @Override
        public void accept() {
            execute(() -> promptsApi.acceptAlert(sessionId));
        }

        @Override
        public void dismiss() {
            execute(() -> promptsApi.dismissAlert(sessionId));
        }

        @Override
        public @Nonnull
        String getText(@Nonnull String text) {
            return execute(() -> promptsApi.getAlertText(sessionId).getValue());
        }

        @Override
        public void sendText(@Nonnull String text) {
            execute(() -> {
                SendAlertTextRequest sendAlertTextRequest = new SendAlertTextRequest().text(text);
                return promptsApi.sendAlertText(sessionId, sendAlertTextRequest);
            });
        }
    }

    class Session implements WebDriver.Session {

        @Override
        public void delete() {
            execute(() -> sessionsApi.deleteSession(sessionId));
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
                    public @Nonnull
                    String getMessage() {
                        return response.getMessage();
                    }
                };
            });
        }

    }

    class Navigation implements WebDriver.Navigation {

        @Override
        public void back() {
            execute(() -> navigationApi.navigateBack(sessionId));
        }

        @Override
        public void forward() {
            execute(() -> navigationApi.navigateForward(sessionId));
        }

        @Nonnull
        @Override
        public String getUrl() {
            return execute(() -> navigationApi.getCurrentUrl(sessionId).getValue());
        }

        @Override
        public void navigate(@Nonnull String url) {
            execute(() -> {
                UrlRequest urlRequest = new UrlRequest().url(url);
                return navigationApi.navigateTo(sessionId, urlRequest);
            });
        }

        @Override
        public void refresh() {
            execute(() -> navigationApi.refreshPage(sessionId));
        }

        @Nonnull
        @Override
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
            execute(() -> timeoutsApi.setTimeouts(sessionId, timeouts));
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
