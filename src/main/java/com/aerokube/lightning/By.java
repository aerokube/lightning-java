package com.aerokube.lightning;

import com.aerokube.lightning.model.LocatorStrategy;

import javax.annotation.Nonnull;

import static com.aerokube.lightning.model.LocatorStrategy.*;

public class By implements WebDriver.Locator {

    private final String expression;
    private final LocatorStrategy strategy;

    public By(String expression, LocatorStrategy strategy) {
        this.expression = expression;
        this.strategy = strategy;
    }

    @Nonnull
    public static By cssSelector(@Nonnull String cssSelector) {
        return new By(cssSelector, CSS_SELECTOR);
    }

    @Nonnull
    public static By xpath(@Nonnull String xpath) {
        return new By(xpath, XPATH);
    }

    @Nonnull
    public static By tagName(@Nonnull String tagName) {
        return new By(tagName, TAG_NAME);
    }

    @Nonnull
    public static By linkText(@Nonnull String linkText) {
        return new By(linkText, LINK_TEXT);
    }

    @Nonnull
    public static By partialLinkText(@Nonnull String partialLinkText) {
        return new By(partialLinkText, PARTIAL_LINK_TEXT);
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public LocatorStrategy getStrategy() {
        return strategy;
    }
}
