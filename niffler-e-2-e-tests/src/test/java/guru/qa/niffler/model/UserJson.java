package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.UserEntity;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        String currency,
        String firstName,
        String surname,
        String fullName,
        byte[] photo,
        byte[] photoSmall) {

    public static UserJson fromEntity(UserEntity entity) {

        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getCurrency(),
                entity.getFirstName(),
                entity.getSurname(),
                entity.getFullName(),
                entity.getPhoto(),
                entity.getPhotoSmall());
    }
}
