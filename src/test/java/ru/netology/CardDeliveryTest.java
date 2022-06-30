package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Long.parseLong;
import static java.time.Duration.ofSeconds;
import static java.util.Calendar.DAY_OF_YEAR;
import static org.testng.Assert.assertEquals;

public class CardDeliveryTest {
    SelenideElement form = $x("//form[contains(@class, form)]");
    SelenideElement notification = $x("//div[@data-test-id='notification']");
    SelenideElement cityMenu = $$x("//body/div").get(2);
    SelenideElement calendarMenu = $$x("//body/div").get(1);

    //получение даты из поля формы
    public String getExpectedDate() {
        return form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").getValue();
    }

    //создание даты встречи с заданным кол-вом дней от сегодня
    public Calendar meetingDate(int addDays) {
        Calendar date = new GregorianCalendar();
        date.add(DAY_OF_YEAR, addDays);
        return date;
    }

    //форматирование даты в формат дд.мм.гггг
    public String inputMeetingDate(Calendar meetingDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(meetingDate.getTime());
    }

    //выбор кликом дня в виджете календаря
    public void selectMeetingDate(Calendar date) {
        long meetingDateMax = date.getTimeInMillis();
        long meetingDateMin = meetingDateMax - 86_400_000;
        boolean loop = true;
        while (loop) {
            int lastIndex = calendarMenu.$$x(".//td[@data-day]").size();
            for (int i = 0; i < lastIndex; i++) {
                long dataDay = getMillisecondFromCalendar(i);
                if (dataDay >= meetingDateMin && dataDay <= meetingDateMax) {
                    calendarMenu.$$x(".//td[@data-day]").get(i).click();
                    loop = false;
                    return;
                }
            }
            calendarMenu.$x(".//div[@data-step='1']").click();
        }
    }

    //получение строки миллисекунд по индексу дня в виджете календаря
    public long getMillisecondFromCalendar(int i) {
        return parseLong(calendarMenu.$$x(".//td[@data-day]").get(i).getDomAttribute("data-day"));
    }

    //получение город из поля формы
    public String getActualCity() {
        return form.$x(".//span[@data-test-id='city']//child::input").getValue();
    }

    @BeforeMethod
    public void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldSuccessDefaultDateTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Петрозаводск");
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldSuccessCustomDateTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Петрозаводск");
        String inputMeetingDate = inputMeetingDate(meetingDate(5));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidCityTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Севастополь");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidCityTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Орёл");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldInvalidCityTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Moscow");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldInvalidCityTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тольятти");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldInvalidDateTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(2));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='date']/span/span").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldInvalidDateTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(0));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='date']/span/span").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldInvalidDateTestThree() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(-3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='date']/span/span").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату невозможен"));
    }

    //todo bug
    @Test
    public void shouldValidNameTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Артёменко Артём");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidNameTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Мамин-Сибиряк Дмитрий");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidNameTestThree() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Хосе Иглесиас Хулио");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    //todo bug
    @Test
    public void shouldInvalidNameTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Имя и Фамилия указаные неверно"));
    }

    @Test
    public void shouldInvalidNameTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Ivanov Ivan");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Имя и Фамилия указаные неверно"));
    }

    @Test
    public void shouldInvalidNameTestThree() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Ив@нов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Имя и Фамилия указаные неверно"));
    }

    @Test
    public void shouldInvalidPhoneTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+7921123456");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldInvalidPhoneTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+792112345678");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldInvalidPhoneTestThree() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("89211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldInvalidPhoneTestFour() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+7921@2h4к67");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldEmptyCityTest() {
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyNameTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyPhoneTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyCheckboxTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//label[@data-test-id='agreement']").
                should(cssClass("input_invalid"));
    }

    @Test
    public void shouldSelectCityTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Пе");
        cityMenu.should(visible);
        cityMenu.$x(".//span[contains(text(), 'Петрозаводск')]//ancestor::div[contains(@class, 'menu-item')]").click();
        String actualCity = getActualCity();
        assertEquals(actualCity, "Петрозаводск");
        cityMenu.should(hidden);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldSelectCityTestTwo() {
        form.$x(".//span[@data-test-id='city']//child::input").val("пе");
        cityMenu.should(visible);
        cityMenu.$x(".//span[contains(text(), 'Петрозаводск')]//ancestor::div[contains(@class, 'menu-item')]").click();
        String actualCity = getActualCity();
        assertEquals(actualCity, "Петрозаводск");
        cityMenu.should(hidden);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldSelectCityTestThree() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Пе");
        cityMenu.should(visible);
        cityMenu.$x(".//span[contains(text(), 'Липецк')]//ancestor::div[contains(@class, 'menu-item')]").click();
        String actualCity = getActualCity();
        assertEquals(actualCity, "Липецк");
        cityMenu.should(hidden);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldInvalidSelectCityTestOne() {
        form.$x(".//span[@data-test-id='city']//child::input").val("gt");
        cityMenu.should(hidden);
    }

    @Test
    public void shouldSelectDateThrough7DaysTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Петрозаводск");
        form.$x(".//span[@data-test-id='date']//child::button").click();
        calendarMenu.should(visible);
        String expectedDay = inputMeetingDate(meetingDate(7));
        selectMeetingDate(meetingDate(7));
        calendarMenu.should(hidden);
        assertEquals(form.$x(".//span[@data-test-id='date']//child::input").getValue(), expectedDay);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldSelectDateThrough30DaysTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Петрозаводск");
        form.$x(".//span[@data-test-id='date']//child::button").click();
        calendarMenu.should(visible);
        String expectedDay = inputMeetingDate(meetingDate(30));
        selectMeetingDate(meetingDate(30));
        calendarMenu.should(hidden);
        assertEquals(form.$x(".//span[@data-test-id='date']//child::input").getValue(), expectedDay);
        form.$x(".//span[@data-test-id='name']//child::input").val("Иванов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }
}
