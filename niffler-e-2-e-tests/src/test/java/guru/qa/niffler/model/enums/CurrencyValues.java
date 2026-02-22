package guru.qa.niffler.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrencyValues {
    RUB("₽"),
    USD("$"),
    EUR("€"),
    KZT("₸");

    private final String symbol;
}
