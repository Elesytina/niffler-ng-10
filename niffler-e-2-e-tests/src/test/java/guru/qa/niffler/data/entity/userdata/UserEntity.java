package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEntity {
    private UUID id;
    private String username;
    private String firstName;
    private String surname;
    private String currency;
    private String fullName;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson json) {
        UserEntity entity = new UserEntity();
        entity.setId(json.id());
        entity.setFirstName(json.firstName());
        entity.setSurname(json.surname());
        entity.setFullName(json.fullName());
        entity.setUsername(json.username());
        entity.setCurrency(json.currency());
        entity.setPhoto(json.photo());
        entity.setPhotoSmall(json.photoSmall());

        return entity;
    }
}
