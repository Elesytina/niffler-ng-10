package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.enums.CurrencyValues;

import java.util.Arrays;
import java.util.Random;

public class RandomDataUtils {
    private final static Faker faker = new Faker();
    private final static Random rand = new Random();

    public static String randomUsername() {
        return faker.name().username();
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
}
