package com.aerokube.lightning;

import com.aerokube.lightning.WebDriver.Windows.Window;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WindowsTest extends BaseTest {

    @Test
    void testWindows() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://example.com");

            List<Window> list = driver.windows().list();
            assertThat(list, hasSize(1));
            Window defaultWindow = list.get(0);

            Size size = driver.windows().current()
                    .fullscreen()
                    .minimize()
                    .maximize()
                    .getSize();
            assertThat(size.getWidth(), greaterThan(0));
            assertThat(size.getHeight(), greaterThan(0));

            Position position = driver.windows().current().getPosition();
            assertThat(position.getX(), equalTo(0));
            assertThat(position.getY(), equalTo(0));

            Size updatedSize = driver.windows().current()
                    .setSize(1024, 768)
                    .getSize();
            assertThat(updatedSize.getWidth(), equalTo(1024));
            assertThat(updatedSize.getHeight(), equalTo(768));

            Position updatedPosition = driver.windows().current()
                    .setPosition(200, 100)
                    .getPosition();

            assertThat(updatedPosition.getX(), equalTo(200));
            assertThat(updatedPosition.getY(), equalTo(100));

            Window newWindow = driver.windows().createWindow();
            Window newTab = driver.windows().createTab();

            List<Window> updatedList = driver.windows().list();
            assertThat(updatedList, hasSize(3));

            newWindow.switchTo();
            newWindow.close();
            newTab.switchTo();
            newTab.close();
            defaultWindow.switchTo();

            List<Window> listAfterRemovingAll = driver.windows().list();
            assertThat(listAfterRemovingAll, hasSize(1));
        });
    }

}
