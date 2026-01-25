package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
@WebTest
public class FriendsTest {

    @User(friendsCount = 1)
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        var friendUsername = user.testData().friends().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(friendUsername)
                .checkFriendsArePresented()
                .checkNameIsPresentedInFriendsTable(friendUsername);
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
                .checkRequestsArePresented()
                .checkNameIsPresentedInRequestTable(targetUsername);
    }

    @User(outcomeInvitationsCount = 1)
    @Test
    void outcomeRequestShouldBePresentInAllPeopleTable(UserJson user) {
        var targetUsername = user.testData().outcomeInvitations().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseAllPeople()
                .searchRequest(targetUsername)
                .checkThatOutcomeRequestsArePresented()
                .checkNameIsPresentedInOutcomeRequests(targetUsername);
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
                .checkRequestsArePresented()
                .checkNameIsPresentedInRequestTable(targetUsername)
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
                .checkRequestsArePresented()
                .checkNameIsPresentedInRequestTable(targetUsername)
                .declineRequest()
                .checkNameIsAbsentInRequestTable(targetUsername);
    }

}
