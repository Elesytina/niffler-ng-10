package guru.qa.niffler.model.userdata;

import guru.qa.niffler.model.enums.CurrencyValues;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        CurrencyValues currency,
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
