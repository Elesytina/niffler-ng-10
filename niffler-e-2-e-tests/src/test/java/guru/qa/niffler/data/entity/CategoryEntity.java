package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CategoryJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryEntity {
    private UUID id;
    private String name;
    private String username;
    private boolean archived;

    public static CategoryEntity fromJson(CategoryJson json) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(json.id());
        entity.setName(json.name());
        entity.setUsername(json.name());
        entity.setArchived(json.archived());
        return entity;
    }
}