package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import java.util.List;
import java.util.UUID;

public interface FriendshipDao {

    FriendshipEntity create(FriendshipEntity friendship);

    void createAll(List<FriendshipEntity> friendship);

    void deleteAll(UUID userId);

}
