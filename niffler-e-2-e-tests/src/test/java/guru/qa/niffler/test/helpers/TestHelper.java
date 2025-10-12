package guru.qa.niffler.test.helpers;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestHelper {
    public static final Faker FAKER = Faker.instance();
}
