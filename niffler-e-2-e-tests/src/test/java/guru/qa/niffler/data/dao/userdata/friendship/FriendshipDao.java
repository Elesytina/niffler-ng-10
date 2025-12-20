package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import java.util.List;

public interface FriendshipDao {

    FriendshipEntity create(FriendshipEntity friendship);

    void createAll(List<FriendshipEntity> friendship);
}
