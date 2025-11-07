package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.UUID;

public record CategoryJson(
        UUID id,
        String name,
        String username,
        boolean archived) {

    public static CategoryJson fromEntity(CategoryEntity entity) {

        return new CategoryJson(
                entity.getId(),
                entity.getName(),
                entity.getUsername(),
                entity.isArchived()
        );
    }
}
