package com.aerokube.lightning.adapter;

import com.aerokube.lightning.Position;
import com.aerokube.lightning.Size;
import com.aerokube.lightning.model.LocatorStrategy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.logging.Logs;

import javax.annotation.Nonnull;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.aerokube.lightning.By.cssSelector;

public class SeleniumWebDriver implements WebDriver, WebDriver.Options, WebDriver.Navigation, WebDriver.TargetLocator, WebDriver.Timeouts, WebDriver.Window, Alert, JavascriptExecutor, TakesScreenshot, HasCapabilities {

    private final com.aerokube.lightning.WebDriver webDriver;

    public SeleniumWebDriver(com.aerokube.lightning.WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    static <T> T execute(@Nonnull Callable<T> action) {
        try {
            return action.call();
        } catch (com.aerokube.lightning.WebDriverException e) {
            throw processException(e);
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
    }

    @Nonnull
    private static WebDriverException processException(com.aerokube.lightning.WebDriverException e) {
        switch (e.getErrorCode()) {
            case ELEMENT_CLICK_INTERCEPTED:
                return new ElementClickInterceptedException(e.getMessage(), e);
            case ELEMENT_NOT_INTERACTABLE:
                return new ElementNotInteractableException(e.getMessage(), e);
            case INSECURE_CERTIFICATE:
                break;
            case INVALID_ARGUMENT:
                return new InvalidArgumentException(e.getMessage(), e);
            case INVALID_COOKIE_DOMAIN:
                return new InvalidCookieDomainException(e.getMessage(), e);
            case INVALID_ELEMENT_STATE:
                return new InvalidElementStateException(e.getMessage(), e);
            case INVALID_SELECTOR:
                return new InvalidSelectorException(e.getMessage(), e);
            case INVALID_SESSION_ID:
                return new NoSuchSessionException(e.getMessage(), e);
            case JAVASCRIPT_ERROR:
                return new JavascriptException(e.getMessage(), e);
            case MOVE_TARGET_OUT_OF_BOUNDS:
                return new MoveTargetOutOfBoundsException(e.getMessage(), e);
            case NO_SUCH_ALERT:
                return new NoAlertPresentException(e.getMessage(), e);
            case NO_SUCH_COOKIE:
                return new NoSuchCookieException(e.getMessage());
            case NO_SUCH_ELEMENT:
                return new NoSuchElementException(e.getMessage(), e);
            case NO_SUCH_FRAME:
                return new NoSuchFrameException(e.getMessage(), e);
            case NO_SUCH_WINDOW:
                return new NoSuchWindowException(e.getMessage(), e);
            case SCRIPT_TIMEOUT:
                return new ScriptTimeoutException(e.getMessage(), e);
            case SESSION_NOT_CREATED:
                return new SessionNotCreatedException(e.getMessage(), e);
            case STALE_ELEMENT_REFERENCE:
                return new StaleElementReferenceException(e.getMessage(), e);
            case TIMEOUT:
                return new TimeoutException(e.getMessage(), e);
            case UNABLE_TO_SET_COOKIE:
                return new UnableToSetCookieException(e.getMessage(), e);
            case UNEXPECTED_ALERT_OPEN:
                return new UnhandledAlertException(e.getMessage());
            case UNSUPPORTED_OPERATION:
                return new UnsupportedCommandException(e.getMessage(), e);
        }
        return new WebDriverException(e.getMessage(), e);
    }

    @Nonnull
    static com.aerokube.lightning.WebDriver.Locator byToLocator(By by) {
        if (by instanceof By.Remotable) {
            By.Remotable.Parameters remoteParameters = ((By.Remotable) by).getRemoteParameters();
            LocatorStrategy locatorStrategy = LocatorStrategy.fromValue(remoteParameters.using());
            String expression = String.valueOf(remoteParameters.value());
            return new com.aerokube.lightning.By(expression, locatorStrategy);
        }
        throw new WebDriverException(String.format("Unsupported locator type: %s", by.getClass().getCanonicalName()));
    }

    @Nonnull
    private static com.aerokube.lightning.Cookie convertSeleniumCookie(Cookie cookie) {
        com.aerokube.lightning.Cookie.CookieBuilder cookieBuilder = com.aerokube.lightning.Cookie.create(cookie.getName(), cookie.getValue())
                .path(cookie.getPath())
                .domain(cookie.getDomain())
                .expires(cookie.getExpiry().toInstant())
                .sameSitePolicy(com.aerokube.lightning.model.Cookie.SameSiteEnum.valueOf(cookie.getSameSite()));
        if (cookie.isHttpOnly()) {
            cookieBuilder = cookieBuilder.httpOnly();
        }
        if (cookie.isSecure()) {
            cookieBuilder = cookieBuilder.secureOnly();
        }
        return cookieBuilder.build();
    }

    @Nonnull
    private static Cookie convertLightningCookie(com.aerokube.lightning.Cookie cookie) {
        Optional<Instant> expires = cookie.getExpires();
        Date date = null;
        if (expires.isPresent()) {
            date = Date.from(expires.get());
        }
        return new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), date, cookie.isSecureOnly(), cookie.isHttpOnly(), cookie.getSameSitePolicy().getValue());
    }

    @Nonnull
    public String getSessionId() {
        return webDriver.getSessionId();
    }

    @Override
    public void get(String url) {
        execute(() -> webDriver.navigation().navigate(url));
    }

    @Override
    public String getCurrentUrl() {
        return execute(() -> webDriver.navigation().getUrl());
    }

    @Override
    public String getTitle() {
        return execute(() -> webDriver.navigation().getTitle());
    }

    @Override
    public List<WebElement> findElements(By by) {
        return execute(() -> webDriver.elements().findAll(byToLocator(by))
                .stream().map(SeleniumWebElement::new)
                .collect(Collectors.toList()));
    }

    @Override
    public WebElement findElement(By by) {
        com.aerokube.lightning.WebElement webElement = execute(() -> webDriver.elements().findFirst(byToLocator(by)));
        return new SeleniumWebElement(webElement);
    }

    @Override
    public String getPageSource() {
        return execute(() -> webDriver.document().getPageSource());
    }

    @Override
    public void close() {
        execute(() -> webDriver.windows().current().close());
    }

    @Override
    public void quit() {
        execute(() -> {
            webDriver.session().delete();
            return null;
        });
    }

    @Override
    public Set<String> getWindowHandles() {
        return execute(() -> webDriver.windows().list()
                .stream().map(com.aerokube.lightning.Window::getHandle)
                .collect(Collectors.toSet()));
    }

    @Override
    public String getWindowHandle() {
        return execute(() -> webDriver.windows().current().getHandle());
    }

    @Override
    public TargetLocator switchTo() {
        return this;
    }

    @Override
    public Navigation navigate() {
        return this;
    }

    @Override
    public Options manage() {
        return this;
    }

    @Override
    public void back() {
        execute(() -> webDriver.navigation().back());
    }

    @Override
    public void forward() {
        execute(() -> webDriver.navigation().forward());
    }

    @Override
    public void to(String url) {
        execute(() -> webDriver.navigation().navigate(url));
    }

    @Override
    public void to(URL url) {
        execute(() -> webDriver.navigation().navigate(url.toString()));
    }

    @Override
    public void refresh() {
        execute(() -> webDriver.navigation().refresh());
    }

    @Override
    public WebDriver frame(int index) {
        execute(() -> {
            webDriver.frames().switchTo(index);
            return null;
        });
        return this;
    }

    @Override
    public WebDriver frame(String nameOrId) {
        com.aerokube.lightning.WebDriver.Locator byNameLocator = cssSelector(String.format("frame[name='%s'],iframe[name='%s']", nameOrId, nameOrId));
        List<com.aerokube.lightning.WebElement> byName = execute(() -> webDriver.elements().findAll(byNameLocator));
        if (byName.size() > 0) {
            frame(new SeleniumWebElement(byName.get(0)));
            return this;
        }

        com.aerokube.lightning.WebDriver.Locator byIdLocator = cssSelector(String.format("frame#%s,iframe#%s", nameOrId, nameOrId));
        List<com.aerokube.lightning.WebElement> byId = execute(() -> webDriver.elements().findAll(byIdLocator));
        if (byId.size() > 0) {
            frame(new SeleniumWebElement(byId.get(0)));
            return this;
        }

        throw new NoSuchFrameException("No frame element found by name or id " + nameOrId);
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        if (frameElement instanceof SeleniumWebElement) {
            execute(() -> {
                webDriver.frames().switchTo(((SeleniumWebElement) frameElement).raw());
                return null;
            });
            return this;
        }
        throw new IllegalArgumentException("This operation is not supported on third-party WebElement instances");
    }

    @Override
    public WebDriver parentFrame() {
        execute(() -> {
            webDriver.frames().switchToParent();
            return null;
        });
        return this;
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        List<com.aerokube.lightning.Window> windows = execute(() -> webDriver.windows().list());
        windows.stream().filter(
                        w -> getWindowHandles().contains(nameOrHandle)
                )
                .findFirst()
                .orElseThrow(() -> new NoSuchWindowException(String.format("Window %s not found", nameOrHandle)))
                .switchTo();
        return this;
    }

    @Override
    public WebDriver newWindow(WindowType typeHint) {
        switch (typeHint) {
            case WINDOW:
                execute(() -> webDriver.windows().createWindow());
                break;
            case TAB:
                execute(() -> webDriver.windows().createTab());
                break;
        }
        return this;
    }

    @Override
    public WebDriver defaultContent() {
        execute(() -> {
            webDriver.frames().switchToDefault();
            return null;
        });
        return this;
    }

    @Override
    public WebElement activeElement() {
        com.aerokube.lightning.WebElement webElement = execute(() -> webDriver.elements().current());
        return new SeleniumWebElement(webElement);
    }

    @Override
    public Alert alert() {
        return this;
    }

    @Override
    public void dismiss() {
        execute(() -> webDriver.prompts().dismiss());
    }

    @Override
    public void accept() {
        execute(() -> webDriver.prompts().accept());
    }

    @Override
    public String getText() {
        return execute(() -> webDriver.prompts().getText());
    }

    @Override
    public void sendKeys(String keysToSend) {
        execute(() -> webDriver.prompts().sendText(keysToSend));
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return execute(() -> webDriver.document().executeScript(script, args));
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return execute(() -> webDriver.document().executeAsyncScript(script, args));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return target.convertFromPngBytes(execute(() -> webDriver.screenshot().take()));
    }

    @Override
    public void addCookie(Cookie cookie) {
        execute(() -> webDriver.cookies().add(convertSeleniumCookie(cookie)));
    }

    @Override
    public void deleteCookieNamed(String name) {
        execute(() -> webDriver.cookies().delete(name));
    }

    @Override
    public void deleteCookie(Cookie cookie) {
        execute(() -> webDriver.cookies().delete(cookie.getName()));
    }

    @Override
    public void deleteAllCookies() {
        execute(() -> webDriver.cookies().deleteAll());
    }

    @Override
    public Set<Cookie> getCookies() {
        return execute(() -> webDriver.cookies().getAll())
                .stream().map(SeleniumWebDriver::convertLightningCookie)
                .collect(Collectors.toSet());
    }

    @Override
    public Cookie getCookieNamed(String name) {
        com.aerokube.lightning.Cookie cookie = execute(() -> webDriver.cookies().get(name));
        return convertLightningCookie(cookie);
    }

    @Override
    public Timeouts timeouts() {
        return this;
    }

    @Override
    public Window window() {
        return this;
    }

    @Override
    public Logs logs() {
        //TODO: need to implement this in Lightning first
        throw new UnsupportedOperationException("Logs is not yet supported");
    }

    @Override
    public Duration getImplicitWaitTimeout() {
        return execute(() -> webDriver.timeouts().getImplicitWaitTimeout());
    }

    @Override
    public Duration getScriptTimeout() {
        Optional<Duration> scriptTimeout = execute(() -> webDriver.timeouts().getScriptTimeout());
        if (scriptTimeout.isEmpty()) {
            throw new WebDriverException("Script timeout is empty");
        }
        return scriptTimeout.get();
    }

    @Override
    public Duration getPageLoadTimeout() {
        return execute(() -> webDriver.timeouts().getPageLoadTimeout());
    }

    @Override
    public Timeouts implicitlyWait(long time, TimeUnit unit) {
        execute(() -> webDriver.timeouts().setImplicitWaitTimeout(Duration.of(time, unit.toChronoUnit())));
        return this;
    }

    @Override
    public Timeouts setScriptTimeout(long time, TimeUnit unit) {
        execute(() -> webDriver.timeouts().setScriptTimeout(Duration.of(time, unit.toChronoUnit())));
        return this;
    }

    @Override
    public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
        execute(() -> webDriver.timeouts().setPageLoadTimeout(Duration.of(time, unit.toChronoUnit())));
        return this;
    }

    @Override
    public Dimension getSize() {
        Size size = execute(() -> webDriver.windows().current().getSize());
        return new Dimension(size.getWidth(), size.getHeight());
    }

    @Override
    public void setSize(Dimension targetSize) {
        execute(() -> webDriver.windows().current().setSize(targetSize.getWidth(), targetSize.getHeight()));
    }

    @Override
    public Point getPosition() {
        Position position = execute(() -> webDriver.windows().current().getPosition());
        return new Point(position.getX(), position.getY());
    }

    @Override
    public void setPosition(Point targetPosition) {
        execute(() -> webDriver.windows().current().setPosition(targetPosition.getX(), targetPosition.getY()));
    }

    @Override
    public void maximize() {
        execute(() -> webDriver.windows().current().maximize());
    }

    @Override
    public void minimize() {
        execute(() -> webDriver.windows().current().minimize());
    }

    @Override
    public void fullscreen() {
        execute(() -> webDriver.windows().current().fullscreen());
    }

    @Override
    public Capabilities getCapabilities() {
        return new ImmutableCapabilities(webDriver.getCapabilities().raw());
    }
}
