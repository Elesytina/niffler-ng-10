package guru.qa.niffler.utils;

import guru.qa.niffler.model.spend.SpendJson;
import org.junit.jupiter.api.Assertions;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class TextUtils {

    public static String[] getExpectedStatisticItems(SpendJson... spends) {
        DecimalFormat df = new DecimalFormat("0.######");

        return Arrays.stream(spends)
                .map(spend -> "%s %s %s".formatted(
                        spend.category().name(),
                        df.format(spend.amount()),
                        spend.currency().getSymbol()))
                .sorted()
                .toArray(String[]::new);
    }

    public static String getExpectedStatisticArchivedItems(List<SpendJson> spends) {
        Assertions.assertFalse(spends.isEmpty(), "spends list is empty");
        DecimalFormat df = new DecimalFormat("0.######");
        Double sum = spends.stream()
                .map(SpendJson::amount)
                .reduce(Double::sum)
                .get();

        return "Archived %s %s".formatted(df.format(sum), spends.getFirst().currency().getSymbol());
    }

}
