package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

public interface FriendshipDao {

    FriendshipEntity create(FriendshipEntity friendship);

    FriendshipEntity update(FriendshipEntity friendship);
}
