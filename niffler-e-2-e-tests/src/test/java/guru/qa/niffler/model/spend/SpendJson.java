package guru.qa.niffler.model.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        UUID id,
        Date spendDate,
        CategoryJson category,
        String currency,
        Double amount,
        String description,
        String username) {

    public static SpendJson fromEntity(SpendEntity entity) {
        final CategoryEntity category = entity.getCategory();
        final String username = entity.getUsername();

        return new SpendJson(
                entity.getId(),
                entity.getSpendDate(),
                new CategoryJson(
                        category.getId(),
                        category.getName(),
                        username,
                        category.isArchived()
                ),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getDescription(),
                username
        );
    }
}
