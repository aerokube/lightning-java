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

import static com.aerokube.lightning.model.NewWindowRequest.TypeEnum.TAB;
import static com.aerokube.lightning.model.NewWindowRequest.TypeEnum.WINDOW;

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
    private final WebDriver.Windows windows;
    private final WebDriver.Frames frames;

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
        this.windows = new Windows();
        this.frames = new Frames();
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

    public WebDriver.Windows windows() {
        return windows;
    }

    public WebDriver.Frames frames() {
        return frames;
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
        public Cookies add(@Nonnull Cookie cookie) {
            CookieRequest cookieRequest = new CookieRequest()
                    .cookie(((com.aerokube.lightning.Cookie) cookie).raw());
            execute(() -> cookiesApi.addCookie(sessionId, cookieRequest));
            return this;
        }

        @Override
        public Cookies delete(@Nonnull String name) {
            execute(() -> cookiesApi.deleteCookie(sessionId, name));
            return this;
        }

        @Override
        public Cookies deleteAll() {
            execute(() -> cookiesApi.deleteAllCookies(sessionId));
            return this;
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
        public Prompts accept() {
            execute(() -> promptsApi.acceptAlert(sessionId));
            return this;
        }

        @Override
        public Prompts dismiss() {
            execute(() -> promptsApi.dismissAlert(sessionId));
            return this;
        }

        @Override
        public @Nonnull
        String getText(@Nonnull String text) {
            return execute(() -> promptsApi.getAlertText(sessionId).getValue());
        }

        @Override
        public Prompts sendText(@Nonnull String text) {
            execute(() -> {
                SendAlertTextRequest sendAlertTextRequest = new SendAlertTextRequest().text(text);
                return promptsApi.sendAlertText(sessionId, sendAlertTextRequest);
            });
            return this;
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
        public Navigation back() {
            execute(() -> navigationApi.navigateBack(sessionId));
            return this;
        }

        @Override
        public WebDriver.Navigation forward() {
            execute(() -> navigationApi.navigateForward(sessionId));
            return this;
        }

        @Nonnull
        @Override
        public String getUrl() {
            return execute(() -> navigationApi.getCurrentUrl(sessionId).getValue());
        }

        @Override
        public Navigation navigate(@Nonnull String url) {
            execute(() -> {
                UrlRequest urlRequest = new UrlRequest().url(url);
                return navigationApi.navigateTo(sessionId, urlRequest);
            });
            return this;
        }

        @Override
        public WebDriver.Navigation refresh() {
            execute(() -> navigationApi.refreshPage(sessionId));
            return this;
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

        Timeouts setTimeouts(com.aerokube.lightning.model.Timeouts timeouts) {
            execute(() -> timeoutsApi.setTimeouts(sessionId, timeouts));
            return this;
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
        public Timeouts setPageLoadTimeout(@Nonnull Duration value) {
            return setTimeouts(new com.aerokube.lightning.model.Timeouts() {
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
        public Timeouts setScriptTimeout(@Nullable Duration value) {
            return setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setScript(value != null ? value.toMillis() : null);
                }
            });
        }
    }


    class Windows implements WebDriver.Windows {

        @Override
        public List<Window> list() {
            return execute(() -> contextsApi.getWindowHandles(sessionId).getValue()).stream()
                    .map(StdWindow::new)
                    .collect(Collectors.toList());
        }

        @Override
        public Window createWindow() {
            return createWindow(WINDOW);
        }

        private Window createWindow(NewWindowRequest.TypeEnum type) {
            NewWindowRequest newWindowRequest = new NewWindowRequest().type(type);
            String handle = execute(() -> contextsApi.createNewWindow(sessionId, newWindowRequest).getValue().getHandle());
            return new StdWindow(handle);
        }

        @Override
        public Window createTab() {
            return createWindow(TAB);
        }

        @Override
        public Window current() {
            String handle = execute(() -> contextsApi.getWindowHandle(sessionId).getValue());
            return new StdWindow(handle);
        }

        class StdWindow implements WebDriver.Windows.Window {

            private final String handle;

            StdWindow(String handle) {
                this.handle = handle;
            }

            @Override
            public WebDriver.Windows.Window close() {
                switchTo();
                execute(() -> contextsApi.closeWindow(sessionId));
                return this;
            }

            @Override
            public WebDriver.Windows.Window fullscreen() {
                switchTo();
                execute(() -> contextsApi.fullscreenWindow(sessionId));
                return this;
            }

            @Override
            public WebDriver.Windows.Window maximize() {
                switchTo();
                execute(() -> contextsApi.maximizeWindow(sessionId));
                return this;
            }

            @Override
            public WebDriver.Windows.Window minimize() {
                switchTo();
                execute(() -> contextsApi.minimizeWindow(sessionId));
                return this;
            }

            @Override
            public WebDriver.Windows.Window setSize(int width, int height) {
                switchTo();
                com.aerokube.lightning.model.Rect rect = new com.aerokube.lightning.model.Rect()
                        .width(width).height(height);
                execute(() -> contextsApi.setWindowRect(sessionId, rect));
                return this;
            }

            @Override
            public Size getSize() {
                return new Rect(execute(() -> contextsApi.getWindowRect(sessionId)));
            }

            @Override
            public Position getPosition() {
                return new Rect(execute(() -> contextsApi.getWindowRect(sessionId)));
            }

            @Override
            public WebDriver.Windows.Window setPosition(int x, int y) {
                switchTo();
                com.aerokube.lightning.model.Rect rect = new com.aerokube.lightning.model.Rect()
                        .x(x).y(y);
                execute(() -> contextsApi.setWindowRect(sessionId, rect));
                return this;
            }

            @Override
            public WebDriver.Windows.Window switchTo() {
                SwitchToWindowRequest switchToWindowRequest = new SwitchToWindowRequest().handle(handle);
                execute(() -> contextsApi.switchToWindow(sessionId, switchToWindowRequest));
                return this;
            }
        }

        class Rect implements Window.Position, Window.Size {

            private final com.aerokube.lightning.model.Rect rect;

            Rect(com.aerokube.lightning.model.Rect rect) {
                this.rect = rect;
            }

            @Override
            public int getWidth() {
                return rect.getWidth();
            }

            @Override
            public int getHeight() {
                return rect.getHeight();
            }

            @Override
            public int getX() {
                return rect.getX();
            }

            @Override
            public int getY() {
                return rect.getY();
            }
        }
    }

    class Frames implements WebDriver.Frames {

        @Override
        public void switchTo(int index) {
            SwitchToFrameRequest switchToFrameRequest = new SwitchToFrameRequest()
                    .id(new FrameId(index));
            execute(() -> contextsApi.switchToFrame(sessionId, switchToFrameRequest));
        }

        @Override
        public void switchToParent() {
            execute(() -> contextsApi.switchToParentFrame(sessionId));
        }

        @Override
        public void switchToDefault() {
            SwitchToFrameRequest switchToFrameRequest = new SwitchToFrameRequest()
                    .id(new FrameId((Integer) null));
            execute(() -> contextsApi.switchToFrame(sessionId, switchToFrameRequest));
        }
    }

}
