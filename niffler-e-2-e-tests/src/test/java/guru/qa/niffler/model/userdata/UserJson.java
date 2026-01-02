package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.TestData;
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
        byte[] photoSmall,
        @JsonIgnore
        TestData testData) {

    public static UserJson fromEntity(UserEntity entity) {

        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getCurrency(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getFullname(),
                entity.getPhoto(),
                entity.getPhotoSmall(),
                null);
    }

    public UserJson addTestData(TestData testData) {
        return new UserJson(
                id,
                username,
                currency,
                firstName,
                surname,
                fullName,
                photo,
                photoSmall,
                testData
        );
    }
}
