package com.aerokube.lightning.adapter;

import com.aerokube.lightning.Position;
import com.aerokube.lightning.Size;
import org.openqa.selenium.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.aerokube.lightning.adapter.SeleniumWebDriver.byToLocator;
import static com.aerokube.lightning.adapter.SeleniumWebDriver.execute;

public class SeleniumWebElement implements WebElement {

    private final com.aerokube.lightning.WebElement webElement;

    public SeleniumWebElement(com.aerokube.lightning.WebElement webElement) {
        this.webElement = webElement;
    }

    @Nonnull
    com.aerokube.lightning.WebElement raw() {
        return webElement;
    }

    @Override
    public void click() {
        execute(webElement::click);
    }

    @Override
    public void submit() {
        throw new UnsupportedOperationException("This command is not supported in W3C Webdriver standard");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        execute(() -> webElement.sendKeys(Arrays.toString(keysToSend)));
    }

    @Override
    public void clear() {
        execute(webElement::clear);
    }

    @Override
    public String getTagName() {
        return execute(webElement::getTagName);
    }

    @Override
    public String getAttribute(String name) {
        return execute(() -> webElement.getAttribute(name).orElse(null));
    }

    @Override
    public boolean isSelected() {
        return execute(webElement::isSelected);
    }

    @Override
    public boolean isEnabled() {
        return execute(webElement::isEnabled);
    }

    @Override
    public String getText() {
        return execute(webElement::getText);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return execute(() -> webElement.findAll(byToLocator(by)))
                .stream().map(SeleniumWebElement::new)
                .collect(Collectors.toList());
    }

    @Override
    public WebElement findElement(By by) {
        com.aerokube.lightning.WebElement found = execute(() -> webElement.findFirst(byToLocator(by)));
        return new SeleniumWebElement(found);
    }

    @Override
    public boolean isDisplayed() {
        return execute(webElement::isDisplayed);
    }

    @Override
    public Point getLocation() {
        Position position = execute(webElement::getPosition);
        return new Point(position.getX(), position.getY());
    }

    @Override
    public Dimension getSize() {
        Size size = execute(webElement::getSize);
        return new Dimension(size.getWidth(), size.getHeight());
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getLocation(), getSize());
    }

    @Override
    public String getCssValue(String propertyName) {
        return execute(() -> webElement.getCssProperty(propertyName));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return target.convertFromPngBytes(execute(webElement::takeScreenshot));
    }
}
