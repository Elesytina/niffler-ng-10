package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @User(friendsCount = 1)
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        var friendUsername = user.testData().friends().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(friendUsername)
                .checkFriendsArePresent()
                .checkNameIsPresentInFriendsTable(friendUsername);
    }

    @User(incomeInvitationsCount = 1)
    @Test
    void incomeRequestShouldBePresentInFriendsTable(UserJson user) {
        var targetUsername = user.testData().incomeInvitations().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(targetUsername)
                .checkRequestsArePresent()
                .checkNameIsPresentInRequestTable(targetUsername);
    }

    @User(outcomeInvitationsCount = 2)
    @Test
    void outcomeRequestShouldBePresentInAllPeopleTable(UserJson user) {
        var targetUsername = user.testData().outcomeInvitations().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseAllPeople()
                .searchRequest(targetUsername)
                .checkThatOutcomeRequestsArePresent()
                .checkNameIsPresentInOutcomeRequests(targetUsername);
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .checkFriendsTableIsEmpty();
    }

    @User(incomeInvitationsCount = 1)
    @Test
    void shouldAcceptRequestForFriendship(UserJson user) {
        var targetUsername = user.testData().incomeInvitations().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(targetUsername)
                .checkRequestsArePresent()
                .checkNameIsPresentInRequestTable(targetUsername)
                .acceptRequest()
                .checkNameIsPresentInFriendsTable(targetUsername);
    }

    @User(incomeInvitationsCount = 1)
    @Test
    void shouldDeclineRequestForFriendship(UserJson user) {
        var targetUsername = user.testData().incomeInvitations().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(targetUsername)
                .checkRequestsArePresent()
                .checkNameIsPresentInRequestTable(targetUsername)
                .declineRequest()
                .checkNameIsAbsentInRequestTable(targetUsername);
    }

}
