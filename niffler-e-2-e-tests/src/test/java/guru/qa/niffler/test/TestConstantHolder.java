package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstantHolder {
    public static final Faker FAKER = new Faker();
}
