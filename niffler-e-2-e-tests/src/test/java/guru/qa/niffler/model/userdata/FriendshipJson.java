package guru.qa.niffler.model.userdata;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.model.enums.FriendshipStatus;

import java.util.Date;

public class FriendshipJson {

    private UserJson requester;

    private UserJson addressee;

    private Date createdDate;

    private FriendshipStatus status;

    public static FriendshipJson fromEntity(FriendshipEntity entity) {
        FriendshipJson json = new FriendshipJson();
        json.createdDate = entity.getCreatedDate();
        json.status = entity.getStatus();
        json.requester = UserJson.fromEntity(entity.getRequester());
        json.addressee = UserJson.fromEntity(entity.getAddressee());

        return json;
    }
}
