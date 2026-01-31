package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {

        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.INSTANCE
                : LocalConfig.INSTANCE;
    }

    String frontUrl();

    String spendUrl();

    String authUrl();

    String gatewayUrl();

    String userdataUrl();

    String spendJdbcUrl();

    String githubUrl();

    String userdataJdbcUrl();

    String currencyJdbcUrl();

    String authJdbcUrl();
}
