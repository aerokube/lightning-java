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
    private final PrintApi printApi;

    private final String sessionId;

    private final WebDriver.Actions actions;
    private final WebDriver.Document document;
    private final WebDriver.Elements elements;
    private final WebDriver.Navigation navigation;
    private final WebDriver.Prompts prompts;
    private final WebDriver.Session session;
    private final WebDriver.Screenshot screenshot;
    private final WebDriver.Timeouts timeouts;
    private final WebDriver.Windows windows;
    private final WebDriver.Frames frames;

    public StdWebDriver(@Nullable String baseUri, @Nonnull Capabilities capabilities) {
        this(
                baseUri, capabilities,
                apiClient -> apiClient.setHttpClientBuilder(
                        HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                )
        );
    }

    public StdWebDriver(@Nullable String baseUri, @Nonnull Capabilities capabilities, @Nonnull Consumer<ApiClient> apiClientConfigurator) {
        initApiClient(baseUri, apiClientConfigurator);

        actionsApi = new ActionsApi(apiClient);
        contextsApi = new ContextsApi(apiClient);
        cookiesApi = new CookiesApi(apiClient);
        documentApi = new DocumentApi(apiClient);
        elementsApi = new ElementsApi(apiClient);
        navigationApi = new NavigationApi(apiClient);
        promptsApi = new PromptsApi(apiClient);
        screenshotsApi = new ScreenshotsApi(apiClient);
        sessionsApi = new SessionsApi(apiClient);
        timeoutsApi = new TimeoutsApi(apiClient);
        printApi = new PrintApi(apiClient);

        this.sessionId = execute(() -> {
            NewSessionRequestCapabilities newSessionRequestCapabilities = new NewSessionRequestCapabilities()
                    .alwaysMatch(capabilities.raw());
            NewSessionRequest newSessionRequest = new NewSessionRequest()
                    .capabilities(newSessionRequestCapabilities);
            NewSessionResponse session = sessionsApi.createSession(newSessionRequest);
            return session.getValue().getSessionId();
        });

        this.actions = new Actions();
        this.document = new Document();
        this.elements = new Elements();
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
            return new WebDriverException(errorMessage.toString(), value.getError());
        } catch (IOException caught) {
            return new WebDriverException(caught);
        }
    }

    @Nonnull
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Nonnull
    @Override
    public WebDriver.Document document() {
        return document;
    }

    @Nonnull
    @Override
    public WebDriver.Elements elements() {
        return elements;
    }

    @Nonnull
    @Override
    public WebDriver.Navigation navigation() {
        return navigation;
    }

    @Nonnull
    @Override
    public WebDriver.Prompts prompts() {
        return prompts;
    }

    @Nonnull
    @Override
    public WebDriver.Print print() {
        return new Print();
    }

    @Nonnull
    @Override
    public WebDriver.Screenshot screenshot() {
        return screenshot;
    }

    @Nonnull
    @Override
    public WebDriver.Session session() {
        return session;
    }

    @Nonnull
    @Override
    public WebDriver.Actions actions() {
        return actions;
    }

    @Nonnull
    @Override
    public WebDriver.Windows windows() {
        return windows;
    }

    @Nonnull
    @Override
    public WebDriver.Frames frames() {
        return frames;
    }

    @Nonnull
    @Override
    public Cookies cookies() {
        return new Cookies();
    }

    @Nonnull
    @Override
    public WebDriver.Timeouts timeouts() {
        return timeouts;
    }

    static class Rect implements Position, Size {

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

    class Cookies implements WebDriver.Cookies {

        @Nonnull
        @Override
        public Cookies add(@Nonnull Cookie cookie) {
            CookieRequest cookieRequest = new CookieRequest()
                    .cookie(((StdCookie) cookie).raw());
            execute(() -> cookiesApi.addCookie(sessionId, cookieRequest));
            return this;
        }

        @Nonnull
        @Override
        public Cookies delete(@Nonnull String name) {
            execute(() -> cookiesApi.deleteCookie(sessionId, name));
            return this;
        }

        @Nonnull
        @Override
        public Cookies deleteAll() {
            execute(() -> cookiesApi.deleteAllCookies(sessionId));
            return this;
        }

        @Nonnull
        @Override
        public Cookie get(@Nonnull String name) {
            return new StdCookie(
                    execute(() -> cookiesApi.getNamedCookie(sessionId, name).getValue())
            );
        }

        @Nonnull
        @Override
        public List<Cookie> getAll() {
            return execute(() -> cookiesApi.getAllCookies(sessionId).getValue())
                    .stream()
                    .map(StdCookie::new)
                    .collect(Collectors.toList());
        }

    }

    class Actions implements WebDriver.Actions {

        @Nonnull
        @Override
        public WebDriver.Actions release() {
            execute(() -> actionsApi.releaseActions(sessionId));
            return this;
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

        @Nonnull
        @Override
        public Prompts accept() {
            execute(() -> promptsApi.acceptAlert(sessionId, new EmptyRequest()));
            return this;
        }

        @Nonnull
        @Override
        public Prompts dismiss() {
            execute(() -> promptsApi.dismissAlert(sessionId, new EmptyRequest()));
            return this;
        }

        @Override
        public @Nonnull
        String getText() {
            return execute(() -> promptsApi.getAlertText(sessionId).getValue());
        }

        @Nonnull
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

        @Nonnull
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

        @Nonnull
        @Override
        public Navigation back() {
            execute(() -> navigationApi.navigateBack(sessionId, new EmptyRequest()));
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Navigation forward() {
            execute(() -> navigationApi.navigateForward(sessionId, new EmptyRequest()));
            return this;
        }

        @Nonnull
        @Override
        public String getUrl() {
            return execute(() -> navigationApi.getCurrentUrl(sessionId).getValue());
        }

        @Nonnull
        @Override
        public Navigation navigate(@Nonnull String url) {
            execute(() -> {
                UrlRequest urlRequest = new UrlRequest().url(url);
                return navigationApi.navigateTo(sessionId, urlRequest);
            });
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Navigation refresh() {
            execute(() -> navigationApi.refreshPage(sessionId, new EmptyRequest()));
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
        public byte[] take() {
            return execute(() -> {
                String encodedBytes = screenshotsApi.takeScreenshot(sessionId).getValue();
                return Base64.getDecoder().decode(encodedBytes);
            });
        }

        @Override
        public byte[] take(@Nonnull WebElement element) {
            return execute(() -> {
                String encodedBytes = screenshotsApi.takeElementScreenshot(sessionId, element.getId()).getValue();
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
            Long implicit = getTimeouts().getImplicit();
            if (implicit == null) {
                implicit = 0L;
            }
            return Duration.ofMillis(implicit);
        }

        @Override
        public Timeouts setImplicitWaitTimeout(@Nonnull Duration value) {
            setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setImplicit(value.toMillis());
                }
            });
            return this;
        }

        @Nonnull
        @Override
        public Duration getPageLoadTimeout() {
            Long pageLoad = getTimeouts().getPageLoad();
            if (pageLoad == null) {
                pageLoad = 0L;
            }
            return Duration.ofMillis(pageLoad);
        }

        @Nonnull
        @Override
        public Timeouts setPageLoadTimeout(@Nonnull Duration value) {
            return setTimeouts(new com.aerokube.lightning.model.Timeouts() {
                {
                    setPageLoad(value.toMillis());
                }
            });
        }

        @Nonnull
        @Override
        public Optional<Duration> getScriptTimeout() {
            Long value = getTimeouts().getScript();
            return value != null ? Optional.of(Duration.ofMillis(value)) : Optional.empty();
        }

        @Nonnull
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

        @Nonnull
        @Override
        public List<Window> list() {
            return execute(() -> contextsApi.getWindowHandles(sessionId).getValue()).stream()
                    .map(StdWindow::new)
                    .collect(Collectors.toList());
        }

        @Nonnull
        @Override
        public Window createWindow() {
            return createWindow(WINDOW);
        }

        private Window createWindow(NewWindowRequest.TypeEnum type) {
            NewWindowRequest newWindowRequest = new NewWindowRequest().type(type);
            String handle = execute(() -> contextsApi.createNewWindow(sessionId, newWindowRequest).getValue().getHandle());
            return new StdWindow(handle);
        }

        @Nonnull
        @Override
        public Window createTab() {
            return createWindow(TAB);
        }

        @Nonnull
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

            @Nonnull
            @Override
            public WebDriver.Windows.Window close() {
                switchTo();
                execute(() -> contextsApi.closeWindow(sessionId));
                return this;
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window fullscreen() {
                switchTo();
                execute(() -> contextsApi.fullscreenWindow(sessionId, new EmptyRequest()));
                return this;
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window maximize() {
                switchTo();
                execute(() -> contextsApi.maximizeWindow(sessionId, new EmptyRequest()));
                return this;
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window minimize() {
                switchTo();
                execute(() -> contextsApi.minimizeWindow(sessionId, new EmptyRequest()));
                return this;
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window setSize(int width, int height) {
                switchTo();
                com.aerokube.lightning.model.Rect rect = new com.aerokube.lightning.model.Rect()
                        .width(width).height(height);
                execute(() -> contextsApi.setWindowRect(sessionId, rect));
                return this;
            }

            @Nonnull
            @Override
            public Size getSize() {
                return new StdWebDriver.Rect(execute(() -> contextsApi.getWindowRect(sessionId)));
            }

            @Nonnull
            @Override
            public Position getPosition() {
                return new StdWebDriver.Rect(execute(() -> contextsApi.getWindowRect(sessionId)));
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window setPosition(int x, int y) {
                switchTo();
                com.aerokube.lightning.model.Rect rect = new com.aerokube.lightning.model.Rect()
                        .x(x).y(y);
                execute(() -> contextsApi.setWindowRect(sessionId, rect));
                return this;
            }

            @Nonnull
            @Override
            public WebDriver.Windows.Window switchTo() {
                SwitchToWindowRequest switchToWindowRequest = new SwitchToWindowRequest().handle(handle);
                execute(() -> contextsApi.switchToWindow(sessionId, switchToWindowRequest));
                return this;
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
        public void switchTo(@Nonnull WebElement element) {
            SwitchToFrameRequest switchToFrameRequest = new SwitchToFrameRequest()
                    .id(new FrameId(new com.aerokube.lightning.model.WebElement().element606611e4A52e4f735466cecf(element.getId())));
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

    class Elements implements WebDriver.Elements {

        @Nonnull
        @Override
        public WebElement findFirst(@Nonnull Locator locator) {
            FindElementRequest findElementRequest = new FindElementRequest()
                    .using(locator.getStrategy()).value(locator.getExpression());
            String elementId = execute(() -> elementsApi.findElement(sessionId, findElementRequest)
                    .getValue().getElement606611e4A52e4f735466cecf());
            return new StdElement(elementId);
        }

        @Nonnull
        @Override
        public List<WebElement> findAll(@Nonnull Locator locator) {
            FindElementRequest findElementRequest = new FindElementRequest()
                    .using(locator.getStrategy()).value(locator.getExpression());
            return execute(() -> elementsApi.findElements(sessionId, findElementRequest)
                    .getValue()).stream()
                    .map(e -> new StdElement(e.getElement606611e4A52e4f735466cecf()))
                    .collect(Collectors.toList());
        }

        @Nonnull
        @Override
        public WebElement current() {
            return new StdElement(execute(() ->
                    elementsApi.getActiveElement(sessionId).getValue().getElement606611e4A52e4f735466cecf()));
        }

        class StdElement implements WebElement {

            private final String id;

            StdElement(String id) {
                this.id = id;
            }

            @Nonnull
            @Override
            public WebElement click() {
                execute(() -> elementsApi.elementClick(sessionId, id, new EmptyRequest()));
                return this;
            }

            @Nonnull
            @Override
            public WebElement clear() {
                execute(() -> elementsApi.elementClear(sessionId, id, new EmptyRequest()));
                return this;
            }

            @Nonnull
            @Override
            public List<WebElement> findAll(@Nonnull Locator locator) {
                FindElementRequest findElementRequest = new FindElementRequest()
                        .using(locator.getStrategy()).value(locator.getExpression());
                return execute(() -> elementsApi.findElementsFromElement(sessionId, id, findElementRequest)
                        .getValue()).stream()
                        .map(e -> new StdElement(e.getElement606611e4A52e4f735466cecf()))
                        .collect(Collectors.toList());
            }

            @Nonnull
            @Override
            public WebElement findFirst(@Nonnull Locator locator) {
                FindElementRequest findElementRequest = new FindElementRequest()
                        .using(locator.getStrategy()).value(locator.getExpression());
                String elementId = execute(() -> elementsApi.findElementFromElement(sessionId, id, findElementRequest)
                        .getValue().getElement606611e4A52e4f735466cecf());
                return new StdElement(elementId);
            }

            @Override
            public boolean isSelected() {
                return execute(() -> elementsApi.isElementSelected(sessionId, id).getValue());
            }

            @Override
            public boolean isEnabled() {
                return execute(() -> elementsApi.isElementEnabled(sessionId, id).getValue());
            }

            @Override
            public boolean isDisplayed() {
                return execute(() -> elementsApi.isElementDisplayed(sessionId, id).getValue());
            }

            @Nonnull
            @Override
            public Optional<String> getAttribute(@Nonnull String name) {
                return Optional.ofNullable(
                        execute(() -> elementsApi.getElementAttribute(sessionId, id, name).getValue())
                );
            }

            @Nonnull
            @Override
            public String getId() {
                return id;
            }

            @Nonnull
            @Override
            public Position getPosition() {
                return new Rect(execute(() -> elementsApi.getElementRect(sessionId, id)));
            }

            @Nonnull
            @Override
            public Optional<String> getProperty(@Nonnull String name) {
                return Optional.ofNullable(
                        execute(() -> elementsApi.getElementProperty(sessionId, id, name).getValue())
                );
            }

            @Nonnull
            @Override
            public String getCssProperty(@Nonnull String name) {
                return execute(() -> elementsApi.getElementCSSProperty(sessionId, id, name).getValue());
            }

            @Nonnull
            @Override
            public Size getSize() {
                return new Rect(execute(() -> elementsApi.getElementRect(sessionId, id)));
            }

            @Nonnull
            @Override
            public String getTagName() {
                return execute(() -> elementsApi.getElementTagName(sessionId, id).getValue());
            }

            @Nonnull
            @Override
            public String getText() {
                return execute(() -> elementsApi.getElementText(sessionId, id).getValue());
            }

            @Nonnull
            @Override
            public WebElement sendKeys(@Nonnull String text) {
                ElementSendKeysRequest elementSendKeysRequest = new ElementSendKeysRequest().text(text);
                execute(() -> elementsApi.elementSendKeys(sessionId, id, elementSendKeysRequest));
                return this;
            }

            @Nonnull
            @Override
            public Accessibility accessibility() {
                return new Accessibility() {
                    @Nonnull
                    @Override
                    public String getRole() {
                        return execute(() -> elementsApi.getElementComputedRole(sessionId, id).getValue());
                    }

                    @Nonnull
                    @Override
                    public String getLabel() {
                        return execute(() -> elementsApi.getElementComputedLabel(sessionId, id).getValue());
                    }
                };
            }
        }

    }

    class Print implements WebDriver.Print {

        private final PrintRequestOptions printRequestOptions = new PrintRequestOptions()
                .page(new PrintRequestOptionsPage())
                .margin(new PrintRequestOptionsMargin());

        @Nonnull
        @Override
        public WebDriver.Print addPages(@Nonnull int... pages) {
            Arrays.stream(pages).forEach(
                    p -> printRequestOptions.getPageRanges().add(String.valueOf(p))
            );
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print addPages(@Nonnull String... pages) {
            Arrays.stream(pages).forEach(
                    p -> printRequestOptions.getPageRanges().add(p)
            );
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print scale(float scale) {
            printRequestOptions.setScale(scale);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print originalSize() {
            printRequestOptions.setShrinkToFit(false);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print width(float value) {
            printRequestOptions.getPage().setWidth(value);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print height(float value) {
            printRequestOptions.getPage().setHeight(value);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print marginTop(float value) {
            printRequestOptions.getMargin().setTop(value);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print marginBottom(float value) {
            printRequestOptions.getMargin().setBottom(value);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print marginLeft(float value) {
            printRequestOptions.getMargin().setLeft(value);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Print marginRight(float value) {
            printRequestOptions.getMargin().setRight(value);
            return this;
        }

        @Override
        public byte[] pdf() {
            PrintRequest printRequest = new PrintRequest().options(printRequestOptions);
            return execute(() -> {
                String encodedBytes = printApi.printPage(sessionId, printRequest).getValue();
                return Base64.getDecoder().decode(encodedBytes);
            });
        }
    }
}
