package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.enums.CurrencyValues;

import java.util.Arrays;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class RandomDataUtils {
    private final static Faker faker = new Faker();
    private final static Random rand = new Random();

    public static String randomUsername() {
        return faker.name().username() + randomNumeric(2);
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomFullName() {
        return faker.name().fullName();
    }

    public static String randomCategoryName() {
        return faker.book().title() + faker.random().nextInt(1000);
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static CurrencyValues randomCurrency() {
        var values = CurrencyValues.values();

        return Arrays.stream(values)
                .skip(rand.nextInt(values.length))
                .findFirst()
                .orElse(null);
    }

    public static double randomDouble(double min, double max) {
        return rand.nextDouble(min, max);
    }

    public static int randomInteger(int min, int max) {
        return rand.nextInt(min, max);
    }
}
