package guru.qa.niffler.model.enums;

import java.sql.Date;
import java.time.LocalDate;

import static java.time.LocalDate.now;

public enum DateFilterValues {
    TODAY, WEEK, MONTH;

    public static Date getSpendEndDate(DateFilterValues dateFilterValues) {
        LocalDate localDate = switch (dateFilterValues) {
            case TODAY -> now();
            case WEEK -> now().plusWeeks(1);
            case MONTH -> now().plusMonths(1);
        };

        return Date.valueOf(localDate);
    }
}
