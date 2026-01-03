package guru.qa.niffler.page.component;

import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class Header {

    private final String FRONT_URL = CFG.frontUrl();

    public FriendsPage toFriendsPage() {
        open(FRONT_URL + "people/friends");

        return new FriendsPage();
    }

    public AllPeoplePage toAllPeoplePage() {
        open(FRONT_URL + "people/all");

        return new AllPeoplePage();
    }

    public ProfilePage toProfilePage() {
        open(FRONT_URL + "profile");

        return new ProfilePage();
    }

    public LoginPage toLoginPage() {
        open(FRONT_URL + "login");

        return new LoginPage();
    }

    public MainPage toMainPage() {
        open(FRONT_URL + "main");

        return new MainPage();
    }

    public EditSpendingPage toEditSpendingPage(UUID spendingId) {
        open(FRONT_URL + "spending/{spenId}");

        return new EditSpendingPage();
    }
}
