package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DocumentTest extends BaseTest {

    @Test
    void testGetPageSource() {
        test(driver -> {
            driver.navigation().navigate("https://example.com/");
            assertThat(driver.document().getPageSource(), is(not(emptyString())));
        });
    }

    @Test
    void testExecuteScript() {
        test(driver -> {
            driver.navigation().navigate("https://example.com/");
            Object returnTrue = driver.document().executeScript("return true;");
            assertThat(returnTrue, is(true));
            Object sumOfIntegers = driver.document().executeScript("return arguments[0] + arguments[1];", 1, 2);
            assertThat(sumOfIntegers, equalTo(3));
            Object sumOfStrings = driver.document().executeScript("return arguments[0] + arguments[1];", "1", "2");
            assertThat(sumOfStrings, equalTo("12"));
        });
    }

    @Test
    void testExecuteAsyncScript() {
        test(driver -> {
            driver.navigation().navigate("https://example.com/");
            Object asyncResult = driver.document().executeAsyncScript(
                    "var x = arguments[0]; var y = arguments[1]; var result = x + y; var callback = arguments[arguments.length - 1]; window.setTimeout(callback(result), 100);",
                    40, 2
            );
            assertThat(asyncResult, equalTo(42));
        });
    }

}
