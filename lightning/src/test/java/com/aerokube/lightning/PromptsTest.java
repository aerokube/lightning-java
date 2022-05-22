package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class PromptsTest extends BaseTest {

    public static final String TEST_PAGE = "https://testpages.herokuapp.com/styled/alerts/alert-test.html";

    @Test
    void testAcceptAlert() {
        test(driver -> {
            driver
                    .navigation().navigate(TEST_PAGE)
                    .elements().findFirst(By.cssSelector("#alertexamples")).click();
            String text = driver.prompts().getText();
            assertThat(text.length(), is(greaterThan(0)));
            driver.prompts().accept();
        });
    }

    @Test
    void testDismissConfirm() {
        test(driver -> {
            driver
                    .navigation().navigate(TEST_PAGE)
                    .elements().findFirst(By.cssSelector("#confirmexample")).click();
            driver.prompts().dismiss();
        });
    }

    @Test
    void testSendPromptText() {
        test(driver -> {
            driver
                    .navigation().navigate(TEST_PAGE)
                    .elements().findFirst(By.cssSelector("#promptexample")).click();
            driver.prompts().sendText("some-text");
        });
    }

}
