package guru.qa.niffler.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Spending(String category, BigDecimal amount, String description, LocalDate date) {
}
