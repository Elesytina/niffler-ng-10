package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void friendsShouldBePresentInFriendsTable(@UserType(UserType.Type.WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openProfilePopupMenu()
                .chooseFriends()
                .checkFriendsArePresent()
                .checkNameIsPresentInFriendsTable(user.friend());
    }

    @Test
    void incomeRequestShouldBePresentInFriendsTable(@UserType(UserType.Type.WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openProfilePopupMenu()
                .chooseFriends()
                .checkRequestsArePresent()
                .checkNameIsPresentInRequestTable(user.income());
    }

    @Test
    void outcomeRequestShouldBePresentInAllPeopleTable(@UserType(UserType.Type.WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openProfilePopupMenu()
                .chooseAllPeople()
                .checkThatOutcomeRequestsArePresent()
                .checkNameIsPresentInOutcomeRequests(user.outcome());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(UserType.Type.EMPTY) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openProfilePopupMenu()
                .chooseFriends()
                .checkFriendsTableIsEmpty();
    }

}
