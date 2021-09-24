package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface WebElement {

    @Nonnull
    WebElement click();

    @Nonnull
    WebElement clear();

    @Nonnull
    List<WebElement> findAll(@Nonnull WebDriver.Locator locator);

    @Nonnull
    WebElement findFirst(@Nonnull WebDriver.Locator locator);

    boolean isSelected();

    boolean isEnabled();

    @Nonnull
    Optional<String> getAttribute(@Nonnull String name);

    @Nonnull
    String getId();

    @Nonnull
    WebDriver.Position getPosition();

    @Nonnull
    Optional<String> getProperty(@Nonnull String name);

    @Nonnull
    String getCssProperty(@Nonnull String name);

    @Nonnull
    WebDriver.Size getSize();

    @Nonnull
    String getTagName();

    @Nonnull
    String getText();

    @Nonnull
    WebElement sendKeys(@Nonnull String text);

    @Nonnull
    Accessibility accessibility();

    interface Accessibility {

        @Nonnull
        String getRole();

        @Nonnull
        String getLabel();

    }

}
