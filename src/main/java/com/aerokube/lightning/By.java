package com.aerokube.lightning;

import com.aerokube.lightning.model.LocatorStrategy;

import javax.annotation.Nonnull;

import static com.aerokube.lightning.model.LocatorStrategy.*;

public final class By {

    @Nonnull
    public static WebDriver.Locator cssSelector(@Nonnull String cssSelector) {
        return locator(cssSelector, CSS_SELECTOR);
    }

    @Nonnull
    public static WebDriver.Locator xpath(@Nonnull String xpath) {
        return locator(xpath, XPATH);
    }

    @Nonnull
    public static WebDriver.Locator tagName(@Nonnull String tagName) {
        return locator(tagName, TAG_NAME);
    }

    @Nonnull
    public static WebDriver.Locator linkText(@Nonnull String linkText) {
        return locator(linkText, LINK_TEXT);
    }

    @Nonnull
    public static WebDriver.Locator partialLinkText(@Nonnull String partialLinkText) {
        return locator(partialLinkText, PARTIAL_LINK_TEXT);
    }

    @Nonnull
    private static WebDriver.Locator locator(String expression, LocatorStrategy locatorStrategy) {
        return new WebDriver.Locator() {
            @Nonnull
            @Override
            public String getExpression() {
                return expression;
            }

            @Nonnull
            @Override
            public LocatorStrategy getStrategy() {
                return locatorStrategy;
            }
        };
    }

}
