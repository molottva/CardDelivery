package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    SelenideElement form = $x("//form[contains(@class, form)]");
    SelenideElement notification = $x("//div[@data-test-id='notification']");

    @BeforeMethod
    public void setup() {
        open("http://127.0.0.1:9999/");
    }

    @Test
    public void shouldCriticalPathDefaultDateTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Петрозаводск");
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, Duration.ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на"));
        String expectedDate = form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").getValue();
        notification.$x(".//div[@class='notification__content']").should(text(expectedDate));
    }
}
