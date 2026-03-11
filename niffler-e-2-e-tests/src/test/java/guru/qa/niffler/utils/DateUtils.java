package guru.qa.niffler.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    public static LocalDate getSpendingDateFromString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDate getLocalDateFromDate(java.util.Date date) {

        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
