package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import java.util.Optional;
import java.util.UUID;

public interface FriendshipDao {

    FriendshipEntity create(FriendshipEntity friendship);

    Optional<FriendshipEntity> findByAddresseeId(UUID userId);
}
