package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElementsTest extends BaseTest {

    @Test
    void testFindFirstMissingElement() {
        assertThrows(
                WebDriverException.class,
                () -> test(
                        driver -> driver
                                .navigation().navigate("https://example.com/")
                                .elements().findFirst(By.cssSelector("missing-element"))
                )
        );
    }

    @Test
    void testFindFirstByTagName() {
        test(driver -> {
            WebElement h1 = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.tagName("h1"));
            assertThat(h1.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testFindFirstByCssSelector() {
        test(driver -> {
            WebElement h1 = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.cssSelector("div h1"));
            assertThat(h1.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testFindFirstByXPath() {
        test(driver -> {
            WebElement h1 = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.xpath("/html/body/div/h1"));
            assertThat(h1.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testFindFirstByLinkText() {
        test(driver -> {
            WebElement link = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.linkText("More information..."));
            assertThat(link.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testFindFirstByPartialLinkText() {
        test(driver -> {
            WebElement link = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.partialLinkText("More"));
            assertThat(link.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testFindAllMissingElement() {
        test(driver -> {
            List<WebElement> elements = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findAll(By.cssSelector("missing-element"));
            assertThat(elements, is(empty()));
        });
    }

    @Test
    void testFindAllByTagName() {
        test(driver -> {
            List<WebElement> elements = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findAll(By.tagName("h1"));
            assertThat(elements, hasSize(1));
            assertThat(elements.get(0).getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testGetActiveElement() {
        test(driver -> {
            WebElement currentElement = driver
                    .navigation().navigate("https://example.com/")
                    .elements().current();
            assertThat(currentElement.getId(), is(not(emptyString())));
        });
    }

    @Test
    void testGetElementInsideElement() {
        test(driver -> {
            WebElement body = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.tagName("body"));
            WebElement h1 = body.findFirst(By.tagName("h1"));
            assertThat(h1.getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testGetElementsInsideElement() {
        test(driver -> {
            WebElement body = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.tagName("body"));
            List<WebElement> elements = body.findAll(By.tagName("h1"));
            assertThat(elements, hasSize(1));
            assertThat(elements.get(0).getText().length(), is(greaterThan(0)));
        });
    }

    @Test
    void testElementProperties() {
        test(driver -> {
            WebElement link = driver
                    .navigation().navigate("https://example.com/")
                    .elements().findFirst(By.cssSelector("a[href]"));

            assertThat(link.getTagName(), equalTo("a"));

            Position position = link.getPosition();
            assertThat(position.getX(), is(greaterThan(0)));
            assertThat(position.getY(), is(greaterThan(0)));

            Size size = link.getSize();
            assertThat(size.getWidth(), is(greaterThan(0)));
            assertThat(size.getHeight(), is(greaterThan(0)));

            Optional<String> maybeHref = link.getAttribute("href");
            assertThat(maybeHref.isPresent(), is(true));
            assertThat(maybeHref.get(), is(not(emptyString())));

            assertThat(link.getAttribute("missing-attribute").isPresent(), is(false));

            assertThat(link.isDisplayed(), is(true));
            assertThat(link.isEnabled(), is(true));
            assertThat(link.isSelected(), is(false));

            assertThat(link.getCssProperty("font-family"), is(not(emptyString())));
            assertThat(link.getCssProperty("missing-css-property"), is(emptyString()));

            assertThat(link.accessibility().getRole(), equalTo("link"));
            assertThat(link.accessibility().getLabel(), is(not(emptyString())));

            String currentUrl = driver.navigation().getUrl();
            link.click();
            String newUrl = driver.navigation().getUrl();
            assertThat(newUrl, is(not(equalTo(currentUrl))));
        });

    }

    @Test
    void testElementSendKeys() {
        test(driver -> {
            WebElement input = driver
                    .navigation().navigate("https://www.w3schools.com/html/html_forms.asp")
                    .elements().findFirst(By.cssSelector("input#fname"));
            assertThat(input.getProperty("value").orElse(""), is(not(emptyString())));
            input.clear();
            assertThat(input.getProperty("value").orElse(""), is(emptyString()));
            input.sendKeys("some-text");
            assertThat(input.getProperty("value").orElse(""), is(equalTo("some-text")));
        });
    }
}
