package guru.qa.niffler.helper;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.enums.CurrencyValues;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstantHolder {
    public static final Config CFG = Config.getInstance();
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static final String DEFAULT_PASSWORD = "123";

}
