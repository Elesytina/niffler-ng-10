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
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(user.testData().friends().getFirst().username())
                .checkFriendsArePresent()
                .checkNameIsPresentInFriendsTable(user.testData().friends().getFirst().username());
    }

    @User(incomeInvitationsCount = 1)
    @Test
    void incomeRequestShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseFriends()
                .searchFriend(user.testData().incomeInvitations().getFirst().username())
                .checkRequestsArePresent()
                .checkNameIsPresentInRequestTable(user.testData().incomeInvitations().getFirst().username());
    }

    @User(outcomeInvitationsCount = 1)
    @Test
    void outcomeRequestShouldBePresentInAllPeopleTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseAllPeople()
                .searchRequest(user.testData().outcomeInvitations().getFirst().username())
                .checkThatOutcomeRequestsArePresent()
                .checkNameIsPresentInOutcomeRequests(user.testData().outcomeInvitations().getFirst().username());
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

}
