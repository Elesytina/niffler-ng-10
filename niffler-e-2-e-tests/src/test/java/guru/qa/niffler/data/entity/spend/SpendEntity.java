package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.spend.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity {
    private UUID id;
    private String username;
    private String currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity category;

    public static SpendEntity fromJson(SpendJson json) {
        SpendEntity entity = new SpendEntity();
        entity.setId(json.id());
        entity.setAmount(json.amount());
        entity.setDescription(json.description());
        entity.setSpendDate(new java.sql.Date(json.spendDate().getTime()));
        entity.setCurrency(json.currency());
        entity.setUsername(json.username());
        entity.setCategory(CategoryEntity.fromJson(json.category()));

        return entity;
    }
}
