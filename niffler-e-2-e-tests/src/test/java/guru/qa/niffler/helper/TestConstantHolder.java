package guru.qa.niffler.helper;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstantHolder {
    public static final Faker FAKER = new Faker();
    public static final Config CFG = Config.getInstance();
}
